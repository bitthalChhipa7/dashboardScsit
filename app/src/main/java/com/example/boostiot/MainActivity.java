package com.example.boostiot;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
public class MainActivity extends AppCompatActivity {



    EditText editText1;
    EditText editText2;
    ImageView imageView;
    ProgressBar progressBar;
    TextView forgotPasswordButton;
    TextView textView2;
    TextView SignUpButton;
    TextView tempText;
    Button loginButton;
    TextInputLayout editTextLayoutUsername;
    TextInputLayout editTextLayoutPassword;
    FirebaseFirestore fb;
    private long backPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        editTextLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        loginButton = findViewById(R.id.button2);
        forgotPasswordButton = findViewById(R.id.textView);
        tempText = findViewById(R.id.textViewTemp);
        SignUpButton = findViewById(R.id.signUpTextView);
        textView2 = findViewById(R.id.textView2);
        progressBar = findViewById(R.id.progressBar);
        editText1 = findViewById(R.id.editTextTextPersonName1);
        editText2 = findViewById(R.id.editTextTextPersonName2);
        imageView = findViewById(R.id.imageView2);




        progressBar.setVisibility(View.GONE);
        editTextLayoutUsername.setVisibility(View.VISIBLE);
        editTextLayoutPassword.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        forgotPasswordButton.setVisibility(View.VISIBLE);
        tempText.setVisibility(View.VISIBLE);
        SignUpButton.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);

    }
    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {
            backPressedTime = t;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }
    public void onLoginClick(View view) {



        String UserName = editText1.getText().toString();
        String PassWord = editText2.getText().toString();
        if(UserName.isEmpty() || PassWord.isEmpty()){
            Toast.makeText(this, "Please fill username and password!", Toast.LENGTH_SHORT).show();
        }
        else{
            editTextLayoutUsername.setVisibility(View.GONE);
            editTextLayoutPassword.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            forgotPasswordButton.setVisibility(View.GONE);
            tempText.setVisibility(View.GONE);
            SignUpButton.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);


            progressBar.setVisibility(View.VISIBLE);





            getAllData(UserName , PassWord);}
    }
        public void onSignUpClick(View view) {
        startActivity(new Intent(this, SignPage.class));
    }
    public void onForgotPasswordClick(View view) {
        startActivity(new Intent(this, ForgotPasswordPage.class));
    }
    public void getAllData(String editTextUname , String editTextPass) {
        fb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fb.collection("user").document(editTextUname);
        documentReference.get().addOnCompleteListener(task -> {
            String str1 , str2;
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                str1  =String.valueOf(document.getData().get("username"));
                str2 =  String.valueOf(document.getData().get("password"));
                if(editTextPass.equals(str2)){
                Intent intent = new Intent(getApplicationContext() , MainPage.class);
                intent.putExtra("uname" , str1);

                startActivity(intent);
                }
                else{
                    editTextLayoutUsername.setVisibility(View.VISIBLE);
                    editTextLayoutPassword.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    forgotPasswordButton.setVisibility(View.VISIBLE);
                    tempText.setVisibility(View.VISIBLE);
                    SignUpButton.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);


                    Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                editTextLayoutUsername.setVisibility(View.VISIBLE);
                editTextLayoutPassword.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                forgotPasswordButton.setVisibility(View.VISIBLE);
                tempText.setVisibility(View.VISIBLE);
                SignUpButton.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


                Toast.makeText(this, "Invalid user!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}