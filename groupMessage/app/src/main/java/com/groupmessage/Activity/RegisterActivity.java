package com.groupmessage.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.groupmessage.R;
import com.groupmessage.utils.ToastUtils;
import com.groupmessage.xmpp.XmppConnection;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;

public class RegisterActivity extends BaseActivity {

    private String email, account, name, password;

    private EditText etEmail, etAccount, etName, etPassword;

    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        etEmail = findViewById(R.id.et_email);
        etAccount = findViewById(R.id.et_acount);;
        etName = findViewById(R.id.et_name);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_user_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    private void register(){
        email = etEmail.getText().toString();
        account = etAccount.getText().toString();
        name = etName.getText().toString();
        password = etPassword.getText().toString();

        if (email.length()==0 ||account.length()==0||name.length()==0||password.length()==0){
            ToastUtils.showShort(this, "填写信息不能为空");
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                Registration reg = new Registration();
                //设置类型
                reg.setType(IQ.Type.SET);
                //发送到服务器
                reg.setTo(XmppConnection.getConnection().getServiceName());
                //设置用户名
                reg.setUsername(account);
                //设置密码
                reg.setPassword(password);
                //设置其余属性 不填可能会报500异常 连接不到服务器 amack一个Bug
                reg.addAttribute("name", name);
                reg.addAttribute("email", email);
                reg.addAttribute("android", "geolo_createUser_android");       //设置安卓端登录
                //创建包过滤器
                PacketFilter filter = new AndFilter(new PacketIDFilter(reg
                        .getPacketID()), new PacketTypeFilter(IQ.class));
                //创建包收集器
                PacketCollector collector = XmppConnection.getConnection()
                        .createPacketCollector(filter);
                //发送包
                XmppConnection.getConnection().sendPacket(reg);
                //获取返回信息
                IQ result = (IQ) collector.nextResult(SmackConfiguration
                        .getPacketReplyTimeout());
                // 停止请求results（是否成功的结果）
                collector.cancel();
                //通过返回信息判断
                if (result == null) {   //无返回，连接不到服务器
                    Message msg = new Message();
                    msg.obj = "服务器没有返回结果";
                    handler.sendMessage(msg);
                } else if (result.getType() == IQ.Type.ERROR) {     //错误状态

                    if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                        //账户存在 409判断
                        Message msg = new Message();
                        msg.obj = "这个账号已经存在";
                        handler.sendMessage(msg);

                    } else {
                        Message msg = new Message();
                        msg.obj = "注册失败";
                        handler.sendMessage(msg);

                    }

                } else if (result.getType() == IQ.Type.RESULT) {//注册成功跳转登录
                    try {
                        XmppConnection.getConnection().login(account, password);
                        Presence presence = new Presence(
                                Presence.Type.available);
                        XmppConnection.getConnection().sendPacket(presence);

                        Message msg = new Message();
                        msg.obj = "注册成功";
                        handler.sendMessage(msg);
                        XmppConnection.setUsername(account+"@"+XmppConnection.SERVER_NAME);
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this,
                                FriendActivity.class);
                        startActivity(intent);
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }


                }
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ToastUtils.showShort(RegisterActivity.this, msg.obj.toString());
        }
    };
}
