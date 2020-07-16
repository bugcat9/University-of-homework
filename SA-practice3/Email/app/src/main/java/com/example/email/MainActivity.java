package com.example.email;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendEmail =findViewById(R.id.sendEmail);
        Button validateEmail =findViewById(R.id.validateEmail);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent();
                intent.setClass(MainActivity.this,SendEmail.class);
                startActivity(intent);
            }
        });

        validateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent();
                intent.setClass(MainActivity.this,validateEmail.class);
                startActivity(intent);
            }
        });
    }
}
