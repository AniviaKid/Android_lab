package com.example.asus.lab8;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static MyDatabase myDatabase;
    List<Map<String,String>> list=new ArrayList<>();
    SimpleAdapter simpleAdapter;
    Cursor contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabase=new MyDatabase(this,MyDatabase.Database_name);
        findViewById(R.id.add).setOnClickListener(this);
        contacts=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        ListView listView=(ListView)findViewById(R.id.info);
        simpleAdapter=new SimpleAdapter(this,list,R.layout.list_item,new String[]{"name","birth","gift"},new int[]{R.id.item_name,R.id.item_birthday,R.id.item_gift});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String name=list.get(position).get("name");
                String birth=list.get(position).get("birth");
                String gift=list.get(position).get("gift");
                LayoutInflater factory=LayoutInflater.from(MainActivity.this);
                View entryView=factory.inflate(R.layout.dialoglayout,null);
                TextView name_view=(TextView) entryView.findViewById(R.id.name_edit);
                final EditText birth_view=(EditText) entryView.findViewById(R.id.birthday_edit);
                final EditText gift_view=(EditText) entryView.findViewById(R.id.gift_edit);
                TextView number=(TextView) entryView.findViewById(R.id.phone_number);
                name_view.setText(name);
                birth_view.setText(birth);
                gift_view.setText(gift);

                if(contacts.moveToFirst()==false){
                    number.setText("电话：无");
                }
                else{
                    boolean flag=false;
                    do{
                        String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                        String Number = "";
                        while(phone.moveToNext()) {
                            Number += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
                        }
                        String username = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        //Toast.makeText(MainActivity.this,username+" "+Number,Toast.LENGTH_SHORT).show();
                        if(username.equals(name)){
                            flag=true;
                            number.setText("电话："+Number);
                        }
                    }while (contacts.moveToNext());
                    if(!flag) number.setText("电话：无");
                }

                //设置对话框
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("(￣▽￣)~*");
                dialog.setView(entryView);
                dialog.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String birth_new=birth_view.getText().toString();
                        String gift_new=gift_view.getText().toString();
                        myDatabase.update(name,birth_new,gift_new);
                        UpdateListview();
                    }
                });
                dialog.show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String name=list.get(position).get("name");
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
                        myDatabase.delete(name);
                        UpdateListview();
                    }
                });
                dialog.show();
                return true;
            }
        });
        //myDatabase.insert("213","123","321");
        UpdateListview();
    }
    public void UpdateListview(){
        SQLiteDatabase db=myDatabase.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+MyDatabase.Table_name,null);
        if(cursor.moveToFirst()==false) return;
        else{
            list.clear();
            do{
                int name_index=cursor.getColumnIndex("name");
                String name=cursor.getString(name_index);
                int birth_index=cursor.getColumnIndex("birth");
                String birth=cursor.getString(birth_index);
                int gift_index=cursor.getColumnIndex("gift");
                String gift=cursor.getString(gift_index);
                Map<String,String> tmp=new HashMap<>();
                tmp.put("name",name);
                tmp.put("birth",birth);
                tmp.put("gift",gift);
                list.add(tmp);
            }while (cursor.moveToNext());
            simpleAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onRestart(){
        super.onRestart();
        UpdateListview();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add){
            startActivity(new Intent("Add_Info_Activity"));
        }
    }

}
