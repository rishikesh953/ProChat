package com.google.prochat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.prochat.Models.User;
import com.google.prochat.Adapters.UsersAdapter;
import com.google.prochat.databinding.FragmentUsersBinding;

import java.util.ArrayList;

public class UsersFragment extends Fragment {


    FragmentUsersBinding binding;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://prochat-3cbfe-default-rtdb.asia-southeast1.firebasedatabase.app/");

        ArrayList<User> allusers = new ArrayList<>();

        UsersAdapter usersAdapter = new UsersAdapter(getContext(), allusers);
        binding.userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.userRecyclerView.setAdapter(usersAdapter);

        allusers.add(new User());

        mDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                allusers.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    User user = snapshot1.getValue(User.class);
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        allusers.add(user);
                    }
                }
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return binding.getRoot();
    }

}