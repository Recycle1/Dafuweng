package com.example.dafuweng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tv_number;
    private Button btn_create;
    private Button btn_begin;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(MainActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(MainActivity.this, "用户名已占用", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if(message.arg1==0){
                        Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent=new Intent(MainActivity.this,GameActivity.class);
                    intent.putExtra("number",((String [])message.obj)[0]);
                    intent.putExtra("name",((String [])message.obj)[1]);
                    startActivity(intent);
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "请输入房间号或用户名", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_create=findViewById(R.id.btn_create);
        btn_begin=findViewById(R.id.btn_start);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder5=new AlertDialog.Builder(MainActivity.this);
                View view1= LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dialog,null);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etName=view1.findViewById(R.id.et_money);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})Button btnLogin=view1.findViewById(R.id.btn_login);
                final AlertDialog dialog=builder5.setTitle("创建房间").setView(view1).show();
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String number="";
                                    Random random=new Random();
                                    for(int i=0;i<4;i++){
                                        number+=String.valueOf(random.nextInt(9));
                                    }
                                    String name=etName.getText().toString();
                                    String result=Get_data.touchHtml(Get_data.url+"dafuweng/get_name_used.php?number="+number+"&name="+name);

                                    if(result.equals("no")){
                                        handler.sendEmptyMessage(0);
                                    }
                                    if(result.equals("有")){
                                        //Toast.makeText(mContext, "转账成功", Toast.LENGTH_SHORT).show();
                                        handler.sendEmptyMessage(1);
                                        dialog.dismiss();
                                    }
                                    else{
                                        String result1=Get_data.touchHtml(Get_data.url+"dafuweng/create_room.php?number="+number+"&name="+name);
                                        if(result1.equals("成功")){
                                            Message message=new Message();
                                            message.what=2;
                                            message.arg1=0;
                                            String [] strs=new String[2];
                                            strs[0]=number;
                                            strs[1]=name;
                                            message.obj=strs;
                                            handler.sendMessage(message);
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
        });
        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder5=new AlertDialog.Builder(MainActivity.this);
                View view1= LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dialog1,null);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etNumber=view1.findViewById(R.id.et_number);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etName=view1.findViewById(R.id.et_name);
                etNumber.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})Button btnLogin=view1.findViewById(R.id.btn_login);
                final AlertDialog dialog=builder5.setTitle("输入房间").setView(view1).show();
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String number=etNumber.getText().toString();
                                    String name=etName.getText().toString();
                                    String result=Get_data.touchHtml(Get_data.url+"dafuweng/get_name_used.php?number="+number+"&name="+name);
                                    if(number.length()==0||name.length()==0){
                                        handler.sendEmptyMessage(3);
                                    }
                                    else if(result.equals("no")){
                                        handler.sendEmptyMessage(0);
                                    }
                                    else{
                                        boolean flag=false;
                                        if(result.equals("有")){//Toast.makeText(mContext, "转账成功", Toast.LENGTH_SHORT).show();
                                            flag=true;
                                        }
                                        else{
                                            String result1=Get_data.touchHtml(Get_data.url+"dafuweng/create_room.php?number="+number+"&name="+name);
                                            if(result1.equals("no")){
                                                handler.sendEmptyMessage(0);
                                            }
                                            else if(result1.equals("成功")){
                                                flag=true;
                                            }
                                        }
                                        if(flag){
                                            Message message=new Message();
                                            message.what=2;
                                            message.arg1=1;
                                            String [] strs=new String[2];
                                            strs[0]=number;
                                            strs[1]=name;
                                            message.obj=strs;
                                            handler.sendMessage(message);
                                            dialog.dismiss();
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
        });
    }
}