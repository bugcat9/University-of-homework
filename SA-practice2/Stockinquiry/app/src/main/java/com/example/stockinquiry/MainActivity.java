package com.example.stockinquiry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends BaseActicity implements View.OnClickListener{


    Button btn_open,btn_close,btn_turn,btn_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_open =findViewById(R.id.btn_open);
        btn_close = findViewById(R.id.btn_close);
        btn_turn =findViewById(R.id.btn_turn);
        btn_sum = findViewById(R.id.btn_sum);

       btn_open.setOnClickListener(this);
       btn_close.setOnClickListener(this);
       btn_turn.setOnClickListener(this);
       btn_sum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        intent.setClass(this, OpeningActivity.class);
        switch (v.getId()){
            case R.id.btn_open:
                intent.putExtra("InquiryDate",1);
                break;
            case R.id.btn_close:
                intent.putExtra("InquiryDate",2);
                break;
            case R.id.btn_turn:
                intent.putExtra("InquiryDate",3);
                break;
            case R.id.btn_sum:
                intent.putExtra("InquiryDate",4);
                break;

        }
        //开启连接
        new Thread(new Runnable() {
            @Override
            public void run() {
               Connection.getConnection();
            }
        }).start();
        startActivity(intent);
    }
}
