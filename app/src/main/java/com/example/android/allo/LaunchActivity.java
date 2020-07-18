package com.example.android.allo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class LaunchActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(FirebaseAuth.getInstance().getUid() != null) intent = new Intent(LaunchActivity.this, MainActivity.class);
                else intent = new Intent(LaunchActivity.this, GetPermissionsActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}