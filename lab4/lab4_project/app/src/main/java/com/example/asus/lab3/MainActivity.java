package com.example.asus.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    String[] name = {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis", "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
    String[] price = {"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00", "¥ 14.90", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};
    int[] img={R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};
    List<Map<String, String>> listems = new ArrayList<>();
    List<Map<String, String>> list = new ArrayList<>();
    Map<String, Integer> m = new HashMap<>();
    SimpleAdapter simpleAdapter;
    public static boolean flag;//控制restart时应该显示商品列表还是购物车


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent static_intent=new Intent("static_broadcast");
        sendBroadcast(static_intent);

        EventBus.getDefault().register(this);

        findViewById(R.id.shopcar).setVisibility(View.GONE);//设置购物车不可见
        findViewById(R.id.mainpage).setVisibility(View.GONE);//设置mainpage不可见
        findViewById(R.id.RecyclerView).setVisibility(View.VISIBLE);//设置RecyclerView可见
        findViewById(R.id.shoplist).setVisibility(View.VISIBLE);//设置
        flag=true;

        findViewById(R.id.shoplist).setOnClickListener(this);
        findViewById(R.id.mainpage).setOnClickListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshShopcar");
        registerReceiver(mBroadcastReceiver, intentFilter);

        Map<String, String> tmp = new HashMap<>();
        tmp.put("icon", "*");
        tmp.put("name", "购物车");
        tmp.put("price", "价格");
        list.add(tmp);

        init();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, listems);
        recyclerAdapter.setItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View v, int position) {
                //Toast.makeText(MainActivity.this,position+" ShortClick",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("android.intent.action.ProductActivity");
                Bundle bundle = new Bundle();
                bundle.putInt("key", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void OnLongClick(View v, int position) {
                recyclerAdapter.notifyItemRemoved(position);
                recyclerAdapter.DeleteData(position);
                Toast.makeText(MainActivity.this, "移除第" + (position + 1) + "个商品", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(recyclerAdapter);

        ListView listView = (ListView) findViewById(R.id.shopcar);
        simpleAdapter = new SimpleAdapter(this, list, R.layout.list_item, new String[]{"icon", "name", "price"}, new int[]{R.id.icon, R.id.text, R.id.price});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this,position+" ShortClick",Toast.LENGTH_SHORT).show();
                if (position != 0) {
                    Intent intent = new Intent("android.intent.action.ProductActivity");
                    Bundle bundle = new Bundle();
                    bundle.putInt("key", m.get(list.get(position).get("name")));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(MainActivity.this,position+" LongClick",Toast.LENGTH_SHORT).show();
                if (position != 0) {
                    AlertDialog.Builder talk1 = new AlertDialog.Builder(MainActivity.this);
                    talk1.setTitle("移除商品");
                    talk1.setMessage("从购物车移除" + name[m.get(list.get(position).get("name"))]);
                    talk1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(BasicActivity.this,"您选择了[取消]",Toast.LENGTH_SHORT).show();
                        }
                    });
                    talk1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.remove(position);
                            simpleAdapter.notifyDataSetChanged();
                        }
                    });
                    talk1.show();
                }
                return true;
            }
        });

    }

    public void init() {
        for (int i = 0; i < name.length; i++) {
            Map<String, String> tmp = new HashMap<>();
            tmp.put("icon", String.valueOf(name[i].charAt(0)));
            tmp.put("name", name[i]);
            listems.add(tmp);
            m.put(name[i], i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.shoplist) {
            findViewById(R.id.shopcar).setVisibility(View.VISIBLE);
            findViewById(R.id.mainpage).setVisibility(View.VISIBLE);
            findViewById(R.id.RecyclerView).setVisibility(View.GONE);
            findViewById(R.id.shoplist).setVisibility(View.GONE);
        } else if (v.getId() == R.id.mainpage) {
            findViewById(R.id.shopcar).setVisibility(View.GONE);//设置购物车不可见
            findViewById(R.id.mainpage).setVisibility(View.GONE);//设置mainpage不可见
            findViewById(R.id.RecyclerView).setVisibility(View.VISIBLE);//设置RecyclerView可见
            findViewById(R.id.shoplist).setVisibility(View.VISIBLE);//设置
            simpleAdapter.notifyDataSetChanged();
            //Toast.makeText(MainActivity.this,pos+" "+shoplist.size(),Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {//动态广播
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshShopcar")) {
                /*Bundle bundle = intent.getExtras();
                int pos = bundle.getInt("key");
                Map<String, String> tmp = new HashMap<>();
                tmp.put("icon", String.valueOf(name[pos].charAt(0)));
                tmp.put("name", name[pos]);
                tmp.put("price", price[pos]);
                list.add(tmp);
                simpleAdapter.notifyDataSetChanged();*/

                Bundle bundle = intent.getExtras();
                int pos = bundle.getInt("key");
                Notification.Builder builder=new Notification.Builder(context);
                builder.setContentTitle("马上下单");
                builder.setContentText(name[pos]+"已添加到购物车");
                builder.setTicker("您有一条新消息");
                builder.setSmallIcon(img[pos]);
                builder.setLargeIcon(BitmapFactory.decodeResource(MainActivity.this.getResources(),img[pos]));
                builder.setAutoCancel(true);
                flag=false;
                Intent mainIntent=new Intent(context,MainActivity.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(context,0,mainIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=builder.build();
                manager.notify(0,notify);
            }
            }
        };

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(MessageEvent messageEvent){
            int pos=messageEvent.pos;
            Map<String, String> tmp = new HashMap<>();
            tmp.put("icon", String.valueOf(name[pos].charAt(0)));
            tmp.put("name", name[pos]);
            tmp.put("price", price[pos]);
            list.add(tmp);
            simpleAdapter.notifyDataSetChanged();
        }


        @Override
        protected void onRestart(){
            super.onRestart();
            if(flag==false)
            {
                findViewById(R.id.shopcar).setVisibility(View.VISIBLE);
                findViewById(R.id.mainpage).setVisibility(View.VISIBLE);
                findViewById(R.id.RecyclerView).setVisibility(View.GONE);
                findViewById(R.id.shoplist).setVisibility(View.GONE);
                ProductActivity.instance.finish();
            }
            else{
                findViewById(R.id.shopcar).setVisibility(View.GONE);//设置购物车不可见
                findViewById(R.id.mainpage).setVisibility(View.GONE);//设置mainpage不可见
                findViewById(R.id.RecyclerView).setVisibility(View.VISIBLE);//设置RecyclerView可见
                findViewById(R.id.shoplist).setVisibility(View.VISIBLE);//设置
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            unregisterReceiver(mBroadcastReceiver);
            EventBus.getDefault().unregister(this);
        }

    }


