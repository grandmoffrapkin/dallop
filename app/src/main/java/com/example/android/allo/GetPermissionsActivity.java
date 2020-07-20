package com.example.android.allo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GetPermissionsActivity extends AppCompatActivity {

    Button getPermissions;
    Map<String, Integer> reqCodes;
    int numberOfPermissionsDenied;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_get_permissions);

        getPermissions = findViewById(R.id.get_perms);
        reqCodes = new HashMap<>();
        reqCodes.put("camera", 300);
        reqCodes.put("read_storage", 401);
        reqCodes.put("write_storage", 402);
        reqCodes.put("read_contacts", 501);
        reqCodes.put("write_contacts", 502);
        reqCodes.put("record_audio", 600);

        getPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfPermissionsDenied = 0;
                getAllSetupPermissions(reqCodes);
                if(numberOfPermissionsDenied > 0) Toast.makeText(GetPermissionsActivity.this, "Please grant all the permissions for smooth functioning of the app", Toast.LENGTH_SHORT).show();
                else if (numberOfPermissionsDenied == 0) startActivity(new Intent(GetPermissionsActivity.this, LoginActivity.class));
            }
        });

    }

    private void getAllSetupPermissions(Map<String, Integer> reqCodes) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, reqCodes.get("camera"));
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, reqCodes.get("read_storage"));
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCodes.get("write_storage"));
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, reqCodes.get("read_contacts"));
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_CONTACTS}, reqCodes.get("write_contacts"));
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, reqCodes.get("record_audio"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == reqCodes.get("camera")){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) numberOfPermissionsDenied++;
        }

        else if(requestCode == reqCodes.get("read_storage")){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) numberOfPermissionsDenied++;
        }

        else if(requestCode == reqCodes.get("write_storage")){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) numberOfPermissionsDenied++;
        }

        else if(requestCode == reqCodes.get("read_contacts")){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) numberOfPermissionsDenied++;
        }

        else if(requestCode == reqCodes.get("write_contacts")){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) numberOfPermissionsDenied++;
        }

        else if(requestCode == reqCodes.get("record_audio")){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) numberOfPermissionsDenied++;
        }
    }
}