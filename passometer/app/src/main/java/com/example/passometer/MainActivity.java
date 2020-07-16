package com.example.passometer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    /** Called when the activity is first created. */
    //Create a LOG label
    private Button mWriteButton, mStopButton;
    private LineChart mChart;
    private boolean doWrite = false;
    private SensorManager sm;
    private float lowX = 0, lowY = 0, lowZ = 0;
    private final float FILTERING_VALAUE = 0.1f;    //筛选值
    private TextView AT,ACT,StepNumText;
    private int StepNum = 0;
    //存放三轴的数据
    private float[] oriValues  =new float[3];

    private final int ValueNum = 4;

    //用于存放计算阈值波峰波谷差值
    private float[] tempValue = new float[ValueNum];

    private int tempcount = 0;

    private boolean isDirectioup = false;

    private int continueUpCount = 0;    //持续上升的次数

    private int continueUpFormerCount = 0;//记录波峰的上升次数

    private boolean laststatus = false; //上一点的状态，上升还是下降

    private float peakOfWave = 0;//波峰值

    private float valleyOfWave = 0;//波谷值

    private long timeofThisPeak = 0; //此次波峰时间

    private long timeOflastPeak = 0;   //上次波峰时间

    private long timeOfNow = 0;//当前的时间

    private float   gravityNew = 0; //当前传感器的值

    private float gravityOld = 0;//上次传感器的值

    //动态阈值
    private final float InitiaValue = (float)1.3;

    //
    private float ThreadValue = (float)2.0;

    private int TimeInterVal = 250;

    int maxcount=10;

    //写文件申请的权限
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        AT = findViewById(R.id.AT);
//        ACT = findViewById(R.id.onAccuracyChanged);
        StepNumText =findViewById(R.id.StepNum);
//Create a SensorManager to get the system’s sensor service
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
/*
*Using the most common method to register an event
* Parameter1 ：SensorEventListener detectophone
* Parameter2 ：Sensor one service could have several Sensor
realizations.Here,We use getDefaultSensor to get the defaulted Sensor
* Parameter3 ：Mode We can choose the refresh frequency of the
data change
* */
// Register the acceleration sensor
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        //Highsampling rate；.SENSOR_DELAY_NORMAL means a lower sampling rate
        try {
            FileOutputStream fout = openFileOutput("acc.txt",
                    Context.MODE_PRIVATE);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mWriteButton = (Button) findViewById(R.id.Button_Write);
        mWriteButton.setOnClickListener(this);
        mStopButton = (Button) findViewById(R.id.Button_Stop);
        mStopButton.setOnClickListener(this);
        mChart =findViewById(R.id.linechart);

    }

    public void onPause(){
        super.onPause();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.Button_Write) {
            doWrite = true;
//            addEntry();
            Toast.makeText(this,"允许写",Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.Button_Stop) {
            doWrite = false;
            Toast.makeText(this,"不允许写",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        ACT.setText("onAccuracyChanged is detonated");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String message = new String();
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float X = event.values[0];      //x轴加速度
            float Y = event.values[1];      //y轴加速度
            float Z = event.values[2];      //z轴加速度

            //Low-Pass Filter
            lowX = X * FILTERING_VALAUE + lowX * (1.0f -
                    FILTERING_VALAUE);
            lowY = Y * FILTERING_VALAUE + lowY * (1.0f -
                    FILTERING_VALAUE);
            lowZ = Z * FILTERING_VALAUE + lowZ * (1.0f -
                    FILTERING_VALAUE);

            //High-pass filter
            float highX = X - lowX;
            float highY = Y - lowY;
            float highZ = Z - lowZ;
            double highA = Math.sqrt(highX * highX + highY * highY + highZ
                    * highZ);


            if (doWrite) {
                write2file(message);
                gravityNew = (float)highA;
                deteorNewStep(gravityNew);
                DecimalFormat df = new DecimalFormat("#,##0.000");
                message = df.format(highX) + " ";
                message += df.format(highY) + " ";
                message += df.format(highZ) + " ";
                message += df.format(highA) + "\n";
                AT.setText(message + "\n");
            }
        }
    }

    //进行计步
    private void deteorNewStep(float values){
            if (gravityOld==0){
                gravityOld = values;
            }else {
                //检测是否为波峰
                if (detectorPeak(values,gravityOld)){
                    //如果为波峰，记录这次的时间和上次的时间
                    timeOflastPeak = timeofThisPeak;
                    timeOfNow =System.currentTimeMillis();
                    //两次时间差大于250，并且波峰和波谷的差值大于阈值就判定为一步，进行步数加1
                    if (timeOfNow-timeOflastPeak>=TimeInterVal &&(peakOfWave-valleyOfWave>=ThreadValue)){
                        timeOflastPeak = timeOfNow;
                            addEntry(values);
                        //开始进行步数加一
                        StepNum++;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                mHandler.sendMessage(message);
                            }
                        }).start();

                    }
                }

                if (timeOfNow-timeOflastPeak>=TimeInterVal&&(peakOfWave-valleyOfWave>=InitiaValue)){
                    timeOflastPeak =timeOfNow;
                    ThreadValue = peakValleyThread(peakOfWave-valleyOfWave);
                }
            }
        gravityOld = values;
    }

    /**
     * 检测波峰
     * @param newValue
     * @param oldValue
     * @return
     */
    private boolean detectorPeak(float newValue,float oldValue){
        laststatus = isDirectioup;
        //新的值大于旧值说明在上升
        if (newValue>=oldValue){
            isDirectioup =true;
            continueUpCount++;
        }else {
            continueUpFormerCount =continueUpCount;
            continueUpCount = 0;
            isDirectioup = false;
        }

        //如果目前点为下降的趋势,isDirectioup为false
        //之前的点为上升
        //波峰的值大于等于20
        if (!isDirectioup&&laststatus&&(continueUpFormerCount>=2||oldValue>=20)){
            peakOfWave = oldValue;
            return  true;
        }else if (!laststatus&&isDirectioup){
            valleyOfWave =oldValue;
            return false;
        }else {
            return false;
        }
    }

    //阈值的计算
    private float peakValleyThread(float value){
        float tempThread =ThreadValue;

        if (tempcount<ValueNum){
            tempValue[tempcount] =value;
            tempcount++;
        }else {
            tempThread =averageValue(tempValue,ValueNum);
            for (int i=1;i<ValueNum;i++){
                tempValue[i-1] =tempValue[i];
            }
            tempValue[ValueNum-1] =value;
        }
        return tempThread;
    }


    private float averageValue(float value[],int n){
        float ave = 0;
        for (int i=0;i<n;i++){
            ave+=value[i];
        }
        ave =ave/ValueNum;
        if (ave>=8)
            ave =(float)4.3;
        else if (ave>=7&&ave<8)
            ave =(float)3.3;
        else if (ave>=4&&ave<7)
            ave = (float)2.3;
        else if (ave>=3&&ave<4)
            ave =(float)2.0;
        else
            ave =(float)1.3;

        return ave;
    }
    //写文件
    private void write2file(String a){
        try {
            File file = new File("/sdcard/acc.txt");
            //write the resultinto/sdcard/acc.txt
            if (!file.exists()){
                file.createNewFile();}
            // Open a random access file stream for reading and writing
            RandomAccessFile randomFile = new
                    RandomAccessFile("/sdcard/acc.txt", "rw");
            // The length of the file (the number of bytes)
            long fileLength = randomFile.length();
            // Move the file pointer to the end of the file
            randomFile.seek(fileLength);
            randomFile.writeBytes(a);
            randomFile.close();
        } catch (IOException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {

            StepNumText.setText("步数"+StepNum);
        };

    };

//    Random random = new Random();
    int i=0;
    public void addEntry(float highA) {

        LineData lineData = mChart.getData();
        if (lineData != null) {
            Entry entry =new Entry(i++,highA);
            int indexLast = lineData.getDataSetCount()-1;
            LineDataSet lastDataSet = (LineDataSet) lineData.getDataSetByIndex(indexLast);
            if (lineData.getEntryCount()>maxcount){
                Entry firstEntry = lastDataSet.getEntryForIndex(
                       0);
                lineData.removeEntry(firstEntry, 0);

            }
            lineData.addEntry(entry,0);
            mChart.notifyDataSetChanged();
            mChart.invalidate();                                                //刷新显示绘图
        }else {
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry( i++, highA));
            LineDataSet set = new LineDataSet(entries, "加速度");
            set.setColor(getResources().getColor(R.color.blue));         //设置线条颜色
            set.setDrawValues(true);                                      //设置显示数据点值
            set.setValueTextColor(getResources().getColor(R.color.green));//设置显示值的字体颜色
            set.setValueTextSize(12);
            LineData newlineData = new LineData(set);
            mChart.setData(newlineData);
            mChart.invalidate();                                                //刷新显示绘图
        }
    }

}
