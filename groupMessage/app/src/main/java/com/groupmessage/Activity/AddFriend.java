package com.groupmessage.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.groupmessage.R;
import com.groupmessage.xmpp.XmppConnection;

import org.jivesoftware.smack.packet.Presence;

public class AddFriend extends BaseActivity {

    private EditText et_friend_name;

    private Button btn_addfriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        init();
    }

    private  void init(){
        et_friend_name = findViewById(R.id.et_friend_name);
        btn_addfriend = findViewById(R.id.btn_add_friend);

        btn_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
    }

    //添加好友
    private  void addFriend(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = et_friend_name.getText().toString();
                //设置添加好友请求
                Presence subscription = new Presence(Presence.Type.subscribe);
                //拼接好友全称
                subscription.setTo(name+"@"+XmppConnection.SERVER_NAME+"/Smack");
//                subscription.setTo(name);
                //发送请求
                XmppConnection.getConnection().sendPacket(subscription);
            }
        }).start();
        Intent intent = new Intent();
        intent.putExtra("user", et_friend_name.getText().toString());  //先将其存起来
        intent.putExtra("name", et_friend_name.getText().toString());   //先将其存起来
        setResult(1, intent);
        finish();   //活动结束

    }
}
