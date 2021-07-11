package com.example.freshveggies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread=new Thread(){
          public void run(){
              try {
                  sleep(1500);
              }
              catch (Exception e){
                  e.printStackTrace();
              }finally {
                  startActivity(new Intent(getApplicationContext(),MainActivity.class));
              }
          }
        };thread.start();
    }
}