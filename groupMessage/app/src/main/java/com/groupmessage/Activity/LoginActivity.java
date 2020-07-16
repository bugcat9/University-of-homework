package com.groupmessage.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.groupmessage.R;
import com.groupmessage.utils.LogUtil;
import com.groupmessage.utils.ToastUtils;
import com.groupmessage.xmpp.XmppConnection;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;


public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private EditText etUser, etPassword;

    private CheckBox checkMemory, checkAuto;

    private Button btnLogin, btnRegister;

    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){

        etUser = findViewById(R.id.edit_username);
        etPassword = findViewById(R.id.edit_password);
//        checkMemory = findViewById(R.id.checkBox_memory);
//        checkAuto = findViewById(R.id.checkBox_auto);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:    //开始登录
                LogUtil.i("click","成功了");
                login();
                break;
            case R.id.btn_register: //开始注册
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }


    }

    private void login(){
        username = etUser.getText().toString();
        password = etPassword.getText().toString();
        if(username.length()==0){

            ToastUtils.showShort(this,getString(R.string.invalid_username));
            return;
        }

        if (password.length()==0){
            ToastUtils.showShort(this,getString(R.string.invalid_password));
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 连接服务器
                    XmppConnection.getConnection().login(username, password);
                    // 连接服务器成功，更改在线状态
                    Presence presence = new Presence(Presence.Type.available);
                    XmppConnection.getConnection().sendPacket(presence);
                    handler.sendEmptyMessage(1);     //登陆成功
                    XmppConnection.setUsername(username+"@"+XmppConnection.SERVER_NAME);
                } catch (XMPPException e) {
                    XmppConnection.closeConnection();
                    handler.sendEmptyMessage(2);    //登陆失败
                }
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg.what == 1) {
                ToastUtils.showShort(LoginActivity.this, getString(R.string.welcome));      //登陆成功
                // 跳转到好友列表
                Intent intent = new Intent();

                intent.setClass(LoginActivity.this, FriendActivity.class);
                //intent.setClass(LoginActivity.this,GroupActivity.class);
                activity.finish();
                startActivity(intent);

              //  toNextPage();
            } else if (msg.what == 2) {
                ToastUtils.showShort(LoginActivity.this, getString(R.string.login_failed));     //登陆失败
            }
        }
    };


}
