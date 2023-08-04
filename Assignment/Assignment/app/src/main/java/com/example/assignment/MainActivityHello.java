package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.assignment.filebase.MainActivityLogin;

public class MainActivityHello extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hello);

        //Sử dụng handler chuyển màn hình sau 3s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Sau 3s chạy màn hình Login firebase
                startActivity(new Intent(MainActivityHello.this, MainActivityLogin.class));
            }
        }, 3000);
    }
}