package com.example.emotion_recognition.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emotion_recognition.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_SELECT_PIC = 2;
    private static final int MAX_FACE_NUM = 5;//最大可以检测出的人脸数量
    private int realFaceNum = 0;//实际检测出的人脸数量
    public  static final int CHOOSE_PHOTO = 2;
    private Button selectBtn;
    private Button detectBtn;
    private ImageView image;
    private ProgressDialog pd;

    private Bitmap bm;//选择的图片的Bitmap对象
    private Paint paint;//画人脸区域用到的Paint

    private boolean hasDetected = false;//标记是否检测到人脸

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);//设置话出的是空心方框而不是实心方块

        pd = new ProgressDialog(this);
        pd.setTitle("提示");
        pd.setMessage("正在检测，请稍等");
        
    }

    private void initView() {
        selectBtn = (Button) findViewById(R.id.btn_select);
        selectBtn.setOnClickListener(this);
        detectBtn = (Button) findViewById(R.id.btn_detect);
        detectBtn.setOnClickListener(this);
        image = (ImageView) findViewById(R.id.image);

    }

    /**
     * 从图库选择图片
     */
    private void selectPicture(){
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            openAlbum();
        }
    }

    //打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                    }
                }
                break;
            default:
                break;
        }
    }

    //得到文件的路径
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        //图片路径
        String picPath = null;
        if (data != null) {
            Uri uri = data.getData();
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // 如果是document类型的Uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1]; // 解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    picPath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    picPath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // 如果是content类型的Uri，则使用普通方式处理
                picPath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // 如果是file类型的Uri，直接获取图片路径即可
                picPath = uri.getPath();
            }
        }
        bm = BitmapFactory.decodeFile(picPath);
        //要使用Android内置的人脸识别，需要将Bitmap对象转为RGB_565格式，否则无法识别
        bm = bm.copy(Bitmap.Config.RGB_565, true);

        image.setImageBitmap(bm);
        hasDetected = false;


    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }



    /**
     * 检测人脸
     */
    private void detectFace(){
        if(bm == null){
            Toast.makeText(this, "请先选择图片", Toast.LENGTH_SHORT).show();
            return ;
        }
        if(hasDetected){
            Toast.makeText(this, "已检测出人脸", Toast.LENGTH_SHORT).show();
        }else{
            new FindFaceTask().execute();
        }
    }

    private void drawFacesArea(FaceDetector.Face[] faces) {
        Toast.makeText(this, "图片中检测到" + realFaceNum + "张人脸", Toast.LENGTH_SHORT).show();
        float eyesDistance = 0f;//两眼间距
        Canvas canvas = new Canvas(bm);
        for (int i = 0; i < faces.length; i++) {
            FaceDetector.Face face = faces[i];
            if (face != null) {
                PointF pointF = new PointF();
                face.getMidPoint(pointF);//获取人脸中心点
                eyesDistance = face.eyesDistance();//获取人脸两眼的间距
                //画出人脸的区域
                canvas.drawRect(pointF.x - eyesDistance, pointF.y - eyesDistance, pointF.x + eyesDistance, pointF.y + eyesDistance, paint);
                hasDetected = true;
            }
        }
        //画出人脸区域后要刷新ImageView
        image.invalidate();
    }
    /**
     * 检测图像中的人脸需要一些时间，所以放到AsyncTask中去执行
     *
     *
     */
    private class FindFaceTask extends AsyncTask<Void, Void, FaceDetector.Face[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected FaceDetector.Face[] doInBackground(Void... arg0) {
            //最关键的就是下面三句代码
            FaceDetector faceDetector = new FaceDetector(bm.getWidth(), bm.getHeight(), MAX_FACE_NUM);
            FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_NUM];
            realFaceNum = faceDetector.findFaces(bm, faces);
            if(realFaceNum > 0){
                return faces;
            }
            return null;
        }

        @Override
        protected void onPostExecute(FaceDetector.Face[] result) {
            super.onPostExecute(result);
            pd.dismiss();
            if(result == null){
                Toast.makeText(MainActivity.this, "抱歉，图片中未检测到人脸", Toast.LENGTH_SHORT).show();
            }else{
                drawFacesArea(result);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_select://选择图片
                selectPicture();
                break;
            case R.id.btn_detect://检测人脸
                detectFace();
                break;
        }
    }

}
