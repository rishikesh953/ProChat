package com.google.prochat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.prochat.Adapters.ChatAdapter;
import com.google.prochat.Models.ChatMessage;
import com.google.prochat.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    private ArrayList<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;

    private  String UserRoom, SenderRoom;
    private FirebaseDatabase mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarTopChat);
        getSupportActionBar().setTitle(getIntent().getStringExtra("UserName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserRoom = FirebaseAuth.getInstance().getUid() +  getIntent().getStringExtra("UserId");
        SenderRoom = getIntent().getStringExtra("UserId") + FirebaseAuth.getInstance().getUid();

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);
        binding.messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.messageRecyclerView.setAdapter(chatAdapter);

        mDatabase = FirebaseDatabase.getInstance("https://prochat-3cbfe-default-rtdb.asia-southeast1.firebasedatabase.app/");

        mDatabase.getReference()
                .child("Chats")
                .child(UserRoom)
                .child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        chatMessages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            chatMessages.add( snapshot1.getValue(ChatMessage.class));
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    binding.sendButton.setEnabled(true);
                } else {
                    binding.sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mMessageEditText = binding.messageEditText.getText().toString();
                Date date = new Date();
                ChatMessage messageChat = new ChatMessage(UserRoom, mMessageEditText,
                        FirebaseAuth.getInstance().getUid(),date.getTime());


                HashMap<String, Object> lastMessageObj = new HashMap<>();
                lastMessageObj.put("lastMsg", messageChat.getMessage());
                lastMessageObj.put("lastMsgTime", date.getTime());
                mDatabase.getReference()
                        .child("Chats")
                        .child(SenderRoom)
                        .updateChildren(lastMessageObj);

                mDatabase.getReference()
                        .child("Chats")
                        .child(UserRoom)
                        .updateChildren(lastMessageObj);


                mDatabase.getReference()
                        .child("Chats")
                        .child(UserRoom)
                        .child("Messages")
                        .push()
                        .setValue(messageChat).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        mDatabase.getReference()
                                .child("Chats")
                                .child(SenderRoom)
                                .child("Messages")
                                .push()
                                .setValue(messageChat).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                                binding.messageEditText.setText("");

                            }
                        });

                    }
                });

            }
        });


    }

}