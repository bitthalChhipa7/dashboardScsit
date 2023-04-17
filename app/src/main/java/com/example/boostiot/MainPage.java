package com.example.boostiot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainPage extends AppCompatActivity {
String Uname;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Uname = getIntent().getStringExtra("uname");

    }
    public void onHomeAutoClick(View view) {
        intent = new Intent(this , HomeAutomation.class);
        intent.putExtra("uname" , Uname);
        startActivity(intent);
    }

    public void onSmartNoticeClick(View view) {
        startActivity(new Intent(this , SmartNoticeBoard.class));
    }

    public void onSmartDustbinClick(View view) {
        startActivity(new Intent(this , SmartDustbin.class));
    }
}