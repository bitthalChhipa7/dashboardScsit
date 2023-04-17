package com.example.boostiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Switch;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class HomeAutomation extends AppCompatActivity {
    Switch switch1;
    Switch switch2;
    String Uname;
    FirebaseFirestore fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_automation);

        Uname = getIntent().getStringExtra("uname");

        fb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fb.collection("HomeAuto").document(Uname);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                switch1 = findViewById(R.id.switch1);
                switch2 = findViewById(R.id.switch2);
                String str1 , str2;

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    str1  =String.valueOf(document.getData().get("switch1"));
                    str2 =  String.valueOf(document.getData().get("switch2"));
                    switch1.setChecked(Boolean.parseBoolean(str1));
                    switch2.setChecked(Boolean.parseBoolean(str2));
                }
            }
        });
    }

    public void onSwitch1Click(View view) {
        fb = FirebaseFirestore.getInstance();
        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        HomeAutoComp comp = new HomeAutoComp();

        comp.setSwitch2(switch2.isChecked());
        comp.setSwitch1(switch1.isChecked());
        fb.collection("HomeAuto").document(Uname).set(comp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else if (!task.isSuccessful()) {

                }
            }
        });
    }

    public void onSwitch2Click(View view) {
        fb = FirebaseFirestore.getInstance();
        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);

        HomeAutoComp comp = new HomeAutoComp();

        comp.setSwitch1(switch1.isChecked());
        comp.setSwitch2(switch2.isChecked());
        fb.collection("HomeAuto").document(Uname).set(comp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else if (!task.isSuccessful()) {

                }
            }
        });

    }
}