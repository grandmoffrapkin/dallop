package com.example.android.allo.Chat;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.allo.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter {

    ArrayList<MessageObject> messageList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessageAdapter(ArrayList<MessageObject> Message) {
        this.messageList = Message;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView;

        if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);
            return new ReceivedMessageViewHolder(layoutView);
        }

        else if(viewType == VIEW_TYPE_MESSAGE_SENT){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, null,false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);
            return new SentMessageViewHolder(layoutView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageObject messageObject = messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageViewHolder) holder).bind(messageObject);
                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageViewHolder) holder).bind(messageObject);
                break;
        }

//        if(messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty())
//            holder.mViewMedia.setVisibility(View.GONE);
//
//        holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUrlList())
//                        .setStartPosition(0)
//                        .show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageObject message = messageList.get(position);

        if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return VIEW_TYPE_MESSAGE_SENT;
        else return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView mMessage, mTime;
        //TextView mSender;
        //Button mViewMedia;
        LinearLayout mLayout;

        SentMessageViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.layout);
            mMessage = view.findViewById(R.id.text_message_body);
            //mSender = view.findViewById(R.id.sender);
            mTime = view.findViewById(R.id.text_message_time);
            //mViewMedia = view.findViewById(R.id.viewMedia);
        }

        void bind(MessageObject message) {
            mMessage.setText(message.getMessage());

            mTime.setText(DateUtils.formatDateTime(null, 90000, 1));
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage, mSender, mTime;
        //Button mViewMedia;
        LinearLayout mLayout;
        //profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);

        ReceivedMessageViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.layout);
            mMessage = view.findViewById(R.id.text_message_body);
            mSender = view.findViewById(R.id.text_message_name);
            mTime = view.findViewById(R.id.text_message_time);
            //mViewMedia = view.findViewById(R.id.viewMedia);
        }

        void bind(MessageObject message) {
            mMessage.setText(message.getMessage());
            mTime.setText(DateUtils.formatDateTime(null, 90000, 1));
            mSender.setText(message.getSenderId());
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }
}

