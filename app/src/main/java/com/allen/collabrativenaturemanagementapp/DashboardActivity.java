package com.allen.collabrativenaturemanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1000);
        }else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try {
                String city = Location(location.getLatitude(), location.getLongitude());
                Toast.makeText(DashboardActivity.this, city, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DashboardActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
            }

        }


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
                            Toast.makeText(DashboardActivity.this, city, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DashboardActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(DashboardActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.home_icon :
                Toast.makeText(DashboardActivity.this, "Home Icon Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_icon :
                Toast.makeText(DashboardActivity.this, "add_icon Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Addnew.class));
                break;
            case R.id.area :
                Toast.makeText(DashboardActivity.this, "area Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), show.class));
                break;
            case R.id.common :
                Toast.makeText(DashboardActivity.this, "common Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), common.class));
                break;
            case R.id.logout :
                Toast.makeText(DashboardActivity.this, "logout Clicked", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(DashboardActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}