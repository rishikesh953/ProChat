package com.google.prochat.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.prochat.Fragments.CallsFragment;
import com.google.prochat.Fragments.StatusFragment;
import com.google.prochat.Fragments.UsersFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private String Titles[] =  new String[] {"Chats", "Status", "Calls"};

    public MainViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:
                return new UsersFragment();
            case 1:
                return new StatusFragment();
            case 2:
                return new CallsFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}
