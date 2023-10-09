package com.example.dafuweng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    public ArrayList<Record> list;
    public String number;

    public RecordAdapter(Context context, String number){
        this.mContext=context;
        this.number=number;
    }

    public void setList(ArrayList<Record> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new linearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_record_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        linearViewHolder holder1=(linearViewHolder)holder;
        holder1.tv_name1.setText(list.get(position).user_name1);
        holder1.tv_datetime.setText(list.get(position).datetime);
        switch (list.get(position).mode){
            case 0:
                holder1.tv_2.setVisibility(View.GONE);
                holder1.tv_name2.setVisibility(View.GONE);
                holder1.tv_3.setText(" 加款");
                holder1.tv_money.setText("¥"+list.get(position).money);
                break;
            case 1:
                holder1.tv_2.setVisibility(View.GONE);
                holder1.tv_name2.setVisibility(View.GONE);
                holder1.tv_3.setText(" 扣款");
                holder1.tv_money.setText("¥"+list.get(position).money);
                break;
            case 2:
                holder1.tv_2.setVisibility(View.VISIBLE);
                holder1.tv_name2.setVisibility(View.VISIBLE);
                holder1.tv_2.setText(" 向 玩家 ");
                holder1.tv_name2.setText(list.get(position).user_name2);
                holder1.tv_3.setText(" 转账");
                holder1.tv_money.setText("¥"+list.get(position).money);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class linearViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name1;
        private TextView tv_2;
        private TextView tv_name2;
        private TextView tv_3;
        private TextView tv_money;
        private TextView tv_datetime;

        public linearViewHolder(View itemView) {
            super(itemView);
            tv_name1=itemView.findViewById(R.id.tv_name1);
            tv_2=itemView.findViewById(R.id.tv_2);
            tv_name2=itemView.findViewById(R.id.tv_name2);
            tv_3=itemView.findViewById(R.id.tv_3);
            tv_money=itemView.findViewById(R.id.tv_money);
            tv_datetime=itemView.findViewById(R.id.tv_datetime);
        }
    }

}
