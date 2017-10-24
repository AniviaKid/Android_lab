package com.example.asus.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.mViewHolder>{

    private Context mContext;
    private List<Map<String,String>> mdata;
    private OnItemClickListener mOnItemClickListener;

    public void DeleteData(int position){
        mdata.remove(position);
    }

    public interface OnItemClickListener{
        void OnClick(View v,int position);
        void OnLongClick(View v,int position);
    }
    public void setItemClickListener(OnItemClickListener listener){
        mOnItemClickListener=listener;
    }

    public RecyclerAdapter(Context  context,List<Map<String,String>> datas)
    {
        this.mContext=context;
        this.mdata=datas;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View v=LayoutInflater.from(mContext).inflate(R.layout.recycler_item,parent,false);
        mViewHolder holder=new mViewHolder(v,mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
        holder.icon.setText(mdata.get(position).get("icon"));
        holder.text.setText(mdata.get(position).get("name"));
    }

    @Override
    public int getItemCount(){ return mdata.size();}

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView icon;
        TextView text;
        OnItemClickListener mListener;

        public mViewHolder(View v,OnItemClickListener tmp) {
            super(v);
            icon=(TextView)v.findViewById(R.id.icon);
            text=(TextView)v.findViewById(R.id.text);
            this.mListener=tmp;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.OnClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mListener!=null){
                mListener.OnLongClick(v,getPosition());
            }
            return true;
        }
    }
}


