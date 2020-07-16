package com.groupmessage.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.groupmessage.R;
import com.groupmessage.utils.ActivityCollector;
import com.groupmessage.xmpp.XmppConnection;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;


import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

import java.util.ArrayList;

public class GroupActivity extends BaseActivity {

    ListView group_list;
    ArrayList<String> m_groups;
    ArrayAdapter<String> m_adpter;

    Button btn_addgroup;

    private BottomBar mBottomBar;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 2:
                if(resultCode==1)
                    m_groups.add(data.getStringExtra("user"));
        }
        m_adpter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        init();

        //和好友列表一样添加底部菜单
//      mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar = findViewById(R.id.bottomBar);
        mBottomBar.setDefaultTabPosition(1);
        mBottomBar.setItemsFromMenu(R.menu.bottom_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                switch (menuItemId) {
                    case R.id.tab_friend:
//                        ToastUtils.showShort(GroupActivity.this,"1");
                        Intent intent = new Intent();
                        intent.setClass(GroupActivity.this,FriendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.tab_group:
//                        ToastUtils.showShort(GroupActivity.this,"2");
                        break;
                }
            }
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });


    }

    private void init(){
        group_list = findViewById(R.id.grop_list);
        m_groups = new ArrayList<>();
//        m_groups.add("2222");
        m_adpter =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,m_groups);
        group_list.setAdapter(m_adpter);

        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String groupName = m_groups.get(position);
                        XmppConnection.joinMultiUserChat(activity.getXmppConnection().getUser(), "",groupName);
                        Intent intent = new Intent(GroupActivity.this, ChatActivity.class);
                        intent.putExtra("isGroup", true);
                        intent.putExtra("USERNAME", groupName);
                        startActivity(intent);
            }
        });


        btn_addgroup = findViewById(R.id.btn_add_2);
        btn_addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this,AddGroup.class);
                startActivityForResult(intent,2);
            }
        });
        loadGroups();
    }

    private void loadGroups(){
        try {
            //遍历每个人所创建的群
            for (HostedRoom host : MultiUserChat.getHostedRooms(XmppConnection.getConnection(), XmppConnection.getConnection().getServiceName())) {
                //遍历某个人所创建的群
                for (HostedRoom singleHost : MultiUserChat.getHostedRooms(XmppConnection.getConnection(), host.getJid())) {
                    RoomInfo info = MultiUserChat.getRoomInfo(XmppConnection.getConnection(),
                            singleHost.getJid());
                    if (singleHost.getJid().indexOf("@") > 0) {
                        m_groups.add(singleHost.getName());
                    }
                }
            }
            handler.sendEmptyMessage(0);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            m_adpter.notifyDataSetChanged();
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d("在", "onKeyDown: back键被按下");
               android.support.v7.app.AlertDialog.Builder dialog = new AlertDialog.Builder(this);
               dialog.setTitle("提示");
               dialog.setMessage("确定退出吗?");
               dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       ActivityCollector.finishAll();
                   }
               });

               dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
               dialog.show();

        }
        return true;
    }

}
