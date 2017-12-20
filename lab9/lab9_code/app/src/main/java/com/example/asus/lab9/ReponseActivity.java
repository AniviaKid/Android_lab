package com.example.asus.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS on 2017/12/20.
 */

public class ReponseActivity extends AppCompatActivity implements View.OnClickListener{

    public interface ReposInterface{
        @GET("/users/{user}/repos")
        Observable<List<Github>> getUser(@Path("user") String user);
    }

    public List<Map<String,String>> list=new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.repository_mode_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter=new RecyclerAdapter(this,list);
        recyclerView.setAdapter(recyclerAdapter);

        Bundle bundle=this.getIntent().getExtras();
        String name=bundle.getString("key");
        //Toast.makeText(ReponseActivity.this,name,Toast.LENGTH_SHORT).show();
        ReposInterface reposInterface=ServiceGenerator.createService(ReposInterface.class);
        reposInterface.getUser(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Github>>() {
                    @Override
                    public void onCompleted() {
                        findViewById(R.id.repository_mode_view).setVisibility(View.VISIBLE);
                        findViewById(R.id.repos_progressbar).setVisibility(View.GONE);

                        recyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Github> githubs) {
                        for(int i=0;i<githubs.size();i++){
                            Map<String,String> tmp=new HashMap<>();
                            tmp.put("name",githubs.get(i).getName());
                            tmp.put("id_or_language",githubs.get(i).getLanguage()==null ? "无" : githubs.get(i).getLanguage());
                            tmp.put("blog_or_description",githubs.get(i).getDescription()==null ? "无" : githubs.get(i).getDescription());
                            list.add(tmp);
                        }
                        //recyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }
}
