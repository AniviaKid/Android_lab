package com.example.asus.lab3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class mWidget extends AppWidgetProvider {
    String[] name = {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis", "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
    String[] price = {"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00", "¥ 14.90", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};
    int[] img={R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};
    boolean flag=false;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            if(!flag){//第一次启用widget，设置为没有任何商品信息且点击进入app
                RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.m_widget);
                Intent intent=new Intent(context,MainActivity.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
                ComponentName me=new ComponentName(context,mWidget.class);
                appWidgetManager.updateAppWidget(me,views);
                flag=true;
            }
            else updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context,Intent intent){
        super.onReceive(context,intent);
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
        if(intent.getAction().equals("widget_broadcast")){
            //Toast.makeText(context,"test",Toast.LENGTH_SHORT).show();
            Bundle tmp=intent.getExtras();
            int pos=tmp.getInt("key");
            RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.m_widget);
            views.setTextViewText(R.id.appwidget_text,name[pos]+"仅售"+price[pos]+"!");
            views.setImageViewResource(R.id.appwidget_img,img[pos]);
            Intent i=new Intent(context,ProductActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt("key",pos);
            i.putExtras(bundle);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
            ComponentName me=new ComponentName(context,mWidget.class);
            appWidgetManager.updateAppWidget(me,views);
        }
    }
}

