package com.google.prochat.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.prochat.Activities.SetProfile;
import com.google.prochat.databinding.ActivityOtpactivityBinding;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {


    ActivityOtpactivityBinding binding;

    private String phoneNumber;

    private String TAG = "OTPActivity";

    private FirebaseAuth mAuth;
    private String mVerificationId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phoneNumber");

        mAuth = FirebaseAuth.getInstance();

        binding.verifyTextView.setText("Verification of: "+ phoneNumber);


        sendVerificationCode();


        binding.Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.otpEditText.getText().toString().isEmpty())
                {
                    binding.otpEditText.setError("Enter OTP");
                }
                else if(binding.otpEditText.getText().toString().replace(" ", "").length() !=6)
                {
                    binding.otpEditText.setError("OTP should be six digit.");
                }
                else{

                    PhoneAuthCredential credential =
                            PhoneAuthProvider.getCredential(mVerificationId,
                                    binding.otpEditText.getText().toString().replace(" ", ""));

                    signInWithPhoneAuthCredential(credential);

                }
            }
        });


    }

    private void sendVerificationCode() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(getApplicationContext(),
                                        "Verification failed: "+e.getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String verifyId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifyId, forceResendingToken);

                                mVerificationId = verifyId;

                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent i = new Intent(getApplicationContext(), SetProfile.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                            finishAffinity(); //finishes all activities before this activity;

                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Verification failed: "+task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}