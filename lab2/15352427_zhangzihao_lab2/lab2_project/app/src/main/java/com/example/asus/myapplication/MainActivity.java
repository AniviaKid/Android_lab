package com.example.asus.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.basic).setOnClickListener(MainActivity.this);
        findViewById(R.id.extend).setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.basic) startActivity(new Intent("android.intent.action.BasicActivity"));
        else if(v.getId()==R.id.extend) startActivity(new Intent("android.intent.action.ExtendActivity"));
    }
}
