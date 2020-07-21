package com.example.android.allo.Chat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.allo.ChatActivity;
import com.example.android.allo.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter<com.example.android.allo.Chat.ChatListAdapter.ChatListViewHolder> {

    ArrayList<ChatObject> chatList;

    public ChatListAdapter(ArrayList<ChatObject> chatList){
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatListViewHolder rcv = new ChatListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {
        holder.mContactName.setText(chatList.get(position).getContactName());
        holder.mLastMessage.setText(chatList.get(position).getLastMessage());
        holder.mProfilePicture.setImageURI(chatList.get(position).getProfileImageUri());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("chatObject", chatList.get(holder.getAdapterPosition()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public static class ChatListViewHolder extends RecyclerView.ViewHolder {

        public TextView mContactName, mLastMessage;
        public ImageView mProfilePicture;
        public LinearLayout mLayout;

        public ChatListViewHolder(View view) {
            super(view);
            mContactName = view.findViewById(R.id.contact_name);
            mLastMessage = view.findViewById(R.id.last_message);
            mProfilePicture = view.findViewById(R.id.profile_image);
            mLayout = view.findViewById(R.id.layout);
        }
    }
}

