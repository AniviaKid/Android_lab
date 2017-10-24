package com.example.asus.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class ExtendActivity extends AppCompatActivity implements View.OnClickListener {

    HashMap<String,String> data=new HashMap<String,String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend);

        Toast.makeText(ExtendActivity.this,"增加了注册、登录功能",Toast.LENGTH_LONG).show();

        findViewById(R.id.sysu).setOnClickListener(ExtendActivity.this);
        findViewById(R.id.button1).setOnClickListener(ExtendActivity.this);
        findViewById(R.id.id1).setOnClickListener(ExtendActivity.this);
        findViewById(R.id.id2).setOnClickListener(ExtendActivity.this);
        findViewById(R.id.button2).setOnClickListener(ExtendActivity.this);

        data.put("123456","6666");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.sysu)//点击头像
        {
            final String[] items=new String[]{"拍摄","从相册选择"};
            AlertDialog.Builder talk1=new AlertDialog.Builder(this);
            talk1.setTitle("上传头像");
            talk1.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0) Toast.makeText(ExtendActivity.this,"您选择了[拍摄]",Toast.LENGTH_SHORT).show();
                    else if(which==1) Toast.makeText(ExtendActivity.this,"您选择了[从相册选择]",Toast.LENGTH_SHORT).show();
                }
            });
            talk1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(ExtendActivity.this,"您选择了[取消]",Toast.LENGTH_SHORT).show();
                }
            });
            talk1.show();
        }
        else if(v.getId()==R.id.button1)//点击登录
        {
            TextInputLayout tmp1= (TextInputLayout) findViewById(R.id.edit1);
            TextInputLayout tmp2= (TextInputLayout) findViewById(R.id.edit2);
            EditText input1 = (EditText) findViewById(R.id.login_num);
            EditText input2 = (EditText) findViewById(R.id.login_password);
            String number=input1.getText().toString();
            String password=input2.getText().toString();
            if(number.isEmpty()) tmp1.setError("学号不能为空");
            else tmp1.setErrorEnabled(false);
            if(password.isEmpty()) tmp2.setError("密码不能为空");
            else tmp2.setErrorEnabled(false);
            if(!number.isEmpty()&&!password.isEmpty())
            {
                boolean flag=false;
                Set set=data.entrySet();
                for(Iterator it=set.iterator();it.hasNext();)
                {
                    HashMap.Entry entry=(HashMap.Entry) it.next();
                    String key=(String)entry.getKey();
                    String value=(String)entry.getValue();
                    if(number.equals(key)&&password.equals(value))
                    {
                        flag=true;
                        break;
                    }
                }
                if(flag)
                {
                    startActivity(new Intent("android.intent.action.login_success"));
                }
                else
                {
                    Snackbar snackbar = Snackbar.make(v,"学号或密码错误",Snackbar.LENGTH_SHORT);
                    snackbar.setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            }
        }
        else if(v.getId()==R.id.id1)
        {
            Snackbar snackbar = Snackbar.make(v,"您选择了学生",Snackbar.LENGTH_SHORT);
            snackbar.setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ExtendActivity.this,"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                }
            });
            snackbar.show();
        }
        else if(v.getId()==R.id.id2)
        {
            Snackbar snackbar = Snackbar.make(v,"您选择了教职工",Snackbar.LENGTH_SHORT);
            snackbar.setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ExtendActivity.this,"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                }
            });
            snackbar.show();
        }
        else if(v.getId()==R.id.button2)
        {
            LayoutInflater factory=LayoutInflater.from(this);
            View textEntryView=factory.inflate(R.layout.registered,null);
            final EditText edit_num=(EditText) textEntryView.findViewById(R.id.registered_number);
            final EditText edit_password=(EditText) textEntryView.findViewById(R.id.registered_password);
            AlertDialog.Builder registered=new AlertDialog.Builder(this);
            registered.setTitle("注册");
            registered.setView(textEntryView);
            registered.setPositiveButton("注册", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String number=edit_num.getText().toString();
                    String password=edit_password.getText().toString();
                    data.put(number,password);
                    Toast.makeText(ExtendActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                }
            });
            registered.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            registered.show();
        }

    }
}
