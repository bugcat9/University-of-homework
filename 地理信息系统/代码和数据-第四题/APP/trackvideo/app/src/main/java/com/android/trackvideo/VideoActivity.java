package com.android.trackvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;

import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.hardware.Camera.*;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener {


    final int UPDATE_LOCATION = 0; //handle的判断信息

    private SurfaceView sv_media_surface;      //显示录像
    private MediaRecorder mediaRecorder;
    private Camera camera;       //相机

    ImageButton start_V;       //拍摄按键
    boolean is_start = false;    //记录是否在拍摄


    //使用高德API定位
    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;
    AMapLocationListener mLocationListener;


    TextView Longitude_;    //显示经度
    TextView Latitude_;      //显示纬度
    TextView Date_Time;     //显示时间


    private String File_path = "/mnt/sdcard/";             //存放路径
    String FileName = "Vedio";                             //存放文件名
    StringBuilder Sb = new StringBuilder();                //构建需要写入到文件的字符串

    String Start_Time;                                 //记录开始录制时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();          //隐藏ActionBar

        initPermission();                //申请权限
        initView();                      //得到控件对象
        initCamera();                   //初始化
        getLocation();                  //开启定位

    }

    /*
    获得控件对象
     */
    private void initView() {
        Longitude_ = findViewById(R.id.longitude_);
        Latitude_ = findViewById(R.id.latitude_);
        Date_Time = findViewById(R.id.time_);
        sv_media_surface = (SurfaceView) findViewById(R.id.sv_media_surface);
        start_V = findViewById(R.id.start_video);
        start_V.setOnClickListener(this);

    }

    /*
    初始化相机
    给相机解除绑定
     */
    private void initCamera() {

        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        camera.unlock();

    }


    /*
    动态请求权限
    要获取的主要权限：录像权限、使用摄像头的权限，写文件的权限，定位权限
     */
    private void initPermission() {
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};

        List<String> mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    /*
    处理线程消息
    如果接收到UPDATE_LOCATION的消息，那么获取消息中的经纬度和时间信息，显示在界面上
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_LOCATION:
                    String str[] = msg.obj.toString().split("_");
                    if (str.length == 3 && is_start) {   //判断消息格式是否正确，判断是否处于录制状态
                        Longitude_.setText("经度:" + str[0]);
                        Latitude_.setText("纬度:" + str[1]);
                        Date_Time.setText("时间:" + str[2]);
                        Sb.append(str[2] + "," + str[0] + "," + str[1] + "," + Start_Time + "\n");
                    }

                    break;
                default:
                    break;
            }
        }
    };


    /*
    开始录制
     */
    private void start() {

        if (mediaRecorder == null) {
            //实例化媒体录制器
            mediaRecorder = new MediaRecorder();
            //设置相机
            mediaRecorder.setCamera(camera);
            mediaRecorder.reset();
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);


            //设置格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //修改保存路径，防重复
            FileName = "Video" + System.currentTimeMillis();
            mediaRecorder.setOutputFile(File_path + FileName + ".mp4");
            //设置显示录像
            mediaRecorder.setPreviewDisplay(sv_media_surface.getHolder().getSurface());

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                mediaRecorder.setAudioSamplingRate(11025);
            }

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();//开始录制
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
    停止录制
    解除相机绑定
     */
    private void stop() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();//停止录制
            mediaRecorder.reset();
            mediaRecorder.release();//释放资源
            camera.unlock();//解除绑定
        }
        mediaRecorder = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_video:
                if (is_start) {
                    stop();
                    WriteCSV();
                    Sb.delete(0, Sb.length());
                    start_V.setBackgroundResource(R.drawable.start);
                    Toast.makeText(this, "停止录制", Toast.LENGTH_SHORT).show();
                    is_start = false;
                } else {
                    Sb.append("Time,Longitude,Latitude,StartTime\n");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//设置日期格式
                    Start_Time = format.format(new Date());//获取开始时间
                    start();
                    Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();
                    start_V.setBackgroundResource(R.drawable.end);
                    is_start = true;
                }
        }


    }


    public void getLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());

        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位间隔,单位毫秒,设置为1秒定位一次
        mLocationOption.setInterval(1000);

        mLocationOption.setHttpTimeOut(20000);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//设置日期格式
                        String date_time = format.format(new Date());
                        Message msg = new Message();
                        msg.what = 0;
                        //设置消息体为：经度_纬度_时间
                        msg.obj = aMapLocation.getLongitude() + "_" + aMapLocation.getLatitude() + "_" + date_time;
                        handler.sendMessage(msg);
                    } else {
                        //定位失败时，通过ErrCode（错误码）信息来确定失败的原因
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());

                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        System.out.println(mLocationClient.getLastKnownLocation());

    }


    /*
     *写文件
     */
    public void WriteCSV() {
        try {
            FileOutputStream os = new FileOutputStream(File_path + FileName + ".CSV", true);
            os.write(Sb.toString().getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


