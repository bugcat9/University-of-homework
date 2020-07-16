package com.example.stockinquiry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.net.Socket;

public class BaseActicity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivtyControl.Add_Activity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    Connection getConnection(){
      return   Connection.getConnection();
    }
}
