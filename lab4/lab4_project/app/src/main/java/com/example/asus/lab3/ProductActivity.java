package com.example.asus.lab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2017/10/22.
 */

public class ProductActivity extends AppCompatActivity implements View.OnClickListener{

    String[] name={"Enchated Forest","Arla Milk","Devondale Milk","Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher","Maltesers","Lindt","Borggreve"};
    String[] price={"¥ 5.00","¥ 59.00","¥ 79.00","¥ 2399.00","¥ 179.00","¥ 14.90","¥ 132.59","¥ 141.43","¥ 139.43","¥ 28.90"};
    String[] type={"作者","产地","产地","版本","重量","产地","重量","重量","重量","重量"};
    String[] information={"Johanna Basford","德国","澳大利亚","8GB","2kg","英国","300g","118g","249g","640g"};
    int[] img={R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};
    String[] more_information={"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
    List<Map<String,String>> list=new ArrayList<>();
    int flag=1;//1代表当前星星为空心，-1代表为实心
    int pos;
    public static ProductActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_information);

        instance=this;

        Bundle bundle=this.getIntent().getExtras();
        int position=bundle.getInt("key");
        pos=position;
        //Toast.makeText(ProductActivity.this,position+" ShortClick",Toast.LENGTH_SHORT).show();
        ImageView imageView=(ImageView)findViewById(R.id.product_img);
        imageView.setImageDrawable(getResources().getDrawable(img[position]));
        TextView product_name=(TextView)findViewById(R.id.product_name);
        product_name.setText(name[position]);
        TextView product_price=(TextView)findViewById(R.id.price);
        product_price.setText(price[position]);
        TextView product_type=(TextView)findViewById(R.id.type);
        product_type.setText(type[position]);
        TextView product_information=(TextView)findViewById(R.id.information);
        product_information.setText(information[position]);

        ListView listView=(ListView)findViewById(R.id.more_information);
        init();
        listView.setAdapter(new SimpleAdapter(this,list,R.layout.more_information,new String[]{"key"},new int[]{R.id.text}));

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.star).setOnClickListener(this);
        findViewById(R.id.add_to_shoplist).setOnClickListener(this);
    }
    public void init() {
        for(int i=0;i<more_information.length;i++){
            Map<String,String> tmp=new HashMap<>();
            tmp.put("key",more_information[i]);
            list.add(tmp);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back){
            ProductActivity.this.finish();
            MainActivity.flag=true;
        }
        else if(v.getId()==R.id.star){
            flag*=-1;
            if(flag==1)
            {
                ImageView tmp=(ImageView)findViewById(R.id.star);
                tmp.setImageDrawable(getResources().getDrawable(R.mipmap.empty_star));
            }
            else if(flag==-1)
            {
                ImageView tmp=(ImageView)findViewById(R.id.star);
                tmp.setImageDrawable(getResources().getDrawable(R.mipmap.full_star));
            }
        }
        else if (v.getId()==R.id.add_to_shoplist){
            Toast.makeText(ProductActivity.this,name[pos]+"已添加到购物车",Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new MessageEvent(pos));
            Intent intent=new Intent();
            intent.setAction("action.refreshShopcar");
            Bundle bundle=new Bundle();
            bundle.putInt("key",pos);
            intent.putExtras(bundle);
            sendBroadcast(intent);
        }
    }
}
