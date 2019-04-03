package com.zim.braincandy.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.zim.braincandy.Fragments.GossipFragment;
import com.zim.braincandy.Fragments.InboxFragment;
import com.zim.braincandy.Fragments.VentFragment;
import com.zim.braincandy.Fragments.ChatFragment;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    public ArrayList<Fragment> fragments;
    public ChatFragment chat;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);


        fragments = new ArrayList<Fragment>();

        fragments.add(VentFragment.newInstance());
        fragments.add(GossipFragment.newInstance());

        chat = ChatFragment.newInstance();

        fragments.add(InboxFragment.newInstance());
       // fragments.add(chat);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = fragments.get(position);

        return f;
    }

    public void killChat() {
        if (fragments.size() > 3) {

            notifyDataSetChanged();
            fragments.remove(3);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    public void newChat() {
        fragments.set(3, ChatFragment.newInstance());
    }

    public void addFragment(Fragment f) {
        fragments.add(f);
    }

    public void clearFragments() {
        fragments.clear();
    }
}