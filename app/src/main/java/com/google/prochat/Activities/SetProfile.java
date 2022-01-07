package com.google.prochat.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.prochat.Models.DateOfBirth;
import com.google.prochat.Models.User;
import com.google.prochat.databinding.ActivitySetProfileBinding;

import java.text.DateFormat;
import java.util.Calendar;

public class SetProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivitySetProfileBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseReference;
    private StorageReference mReference;
    private Uri mSelectedImageUri;
    private ProgressDialog progressDialog;

    final private int RC_PHOTO_CODE = 45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://prochat-3cbfe-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance();

        binding.phoneEditText.setText(mAuth.getCurrentUser().getPhoneNumber());


        binding.profileImage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, RC_PHOTO_CODE);
        });

        binding.DOB.setOnClickListener(v -> {
            DateOfBirth mDatePickerDialogFragment = new DateOfBirth();
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
        });

        binding.doneButton.setOnClickListener(v -> {

            if(binding.nameEditText.getText().toString().isEmpty())
            {
                binding.nameEditText.setError("Invalid Username");
            }
            if(binding.DOB.getText().toString().isEmpty())
            {
                binding.DOB.setError("Invalid Date of Birth");
            }

            progressDialog.setMessage("Updating profile...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            if(mSelectedImageUri != null)
            {
                ImageProfile();
            }
            else
            {
                NoImageProfile();
            }

        });

    }

    private void ImageProfile() {

        mReference = mStorage.getReference().child("Profiles").child(mAuth.getUid());
        mReference.putFile(mSelectedImageUri).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                mReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUri = uri.toString();

                    User user = new User(mAuth.getUid(),
                            binding.nameEditText.getText().toString(),
                            mAuth.getCurrentUser().getPhoneNumber(), imageUri
                            , binding.DOB.getText().toString());

                    mDatabaseReference
                            .child("Users")
                            .child(mAuth.getUid())
                            .setValue(user)
                            .addOnSuccessListener(unused -> {

                                progressDialog.dismiss();
                                Intent i =
                                        new Intent(getApplicationContext(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();

                            });
                });
            }
        });

    }

    private void NoImageProfile() {

        User user = new User(mAuth.getUid(),
                binding.nameEditText.getText().toString(),
                mAuth.getCurrentUser().getPhoneNumber(), "No Image"
                , binding.DOB.getText().toString());

        mDatabaseReference
                .child("Users")
                .child(mAuth.getUid())
                .setValue(user)
                .addOnSuccessListener(unused -> {

                    progressDialog.dismiss();
                    Intent i =
                            new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null)
        {
            if(data.getData() != null)
            {
                binding.profileImage.setImageURI(data.getData());
                mSelectedImageUri = data.getData();
            }
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate =
                DateFormat.getDateInstance(DateFormat.DEFAULT).format(mCalendar.getTime());
        binding.DOB.setText(selectedDate);
    }
}