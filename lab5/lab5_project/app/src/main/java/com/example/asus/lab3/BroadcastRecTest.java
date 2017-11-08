package com.example.asus.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by ASUS on 2017/10/30.
 */

public class BroadcastRecTest extends BroadcastReceiver{
    String[] name = {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis", "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
    String[] price = {"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00", "¥ 14.90", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};
    int[] img={R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(action.equals("static_broadcast")){
            /*Random random=new Random();
            int pos=random.nextInt(name.length);*/
            Bundle tmp=intent.getExtras();
            int pos=tmp.getInt("key");
            Notification.Builder builder=new Notification.Builder(context);
            builder.setContentTitle("新商品热卖");
            builder.setContentText(name[pos]+"仅售"+price[pos]+"!");
            builder.setTicker("您有一条新消息");
            builder.setSmallIcon(img[pos]);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),img[pos]));
            builder.setAutoCancel(true);
            Intent mainIntent=new Intent(context,ProductActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt("key",pos);
            mainIntent.putExtras(bundle);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,mainIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify=builder.build();
            manager.notify(0,notify);
        }
    }
}
