package com.example.boostiot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.view.View;


public class StartPage extends AppCompatActivity {
    View imageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        imageView = findViewById(R.id.imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartPage.this , MainActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartPage.this , imageView , "boostLogo");
                startActivity(intent , options.toBundle());
            }
        } , 3000);
    }
}