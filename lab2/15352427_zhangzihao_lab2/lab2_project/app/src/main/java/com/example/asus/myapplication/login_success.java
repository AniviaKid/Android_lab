package com.example.asus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ASUS on 2017/10/15.
 */

public class login_success extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success);

        findViewById(R.id.return_).setOnClickListener(login_success.this);

        Toast.makeText(login_success.this,"登录成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.return_) startActivity(new Intent("android.intent.action.ExtendActivity"));
    }
}