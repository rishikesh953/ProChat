package com.google.prochat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.prochat.Models.ChatMessage;
import com.google.prochat.R;
import com.google.prochat.databinding.ActivityChatSenderBinding;
import com.google.prochat.databinding.ActivityChatUserBinding;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<ChatMessage> messages;
    private final int ITEM_USER = 101;
    private final int ITEM_SENDER = 102;

    public ChatAdapter(Context context, ArrayList<ChatMessage> messages)
    {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_USER)
        {
            View v = LayoutInflater.from(context).inflate(R.layout.activity_chat_user,parent,
                    false);
            return new UserViewHolder(v);
        }

        else
        {
            View v = LayoutInflater.from(context).inflate(R.layout.activity_chat_sender, parent,
                    false);
            return new SenderViewHolder(v);
        }
    }


    @Override
    public int getItemViewType(int position) {

        ChatMessage message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getUserid()))
        {
            return ITEM_USER;
        }
        else
        {
            return ITEM_SENDER;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        ChatMessage message = messages.get(position);

        if(holder.getClass() == UserViewHolder.class)
        {
            UserViewHolder viewHolder = (UserViewHolder) holder;
            viewHolder.binding.messageTextView.setText(message.getMessage());
        }
        else
        {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.binding.messageTextViewSender.setText(message.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        ActivityChatUserBinding binding;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
           binding = ActivityChatUserBinding.bind(itemView);
        }
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder{

        ActivityChatSenderBinding binding;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ActivityChatSenderBinding.bind(itemView);

        }
    }


}
