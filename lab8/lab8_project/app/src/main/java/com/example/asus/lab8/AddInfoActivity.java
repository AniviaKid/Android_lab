package com.example.asus.lab8;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2017/12/12.
 */

public class AddInfoActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info);

        findViewById(R.id.add).setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add){
            EditText editText1=(EditText)findViewById(R.id.name_edit);
            EditText editText2=(EditText)findViewById(R.id.birthday_edit);
            EditText editText3=(EditText)findViewById(R.id.gift_edit);
            String name=editText1.getText().toString();
            String birth=editText2.getText().toString();
            String gift=editText3.getText().toString();
            if(name.length()==0){
                Toast.makeText(AddInfoActivity.this,"姓名不可为空",Toast.LENGTH_SHORT).show();
            }
            else{
                boolean flag=MainActivity.myDatabase.insert(name,birth,gift);
                if(flag){
                    //Toast.makeText(AddInfoActivity.this,name+" "+birth+" "+gift,Toast.LENGTH_SHORT).show();
                    AddInfoActivity.this.finish();
                }
                else Toast.makeText(AddInfoActivity.this,"姓名重复，插入失败，请重新检查",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
