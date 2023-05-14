package com.example.boostiot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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



    CardView cardView;
    ProgressBar progressBar;
    EditText username;
    EditText password;
    EditText number;
    EditText email;
    EditText confirmPass;
    FirebaseFirestore fb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_page);

        password = findViewById(R.id.editTextPassword);
        confirmPass = findViewById(R.id.editTextConfrimPassword);
        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


    }

    public void onSignupClick(View view) {


        username = findViewById(R.id.editTextUserName);
        password = findViewById(R.id.editTextPassword);
        number = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextEmail);
        confirmPass = findViewById(R.id.editTextConfrimPassword);

        fb = FirebaseFirestore.getInstance();
        UserModel userModel = new UserModel();


        String USERNAME = username.getText().toString();
        String PASSWORD = password.getText().toString();
        String NUMBER = number.getText().toString();
        String EMAIL = email.getText().toString();
        String CONFIRMPASSWORD = confirmPass.getText().toString();

        if(USERNAME.isEmpty() || PASSWORD.isEmpty() || NUMBER.isEmpty() || EMAIL.isEmpty() || CONFIRMPASSWORD.isEmpty()){
            Toast.makeText(this, "Please fill required details!", Toast.LENGTH_SHORT).show();
        }
        else if(PASSWORD.length() <6){
            Toast.makeText(this, "Password is too short!", Toast.LENGTH_SHORT).show();

        }
        else if(!CONFIRMPASSWORD.equals(PASSWORD)){
            Toast.makeText(this, "Confirm password didn't matched!", Toast.LENGTH_SHORT).show();

        }
        else if(NUMBER.length() != 10){
            number.setError("10 digit required");
        }

        else if(!PASSWORD_PATTERN.matcher(PASSWORD).matches()){
            Toast.makeText(this, "Password must contain 1 capital , 1 small , 1 special character , 1 digit!", Toast.LENGTH_SHORT).show();

        }
        else{
            cardView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            userModel.setUsername( USERNAME);
            userModel.setPassword( PASSWORD);
            userModel.setNumber( NUMBER);
            userModel.setEmail(EMAIL);

            DocumentReference documentReference = fb.collection("user").document(USERNAME);
            documentReference.get().addOnCompleteListener(task -> {

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    cardView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
                }
                else{

                    fb.collection("user").document(userModel.getUsername()).set(userModel).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(SignPage.this, "Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext() , MainActivity.class));
                        } else if (!task1.isSuccessful()) {
                            cardView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignPage.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });
        }
    }

    public void onAlreadySignClick(View view) {
        startActivity(new Intent(this , MainActivity.class));
    }
}