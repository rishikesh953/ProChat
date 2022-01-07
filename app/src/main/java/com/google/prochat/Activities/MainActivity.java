package com.google.prochat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.prochat.Adapters.MainViewPagerAdapter;
import com.google.prochat.R;
import com.google.prochat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.userViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        binding.bottomNavigationView.setupWithViewPager(binding.userViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sign_out_menu, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), PhoneAuthentication.class));
                finish();
                return true;
            case R.id.delete_account:

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://prochat-3cbfe-default-rtdb.asia-southeast1.firebasedatabase.app/");
                database.getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("Status")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    startActivity(new Intent(getApplicationContext(), PhoneAuthentication.class));
                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(),
                                                           "" +task.getException(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                });


                            }
                        });
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}