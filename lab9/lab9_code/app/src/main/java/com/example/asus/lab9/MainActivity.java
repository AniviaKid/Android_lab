package com.example.asus.lab9;

import android.annotation.TargetApi;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.TimeUnit;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.Subject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public interface GithubInterface{
        @GET("/users/{user}")
        Observable<Github> getUser(@Path("user") String user);
    }

    public List<Map<String,String>> list=new ArrayList<>();
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.fetch).setOnClickListener(this);

        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.user_mode_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter=new RecyclerAdapter(this,list);
        recyclerAdapter.setItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View v, int position) {
                Intent intent=new Intent("android.intent.action.REPONSE");
                Bundle bundle=new Bundle();
                bundle.putString("key",list.get(position).get("name"));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void OnLongClick(View v, final int position) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("是否删除");
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });
        recyclerView.setAdapter(recyclerAdapter);

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fetch){
            EditText editText=(EditText) findViewById(R.id.edit);
            String name=editText.getText().toString();
            findViewById(R.id.user_mode_view).setVisibility(View.GONE);//View消失
            findViewById(R.id.user_progressbar).setVisibility(View.VISIBLE);//进度条出现，开始网络请求
            GithubInterface githubInterface=ServiceGenerator.createService(GithubInterface.class);
            githubInterface.getUser(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Github>() {
                    @Override
                    public void onCompleted() {
                        findViewById(R.id.user_mode_view).setVisibility(View.VISIBLE);//View显示
                        findViewById(R.id.user_progressbar).setVisibility(View.GONE);//进度条消失
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Github github) {
                        //Toast.makeText(MainActivity.this,github.getId()+"",Toast.LENGTH_SHORT).show();
                        Map<String,String> tmp=new HashMap<>();
                        tmp.put("name",github.getLogin());
                        tmp.put("id_or_language","id:"+github.getId());
                        tmp.put("blog_or_description","blog:"+github.getBlog());
                        //Toast.makeText(MainActivity.this,github.getBlog(),Toast.LENGTH_SHORT).show();
                        list.add(tmp);
                        recyclerAdapter.notifyDataSetChanged();
                    }

                });
        }
        else if(v.getId()==R.id.clear){
            list.clear();
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}
