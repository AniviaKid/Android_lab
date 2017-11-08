package com.example.asus.myapplication;

import android.content.DialogInterface;
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

public class BasicActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        findViewById(R.id.sysu).setOnClickListener(BasicActivity.this);
        findViewById(R.id.button1).setOnClickListener(BasicActivity.this);
        findViewById(R.id.id1).setOnClickListener(BasicActivity.this);
        findViewById(R.id.id2).setOnClickListener(BasicActivity.this);
        findViewById(R.id.button2).setOnClickListener(BasicActivity.this);
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
                    if(which==0) Toast.makeText(BasicActivity.this,"您选择了[拍摄]",Toast.LENGTH_SHORT).show();
                    else if(which==1) Toast.makeText(BasicActivity.this,"您选择了[从相册选择]",Toast.LENGTH_SHORT).show();
                }
            });
            talk1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(BasicActivity.this,"您选择了[取消]",Toast.LENGTH_SHORT).show();
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
                if(number.equals("123456")&&password.equals("6666"))
                {
                    Snackbar snackbar = Snackbar.make(v,"登录成功",Snackbar.LENGTH_SHORT);
                    snackbar.setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
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
                    Toast.makeText(BasicActivity.this,"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BasicActivity.this,"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                }
            });
            snackbar.show();
        }
        else if(v.getId()==R.id.button2)
        {
            RadioGroup radio= (RadioGroup) findViewById(R.id.id0);
            if(radio.getCheckedRadioButtonId()==R.id.id1)
            {
                Snackbar snackbar = Snackbar.make(v,"学生注册功能尚未启用",Snackbar.LENGTH_SHORT);
                snackbar.setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackbar.show();
            }
            else if(radio.getCheckedRadioButtonId()==R.id.id2)
            {
                Toast.makeText(BasicActivity.this,"教职工注册功能尚未启用",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
