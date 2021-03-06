package com.allen.collabrativenaturemanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Addnew extends AppCompatActivity {
    public Button Birds, Animals, Landscape;
    String DBroot;
    String Latitude, Longitude;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew);


        Birds = findViewById(R.id.BirdBtn);
        Animals = findViewById(R.id.AnimalBtn);
        Landscape = findViewById(R.id.sceneBtn);

        Birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Addnew.this,adddata.class);
                DBroot = "Birds";
                intent.putExtra("type", DBroot);
                startActivity(intent);

            }
        });
        Animals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Addnew.this,adddata.class);
                DBroot = "Animals";
                intent.putExtra("type", DBroot);
                startActivity(intent);

            }
        });
        Landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Addnew.this,adddata.class);
                DBroot = "Landscape";
                intent.putExtra("type", DBroot);
                startActivity(intent);

            }
        });

        // for location reuse - copy coe from here.....
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1000);
        }else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try {
                Latitude = String.valueOf(location.getLatitude());
                Longitude = String.valueOf(location.getLongitude());
                city = Location(location.getLatitude(), location.getLongitude());
                Toast.makeText(Addnew.this, city, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Addnew.this, "Not Found", Toast.LENGTH_SHORT).show();
            }

        }
        //here part1 , onRequestPermissionsResult, private String Location()
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    try {
                        String city = Location(location.getLatitude(), location.getLongitude());
                        Toast.makeText(Addnew.this, city, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Addnew.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Addnew.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    private String Location(double lat, double lon){
        String cityname = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat,lon, 10);
            if (addresses.size() > 0 ){
                for (Address adr: addresses){
                    if(adr.getLocality()!= null && adr.getLocality().length()>0){
                        cityname = adr.getLocality();
                        break;
                    }

                }
            }
        }catch (IOException e){
            e.printStackTrace();
            cityname = "XYZ";
        }

        return cityname;
    }

}