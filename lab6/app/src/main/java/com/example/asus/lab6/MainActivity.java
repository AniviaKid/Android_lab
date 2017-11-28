package com.example.asus.lab6;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ServiceConnection sc;
    public static int min;//分钟数
    public static int second;//秒数
    public static TextView begin_time;
    public static TextView end_time;
    public static ImageView imageView;
    public MyService.MyBinder myBinder;
    public String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public final int REQUEST_EXTERNAL_STORAGE = 1;
    int flag=1;//1代表当前按钮显示播放，-1代表当前按钮显示暂停
    public static MainActivity instance=null;
    public static SeekBar seekBar;
    public static boolean isPlaying=false;
    public static boolean isProcessChange=false;
    public static ObjectAnimator animator;
    public float degree=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=MainActivity.this;
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.quit).setOnClickListener(this);
        findViewById(R.id.image).setOnClickListener(this);
        begin_time=(TextView) findViewById(R.id.begin_time);
        end_time=(TextView) findViewById(R.id.end_time);
        seekBar=(SeekBar) findViewById(R.id.seekbar);
        imageView=(ImageView) findViewById(R.id.image) ;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    isProcessChange=true;
                    int now_time=progress;
                    int now_second=now_time/1000,now_min=0;
                    while (now_second>=60){
                        now_second-=60;
                        now_min++;
                    }
                    if(now_second<10) MainActivity.begin_time.setText("0"+now_min+":0"+now_second);
                    else MainActivity.begin_time.setText("0"+now_min+":"+now_second);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isProcessChange=false;
                try{
                    myBinder.onTransact(104,Parcel.obtain(),Parcel.obtain(),0);
                } catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });

        sc=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Toast.makeText(MainActivity.this,"已连接",Toast.LENGTH_SHORT).show();
                myBinder=(MyService.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                sc=null;
            }
        };

        Intent service_intent=new Intent(this,MyService.class);
        startService(service_intent);
        bindService(service_intent,sc, BIND_AUTO_CREATE);
        thread.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.play) {
            Button button=(Button) findViewById(R.id.play);
            TextView status=(TextView) findViewById(R.id.status);
            if(!isPlaying){ //要切换到-1，即暂停
                button.setText("PAUSE");
                status.setText("Playing");
                animator=ObjectAnimator.ofFloat(imageView,"rotation",degree,360+degree);
                animator.setDuration(5000);
                animator.setRepeatCount(-1);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                flag*=-1;
            }
            else {
                button.setText("PLAY");
                status.setText("Paused");
                degree=(Float) animator.getAnimatedValue();
                animator.cancel();
                flag*=-1;
            }
            try{
                myBinder.onTransact(101,Parcel.obtain(),Parcel.obtain(),0);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
        else if(v.getId()==R.id.stop){
            Button button=(Button) findViewById(R.id.play);
            TextView status=(TextView) findViewById(R.id.status);
            button.setText("PLAY");
            status.setText("Stoped");
            degree=0;
            animator.cancel();
            animator=ObjectAnimator.ofFloat(imageView,"rotation",degree,360+degree);
            animator.start();
            animator.cancel();
            try{
                myBinder.onTransact(102,Parcel.obtain(),Parcel.obtain(),0);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
        else if(v.getId()==R.id.quit){
            try{
                myBinder.onTransact(103,Parcel.obtain(),Parcel.obtain(),0);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }
    Thread thread=new Thread(){
        @Override
        public void run(){
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                if(sc!=null&&isPlaying&&!isProcessChange){
                    mHandler.obtainMessage(101).sendToTarget();
                }
            }
        }
    };
    final Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==101){
                int now_time=MyService.mediaPlayer.getCurrentPosition();
                MainActivity.seekBar.setProgress(now_time);
                int now_second=now_time/1000,now_min=0;
                while (now_second>=60){
                    now_second-=60;
                    now_min++;
                }
                if(now_second<10) MainActivity.begin_time.setText("0"+now_min+":0"+now_second);
                else MainActivity.begin_time.setText("0"+now_min+":"+now_second);
            }
        }
    };

    public void Init_Animator(){

    }
}
