package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseUser extends AppCompatActivity {

    private Button ChiefDonor, Subdonor, Needy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        ChiefDonor = findViewById(R.id.chiefbutton);
        ChiefDonor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                regchief(); }
        });

        Subdonor = findViewById(R.id.subbutton);
        Subdonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegSubdonor();
            }
        });

        Needy = findViewById(R.id.needybutton);
        Needy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegNeedy();
            }
        });
    }

    public void regchief(){
        Intent intent = new Intent(this, Chieflogin.class);
        startActivity(intent);
    }

    public void RegSubdonor(){
        Intent intent = new Intent(this, Sublogin.class);
        startActivity(intent);
    }

    public void RegNeedy(){
        Intent intent = new Intent(this, Needylogin.class);
        startActivity(intent);
    }
}