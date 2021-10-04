package com.allen.collabrativenaturemanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class adddata extends AppCompatActivity {
    String Latitude, Longitude;
    String Type, city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);
        Latitude = getIntent().getStringExtra("latitude");
        Longitude = getIntent().getStringExtra("longitude");
        Type =  getIntent().getStringExtra("type");
        city = getIntent().getStringExtra("city");
        // Testing



        //Testing

    }
}