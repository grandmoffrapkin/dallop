package com.example.android.allo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_setup);

        openCamera = findViewById(R.id.open_camera);
        mNext = findViewById(R.id.next);
        profilePicture = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        final String userPhone = getIntent().getStringExtra("userPhone");

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
                if (userName.getText().toString().equals(""))
                    Toast.makeText(ProfileSetupActivity.this, "Please provide a name", Toast.LENGTH_SHORT).show();
                else {

                    final ProgressDialog progressDialog
                            = new ProgressDialog(ProfileSetupActivity.this);
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("user").child(user.getUid()).child("profileImage");

                        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
                                            intent.putExtra("imagePath", imageUri.toString());
                                            intent.putExtra("userName", userName.getText().toString());
                                            intent.putExtra("userPhone", userPhone);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(ProfileSetupActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
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
            imageUri = data.getData();
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