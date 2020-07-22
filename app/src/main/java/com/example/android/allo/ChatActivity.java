package com.example.android.allo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.android.allo.Chat.ChatObject;
import com.example.android.allo.Chat.MediaAdapter;
import com.example.android.allo.Chat.MessageAdapter;
import com.example.android.allo.Chat.MessageObject;
import com.example.android.allo.User.UserObject;
import com.example.android.allo.utils.SendNotification;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChat, mMedia;
    ImageButton mSnapStack;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager, mMediaLayoutManager;

    ArrayList<MessageObject> messageList;

    ChatObject mChatObject;

    DatabaseReference mChatMessagesDb;
    private RecyclerView.Adapter<MediaAdapter.MediaViewHolder> mMediaAdapter;

    int PICK_IMAGE_INTENT = 1;
    ArrayList<String> mediaUriList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatObject = (ChatObject) getIntent().getSerializableExtra("chatObject");

        mChatMessagesDb = FirebaseDatabase.getInstance().getReference().child("chat").child(mChatObject.getChatId()).child("messages");

        Button mSend = findViewById(R.id.send);
        Button mAddMedia = findViewById(R.id.addMedia);

        mSnapStack = findViewById(R.id.snap_stack);
        mSnapStack.setVisibility(View.VISIBLE);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        initializeMessage();

        getChatMessages();
    }

    private void getChatMessages() {
        mChatMessagesDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String text = "",
                            creatorID = "";

                    ArrayList<String> mediaUrlList = new ArrayList<>();

                    if(dataSnapshot.child("text").getValue() != null)
                        text = Objects.requireNonNull(dataSnapshot.child("text").getValue()).toString();

                    if(dataSnapshot.child("creator").getValue() != null)
                        creatorID = Objects.requireNonNull(dataSnapshot.child("creator").getValue()).toString();

                    if (dataSnapshot.child("media").getChildrenCount() > 0)
                        for (DataSnapshot mediaSnapshot : dataSnapshot.child("media").getChildren())
                            mediaUrlList.add(Objects.requireNonNull(mediaSnapshot.getValue()).toString());

                    MessageObject mMessage = new MessageObject(dataSnapshot.getKey(), creatorID, text);

                    messageList.add(mMessage);
                    mChatLayoutManager.scrollToPosition(messageList.size() - 1);
                    mChatAdapter.notifyDataSetChanged();

                    if (!mediaUriList.isEmpty()) {
                        //check for storage read and write permissions

                        //get Bitmap from Uris
                        for (String mediaUri : mediaUrlList) {
                            Bitmap image = getContactBitmapFromUri(ChatActivity.this, Uri.parse(mediaUri));
                            saveBitmapToStorage(ChatActivity.this, image, creatorID, mMessage.getMessageId());
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    int totalMediaUploaded = 0;
    ArrayList<String> mediaIdList = new ArrayList<>();
    EditText mMessage;
    private void sendMessage(){
        mMessage = findViewById(R.id.message);
        String messageId = mChatMessagesDb.push().getKey();
        assert messageId != null;
        final DatabaseReference newMessageDb = mChatMessagesDb.child(messageId);

        final Map<String, Object> newMessageMap = new HashMap<>();

        newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

        if(!mMessage.getText().toString().isEmpty())
            newMessageMap.put("text", mMessage.getText().toString());

        if(!mediaUriList.isEmpty()){
            for(String mediaUri : mediaUriList){
                final String mediaId = newMessageDb.child("media").push().getKey();
                mediaIdList.add(mediaId);
                assert mediaId != null;
                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("chat").child(mChatObject.getChatId()).child(messageId).child(mediaId);

                UploadTask uploadTask = filePath.putFile(Uri.parse(mediaUri));

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newMessageMap.put("/media/" + mediaIdList.get(totalMediaUploaded) + "/", uri.toString());

                                totalMediaUploaded++;
                                if(totalMediaUploaded == mediaUriList.size()) {
                                    updateDatabaseWithNewMessage(newMessageDb, newMessageMap);
                                }
                            }
                        });
                    }
                });
            }
        }
        else {
            if(!mMessage.getText().toString().isEmpty())
                updateDatabaseWithNewMessage(newMessageDb, newMessageMap);
        }
    }

    private void updateDatabaseWithNewMessage(DatabaseReference newMessageDb, Map<String, Object> newMessageMep) {
        newMessageDb.updateChildren(newMessageMep);
        mMessage.setText(null);
        mediaUriList.clear();
        mediaIdList.clear();
        mMediaAdapter.notifyDataSetChanged();

        String message;

        if (newMessageMep.get("text") != null)
            message = Objects.requireNonNull(newMessageMep.get("text")).toString();
        else
            message = "Media";

        for(UserObject mUser : mChatObject.getUserObjectArrayList()){
            if(!mUser.getUid().equals(FirebaseAuth.getInstance().getUid())){
                new SendNotification(message, "New message!", mUser.getNotificationKey());
            }
        }
    }


    private void initializeMessage(){
        messageList = new ArrayList<>();
        mChat = findViewById(R.id.messageList);
        mChat.setNestedScrollingEnabled(false);
        mChat.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mChat.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new MessageAdapter(messageList);
        mChat.setAdapter(mChatAdapter);
    }

    private void initializeMedia(){
        mediaUriList = new ArrayList<>();
        mMedia = findViewById(R.id.mediaList);
        mMedia.setNestedScrollingEnabled(false);
        mMedia.setHasFixedSize(false);
        mMediaLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mMedia.setLayoutManager(mMediaLayoutManager);
        mMediaAdapter = new MediaAdapter(getApplicationContext(), mediaUriList);
        mMedia.setAdapter(mMediaAdapter);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture(s)"), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {
                assert data != null;
                if (data.getClipData() == null) {
                    mediaUriList.add(Objects.requireNonNull(data.getData()).toString());
                } else {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }
            }
            mMediaAdapter.notifyDataSetChanged();
        }
    }

    //Getting bitmap from Uris
    public Bitmap getContactBitmapFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Saving bitmap to storage
    public void saveBitmapToStorage(Context context, Bitmap finalBitmap, String creatorId, String messageId) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mFile = new File("./" + creatorId, messageId + ".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(mFile);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}