package com.example.android.allo.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.allo.R;
import com.example.android.allo.Chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<com.example.android.allo.Chat.MessageAdapter.MessageViewHolder> {

    ArrayList<MessageObject> messageList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessageAdapter(ArrayList<MessageObject> Message){
        this.messageList = Message;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView;

        if(viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);
            return new MessageViewHolder(layoutView);
        }

        else if(viewType == VIEW_TYPE_MESSAGE_SENT){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, null,false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);
            return new MessageViewHolder(layoutView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {

        holder.mMessage.setText(messageList.get(position).getMessage());
        holder.mSender.setText(messageList.get(position).getSenderId());

        if(messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty())
            holder.mViewMedia.setVisibility(View.GONE);

        holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUrlList())
                        .setStartPosition(0)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageObject message = (MessageObject) messageList.get(position);

        if(message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return VIEW_TYPE_MESSAGE_SENT;
        else return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView mMessage, mSender;
        Button mViewMedia;
        LinearLayout mLayout;

        MessageViewHolder(View view){
            super(view);
            mLayout = view.findViewById(R.id.layout);
            mMessage = view.findViewById(R.id.message);
            mSender = view.findViewById(R.id.sender);
            mViewMedia = view.findViewById(R.id.viewMedia);
        }
    }
}

