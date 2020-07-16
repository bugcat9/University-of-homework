package com.example.stockinquiry;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * 连接，使用单例模式
 */
public class Connection extends AsyncTask<Void,String ,Boolean> {
   public static  String IP = "3.92.54.210";
   //public static  String IP = "192.168.43.22";

    public static  int port = 2333 ;

    public static Socket socket ;

    public static   boolean isExectue =false;
    static BufferedReader reader;
    static PrintWriter writer;

    DataListener listener;
    private static Connection intance;

    private Connection(){
        openConnection();
    }

    //背后执行任务
    @Override
    protected Boolean doInBackground(Void... voids) {
        String str=null;
        //接受消息
        try {
            while ((str = reader.readLine()) != null) {
                //调用publishProgress公布进度,最后onProgressUpdate方法将被执行
                publishProgress(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        closeConnection();
        return true;
    }

    //单例模式
    public  static Connection  getConnection(){
        if (intance==null){
            intance = new Connection();
        }
        return  intance;
    }

    private static void openConnection(){
        if (socket == null){
            try {
                socket = new Socket(IP,port);
                reader =new BufferedReader( new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setListener(DataListener listener) {
        this.listener = listener;
    }

    public  void sendMsg(String msg){
        writer.println(msg);
        writer.flush();
    }

    private   void closeConnection(){
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //onProgressUpdate方法用于更新进度信息
    @Override
    protected void onProgressUpdate(String... progresses) {

        String result =progresses[0].split("#")[1];
        if (progresses[0].startsWith("01")){        //查询天的结果
            listener.onDay(result);
        }else if (progresses[0].startsWith("02")){  //查询月的结果
            listener.onMonth(result);

        }else if (progresses[0].startsWith("03")) {
            //查寻平均值
            listener.onAvg(result);

        }else if (progresses[0].startsWith("04")) {
            //查询年的结果，年是以4个季度形式显示的

        }

    }

    //onCancelled方法用于在取消执行中的任务时更改UI
    @Override
    protected void onCancelled() {
        closeConnection();
    }
}
