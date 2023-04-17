package com.example.boostiot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    EditText editText1;
    EditText editText2;
    FirebaseFirestore fb;
    boolean passwordVisible;

    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText2 = findViewById(R.id.editTextTextPersonName2);
        editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX()>=editText2.getRight()-editText2.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = editText2.getSelectionEnd();
                        if(passwordVisible){
                            editText2.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24 , 0);
                            editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }
                        else{
                            editText2.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24 , 0);
                            editText2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        editText2.setSelection(selection);
                        return true;
                    }
                }
                return false;

            }
        });
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {
            backPressedTime = t;
            Toast.makeText(this, "Press back again to exit",
                    Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    public void onLoginClick(View view) {
        editText1 = findViewById(R.id.editTextTextPersonName1);
        editText2 = findViewById(R.id.editTextTextPersonName2);

        String UserName = editText1.getText().toString();
        String PassWord = editText2.getText().toString();
        if(UserName.isEmpty() || PassWord.isEmpty()){
            Toast.makeText(this, "Please fill username and password!", Toast.LENGTH_SHORT).show();
        }
        else{
            getAllData(UserName , PassWord);
        }


    }

    public void onSignUpClick(View view) {
        startActivity(new Intent(this, SignPage.class));
    }

    public void onForgotPasswordClick(View view) {
        startActivity(new Intent(this, ForgotPasswordPage.class));
    }

    public void getAllData(String editTextUname , String editTextPass) {
        fb = FirebaseFirestore.getInstance();
        fb.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String Uname , PassWord;
                    Intent intent = new Intent(getApplicationContext() , MainPage.class);

                    for (QueryDocumentSnapshot query: task.getResult()) {
                        Uname = query.getData().get("username").toString();
                        PassWord  = query.getData().get("password").toString();
                        if(Uname.equals(editTextUname) && PassWord.equals(editTextPass)){
                            intent.putExtra("uname" , Uname);
//                            intent.putExtra("password" , PassWord);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}