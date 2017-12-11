package com.example.asus.lab7;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ASUS on 2017/12/12.
 */

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_editor);

        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.load).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.save){
            EditText edit_title=(EditText)findViewById(R.id.title_edit);
            EditText edit_text=(EditText)findViewById(R.id.text_edit);
            String title,text=new String();
            title=edit_title.getText().toString();
            text=edit_text.getText().toString();
            try(FileOutputStream fileOutputStream=openFileOutput(title,MODE_PRIVATE)){
                fileOutputStream.write(text.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (IOException ex){

            }
            Toast.makeText(EditActivity.this,"Save successfully",Toast.LENGTH_SHORT).show();
        }
        else if(v.getId()==R.id.load){
            EditText edit_title=(EditText)findViewById(R.id.title_edit);
            EditText edit_text=(EditText)findViewById(R.id.text_edit);
            String title,text=new String();
            title=edit_title.getText().toString();
            try(FileInputStream fileInputStream=openFileInput(title)){
                int length=fileInputStream.available();
                byte[] buffer=new byte[length];
                StringBuilder sb = new StringBuilder("");
                int len = 0;
                //读取文件内容:
                while ((len = fileInputStream.read(buffer)) > 0) {
                    sb.append(new String(buffer, 0, len));
                }
                fileInputStream.close();
                text=sb.toString();
                edit_text.setText(text);
            }
            catch (IOException ex){
                Toast.makeText(EditActivity.this,"File does not exist",Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId()==R.id.clear){
            EditText edit_text=(EditText)findViewById(R.id.text_edit);
            edit_text.setText("");
        }
        else if (v.getId()==R.id.delete){
            EditText edit_title=(EditText)findViewById(R.id.title_edit);
            String title=new String();
            title=edit_title.getText().toString();
            deleteFile(title);
            Toast.makeText(EditActivity.this,"Delete successfully",Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                MainActivity.instance.finish();
                finish();
                System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
    @Override
    public void onBackPressed(){
        MainActivity.instance.finish();
        finish();
        System.exit(0);
    }
}
