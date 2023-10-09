package com.example.dafuweng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    RecyclerView rv,rv1;
    LinearAdapter adapter;
    RecordAdapter adapter1;
    Button btn_add;
    Button btn_reduce;
    TextView tv_number;
    ImageView iv_record;
    String number;
    String name;
    ArrayList<Game> list;
    ArrayList<Record> list1;
    public static boolean flag=true;

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    if(rv.getAdapter()==null){
                        adapter.setList(list);
                        rv.setAdapter(adapter);
                    }
                    else{

                        adapter.notifyDataSetChanged();
                        adapter.setList(list);
                    }
                    break;
                case 1:
                    Toast.makeText(GameActivity.this, "转账成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(GameActivity.this, "加款成功", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(GameActivity.this, "扣款成功", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    AlertDialog.Builder builder5=new AlertDialog.Builder(GameActivity.this);
                    View view1= LayoutInflater.from(GameActivity.this).inflate(R.layout.layout_dialog2,null);
                    rv1=view1.findViewById(R.id.rv);
                    rv1.setLayoutManager(new LinearLayoutManager(GameActivity.this));
                    if(rv1.getAdapter()==null){
                        adapter1.setList(list1);
                        rv1.setAdapter(adapter1);
                    }
                    else{
                        adapter1.notifyDataSetChanged();
                        adapter1.setList(list1);
                    }
                    final AlertDialog dialog=builder5.setTitle("记录").setView(view1).show();
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    params.height = 1500 ;
                    dialog.getWindow().setAttributes(params);
                    rv1.scrollToPosition(adapter1.list.size()-1);
                    break;
            }

            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rv=findViewById(R.id.rv);
        btn_add=findViewById(R.id.btn_add);
        btn_reduce=findViewById(R.id.btn_reduce);
        tv_number=findViewById(R.id.tv_number);
        iv_record=findViewById(R.id.iv_record);

        flag=true;

        number=getIntent().getStringExtra("number");
        name=getIntent().getStringExtra("name");
        tv_number.setText("房间号："+number);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new LinearAdapter(this, new LinearAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {

            }
        }, number, name, handler);
        adapter1=new RecordAdapter(this,number);
        iv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            list1=Get_data.get_record(number);
                            handler.sendEmptyMessage(4);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder5=new AlertDialog.Builder(GameActivity.this);
                View view1= LayoutInflater.from(GameActivity.this).inflate(R.layout.layout_dialog,null);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etMoney=view1.findViewById(R.id.et_money);
                etMoney.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})Button btnLogin=view1.findViewById(R.id.btn_login);
                btnLogin.setText("完成");
                etMoney.setHint("金额");
                final AlertDialog dialog=builder5.setTitle("加款").setView(view1).show();
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String money=etMoney.getText().toString();
                                    String result=Get_data.touchHtml(Get_data.url+"dafuweng/add_money.php?number="+number+"&user_name="+name+"&money="+money);
                                    if(result.equals("no")){
                                        handler.sendEmptyMessage(0);
                                    }
                                    if(result.equals("成功")){
                                        //Toast.makeText(mContext, "转账成功", Toast.LENGTH_SHORT).show();
                                        handler.sendEmptyMessage(2);
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
        btn_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder5=new AlertDialog.Builder(GameActivity.this);
                View view1= LayoutInflater.from(GameActivity.this).inflate(R.layout.layout_dialog,null);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etMoney=view1.findViewById(R.id.et_money);
                etMoney.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})Button btnLogin=view1.findViewById(R.id.btn_login);
                btnLogin.setText("完成");
                etMoney.setHint("金额");
                final AlertDialog dialog=builder5.setTitle("扣款").setView(view1).show();
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String money=etMoney.getText().toString();
                                    String result=Get_data.touchHtml(Get_data.url+"dafuweng/reduce_money.php?number="+number+"&user_name="+name+"&money="+money);

                                    if(result.equals("no")){
                                        handler.sendEmptyMessage(0);
                                    }
                                    if(result.equals("成功")){
                                        //Toast.makeText(mContext, "转账成功", Toast.LENGTH_SHORT).show();
                                        handler.sendEmptyMessage(3);
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
        //rv.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag){
                    try {
                        list=Get_data.get_room(number);
                        handler.sendEmptyMessage(0);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        flag=false;
        super.onDestroy();
    }
}