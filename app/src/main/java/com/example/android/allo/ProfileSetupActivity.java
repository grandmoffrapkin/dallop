package com.example.android.allo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ProfileSetupActivity extends AppCompatActivity {

    private static final int cameraAccessCode = 300;
    ImageButton openCamera;
    Button mNext;
    ImageView profilePicture;
    EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        getSupportActionBar().hide();

        openCamera = findViewById(R.id.open_camera);
        mNext = findViewById(R.id.next);
        profilePicture = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProfileSetupActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(ProfileSetupActivity.this, new String[]{Manifest.permission.CAMERA}, cameraAccessCode);
                else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, cameraAccessCode);
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().equals(""))
                    Toast.makeText(ProfileSetupActivity.this, "Please provide a name", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cameraAccessCode) {
            assert data != null;
            Bitmap profile_image = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            profilePicture.setImageBitmap(profile_image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == cameraAccessCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Please grant permission to setup your profile picture", Toast.LENGTH_SHORT).show();
        }
    }
}