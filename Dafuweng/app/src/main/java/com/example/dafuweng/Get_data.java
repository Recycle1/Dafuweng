package com.example.dafuweng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Get_data {

    static public String url="https://www.recycle11.top/";

    public static String touchHtml(String path) throws Exception {
        System.out.println(path);
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            byte[] data = StreamTool.read(inputStream);
            String text = new String(data);
            //System.out.println("1111111111111111111111111111111111111");

            return text;
        }
        return "no";
    }

    public static ArrayList<Game> get_room(String number) throws Exception {
        ArrayList<Game> list=new ArrayList<>();
        String path=url+"dafuweng/get_room.php?number="+number;
        //System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        InputStream json = conn.getInputStream();
        byte[] data = StreamTool.read(json);
        String json_str = new String(data);
        JSONArray jsonArray = new JSONArray(json_str);
        for(int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String user_name=jsonObject.getString("user_name");
            int money=jsonObject.getInt("money");
            int hide=jsonObject.getInt("hide");
            list.add(new Game(number,user_name,money,hide));
        }
        return list;
    }

    public static ArrayList<Record> get_record(String number) throws Exception {
        ArrayList<Record> list=new ArrayList<>();
        String path=url+"dafuweng/get_record.php?number="+number;
        //System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        InputStream json = conn.getInputStream();
        byte[] data = StreamTool.read(json);
        String json_str = new String(data);
        JSONArray jsonArray = new JSONArray(json_str);
        for(int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String user_name1=jsonObject.getString("user_name1");
            String user_name2=jsonObject.getString("user_name2");
            int money=jsonObject.getInt("money");
            int mode=jsonObject.getInt("mode");
            String datetime=jsonObject.getString("datetime");
            list.add(new Record(number,user_name1,user_name2,money,mode,datetime));
        }
        return list;
    }

    public static class StreamTool {
        //从流中读取数据
        public static byte[] read(InputStream inStream) throws Exception{
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inStream.read(buffer)) != -1)
            {
                outStream.write(buffer,0,len);
            }
            inStream.close();
            return outStream.toByteArray();
        }
    }

}
