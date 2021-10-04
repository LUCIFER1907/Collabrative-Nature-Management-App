package com.allen.collabrativenaturemanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class adddata extends AppCompatActivity {
    String Type;
    TextView type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);

        Type =  getIntent().getStringExtra("type");

        // Testing
        type = findViewById(R.id.Test2);
        type.setText(Type);


        //Testing

    }
}