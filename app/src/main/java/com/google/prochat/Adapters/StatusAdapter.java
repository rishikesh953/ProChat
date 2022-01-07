package com.google.prochat.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.prochat.Activities.MainActivity;
import com.google.prochat.Models.StatusClass;
import com.google.prochat.Models.UserStatus;
import com.google.prochat.R;
import com.google.prochat.databinding.StatusListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter  extends RecyclerView.Adapter<StatusAdapter.ViewHolder>{

    Context context;
    ArrayList<UserStatus> statuses;

    public StatusAdapter(Context context, ArrayList<UserStatus> statuses)
    {
        this.context = context;
        this.statuses = statuses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.status_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserStatus userStatus = statuses.get(position);

        StatusClass lastStatus = userStatus.getStatusClasses().get(userStatus.getStatusClasses().size()-1);

        Glide.with(context)
                .load(lastStatus.getStatusImage())
                .into(holder.binding.circleImageView);

        holder.binding.statusTextView.setText(userStatus.getUsername());
        holder.binding.circularStatusView.setPortionsCount(userStatus.getStatusClasses().size());

        int colorCode = getRandomColor();
        holder.binding.statusParentLayout.setBackgroundColor(holder.itemView.getResources().getColor(colorCode));

        holder.binding.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for(StatusClass statusClass: userStatus.getStatusClasses())
                {
                    myStories.add(new MyStory(statusClass.getStatusImage()));
                }
                new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStatus.getUsername())
                        .setSubtitleText(null) // Default is Hidden
                        .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();

            }
        });

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
        return statuses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        StatusListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = StatusListBinding.bind(itemView);

        }
    }

}
