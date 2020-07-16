package com.groupmessage.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;


import com.groupmessage.utils.ActivityCollector;
import com.groupmessage.xmpp.XmppConnection;

import org.jivesoftware.smack.XMPPConnection;

public class BaseActivity extends AppCompatActivity {
    public BaseActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        activity = this;
    }

    public XMPPConnection getXmppConnection() {
        return XmppConnection.getConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public String getUserName(){
        return  XmppConnection.getUsername();
    }

}
