package com.allen.collabrativenaturemanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class show extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Model> list;

    private MyAdapter adapter;


    private String Latitude;
    private String Longitude;
    String city;
    private DatabaseReference root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new MyAdapter(this , list);
        recyclerView.setAdapter(adapter);

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
                root = FirebaseDatabase.getInstance().getReference(city);
                Toast.makeText(show.this, city, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(show.this, "Not Found", Toast.LENGTH_SHORT).show();
            }

        }
        //here part1 , onRequestPermissionsResult, private String Location()

        root = FirebaseDatabase.getInstance().getReference(city);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model model = dataSnapshot.getValue(Model.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                        Toast.makeText(show.this, city, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(show.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(show.this, "Permission not granted", Toast.LENGTH_SHORT).show();
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