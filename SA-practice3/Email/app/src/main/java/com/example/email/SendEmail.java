package com.example.email;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SendEmail extends AppCompatActivity {

    EditText url_et ;
    EditText payload_et;
    String geturl = "http://54.198.119.157:8080/email?_url=%s&&_payload=%s";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        Button button = findViewById(R.id.sendMsg);
        url_et =findViewById(R.id.url);
        payload_et=findViewById(R.id.payload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailurl =url_et.getText().toString();
                final String payload=payload_et.getText().toString();
                //开线程进行http连接
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            String res ="N";
                            if (emailurl.contains(","))
                                res=sendEmailBatch(emailurl.split(","),payload);
                            else
                                res=sendEmail(emailurl,payload);

                            if(res!=null&&res.equals("Y")){
                                Message message =new Message();
                                message.what =0;
                                handler.sendMessage(message);
                            }else {
                                Message message =new Message();
                                message.what =1;
                                handler.sendMessage(message);
                            }
                    }
                }).start();

            }
        });


    }

    private String sendEmail(String _url,String _payload){
        try{

            Log.d("111111111111111111", "run: "+String.format(geturl,_url,_payload));
            URL url =new URL(String.format(geturl,_url,_payload));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
//            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");//设置参数类型是json格式
            connection.connect();
            StringBuilder response = new StringBuilder();

            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

//            if(response.toString().equals("Y")){
//                Message message =new Message();
//                message.what =0;
//                handler.sendMessage(message);
//            }else {
//                Message message =new Message();
//                message.what =1;
//                handler.sendMessage(message);
//            }

            in.close();
            return response.toString();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String sendEmailBatch(String[] _url,String _payload){
        try{
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("urls", _url);
            data.put("_payload", _payload);
            JSONObject obj = new JSONObject(data);
            System.out.println(obj.toString());

            URL url = new URL("http://54.198.119.157:8080/emailBatch");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type","application/json; charset=UTF-8");//设置参数类型是json格式
            //con.connect();

            StringBuilder response = new StringBuilder();

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                writer.write(obj.toString());
                writer.flush();
                writer.close();
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println(response);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  response.toString();
        }catch (MalformedURLException e){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    Handler handler =new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 0:
                    AlertDialog.Builder  dialog = new AlertDialog.Builder(SendEmail.this);
                    dialog.setTitle("验证邮箱");
                    dialog.setMessage("邮箱发送成功");
                    dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                    break;
                case 1:
                    AlertDialog.Builder  dialog1 = new AlertDialog.Builder(SendEmail.this);
                    dialog1.setTitle("验证邮箱");
                    dialog1.setMessage("邮箱发送失败");
                    dialog1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog1.show();
                    break;
            }
        }
    };
}
