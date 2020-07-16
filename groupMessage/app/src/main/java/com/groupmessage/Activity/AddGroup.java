package com.groupmessage.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.groupmessage.R;
import com.groupmessage.utils.ToastUtils;
import com.groupmessage.xmpp.XmppConnection;

public class AddGroup extends AppCompatActivity {


    Button btn_add_group;

    EditText et_group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        init();
    }

    void init(){
        btn_add_group = findViewById(R.id.btn_add_group);
        et_group_name = findViewById(R.id.et_group_name);
        btn_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_group();
            }
        });
    }

    void add_group(){
        boolean isSuccess = XmppConnection.createRoom( et_group_name.getText().toString() + "", "");
        if (isSuccess) {
            Intent intent = new Intent();
            intent.putExtra("user", et_group_name.getText().toString());
            intent.putExtra("name", et_group_name.getText().toString());
            setResult(1, intent);
            finish();
        } else {
            ToastUtils.showShort(this, "创建失败");
        }
    }
}
