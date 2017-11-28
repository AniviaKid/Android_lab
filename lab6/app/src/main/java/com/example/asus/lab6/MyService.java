package com.example.asus.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Map;

/**
 * Created by ASUS on 2017/11/27.
 */

public class MyService extends Service {

    public MyBinder myBinder=new MyBinder();

    public static MediaPlayer mediaPlayer=new MediaPlayer();

    @Override
    public void onCreate(){ //service第一次创建时执行
        super.onCreate();
        Log.i("service","service已创建");
        try{
            File directory_music = new File(Environment.getExternalStorageDirectory(), "Music");
            mediaPlayer.setDataSource(directory_music+"/melt.mp3");
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            MainActivity.seekBar.setMax(mediaPlayer.getDuration());
            MainActivity.min=0;
            MainActivity.second=mediaPlayer.getDuration();
            MainActivity.second/=1000;
            while (MainActivity.second>=60){
                MainActivity.second-=60;
                MainActivity.min++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if(MainActivity.second<10) MainActivity.end_time.setText("0"+MainActivity.min+":0"+MainActivity.second);
        else MainActivity.end_time.setText("0"+MainActivity.min+":"+MainActivity.second);
        //Toast.makeText(MyService.this,"已创建"+" "+MainActivity.second,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){ //每次startService都会执行
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if(code==101){ //播放/暂停按钮，service
                Log.i("播放","101");
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    MainActivity.isPlaying=false;
                }
                else{
                    mediaPlayer.start();
                    MainActivity.isPlaying=true;
                }
            }
            else if(code==102){ //停止按钮,service
                Log.i("停止","102");
                MainActivity.seekBar.setProgress(0);
                MainActivity.begin_time.setText("00:00");
                MainActivity.isPlaying=false;
                if(mediaPlayer!=null){
                    mediaPlayer.stop();
                    try{
                        mediaPlayer.prepare();
                        //mediaPlayer.seekTo(0);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            else if(code==103){ //退出按钮，service
                Log.i("退出","103");
                System.exit(0);
                //Toast.makeText(MyService.this,"退出",Toast.LENGTH_SHORT).show();
            }
            else if(code==104){ //停止拖动进度条
                mediaPlayer.seekTo(MainActivity.seekBar.getProgress());
            }
            else if(code==105){ //拖动进度条过程中

            }

            return super.onTransact(code,data,reply,flags);
        }
    }
}
