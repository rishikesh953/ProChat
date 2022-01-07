package com.google.prochat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.prochat.Activities.ChatActivity;
import com.google.prochat.R;
import com.google.prochat.Models.User;
import com.google.prochat.databinding.UsersListBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private final static String TAG = "UsersAdapter";
    private ArrayList<User> users;
    Context context;

    public UsersAdapter(Context context, ArrayList<User> users)
    {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = users.get(position);
        String userId = FirebaseAuth.getInstance().getUid();
        String senderId = userId + user.getUid();

        FirebaseDatabase.getInstance("https://prochat-3cbfe-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference()
                .child("Chats")
                .child(senderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            Log.e("UsersAdapter", lastMsg);
                            Long lastMsgTime = snapshot.child("lastMsgTime").getValue(Long.class);
                            holder.binding.tapToChat.setText(lastMsg);
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
                            holder.binding.timeTextView.setText(time.format(new Date(lastMsgTime)));
                        }
                        else
                        {
                            holder.binding.timeTextView.setText("00:00");
                            holder.binding.tapToChat.setText("Tap to Chat");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.usernameTextView.setText(user.getUsername());
        Glide
                .with(context)
                .load(user.getProfileImage())
                .placeholder(R.drawable.profileavatar)
                .into(holder.binding.avatarImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("UserName",user.getUsername());
                i.putExtra("UserId", user.getUid());
                context.startActivity(i);
            }
        });
        int colorCode = getRandomColor();
        holder.binding.usersParentLayout.setBackgroundColor(holder.itemView.getResources().getColor(colorCode));

    }

    private int getRandomColor() {
        List<Integer> colors = new ArrayList<>();
        colors.add(R.color.color1);
        colors.add(R.color.color2);
        colors.add(R.color.color3);
        colors.add(R.color.color4);
        colors.add(R.color.color5);
        colors.add(R.color.color6);
        colors.add(R.color.color7);
        colors.add(R.color.color8);
        colors.add(R.color.color9);
        colors.add(R.color.color10);
        colors.add(R.color.color11);
        colors.add(R.color.color12);

        Random random = new Random();
        int number = random.nextInt(colors.size());
        return colors.get(number);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        UsersListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UsersListBinding.bind(itemView);

        }
    }

}
