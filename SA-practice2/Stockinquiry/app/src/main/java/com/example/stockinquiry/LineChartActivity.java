package com.example.stockinquiry;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class LineChartActivity extends AppCompatActivity {


    LineChart lineChart;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        lineChart = findViewById(R.id.linechart);  //关联LineChart控件
        intent =getIntent();
        drawLineChart();
    }

    private void drawLineChart(){
      //  初始化一条折线的数据源 一个数据点对应一个Entry对象 Entry对象包含x值和y值
        List<Entry> entries = new ArrayList<>();
        String result = intent.getStringExtra("result");
        String []data = result.split(",");
        float max =0;
        float min =100;
        for (int i = 0 ; i < data.length-1 ; i++){
          //  entries.add(new Entry(i + 1 , random.nextInt(240) + 30));
            float s=Float.valueOf(data[i]);
            max = Math.max(s,max);
            min = Math.min(s,min);
            entries.add(new Entry(i+1,s));
        }

        LineDataSet set = new LineDataSet(entries, "BarDataSet");
        set.setColor(getResources().getColor(R.color.green));         //设置线条颜色
        set.setDrawValues(true);                                      //设置显示数据点值
        set.setValueTextColor(getResources().getColor(R.color.colorPrimary));//设置显示值的字体颜色
        set.setValueTextSize(12);


        LineData lineData = new LineData(set);
        lineChart.setData(lineData);
        lineChart.invalidate();                                                //刷新显示绘图
        lineChart.setBackgroundColor(getResources().getColor(R.color.black));  //设置LineChart的背景颜色
        //设置折线图中每个数据点的选中监听
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(LineChartActivity.this,e.getX()+","+e.getY(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });

        //lineChart.setNoDataTextDescription("无数据");  //设置无数据时显示的描述信息
        lineChart.setNoDataText("无数据");
        lineChart.setDrawGridBackground(false);        //设置是否绘制网格背景
        lineChart.setScaleEnabled(false);              //设置图表是否缩放
        Description description =new Description();
        description.setText("查某月");
        description.setTextColor(Color.GREEN);
        description.setPosition(550,33);
        lineChart.setDescription( description);            //设置图表的描述信息


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.GREEN);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextSize(12f);
        leftAxis.setAxisLineWidth(2);
        leftAxis.setAxisMinValue(new Float(min-0.3));
        leftAxis.setAxisMaxValue(new Float(max+0.3));
        leftAxis.setAxisLineColor(getResources().getColor(R.color.colorAccent));
        lineChart.getAxisRight().setEnabled(false); // 设置不显示右y轴  默认会显示右y轴



        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setAxisLineWidth(2);
        xAxis.setAxisMinValue(0);
        xAxis.setLabelCount(10);
        xAxis.setAxisMaxValue(data.length+3);
        xAxis.setAxisLineColor(getResources().getColor(R.color.colorAccent));
        xAxis.setTextColor(Color.GREEN);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
    }

}
