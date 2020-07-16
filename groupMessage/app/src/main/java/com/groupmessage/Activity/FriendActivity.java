package com.groupmessage.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.groupmessage.R;
import com.groupmessage.utils.ActivityCollector;
import com.groupmessage.utils.LogUtil;
import com.groupmessage.utils.ToastUtils;
import com.groupmessage.xmpp.XmppConnection;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;


import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;

public class FriendActivity extends BaseActivity {

    private static final int ADD_FRIEND = 0x001;
    private static final int FRESH_VIEW = 0x002;



    ListView friend_list;

    ArrayList<String > m_friends;

    ArrayAdapter<String> m_adpter;


    private BottomBar mBottomBar;

    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        init();

        //mBottomBar = BottomBar.attach(this, savedInstanceState);
        //添加底部的菜单栏
        mBottomBar = findViewById(R.id.bottomBar);
        mBottomBar.setItemsFromMenu(R.menu.bottom_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                switch (menuItemId) {
                    case R.id.tab_friend:
                        //ToastUtils.showShort(FriendActivity.this,"1");
                        break;
                    case R.id.tab_group:
                        //ToastUtils.showShort(FriendActivity.this,"2");
                        Intent intent = new Intent();
                        intent.setClass(FriendActivity.this,GroupActivity.class);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });

    }

    private  void  init(){
        friend_list = findViewById(R.id.friend_list);
        m_friends =new ArrayList<>();
//        m_friends.add("11111");
        m_adpter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,m_friends);

        friend_list.setAdapter(m_adpter);

        loadFriends();
        addSubscriptionListener();  //增加监听

        //点击进行聊天
        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = m_friends.get(position);
                //ToastUtils.showShort(activity,name);
                Intent intent = new Intent(activity,ChatActivity.class);
                if(!name.contains("@"))
                    name=name+"@"+XmppConnection.SERVER_NAME+"/Smack";

                name=name+"/Smack";
                intent.putExtra("friendname",name);
                startActivity(intent);
            }
        });

        //在上面添加工具栏
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("好友列表");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);     //设置菜单图标
        }

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);  //滑动菜单
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();           //滑动菜单按键点击
                return true;
            }
        });

    }


    private void loadFriends(){
         new Thread(new Runnable() {
            public void run() {
                try {
                    Roster roster = XmppConnection.getConnection().getRoster();
                    Collection<RosterEntry> entries = roster.getEntries();
                    for (RosterEntry entry : entries) {
                        m_friends.add(entry.getUser());
                    }
                    Message msg = new Message();
                    msg.what = FRESH_VIEW;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 添加一个监听，监听好友添加请求。
     */
    private void addSubscriptionListener() {
        //创建包过滤器
        PacketFilter filter = new PacketFilter() {
            @Override
            public boolean accept(Packet packet) {
                if (packet instanceof Presence) {
                    Presence presence = (Presence) packet;
                    //是好友邀请状态就返回true 向下执行
                    if (presence.getType().equals(Presence.Type.subscribe)) {
                        return true;
                    }
                }
                return false;
            }
        };
        //开启监听

        XmppConnection.getConnection().addPacketListener(subscriptionPacketListener, filter);
    }




    /**
     * 好友监听，监听有没有加好友的信息
     */
    private PacketListener subscriptionPacketListener = new PacketListener() {

        @Override
        public void processPacket(final Packet packet) {
            //需要过滤自己加自己的状态
            Message msg = new Message();
            msg.obj = packet;
            msg.what = ADD_FRIEND;
            handler.sendMessage(msg);
            LogUtil.i("Friend","有人加你");
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case FRESH_VIEW:
                    //refreshViews();
                    m_adpter.notifyDataSetChanged();
                    break;
                case ADD_FRIEND:
                    showAddFriend((Packet) msg.obj);
                    break;
            }
        }
    };

    private  void  showAddFriend(final Packet packet){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请求添加");
        dialog.setMessage("确定添加"+packet.getFrom()+"为好友");
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showShort(FriendActivity.this,"你同意了");
                m_friends.add(packet.getFrom());
               // m_adpter.add(packet.getFrom());
                m_adpter.notifyDataSetChanged();        //刷新列表

            }
        });

        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showShort(FriendActivity.this,"你不同意");
            }
        });

        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==1)
                    m_friends.add(data.getStringExtra("user"));
        }
        m_adpter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START); //start事为了跟activity_main.xml中的滑动方向一样
                break;

            case R.id.addfriend:
                //Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FriendActivity.this,AddFriend.class);    //进入加好友界面
                startActivityForResult(intent,1);   //请求结果
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;

            default:
        }
        return true;
    }

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
