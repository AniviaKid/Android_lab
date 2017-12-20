package com.example.asus.lab9;

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

    public void DeleteDataFromList(int position){
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
        holder.name.setText(mdata.get(position).get("name"));
        holder.id_or_language.setText(mdata.get(position).get("id_or_language"));
        holder.blog_or_description.setText(mdata.get(position).get("blog_or_description"));
    }

    @Override
    public int getItemCount(){ return mdata.size();}

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView name;
        TextView id_or_language;
        TextView blog_or_description;
        OnItemClickListener mListener;

        public mViewHolder(View v,OnItemClickListener tmp) {
            super(v);
            name=(TextView)v.findViewById(R.id.name);
            id_or_language=(TextView)v.findViewById(R.id.id_or_language);
            blog_or_description=(TextView)v.findViewById(R.id.blog_or_description);
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

