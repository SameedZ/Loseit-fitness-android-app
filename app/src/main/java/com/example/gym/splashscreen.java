package com.example.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;

public class splashscreen extends AppCompatActivity {
    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avisplash);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    startAnim();
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {

                    Intent intent = new Intent(splashscreen.this,login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
                    finish();
                }
            }
        };
        thread.start();
    }


    private void startAnim() {
        avi.smoothToShow();
    }
}