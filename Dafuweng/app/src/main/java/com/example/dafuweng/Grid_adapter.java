package com.example.dafuweng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Grid_adapter extends RecyclerView.Adapter<Grid_adapter.linearViewHolder>{

    private Context mContext;
    private OnItemClickListener mlistener;
    public ArrayList<Game> list;
    public String number;
    public String name;
    public Handler handler;

    public Grid_adapter(Context context, OnItemClickListener listener,String number,String name,Handler handler){
        this.mContext=context;
        this.mlistener=listener;
        this.number=number;
        this.name=name;
        this.handler=handler;
    }

    public void setList(ArrayList<Game> list) {
        this.list = list;
    }

    @Override
    public linearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new linearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(linearViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(list.get(position).hide==0||name.equals(list.get(position).user_name)){
            holder.tv_money.setText(String.valueOf(list.get(position).money));
        }
        else{
            holder.tv_money.setText("隐藏");
        }
        holder.tv_name.setText(list.get(position).user_name);
        if(name.equals(list.get(position).user_name)){
            holder.btn_hide.setVisibility(View.VISIBLE);
            holder.btn_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(holder.btn_hide.getText().toString().equals("隐藏")) {
                                    if (Get_data.touchHtml(Get_data.url + "dafuweng/hide.php?user_name=" + name + "&number=" + number + "&hide=1").equals("成功")) {
                                        holder.btn_hide.setText("取消隐藏");
                                    }
                                }
                                else{
                                    if (Get_data.touchHtml(Get_data.url + "dafuweng/hide.php?user_name=" + name + "&number=" + number + "&hide=0").equals("成功")) {
                                        holder.btn_hide.setText("隐藏");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }
        else{
            holder.btn_payment.setVisibility(View.VISIBLE);
            holder.btn_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder5=new AlertDialog.Builder(mContext);
                    View view1=LayoutInflater.from(mContext).inflate(R.layout.layout_dialog,null);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etMoney=view1.findViewById(R.id.et_money);
                    etMoney.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"})Button btnLogin=view1.findViewById(R.id.btn_login);
                    final AlertDialog dialog=builder5.setTitle("输入金额").setView(view1).show();
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if(Get_data.touchHtml(Get_data.url+"dafuweng/update_money.php?number="+number+"&user_name1="+name+"&user_name2="+list.get(position).user_name+"&money="+Integer.valueOf(etMoney.getText().toString())).equals("成功")){
                                            //Toast.makeText(mContext, "转账成功", Toast.LENGTH_SHORT).show();
                                            handler.sendEmptyMessage(1);
                                            dialog.dismiss();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });



                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class linearViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_money;
        private Button btn_payment;
        private Button btn_hide;

        public linearViewHolder(View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_money=itemView.findViewById(R.id.tv_money);
            btn_payment=itemView.findViewById(R.id.btn_payment);
            btn_hide=itemView.findViewById(R.id.btn_hide);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }

}

