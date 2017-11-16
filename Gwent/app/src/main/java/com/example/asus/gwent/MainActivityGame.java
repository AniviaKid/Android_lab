package com.example.asus.gwent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.io.*;
import java.util.Random;

public class MainActivityGame extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{
    public static List<GwentCard> my_library=new ArrayList<>();//我方卡组
    public static List<GwentCard> opponent_library=new ArrayList<>();//敌方卡组
    public static List<GwentCard> my_hand=new ArrayList<>();//我方手牌
    public static List<GwentCard> opponent_hand=new ArrayList<>();//敌方手牌
    public static List<GwentCard> my_dust=new ArrayList<>();//我方墓地
    public static List<GwentCard> opponent_dust=new ArrayList<>();//敌方墓地
    public static boolean enabled;//使用手牌使能，只有在我方回合才能使用手牌
    public static boolean enabled_resurrection;//复活使能，只有在该使能激活时才能触发墓地的点击事件
    public boolean my_give_up;//我方是否放弃本轮
    public boolean opponent_give_up;//敌方是否放弃本轮
    public int turn;//为1表示我方行动，为-1表示敌方行动
    public int my_life;//我方血槽
    public int opponent_life;//敌方血槽
    public static GwentBoard board;//棋盘

    int img1[]={R.mipmap.decoy,R.mipmap.commander_horn,R.mipmap.blue_stripes_commando,R.mipmap.crinfrid_reavers_dragon_hunter,R.mipmap.catapult,R.mipmap.thaler,R.mipmap.mysterious_elf,R.mipmap.dijkstra,R.mipmap.prince_stennis,R.mipmap.geralt,R.mipmap.yennefer,R.mipmap.villentretenmerth,R.mipmap.ciri,R.mipmap.dun_banner_medic,R.mipmap.esterad_thyssen,R.mipmap.john_natalis,R.mipmap.philippa_eilhart,R.mipmap.vernon_roche};
    int img2[]={R.mipmap.decoy,R.mipmap.commander_horn,R.mipmap.arachas_behemoth,R.mipmap.arachas,R.mipmap.vampire_ekimmara,R.mipmap.vampire_fleder,R.mipmap.vampire_garkain,R.mipmap.vampire_bruxa,R.mipmap.vampire_katakan,R.mipmap.crone_brewess,R.mipmap.crone_weavess,R.mipmap.crone_whispess,R.mipmap.draug,R.mipmap.kayran,R.mipmap.earth_elemental,R.mipmap.imlerith,R.mipmap.leshen,R.mipmap.triss_merigold,R.mipmap.yennefer,R.mipmap.geralt,R.mipmap.ciri,R.mipmap.villentretenmerth,R.mipmap.mysterious_elf};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.to_hand_dust).setOnClickListener(MainActivityGame.this);
        findViewById(R.id.give_up).setOnLongClickListener(MainActivityGame.this);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("use_card");
        intentFilter.addAction("resave_card");
        registerReceiver(mBroadcastReceiver,intentFilter);


        Init_library();
        Init_parameter();
        board=new GwentBoard(this);
        Init_RecyclerView(board);
        Init_hand();
        enabled=true;
        turn=1;
        for(int i=0;i<5;i++){
            my_dust.add(my_library.get(i));
            my_library.remove(i);
        }
        for(int i=0;i<opponent_hand.size();i++){
            if(opponent_hand.get(i).getType().equals("commander_horn")) ;
            else Use_Card(board,opponent_hand.get(i));
        }
        /*while (1){
            if(turn==1){
                Toast.makeText(MainActivityGame.this,"我方行动",Toast.LENGTH_SHORT).show();
                startActivity(new Intent("android.intent.action.Hand_and_Dust"));
            }
            else{
                Toast.makeText(MainActivityGame.this,"敌方行动",Toast.LENGTH_SHORT).show();
            }
        }*/


    }
    public void Init_library(){ //初始化卡组
        InputStream inputStream1=getResources().openRawResource(R.raw.northern_area);//初始化我方卡组
        try {
            String text=getString(inputStream1),tmp=new String();
            String information[]=new String[img1.length];
            int j=0;
            for(int i=0;i<text.length();i++){
                if(text.charAt(i)!='\n') tmp+=text.charAt(i);
                else{
                    information[j]=tmp;
                    j++;
                    tmp="";
                }
            }
            for(int i=0;i<information.length;i++){
                int power,col,num;
                boolean isHero;
                String type=new String(),power_tmp=new String(),name=new String();
                //power=information[i].charAt(0)-'0';
                for(j=0;j<information[i].length();j++){
                    if(information[i].charAt(j)!=' ') power_tmp+=information[i].charAt(j);
                    else break;
                }
                j++;
                power=Integer.valueOf(power_tmp).intValue();
                //Log.i("power",""+power);
                for(;j<information[i].length();j++){
                    if(information[i].charAt(j)!=' ') type+=information[i].charAt(j);
                    else break;
                }
                //Log.i("type",type);
                j++;
                isHero=information[i].charAt(j)=='1' ? true : false;
                j+=2;
                col=information[i].charAt(j)-'0';
                //Log.i("col",""+col);
                j+=2;
                num=information[i].charAt(j)-'0';
                j+=2;
                for(;j<information[i].length();j++){
                    name+=information[i].charAt(j);
                }
                Log.i("name",name);
                //Log.i("num",""+num);
                GwentCard temp=new GwentCard(img1[i],power,type,isHero,col,i,name);
                for(int k=0;k<num;k++) my_library.add(temp);
            }
            //Toast.makeText(this,getString(inputStream),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream inputStream2=getResources().openRawResource(R.raw.monster);//初始化敌方卡组
        try {
            String text=getString(inputStream2),tmp=new String();
            String information[]=new String[img2.length];
            int j=0;
            for(int i=0;i<text.length();i++){
                if(text.charAt(i)!='\n') tmp+=text.charAt(i);
                else{
                    information[j]=tmp;
                    j++;
                    tmp="";
                }
            }
            for(int i=0;i<information.length;i++){
                int power,col,num,id;
                boolean isHero;
                String type=new String(),power_tmp=new String(),id_tmp=new String(),name=new String();
                for(j=0;j<information[i].length();j++){
                    if(information[i].charAt(j)!=' ') power_tmp+=information[i].charAt(j);
                    else break;
                }
                j++;
                power=Integer.valueOf(power_tmp).intValue();
                Log.i("power",""+power);
                for(;j<information[i].length();j++){
                    if(information[i].charAt(j)!=' ') type+=information[i].charAt(j);
                    else break;
                }
                Log.i("type",type);
                j++;
                isHero=information[i].charAt(j)=='1' ? true : false;
                j+=2;
                col=information[i].charAt(j)-'0';col*=-1;
                Log.i("col",""+col);
                j+=2;
                for(;j<information[i].length();j++){
                    if(information[i].charAt(j)!=' ') id_tmp+=information[i].charAt(j);
                    else break;
                }
                id=Integer.valueOf(id_tmp).intValue();
                Log.i("id",""+id);
                j++;
                num=information[i].charAt(j)-'0';
                Log.i("num",""+num);
                j+=2;
                for(;j<information[i].length();j++){
                    name+=information[i].charAt(j);
                }
                Log.i("name",name);
                GwentCard temp=new GwentCard(img2[i],power,type,isHero,col,id,name);
                for(int k=0;k<num;k++) opponent_library.add(temp);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void Init_parameter(){
        enabled=false;
        enabled_resurrection=false;
        my_give_up=false;
        opponent_give_up=false;
        my_life=2;
        opponent_life=2;
        Random random=new Random();
        int tmp=random.nextInt(10);
        if(tmp%2==0) turn=1;
        else turn=-1;
    }

    public void Init_RecyclerView(GwentBoard board){ //初始化board的RecyclerView
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);//设置每个View横向布置
        board.opponent_third=(RecyclerView) findViewById(R.id.opponent_third);
        board.opponent_third.setLayoutManager(linearLayoutManager1);
        board.opponent_third.setAdapter(board.opponent_third_adapter);

        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        board.opponent_second=(RecyclerView) findViewById(R.id.opponent_second);
        board.opponent_second.setLayoutManager(linearLayoutManager2);
        board.opponent_second.setAdapter(board.opponent_second_adapter);

        LinearLayoutManager linearLayoutManager3=new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        board.opponent_first=(RecyclerView) findViewById(R.id.opponent_first);
        board.opponent_first.setLayoutManager(linearLayoutManager3);
        board.opponent_first.setAdapter(board.opponent_first_adapter);

        LinearLayoutManager linearLayoutManager4=new LinearLayoutManager(this);
        linearLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        board.my_first=(RecyclerView) findViewById(R.id.my_first);
        board.my_first.setLayoutManager(linearLayoutManager4);
        board.my_first.setAdapter(board.my_first_adapter);

        LinearLayoutManager linearLayoutManager5=new LinearLayoutManager(this);
        linearLayoutManager5.setOrientation(LinearLayoutManager.HORIZONTAL);
        board.my_second=(RecyclerView) findViewById(R.id.my_second);
        board.my_second.setLayoutManager(linearLayoutManager5);
        board.my_second.setAdapter(board.my_second_adapter);

        LinearLayoutManager linearLayoutManager6=new LinearLayoutManager(this);
        linearLayoutManager6.setOrientation(LinearLayoutManager.HORIZONTAL);
        board.my_third=(RecyclerView) findViewById(R.id.my_third);
        board.my_third.setLayoutManager(linearLayoutManager6);
        board.my_third.setAdapter(board.my_third_adapter);
    }
    public static String getString(InputStream inputStream) throws IOException { //读取文件转为String
        InputStreamReader inputStreamReader=null;
        inputStreamReader=new InputStreamReader(inputStream);
        BufferedReader reader=new BufferedReader(inputStreamReader);
        StringBuffer sb=new StringBuffer("");
        String line;
        while ((line=reader.readLine())!=null){
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }
    public void Update_Textview(GwentBoard board){ //根据传入的board中的数据更新TextView
        TextView textView1=(TextView) findViewById(R.id.opponent_third_num_power);
        textView1.setText(""+board.opponent_third_power);
        TextView textView2=(TextView) findViewById(R.id.opponent_second_num_power);
        textView2.setText(""+board.opponent_second_power);
        TextView textView3=(TextView) findViewById(R.id.opponent_first_num_power);
        textView3.setText(""+board.opponent_first_power);
        TextView textView4=(TextView) findViewById(R.id.my_first_num_power);
        textView4.setText(""+board.my_first_power);
        TextView textView5=(TextView) findViewById(R.id.my_second_num_power);
        textView5.setText(""+board.my_second_power);
        TextView textView6=(TextView) findViewById(R.id.my_third_num_power);
        textView6.setText(""+board.my_third_power);

        TextView my_power=(TextView) findViewById(R.id.my_power);
        int my_power_num=board.my_first_power+board.my_second_power+board.my_third_power;
        my_power.setText(""+my_power_num);
        TextView opponent_power=(TextView) findViewById(R.id.opponent_power);
        int opponent_power_num=board.opponent_first_power+board.opponent_second_power+board.opponent_third_power;
        opponent_power.setText(""+opponent_power_num);

        TextView my_life_view=(TextView) findViewById(R.id.my_life);
        my_life_view.setText("我方剩余生命："+my_life);
        TextView opponent_life_view=(TextView) findViewById(R.id.oppenent_life);
        opponent_life_view.setText("敌方剩余生命："+opponent_life);
    }

    public void Init_hand(){ //初始化双方手牌
        for(int i=0;i<10;i++){
            Random random=new Random();
            int pos=random.nextInt(my_library.size());
            my_hand.add(my_library.get(pos));
            my_library.remove(pos);
        }
        for(int i=0;i<10;i++){
            Random random=new Random();
            int pos=random.nextInt(opponent_library.size());
            opponent_hand.add(opponent_library.get(pos));
            opponent_library.remove(pos);
        }
    }
    public void Use_Card(final GwentBoard board, final GwentCard card){ //将卡添加到board上并发挥效果，没有从手牌中删除
        if(card.getType().equals("human")){
            board.AddCard(card.getCol(),card);
        }
        else if(card.getType().equals("friends")){
            int num=1;
            if(card.getCol()==1){
                for(int i=0;i<board.my_first_list.size();i++){
                    if(board.my_first_list.get(i).getType().equals("friends")&&board.my_first_list.get(i).getId()==card.getId()) num++;
                }
                board.AddCard(card.getCol(),card);
                if(num!=1) Toast.makeText(MainActivityGame.this,card.getName()+"发动同袍之情，点数倍增",Toast.LENGTH_SHORT).show();
                for(int i=0;i<board.my_first_list.size();i++){
                    if(board.my_first_list.get(i).getType().equals("friends")&&board.my_first_list.get(i).getId()==card.getId()) board.my_first_list.get(i).now_power=board.my_first_list.get(i).getPower()*num;
                }
            }
            else if(card.getCol()==2){
                for(int i=0;i<board.my_second_list.size();i++){
                    if(board.my_second_list.get(i).getType().equals("friends")&&board.my_second_list.get(i).getId()==card.getId()) num++;
                }
                board.AddCard(card.getCol(),card);
                if(num!=1) Toast.makeText(MainActivityGame.this,card.getName()+"发动同袍之情，点数倍增",Toast.LENGTH_SHORT).show();
                for(int i=0;i<board.my_second_list.size();i++){
                    if(board.my_second_list.get(i).getType().equals("friends")&&board.my_second_list.get(i).getId()==card.getId()) board.my_second_list.get(i).now_power=board.my_second_list.get(i).getPower()*num;
                }
            }
            else if(card.getCol()==3){
                for(int i=0;i<board.my_third_list.size();i++){
                    if(board.my_third_list.get(i).getType().equals("friends")&&board.my_third_list.get(i).getId()==card.getId()) num++;
                }
                board.AddCard(card.getCol(),card);
                if(num!=1) Toast.makeText(MainActivityGame.this,card.getName()+"发动同袍之情，点数倍增",Toast.LENGTH_SHORT).show();
                for(int i=0;i<board.my_third_list.size();i++){
                    if(board.my_third_list.get(i).getType().equals("friends")&&board.my_third_list.get(i).getId()==card.getId()) board.my_third_list.get(i).now_power=board.my_third_list.get(i).getPower()*num;
                }
            }
        }
        else if (card.getType().equals("spy")){
            if(card.getCol()>0){ //我方卡组的间谍
                board.AddCard(-1*card.getCol(),card);
                Toast.makeText(MainActivityGame.this,"发动间谍特效，我方额外获得两张卡",Toast.LENGTH_SHORT).show();
                for(int i=0;i<2;i++){
                    Random random=new Random();
                    int tmp=random.nextInt(my_library.size());
                    my_hand.add(my_library.get(tmp));
                    my_library.remove(tmp);
                }
            }
            else {
                board.AddCard(-1*card.getCol(),card);
                Toast.makeText(MainActivityGame.this,"发动间谍特效，敌方额外获得两张卡",Toast.LENGTH_SHORT).show();
                for(int i=0;i<2;i++){
                    Random random=new Random();
                    int tmp=random.nextInt(opponent_library.size());
                    opponent_hand.add(opponent_library.get(tmp));
                    opponent_library.remove(tmp);
                }
            }
        }
        else if (card.getType().equals("doctor")){
            if(card.getCol()>0){
                board.AddCard(card.getCol(),card);
                enabled_resurrection=true;//复活使能置true
                enabled=false;//出牌使能置false
                Toast.makeText(MainActivityGame.this,"请从墓地中选择需要复活的卡",Toast.LENGTH_SHORT).show();
                startActivity(new Intent("android.intent.action.Hand_and_Dust"));
            }
            else { //电脑使用医生卡，计划是优先复活间谍，其次复活点数最高的卡

            }
        }
        else if (card.getType().equals("fire")){
            board.AddCard(card.getCol(),card);
            if(card.getCol()>0){ //我方使用三寒鸦
                if(board.opponent_first_power>=10){ //敌方近战力量>10，摧毁最强卡
                    int max_power=0;
                    for(int i=0;i<board.oppeonent_first_list.size();i++){
                        if(board.oppeonent_first_list.get(i).now_power>max_power&&!board.oppeonent_first_list.get(i).getisHero()) max_power=board.oppeonent_first_list.get(i).now_power;
                    }
                    Toast.makeText(MainActivityGame.this,"火灼发动",Toast.LENGTH_SHORT).show();
                    for(int i=0;i<board.oppeonent_first_list.size();i++){
                        if(board.oppeonent_first_list.get(i).now_power==max_power&&!board.oppeonent_first_list.get(i).getisHero()){
                            board.oppeonent_first_list.remove(i);
                            i--;
                        }
                    }
                }
            }
        }
        /*else if(card.getType().equals("commander_horn")){
            if(turn==1){ //我方行动
                AlertDialog.Builder talk=new AlertDialog.Builder(MainActivityGame.this);
                talk.setTitle("请选择领导号角放置的位置");
                String[] items=new String[]{"近战列","远程列","攻城列"};
                talk.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){ //近战列使用号角
                            if(!board.my_first_special_card){ //该列未使用号角
                                board.AddCard(1,card);
                                for(int i=0;i<board.my_first_list.size();i++){
                                    if(!board.my_first_list.get(i).getisHero()){
                                        board.my_first_list.get(i).isDouble=true;
                                        board.my_first_list.get(i).now_power*=2;
                                    }
                                }
                                Toast.makeText(MainActivityGame.this,"我方近战列点数翻倍",Toast.LENGTH_SHORT).show();
                                board.my_first_special_card=true;
                            }
                            else {
                                Toast.makeText(MainActivityGame.this,"该列已有领导号角",Toast.LENGTH_SHORT).show();
                                my_hand.add(card);
                            }
                        }
                        else if(which==1){ //远程列使用号角
                            if(!board.my_second_special_card){
                                board.AddCard(2,card);
                                for(int i=0;i<board.my_second_list.size();i++){
                                    if(!board.my_second_list.get(i).getisHero()) {
                                        board.my_second_list.get(i).isDouble=true;
                                        board.my_second_list.get(i).now_power*=2;
                                    }
                                }
                                Toast.makeText(MainActivityGame.this,"我方远程列点数翻倍",Toast.LENGTH_SHORT).show();
                                board.my_second_special_card=true;
                            }
                            else {
                                Toast.makeText(MainActivityGame.this,"该列已有领导号角",Toast.LENGTH_SHORT).show();
                                my_hand.add(card);
                            }
                        }
                        else if(which==2){ //攻城列使用号角
                            if(!board.my_third_special_card){
                                board.AddCard(3,card);
                                for(int i=0;i<board.my_third_list.size();i++){
                                    if(!board.my_third_list.get(i).getisHero()) {
                                        board.my_third_list.get(i).isDouble=true;
                                        board.my_third_list.get(i).now_power*=2;
                                    }
                                }
                                Toast.makeText(MainActivityGame.this,"我方攻城列点数翻倍",Toast.LENGTH_SHORT).show();
                                board.my_third_special_card=true;
                            }
                            else {
                                Toast.makeText(MainActivityGame.this,"该列已有领导号角",Toast.LENGTH_SHORT).show();
                                my_hand.add(card);
                            }
                        }
                    }
                });
                talk.show();
            }
            else {
                if(!board.opponent_first_special_card){
                    board.AddCard(-1,card);
                    for(int i=0;i<board.oppeonent_first_list.size();i++){
                        if(!board.oppeonent_first_list.get(i).getisHero()){
                            board.oppeonent_first_list.get(i).isDouble=true;
                            board.oppeonent_first_list.get(i).now_power*=2;
                        }
                    }
                    Toast.makeText(MainActivityGame.this,"敌方近战列点数翻倍",Toast.LENGTH_SHORT).show();
                    board.opponent_first_special_card=true;
                }
                else if(!board.opponent_second_special_card){
                    board.AddCard(-2,card);
                    for(int i=0;i<board.opponent_second_list.size();i++){
                        if(!board.opponent_second_list.get(i).getisHero()){
                            board.opponent_second_list.get(i).isDouble=true;
                            board.opponent_second_list.get(i).now_power*=2;
                        }
                    }
                    Toast.makeText(MainActivityGame.this,"敌方远程列点数翻倍",Toast.LENGTH_SHORT).show();
                    board.opponent_second_special_card=true;
                }
                else if(!board.opponent_third_special_card){
                    board.AddCard(-3,card);
                    for(int i=0;i<board.opponent_third_list.size();i++){
                        if(!board.opponent_third_list.get(i).getisHero()){
                            board.opponent_third_list.get(i).isDouble=true;
                            board.opponent_third_list.get(i).now_power*=2;
                        }
                    }
                    Toast.makeText(MainActivityGame.this,"敌方远程列点数翻倍",Toast.LENGTH_SHORT).show();
                    board.opponent_third_special_card=true;
                }
            }
        }*/
        board.Update();//更新数据适配器和每行的TextView
        Update_Textview(board);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.to_hand_dust){
            startActivity(new Intent("android.intent.action.Hand_and_Dust"));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId()==R.id.give_up){
            my_give_up=true;
            Toast.makeText(MainActivityGame.this,"放弃",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) { //广播里从手牌删去了卡
            String action=intent.getAction();
            if(action.equals("use_card")){
                Bundle bundle=intent.getExtras();
                int pos=bundle.getInt("pos");
                Use_Card(board,my_hand.get(pos));
                my_hand.remove(pos);
            }
            else if(action.equals("resave_card")){
                Bundle bundle=intent.getExtras();
                int pos=bundle.getInt("pos");
                Use_Card(board,my_dust.get(pos));
                my_dust.remove(pos);
            }
        }
    };
}
