package com.google.prochat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.prochat.Activities.MainActivity;
import com.google.prochat.Activities.OTPActivity;
import com.google.prochat.databinding.ActivityPhoneAuthenticationBinding;

import java.util.Objects;

public class PhoneAuthentication extends AppCompatActivity {

private ActivityPhoneAuthenticationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityPhoneAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }


        binding.codePicker.registerCarrierNumberEditText(binding.numberEditText);

       binding.nextButton.setOnClickListener(v -> {
           if(Objects.requireNonNull(binding.numberEditText.getText()).toString().isEmpty())
           {
              binding.numberEditText.setError("Enter your number.");
           }
           else if(binding.numberEditText.getText().toString().replace(" ", "").length() != 10)
           {
              binding.numberEditText.setError("Enter valid number.");
           }
           else
           {
               Intent i = new Intent(getApplicationContext(), OTPActivity.class);
               i.putExtra("phoneNumber", binding.codePicker.getFullNumberWithPlus());
              startActivity(i);
              finish();
           }
       });

    }
}