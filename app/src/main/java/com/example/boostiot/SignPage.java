package com.example.boostiot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.content.Intent;
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

import java.util.regex.Pattern;

public class SignPage extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*\\d)" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
//                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");


    EditText username;
    EditText password;
    EditText number;
    EditText email;
    EditText confirmPass;
    FirebaseFirestore fb;
    boolean passwordVisible;
    boolean confirmPasswordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_page);

        password = findViewById(R.id.editTextTextPersonName2);
        confirmPass = findViewById(R.id.editTextTextPersonName5);

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = password.getSelectionEnd();
                        if(passwordVisible){
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24 , 0);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }
                        else{
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24 , 0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;

            }
        });

        confirmPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX()>=confirmPass.getRight()-confirmPass.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = confirmPass.getSelectionEnd();
                        if(confirmPasswordVisible){
                            confirmPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24 , 0);
                            confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            confirmPasswordVisible = false;
                        }
                        else{
                            confirmPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24 , 0);
                            confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            confirmPasswordVisible = true;
                        }
                        confirmPass.setSelection(selection);
                        return true;
                    }
                }
                return false;

            }
        });
    }

    public void onSignupClick(View view) {


        username = findViewById(R.id.editTextTextPersonName1);
        password = findViewById(R.id.editTextTextPersonName2);
        number = findViewById(R.id.editTextTextPersonName3);
        email = findViewById(R.id.editTextTextPersonName4);
        confirmPass = findViewById(R.id.editTextTextPersonName5);

        fb = FirebaseFirestore.getInstance();
        UserModel userModel = new UserModel();


        String USERNAME = username.getText().toString();
        String PASSWORD = password.getText().toString();
        String NUMBER = number.getText().toString();
        String EMAIL = email.getText().toString();
        String CONFIRMPASSWORD = confirmPass.getText().toString();

        if(USERNAME.isEmpty() || PASSWORD.isEmpty() || NUMBER.isEmpty() || EMAIL.isEmpty()){
            Toast.makeText(this, "Please fill required details!", Toast.LENGTH_SHORT).show();
        }
        else if(!CONFIRMPASSWORD.equals(PASSWORD)){
            Toast.makeText(this, "Confirm password didn't matched!", Toast.LENGTH_SHORT).show();

        }
        else if(NUMBER.length() != 10){
            number.setError("10 digit required");
        }
        else if(PASSWORD.length() <6){
            Toast.makeText(this, "Password is too short!", Toast.LENGTH_SHORT).show();

        }
        else if(!PASSWORD_PATTERN.matcher(PASSWORD).matches()){
            Toast.makeText(this, "Password must contain 1 capital , 1 small , 1 special character , 1 digit!", Toast.LENGTH_SHORT).show();

        }
        else{

            userModel.setUsername( USERNAME);
            userModel.setPassword( PASSWORD);
            userModel.setNumber( NUMBER);
            userModel.setEmail(EMAIL);


            fb.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                boolean tFlag = false;

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    String Uname;

                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot query: task.getResult()) {
                            Uname = query.getData().get("username").toString();

                            if(userModel.getUsername().equals(Uname)){
                                Toast.makeText(SignPage.this, "Username already taken!", Toast.LENGTH_SHORT).show();
                                tFlag = true;
                            }
                        }
                    }
                    if(tFlag == false) {
                        fb.collection("user").document(userModel.getUsername()).set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignPage.this, "Registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext() , MainActivity.class));
                                } else if (!task.isSuccessful()) {
                                    Toast.makeText(SignPage.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            });

        }
    }
}