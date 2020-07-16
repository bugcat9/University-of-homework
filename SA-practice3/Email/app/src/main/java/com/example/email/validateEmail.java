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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class validateEmail extends AppCompatActivity {

    String geturl = "http://54.198.119.157:8080/validateEmail?_url=%s";

    EditText email_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_email);
        Button button =findViewById(R.id.validatebtn);
        email_et =findViewById(R.id.email_et);  //得到输入的信息
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailurl =email_et.getText().toString();
                //开线程进行http连接
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{

                            Log.d("111111111111111111", "run: "+String.format(geturl,emailurl));
                            URL url =new URL(String.format(geturl,emailurl));
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setDoOutput(true);
//                            connection.setDoInput(true);
                            connection.setUseCaches(false);
                            //connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");//设置参数类型是json格式
                            connection.connect();

                            StringBuilder response = new StringBuilder();
                            int responseCode = connection.getResponseCode();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(connection.getInputStream()));
                            String inputLine;

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            if(response.toString().equals("Y")){
                                Message message =new Message();
                                message.what =0;
                                handler.sendMessage(message);
                            }else {
                                Message message =new Message();
                                message.what =1;
                                handler.sendMessage(message);
                            }


                            in.close();

                        }catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    Handler handler =new Handler(){
      public void handleMessage(Message message){
          switch (message.what){
              case 0:
                  AlertDialog.Builder  dialog = new AlertDialog.Builder(validateEmail.this);
                  dialog.setTitle("验证邮箱");
                  dialog.setMessage("邮箱格式正确");
                  dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                      }
                  });
                  dialog.show();
                  break;
              case 1:
                  AlertDialog.Builder  dialog1 = new AlertDialog.Builder(validateEmail.this);
                  dialog1.setTitle("验证邮箱");
                  dialog1.setMessage("邮箱格式错误");
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
