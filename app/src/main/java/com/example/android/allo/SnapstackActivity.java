package com.example.android.allo;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class SnapstackActivity extends AppCompatActivity {

    private ArrayList<String> mediaUrlList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapstack);
    }
}