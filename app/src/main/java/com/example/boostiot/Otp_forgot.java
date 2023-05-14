package com.example.boostiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.type.Color;

import java.util.Locale;


public class Otp_forgot extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 60000;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;

    TextView textViewTimer;
    TextView textViewResend;
    CountDownTimer cTimer;
    String providedOTP;
    EditText editTextOTP;
    String USERNAME;

    TextView textViewMobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_forgot);
        USERNAME = getIntent().getStringExtra("USERNAME");
         providedOTP = getIntent().getStringExtra("verificationId");
         textViewMobileNumber = findViewById(R.id.textViewMobileNumber);
         textViewTimer = findViewById(R.id.textViewTimer);
         textViewResend = findViewById(R.id.textViewResend);
         startTimer();

         textViewMobileNumber.setText("+91"+getIntent().getStringExtra("number").replaceAll("\\w(?=\\w{4})", "*"));



    }
    public void onSubmitOtpClick(View view) {
        editTextOTP = findViewById(R.id.editTextOtp);
        String userOTP = editTextOTP.getText().toString();
        if(userOTP.length()!=6){
            Toast.makeText(this, "incorrect OTP", Toast.LENGTH_SHORT).show();
        }
       else{
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(providedOTP, userOTP);
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext() , ChangePassword.class);
                        intent.putExtra("USERNAME" , USERNAME);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    void startTimer() {
        textViewResend.setEnabled(false);
        textViewResend.setTextColor(getResources().getColor(R.color.grey , getApplicationContext().getTheme()));

        cTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            public void onFinish() {
                textViewResend.setTextColor(getResources().getColor(R.color.boost , getApplicationContext().getTheme()));
                textViewResend.setEnabled(true);

            }
        }.start();
    }

    public void updateCountDownText(){
        int minutes = (int)(timeLeftInMillis/1000) /60;
        int seconds = (int ) (timeLeftInMillis/1000) % 60;
        textViewTimer.setText(String.format(Locale.getDefault() ,"%02d:%02d" , minutes , seconds));
    }

    public void onResendClick(View view) {
        Toast.makeText(this, "Resending OTP", Toast.LENGTH_SHORT).show();
        timeLeftInMillis = 300000;
        startTimer();
    }
}