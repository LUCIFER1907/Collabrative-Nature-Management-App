package com.allen.collabrativenaturemanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class adddata extends AppCompatActivity {
    String Type;
    Button uploadBtn, showBtn;
    ImageView imageView;
    TextView Lat, Longi;
    EditText name, Description;
    ProgressBar progressBar;
    DatabaseReference root, common, common2, direct;
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    Uri imageUri;
    String Latitude;
    String Longitude;
    String city;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);

        Type =  getIntent().getStringExtra("type");
        common = FirebaseDatabase.getInstance().getReference("Images");
        common2 = FirebaseDatabase.getInstance().getReference("Common");
        root = FirebaseDatabase.getInstance().getReference(Type);
        uploadBtn = findViewById(R.id.upload);
        showBtn = findViewById(R.id.show);
        Lat = findViewById(R.id.lat);
        Longi = findViewById(R.id.longi);
        name = findViewById(R.id.name);
        Description = findViewById(R.id.description);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageView);
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);

            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null && name.getText().toString().trim().length() > 0){
                    uploadFirebase(imageUri);
                    Intent intent=new Intent(adddata.this,DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(adddata.this, "No imaage Selected", Toast.LENGTH_SHORT).show();
                }
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
                Lat.setText(Latitude);
                Longitude = String.valueOf(location.getLongitude());
                Longi.setText(Longitude);
                city = Location(location.getLatitude(), location.getLongitude());
                direct = FirebaseDatabase.getInstance().getReference(city);
                Toast.makeText(adddata.this, city, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(adddata.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(adddata.this, city, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(adddata.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(adddata.this, "Permission not granted", Toast.LENGTH_SHORT).show();
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
    private void uploadFirebase(Uri Uri) {

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(Uri));
        fileRef.putFile(Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Model model = new Model(uri.toString());
                        String modelID = root.push().getKey();
                        common.child(city).child(modelID).setValue(model);
                        common2.child(modelID).setValue(model);
                        direct.child(modelID).setValue(model);
                        root.child(city).child(modelID).setValue(model);
                        root.child(city).child(modelID).child("Name").setValue(name.getText().toString());
                        root.child(city).child(modelID).child("Description").setValue(Description.getText().toString());
                        root.child(city).child(modelID).child("Latitude").setValue(Latitude);
                        root.child(city).child(modelID).child("Longitude").setValue(Longitude);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(adddata.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(adddata.this, "Upload Failed --"+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}