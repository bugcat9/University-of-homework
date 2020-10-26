package com.example.emotion_recognition.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.arcsoft.face.enums.CompareModel;
import com.arcsoft.face.enums.DetectFaceOrientPriority;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectModel;
import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.bumptech.glide.Glide;
import com.example.emotion_recognition.R;
import com.example.emotion_recognition.model.ClientInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SingleImageActivity extends BaseActivity {
    private static final String TAG = "SingleImageActivity";
    private ImageView ivShow;
    private TextView tvNotice;
    private FaceEngine faceEngine;
    private int faceEngineCode = -1;
    private String filePath;
    boolean isFindFace = false;
    boolean isNet =false;
    //按顺序人脸识别在前，人脸识别在后
    SpannableStringBuilder notificationSpannableStringBuilder = new SpannableStringBuilder();
    SpannableStringBuilder netSpannableStringBuilder = new SpannableStringBuilder();
    /**
     * 请求权限的请求码
     */
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 请求选择本地图片文件的请求码
     */
    private static final int ACTION_CHOOSE_IMAGE = 0x201;
    /**
     * 提示对话框
     */
    private AlertDialog progressDialog;
    /**
     * 被处理的图片
     */
    private Bitmap mBitmap = null;

    /**
     * 所需的所有权限信息
     */
    private static String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置隐藏
        this.statusBarHide();
        setContentView(R.layout.activity_image_process);
        initView();
        /**
         * 在选择图片的时候，在android 7.0及以上通过FileProvider获取Uri，不需要文件权限
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            List<String> permissionList = new ArrayList<>(Arrays.asList(NEEDED_PERMISSIONS));
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            NEEDED_PERMISSIONS = permissionList.toArray(new String[0]);
        }

        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
        }

    }

    private void initEngine() {
        faceEngine = new FaceEngine();
        faceEngineCode = faceEngine.init(this, DetectMode.ASF_DETECT_MODE_IMAGE, DetectFaceOrientPriority.ASF_OP_ALL_OUT,
                16, 10, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine: init: " + faceEngineCode + "  version:" + versionInfo);

        if (faceEngineCode != ErrorInfo.MOK) {
            showToast(getString(R.string.init_failed, faceEngineCode));
        }
    }

    /**
     * 销毁引擎
     */
    private void unInitEngine() {
        if (faceEngine != null) {
            faceEngineCode = faceEngine.unInit();
            faceEngine = null;
            Log.i(TAG, "unInitEngine: " + faceEngineCode);
        }
    }

    @Override
    protected void onDestroy() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        mBitmap = null;

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;

        unInitEngine();
        super.onDestroy();
    }

    private void initView() {
        tvNotice = findViewById(R.id.tv_notice);
        ivShow = findViewById(R.id.iv_show);
        ivShow.setImageResource(R.mipmap.faces);
        progressDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.processing)
                .setView(new ProgressBar(this))
                .create();
    }

    /**
     * 按钮点击响应事件
     *
     * @param view
     */
    public void process(final View view) {
        isFindFace =false;
        isNet = false;
       new FindFaceTask().execute();
       new NetTask().execute();
    }


    /**
     * 主要操作逻辑部分
     */
    public SpannableStringBuilder processImage() {
        /**
         * 1.准备操作（校验，显示，获取BGR）
         */
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.faces);
        }
        // 图像对齐
        Bitmap bitmap = ArcSoftImageUtil.getAlignedBitmap(mBitmap, true);

        SpannableStringBuilder notificationSpannableStringBuilder = new SpannableStringBuilder();
        if (faceEngineCode != ErrorInfo.MOK) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " face engine not initialized!");
            //showNotificationAndFinish(notificationSpannableStringBuilder);
            return notificationSpannableStringBuilder;
        }
        if (bitmap == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " bitmap is null!");
            //showNotificationAndFinish(notificationSpannableStringBuilder);
            return notificationSpannableStringBuilder;
        }
        if (faceEngine == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " faceEngine is null!");
           // showNotificationAndFinish(notificationSpannableStringBuilder);
            return notificationSpannableStringBuilder;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(ivShow.getContext())
                        .load(finalBitmap)
                        .into(ivShow);
            }
        });

        // bitmap转bgr24
        long start = System.currentTimeMillis();
        byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
        int transformCode = ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
        if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
            Log.e(TAG, "transform failed, code is " + transformCode);
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "transform bitmap To ImageData failed", "code is ", String.valueOf(transformCode), "\n");
            return notificationSpannableStringBuilder;
        }
//        Log.i(TAG, "processImage:bitmapToBgr24 cost =  " + (System.currentTimeMillis() - start));
        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "start face detection,imageWidth is ", String.valueOf(width), ", imageHeight is ", String.valueOf(height), "\n");

        List<FaceInfo> faceInfoList = new ArrayList<>();

        /**
         * 2.成功获取到了BGR24 数据，开始人脸检测
         */
        long fdStartTime = System.currentTimeMillis();
//        ArcSoftImageInfo arcSoftImageInfo = new ArcSoftImageInfo(width,height,FaceEngine.CP_PAF_BGR24,new byte[][]{bgr24},new int[]{width * 3});
//        Log.i(TAG, "processImage: " + arcSoftImageInfo.getPlanes()[0].length);
//        int detectCode = faceEngine.detectFaces(arcSoftImageInfo, faceInfoList);
        int detectCode = faceEngine.detectFaces(bgr24, width, height, FaceEngine.CP_PAF_BGR24, DetectModel.RGB, faceInfoList);
        if (detectCode == ErrorInfo.MOK) {
           Log.i(TAG, "processImage: fd costTime = " + (System.currentTimeMillis() - fdStartTime));
        }

        //绘制bitmap
        Bitmap bitmapForDraw = bitmap.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmapForDraw);
        Paint paint = new Paint();
        addNotificationInfo(notificationSpannableStringBuilder, null, "detect result:\nerrorCode is :", String.valueOf(detectCode), "   face Number is ", String.valueOf(faceInfoList.size()), "\n");
        /**
         * 3.若检测结果人脸数量大于0，则在bitmap上绘制人脸框并且重新显示到ImageView，若人脸数量为0，则无法进行下一步操作，操作结束
         */
        if (faceInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "face list:\n");
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setColor(Color.YELLOW);
            for (int i = 0; i < faceInfoList.size(); i++) {
                //绘制人脸框
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(faceInfoList.get(i).getRect(), paint);
                //绘制人脸序号
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                int textSize = faceInfoList.get(i).getRect().width() / 2;
                paint.setTextSize(textSize);

                canvas.drawText(String.valueOf(i), faceInfoList.get(i).getRect().left, faceInfoList.get(i).getRect().top, paint);
                addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", faceInfoList.get(i).toString(), "\n");
            }
            //显示
            final Bitmap finalBitmapForDraw = bitmapForDraw;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(ivShow.getContext())
                            .load(finalBitmapForDraw)
                            .into(ivShow);
                }
            });
        } else {
            addNotificationInfo(notificationSpannableStringBuilder, null, "can not do further action, exit!");
           // showNotificationAndFinish(notificationSpannableStringBuilder);
            return notificationSpannableStringBuilder;
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");


        /**
         * 4.上一步已获取到人脸位置和角度信息，传入给process函数，进行年龄、性别、三维角度、活体检测
         */

        long processStartTime = System.currentTimeMillis();
        int faceProcessCode = faceEngine.process(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList, FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_LIVENESS);

        if (faceProcessCode != ErrorInfo.MOK) {
            addNotificationInfo(notificationSpannableStringBuilder, new ForegroundColorSpan(Color.RED), "process failed! code is ", String.valueOf(faceProcessCode), "\n");
        } else {
            Log.i(TAG, "processImage: process costTime = " + (System.currentTimeMillis() - processStartTime));
        }
        //年龄信息结果
        List<AgeInfo> ageInfoList = new ArrayList<>();
        //性别信息结果
        List<GenderInfo> genderInfoList = new ArrayList<>();
        //人脸三维角度结果
        List<Face3DAngle> face3DAngleList = new ArrayList<>();
        //活体检测结果
        List<LivenessInfo> livenessInfoList = new ArrayList<>();
        //获取年龄、性别、三维角度、活体结果
        int ageCode = faceEngine.getAge(ageInfoList);
        int genderCode = faceEngine.getGender(genderInfoList);
        int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
        int livenessCode = faceEngine.getLiveness(livenessInfoList);

        if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "at least one of age,gender,face3DAngle detect failed!,codes are:",
                    String.valueOf(ageCode), " , ", String.valueOf(genderCode), " , ", String.valueOf(face3DAngleCode));
            //showNotificationAndFinish(notificationSpannableStringBuilder);
            return notificationSpannableStringBuilder;
        }
        /**
         * 5.年龄、性别、三维角度已获取成功，添加信息到提示文字中
         */
        //年龄数据
        if (ageInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "age of each face:\n");
        }
        for (int i = 0; i < ageInfoList.size(); i++) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", String.valueOf(ageInfoList.get(i).getAge()), "\n");
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        //性别数据
        if (genderInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "gender of each face:\n");
        }
        for (int i = 0; i < genderInfoList.size(); i++) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:"
                    , genderInfoList.get(i).getGender() == GenderInfo.MALE ?
                            "MALE" : (genderInfoList.get(i).getGender() == GenderInfo.FEMALE ? "FEMALE" : "UNKNOWN"), "\n");
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");


        //人脸三维角度数据
        if (face3DAngleList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "face3DAngle of each face:\n");
            for (int i = 0; i < face3DAngleList.size(); i++) {
                addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", face3DAngleList.get(i).toString(), "\n");
            }
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        //活体检测数据
        if (livenessInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "liveness of each face:\n");
            for (int i = 0; i < livenessInfoList.size(); i++) {
                String liveness = null;
                switch (livenessInfoList.get(i).getLiveness()) {
                    case LivenessInfo.ALIVE:
                        liveness = "ALIVE";
                        break;
                    case LivenessInfo.NOT_ALIVE:
                        liveness = "NOT_ALIVE";
                        break;
                    case LivenessInfo.UNKNOWN:
                        liveness = "UNKNOWN";
                        break;
                    case LivenessInfo.FACE_NUM_MORE_THAN_ONE:
                        liveness = "FACE_NUM_MORE_THAN_ONE";
                        break;
                    default:
                        liveness = "UNKNOWN";
                        break;
                }
                addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", liveness, "\n");
            }
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        /**
         * 6.最后将图片内的所有人脸进行一一比对并添加到提示文字中
         */
        if (faceInfoList.size() > 0) {

            FaceFeature[] faceFeatures = new FaceFeature[faceInfoList.size()];
            int[] extractFaceFeatureCodes = new int[faceInfoList.size()];

            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "faceFeatureExtract:\n");
            for (int i = 0; i < faceInfoList.size(); i++) {
                faceFeatures[i] = new FaceFeature();
                //从图片解析出人脸特征数据
                long frStartTime = System.currentTimeMillis();
                extractFaceFeatureCodes[i] = faceEngine.extractFaceFeature(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList.get(i), faceFeatures[i]);

                if (extractFaceFeatureCodes[i] != ErrorInfo.MOK) {
                    addNotificationInfo(notificationSpannableStringBuilder, null, "faceFeature of face[", String.valueOf(i), "]",
                            " extract failed, code is ", String.valueOf(extractFaceFeatureCodes[i]), "\n");
                } else {
//                    Log.i(TAG, "processImage: fr costTime = " + (System.currentTimeMillis() - frStartTime));
                    addNotificationInfo(notificationSpannableStringBuilder, null, "faceFeature of face[", String.valueOf(i), "]",
                            " extract success\n");
                }
            }
            addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

            //人脸特征的数量大于2，将所有特征进行比较
            if (faceFeatures.length >= 2) {

                addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "similar of faces:\n");

                for (int i = 0; i < faceFeatures.length; i++) {
                    for (int j = i + 1; j < faceFeatures.length; j++) {
                        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD_ITALIC), "compare face[", String.valueOf(i), "] and  face["
                                , String.valueOf(j), "]:\n");
                        //若其中一个特征提取失败，则不进行比对
                        boolean canCompare = true;
                        if (extractFaceFeatureCodes[i] != 0) {
                            addNotificationInfo(notificationSpannableStringBuilder, null, "faceFeature of face[", String.valueOf(i), "] extract failed, can not compare!\n");
                            canCompare = false;
                        }
                        if (extractFaceFeatureCodes[j] != 0) {
                            addNotificationInfo(notificationSpannableStringBuilder, null, "faceFeature of face[", String.valueOf(j), "] extract failed, can not compare!\n");
                            canCompare = false;
                        }
                        if (!canCompare) {
                            continue;
                        }

                        FaceSimilar matching = new FaceSimilar();
                        //比对两个人脸特征获取相似度信息
                        faceEngine.compareFaceFeature(faceFeatures[i], faceFeatures[j], CompareModel.LIFE_PHOTO, matching);
                        //新增相似度比对结果信息
                        addNotificationInfo(notificationSpannableStringBuilder, null, "similar of face[", String.valueOf(i), "] and  face[",
                                String.valueOf(j), "] is:", String.valueOf(matching.getScore()), "\n");
                    }
                }
            }
        }

        //showNotificationAndFinish(notificationSpannableStringBuilder);
        return notificationSpannableStringBuilder;
    }

    /**
     * 展示提示信息并且关闭提示框
     *
     * @param stringBuilder 带格式的提示文字
     */
    private void showNotificationAndFinish(final SpannableStringBuilder stringBuilder) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tvNotice != null) {
                    tvNotice.setText(stringBuilder);
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 追加提示信息
     *
     * @param stringBuilder 提示的字符串的存放对象
     * @param styleSpan     添加的字符串的格式
     * @param strings       字符串数组
     */
    private void addNotificationInfo(SpannableStringBuilder stringBuilder, ParcelableSpan styleSpan, String... strings) {
        if (stringBuilder == null || strings == null || strings.length == 0) {
            return;
        }
        int startLength = stringBuilder.length();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        int endLength = stringBuilder.length();
        if (styleSpan != null) {
            stringBuilder.setSpan(styleSpan, startLength, endLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 从本地选择文件
     *
     * @param view
     */
    public void chooseLocalImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, ACTION_CHOOSE_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_CHOOSE_IMAGE) {
            if (data == null || data.getData() == null) {
                showToast(getString(R.string.get_picture_failed));
                return;
            }
            try {
                filePath = handleImageOnKitKat(data);
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (mBitmap == null) {
                showToast(getString(R.string.get_picture_failed));
                return;
            }
            Glide.with(ivShow.getContext())
                    .load(mBitmap)
                    .into(ivShow);
        }
    }

    @Override
    void afterRequestPermission(int requestCode, boolean isAllGranted) {
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
                initEngine();
            } else {
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String handleImageOnKitKat(Intent data) {
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
        return picPath;
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



    /***
     * 处理面部识别中费时的工作
     */
    private class FindFaceTask extends AsyncTask<Void, Void, SpannableStringBuilder>{

        //被调用后立即执行，一般用来在执行后台任务前对UI做一些标记
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute() called");
            if (progressDialog == null || progressDialog.isShowing()) {
                return;
            }
            progressDialog.show();
        }

        //进行显示
        @Override
        protected void onPostExecute(SpannableStringBuilder s) {
            isFindFace = true;
            notificationSpannableStringBuilder.append(s);
            //说明网络任务先完成
            if(isFindFace&&isNet){
                notificationSpannableStringBuilder.append(netSpannableStringBuilder);
                showNotificationAndFinish(notificationSpannableStringBuilder);
            }

        }

        @Override
        protected SpannableStringBuilder doInBackground(Void... voids) {
            return processImage();
        }

    }


    /***
     *
     */
    private class NetTask extends  AsyncTask<String,Integer,SpannableStringBuilder>{

        Socket socket;
        @Override
        protected SpannableStringBuilder doInBackground(String... strings) {
            int ans = -1;
            try {
                //建立连接
                socket = new Socket(ClientInfo.IP, ClientInfo.Port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                sendFile(out);
                DataInputStream in = new DataInputStream( socket.getInputStream());
                 ans = in.readInt();
                //String a =Byte.toString(ans);
                Log.i(TAG,"得到答案为"+ans);
                //socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            SpannableStringBuilder s = new SpannableStringBuilder();
            addNotificationInfo(s,null,"the emtion is ",ClientInfo.emotionMap.get(ans));
            return s;
        }

        @Override
        protected void onPostExecute(SpannableStringBuilder s)
        {
            isNet = true;
            //说明人脸识别先完成
            if(isFindFace&&isNet){

                notificationSpannableStringBuilder.append(s);
                showNotificationAndFinish(notificationSpannableStringBuilder);
            }else{
                netSpannableStringBuilder.append(s);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }


        private void sendFile(DataOutputStream out) throws IOException {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                // 文件名和长度,最好变成字节发送这样比较稳妥，不会出任何问题
                byte[] byteArray = file.getName().getBytes();
                out.writeInt(byteArray.length); //先把名字长度发送过去
                out.write(byteArray);
                out.flush();
                out.writeLong(file.length());
                out.flush();

                Log.i(TAG,"======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    out.write(bytes, 0, length);
                    out.flush();
                    progress += length;
                    //System.out.print("| " + (100*progress/file.length()) + "% |");
                }
                //System.out.println();
                Log.i(TAG,"======== 文件传输成功 ========");

            }
        }
    }
}
