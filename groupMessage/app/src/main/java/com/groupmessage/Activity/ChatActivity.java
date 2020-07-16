package com.groupmessage.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupmessage.Entity.Msg;
import com.groupmessage.Entity.MsgAdapter;
import com.groupmessage.R;
import com.groupmessage.utils.LogUtil;
import com.groupmessage.utils.ToastUtils;
import com.groupmessage.xmpp.XmppConnection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatActivity extends BaseActivity {
    private List<Msg> msgList = new ArrayList<Msg>();

    private EditText et_input;

    private Button send,choose_picture;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    private ChatManager chatMan;    //个人聊天

    private MultiUserChat muc;      //群聊

    private Chat newchat;

    String friend_name ;

    private  boolean isGroup;

    public  static final int CHOOSE_PHOTO = 2;

    private final static String Folder = "XmppTest";
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + File.separator + Folder + File.separator;


    private OfflineMessageManager mOfflineMessageManager = new OfflineMessageManager(XmppConnection.getConnection());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    private  void  init(){
        et_input = findViewById(R.id.et_input);
        send = findViewById(R.id.btn_send);
        choose_picture = findViewById(R.id.btn_choose_picture);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);

        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_input.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    et_input.setText(""); // 清空输入框中的内容

                    sendMessage(content);
                }
            }
        });

        choose_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
            }
        });

        Intent intent =getIntent();
        isGroup = intent.getBooleanExtra("isGroup",false);

        if (isGroup){   //是群聊
            muc = XmppConnection.getMultiUserChat();
            addGroupListener();
            //接收文件监听
            addFileListerer();
        }else { //是朋友聊天
            friend_name = intent.getStringExtra("friendname");

            // 消息监听
            chatMan = XmppConnection.getConnection().getChatManager();
            newchat = chatMan.createChat(friend_name, null);

            //getOfflineMessage();    //接收离线信息会导致比较卡

            addChatListener();      //这个消息监听

            //接收文件监听
            addFileListerer();


        }
    }


    //接收离线消息
    public void getOfflineMessage(){

        LogUtil.d("尝试","获取离线消息..");

        if (mOfflineMessageManager != null){
            try {
                Iterator<Message> it = mOfflineMessageManager.getMessages();
                Log.i("离线消息数量: ", "" + mOfflineMessageManager.getMessageCount());
                while (it.hasNext()) {
                    org.jivesoftware.smack.packet.Message message = it.next();
                    Log.i("收到离线消息", "Received from 【" + message.getFrom()
                            + "】 message: " + message.getBody());

                    if (message != null && message.getBody() != null
                            && !message.getBody().equals("null")) {

                        String from = message.getFrom().split("/")[0];
                        //msg.setFromSubJid(from);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //设置在线，只有设置了在线状态，才可以监听在线消息，否则监听都无效
            Presence presence = new Presence(Presence.Type.available);
            XmppConnection.getConnection().sendPacket(presence);
        }
    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    private void addChatListener() {
        newchat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                System.out.println(message.getFrom()+"-->"+message.getTo()+"\n"+message.getBody());
                // 获取自己好友发来的信息
                String[] args = new String[]{friend_name, message.getBody()};
                // 在handler里取出来显示消息
                android.os.Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = args;
                handler.sendMessage(msg);
            }
        });
    }


    private void addGroupListener(){
        muc.addMessageListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                // 接收来自聊天室的聊天信息
                String groupName = message.getFrom();
                String[] nameOrGroup = groupName.split("/");
                //判断是否是本人发出的消息 不是则显示
                if (!nameOrGroup[1].equals(getUserName())) {
                    String[] args = new String[]{nameOrGroup[1], message.getBody()};
                    // 在handler里取出来显示消息
                    android.os.Message msg = handler.obtainMessage();
                    msg.what = 2;
                    msg.obj = args;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private void addFileListerer() {
        FileTransferManager manager = XmppConnection.getFileTransferManager();
        manager.addFileTransferListener(new FileTransferListener() {
            @Override
            public void fileTransferRequest(final FileTransferRequest request) {


                new Thread() {
                    @Override
                    public void run() {
                        //文件接收
                        IncomingFileTransfer transfer = request.accept();
                        //获取文件名字
                        String fileName = transfer.getFileName();
                        //本地创建文件
                        File sdCardDir = new File(ALBUM_PATH);
                        if (!sdCardDir.exists()) {//判断文件夹目录是否存在
                            sdCardDir.mkdir();//如果不存在则创建
                        }
                        String save_path = ALBUM_PATH + fileName;
                        File file = new File(save_path);
                        //接收文件
                        try {
                            transfer.recieveFile(file);
                            while (!transfer.isDone()) {
                                if (transfer.getStatus().equals(FileTransfer.Status.error)) {
                                    System.out.println("ERROR!!! " + transfer.getError());
                                } else {
                                    System.out.println(transfer.getStatus());
                                    System.out.println(transfer.getProgress());
                                }
                                try {
                                    Thread.sleep(1000L);
                                } catch (Exception e) {

                                }

                            }

                            if (transfer.isDone()) {


                                String[] args = new String[]{friend_name, save_path};
                                android.os.Message msg = handler.obtainMessage();
                                msg.what = 4;
                                msg.obj = args;
                                handler.sendMessage(msg);

                            }
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                    }

                }.start();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String[] args = (String[]) msg.obj;
            switch (msg.what) {
                case 1: //接收到好友的消息
                    LogUtil.d("收到消息",args[1]);
                    Msg m = new Msg(args[1], Msg.TYPE_RECEIVED);
                    msgList.add(m);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    break;
                case 2://接受群消息
                    LogUtil.d("收到消息",args[1]);
                    Msg m2 = new Msg(args[1], Msg.TYPE_RECEIVED);
                    msgList.add(m2);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    break;
                case 3://显示发送图片
                    LogUtil.d("图片：",args[1]);
                    Msg m3 = new Msg(args[1], Msg.TYPE_IMAGE_SENT);
                    msgList.add(m3);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    break;
                case 4://显示接收图片
                    LogUtil.d("图片：",args[1]);
                    Msg m4 = new Msg(args[1], Msg.TYPE_IMAGE_RECEIVED);
                    msgList.add(m4);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    break;

            }

        }
    };



    private void sendMessage(final String content){
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (isGroup){//群聊
                    try {
                        muc.sendMessage(content);
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }

                }else {
                    //好友聊天
                    try {
                        Message msg = new Message();
                        msg.setBody(content);
                        // ToastUtils.showShort(ChatActivity.this,content);
                        // 发送消息
                        newchat.sendMessage(msg);
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

//        new SendFileTask().execute(picPath, friend_name);// 开始发送图片
    }

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

            new SendFileTask().execute(picPath, friend_name);// 开始发送图片
        }


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


    class SendFileTask extends AsyncTask<String, Integer, Integer> {
        protected Integer doInBackground(String... params) {
            if (params.length < 2) {
                return Integer.valueOf(-1);
            }
            String img_path = params[0];
            String toId = params[1] ;

            FileTransferManager fileTransferManager = XmppConnection.getFileTransferManager();
            File filetosend = new File(img_path);
            if (filetosend.exists() == false) {
                return -1;
            }
            OutgoingFileTransfer transfer = fileTransferManager.createOutgoingFileTransfer(toId);// 创建一个输出文件传输对象
            try {
                transfer.sendFile(filetosend, "recv img");
                while (!transfer.isDone()) {
                    if (transfer.getStatus().equals(FileTransfer.Status.error)) {
                        System.out.println("ERROR!!! " + transfer.getError());
                    } else {
                        System.out.println(transfer.getStatus());
                        System.out.println(transfer.getProgress());
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (transfer.isDone()) {
                    Log.i("main", "---->");
                    String[] args = new String[]{getXmppConnection().getUser(),img_path};
                    android.os.Message msg = handler.obtainMessage();
                    msg.what = 3;
                    msg.obj = args;
                   handler.sendMessage(msg);
                }
            } catch (XMPPException e1) {
                e1.printStackTrace();
            }
            return 0;
        }
    }
}
