

package com.example.stockinquiry;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

public class OpeningActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_day,btn_month,btn_avg;
    EditText et_day ,et_month,et_avg;

    int chooseChart=   1;         //显示的形式
    int InquiryDate = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        init();
    }

    private void init(){
        btn_day = findViewById(R.id.btn_day);
        et_day = findViewById(R.id.et_day);
        btn_month =findViewById(R.id.btn_month);
        et_month =findViewById(R.id.et_month);
        btn_avg =findViewById(R.id.btn_avg);
        et_avg =findViewById(R.id.et_avg);

        btn_day.setOnClickListener(this);
        btn_month.setOnClickListener(this);
        btn_avg.setOnClickListener(this);
        InquiryDate =getIntent().getIntExtra("InquiryDate",-1); //想得到的数据
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.line_item:
                chooseChart =1;
                break;
            case R.id.bar_item:
                chooseChart =2;
                break;
//            case R.id.pie_item:
//                chooseChart =3;
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }


    @Override
    public void onClick(View v) {

        Connection.getConnection().setListener(listener);
        if (!Connection.isExectue){
            Connection.isExectue = true;
            Connection.getConnection().execute();       //执行任务
        }


        switch (v.getId()){
            case R.id.btn_day:  //查询天
                final String date = et_day.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "01#"+InquiryDate+"#"+date;
                        Connection.getConnection().sendMsg(msg);
                    }
                }).start();
                break;
            case R.id.btn_month:    //查询月
                final String date1 = et_month.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "02#"+InquiryDate+"#"+date1+"-01";
                        Connection.getConnection().sendMsg(msg);
                    }
                }).start();
                break;
            case R.id.btn_avg://查询平均值
                final String date2 = et_avg.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "03#"+InquiryDate+"#"+date2+"-01";
                        Connection.getConnection().sendMsg(msg);
                    }
                }).start();
                break;
        }
    }

    DataListener listener =new DataListener() {
        @Override
        public void onDay(String result) {
            AlertDialog.Builder dialog =new  AlertDialog.Builder(OpeningActivity.this);
            dialog.setTitle("查询结果");

            if (result.hashCode()==0)
                dialog.setMessage("查询无此数据");
            else
                dialog.setMessage(result);

            dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }

        @Override
        public void onMonth(String result) {
            Intent intent =new Intent();
            if (chooseChart ==1)
                intent.setClass(OpeningActivity.this,LineChartActivity.class);
            else if (chooseChart ==2)
                intent.setClass(OpeningActivity.this, BarChartActivity.class);
            else if (chooseChart ==3)
                intent.setClass(OpeningActivity.this, PieChart.class);

            intent.putExtra("result",result);
            startActivity(intent);
        }

        @Override
        public void onAvg(String result) {
            AlertDialog.Builder dialog =new  AlertDialog.Builder(OpeningActivity.this);
            dialog.setTitle("查询的平均值");
            if (result.hashCode()==0)
                dialog.setMessage("查询无此数据");
            else
                dialog.setMessage(result);

            dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }

        @Override
        public void onYear(String result) {

        }

    };

}
