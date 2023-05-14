package com.example.boostiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.FirebaseException;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;


public class ForgotPasswordPage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fb;
    EditText editTextUsername;
    String USERNAME;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        firebaseAuth = FirebaseAuth.getInstance();
        fb = FirebaseFirestore.getInstance();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this , MainActivity.class));
    }

    public void onGetOTPClick(View view) {
        editTextUsername = findViewById(R.id.editTextUserName);
        USERNAME = editTextUsername.getText().toString();


        checkUser(USERNAME);

    }

    public void checkUser(String USERNAME){
        DocumentReference documentReference = fb.collection("user").document(USERNAME);
        documentReference.get().addOnCompleteListener(task -> {

            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                otpSend(String.valueOf(document.getData().get("number")));
            }
            else{
                Toast.makeText(this, "user not exists!", Toast.LENGTH_SHORT).show();
            }

        });

    }
    public void otpSend(String phoneNumber){

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Intent intent = new Intent(getApplicationContext() , Otp_forgot.class);
                intent.putExtra("verificationId" , verificationId);
                intent.putExtra("number" , phoneNumber);
                intent.putExtra("USERNAME" , USERNAME);

                startActivity(intent);

            }
        };


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

}
