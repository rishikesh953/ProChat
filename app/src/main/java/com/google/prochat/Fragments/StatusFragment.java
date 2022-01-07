package com.google.prochat.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.prochat.Adapters.StatusAdapter;
import com.google.prochat.Models.StatusClass;
import com.google.prochat.Models.User;
import com.google.prochat.Models.UserStatus;
import com.google.prochat.databinding.FragmentCallsBinding;
import com.google.prochat.databinding.FragmentStatusBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StatusFragment extends Fragment {

    FragmentStatusBinding binding;
    ArrayList<UserStatus> userStatuses;
    ArrayList<User> users;
    private final int RC_STATUS = 101;
    private FirebaseStorage mStorage;
    private StorageReference mReference;
    private FirebaseDatabase mDatabase;
    private ValueEventListener mDataReferance;
    private User user;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);

        userStatuses = new ArrayList<>();
        users = new ArrayList<>();
        binding.statusRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        StatusAdapter  statusAdapter = new StatusAdapter(getContext(), userStatuses);
        binding.statusRecyclerView.setAdapter(statusAdapter);
        progressDialog = new ProgressDialog(getContext());

        mStorage = FirebaseStorage.getInstance();
        mReference = mStorage.getReference();
        user = new User();

        mDatabase = FirebaseDatabase.getInstance("https://prochat-3cbfe-default-rtdb.asia-southeast1.firebasedatabase.app/");


        mDatabase.getReference().child("Status").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    userStatuses.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        UserStatus status = new UserStatus();
                        status.setUsername(snapshot1.child("userName").getValue(String.class));
                        status.setProfileImage(snapshot1.child("profileImage").getValue(String.class));
                        status.setLastUpdated(snapshot1.child("lastUpdated").getValue(Long.class));


                        ArrayList<StatusClass> statusClasses = new ArrayList<>();

                        for( DataSnapshot statusSnapshot: snapshot1.child("MyStatus").getChildren())
                        {
                            StatusClass newStatus = statusSnapshot.getValue(StatusClass.class);
                            statusClasses.add(newStatus);
                        }
                        status.setStatusClasses(statusClasses);
                        userStatuses.add(status);

                    }


                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        mDataReferance = mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        user = snapshot.getValue(User.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.addStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, RC_STATUS);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        if(data != null)
        {
            if(data.getData() != null)
            {
                Date d = new Date();

                final StorageReference ref = mReference.child("Status")
                        .child(d.getTime() + FirebaseAuth.getInstance().getCurrentUser().getUid());


                UploadTask upload = ref.putFile(data.getData());

                Task<Uri> task = upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            throw task.getException();

                        }

                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        String imageUri = task.getResult().toString();
                        UserStatus userStatus = new UserStatus();
                        userStatus.setUsername(user.getUsername());
                        userStatus.setProfileImage(user.getProfileImage());
                        userStatus.setLastUpdated(d.getTime());

                        HashMap<String, Object> obj = new HashMap<>();
                        obj.put("userName", userStatus.getUsername());
                        obj.put("profileImage", userStatus.getProfileImage());
                        obj.put("lastUpdated", userStatus.getLastUpdated());
                        mDatabase.getReference().child("Status")
                                .child(FirebaseAuth.getInstance().getUid())
                                .updateChildren(obj);

                        StatusClass statusClass = new StatusClass(imageUri,
                                userStatus.getLastUpdated());

                        mDatabase.getReference().child("Status")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child("MyStatus")
                                .push()
                        .setValue(statusClass);

                        Toast.makeText(getContext(), "Yeah! Done.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
        }

    }

}