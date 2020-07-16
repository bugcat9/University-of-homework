package com.example.stockinquiry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarChartActivity extends AppCompatActivity {

    BarChart barChart;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        barChart =findViewById(R.id.barchar);
         intent =getIntent();
         drawBar();
    }

    private void drawBar(){
        String result = intent.getStringExtra("result");
        String []data = result.split(",");

        List<BarEntry> entries = new ArrayList<>();   //数据点的封装类变为BarChart
        Random random = new Random();
        for (int i = 0 ; i < 10 ; i++){
          //  entries.add(new BarEntry(i + 1 , random.nextFloat() + random.nextInt(3) + 35));
            entries.add(new BarEntry(i+1,Float.valueOf(data[i])));
        }
        //同样装载数据：
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColor(getResources().getColor(R.color.colorAccent));  //给线条设置颜色
        set.setDrawValues(true);
        set.setValueTextColor(getResources().getColor(R.color.colorAccent));
        set.setValueTextSize(12);


        BarData Bardata = new BarData(set);      //参数可以是任意个
        Bardata.setBarWidth(0.45f);                   // 单个条形的宽度
        barChart.setData(Bardata);
        barChart.setFitBars(true);
        //barChart.groupBars(0.1f, 0.1f, 0.0f);  //参数一是最左边的条形距离左y轴的距离 参数二是条形对（set和set1组成）与相邻条形对的距离，参数三是条形对中set与set1                                          之间的距离
        barChart.invalidate();

    }
}
