package com.example.asus.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences password;
    boolean flag;
    public static MainActivity instance=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=MainActivity.this;

        findViewById(R.id.button_clear).setOnClickListener(MainActivity.this);
        findViewById(R.id.button_ok).setOnClickListener(MainActivity.this);

        flag=false;
        password= getSharedPreferences("password", 0);
        String str_password=new String();
        str_password=password.getString("pass","default");
        if(str_password.equals("default")){ //尚未创建密码

        }
        else{
            Change_View();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_clear){
            EditText editText1=(EditText)findViewById(R.id.edit1);
            EditText editText2=(EditText)findViewById(R.id.edit2);
            editText1.setText("");
            editText2.setText("");
        }
        else if(v.getId()==R.id.button_ok){
            //Toast.makeText(MainActivity.this,"123",Toast.LENGTH_SHORT).show();
            if(!flag){ //创建密码
                EditText editText1=(EditText)findViewById(R.id.edit1);
                EditText editText2=(EditText)findViewById(R.id.edit2);
                String input1,input2=new String();
                input1=editText1.getText().toString();
                input2=editText2.getText().toString();
                if(!input1.equals(input2)) Toast.makeText(MainActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                else if(input1.length()==0) Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                else{
                    SharedPreferences.Editor editor=password.edit();
                    editor.putString("pass",input1);
                    editor.commit();
                    Change_View();
                    //Toast.makeText(MainActivity.this,"密码为"+input1,Toast.LENGTH_SHORT).show();
                }
            }
            else {
                EditText editText2=(EditText)findViewById(R.id.edit2);
                String input,str_password=new String();
                input=editText2.getText().toString();
                str_password=password.getString("pass","default");
                if(input.length()==0) Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                else if(!input.equals(str_password)) Toast.makeText(MainActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                else startActivity(new Intent("EditActivity")); //进入编辑页面
            }
        }
    }
    public void Change_View(){
        flag=true;
        EditText editText1=(EditText)findViewById(R.id.edit1);
        EditText editText2=(EditText)findViewById(R.id.edit2);
        editText2.setText("");
        Guideline guideline=(Guideline)findViewById(R.id.guideline);
        editText1.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams layoutParams=(ConstraintLayout.LayoutParams)guideline.getLayoutParams();
        layoutParams.setMargins(0,0,0,0);
        editText2.setHint("Password");
    }
}
