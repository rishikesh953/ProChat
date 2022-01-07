package com.google.prochat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.prochat.R;
import com.google.prochat.databinding.FragmentCallsBinding;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class CallsFragment extends Fragment {

    FragmentCallsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCallsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        URL serverURL;

        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .build();

            JitsiMeet.setDefaultConferenceOptions(options);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        binding.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JitsiMeetConferenceOptions  options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(binding.secretCode.getText().toString())
                        .build();

                JitsiMeetActivity.launch(getContext(), options);
            }
        });

    }
}