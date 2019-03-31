package com.rk.mbtio;

import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rk.mbtio.CreateUserFragments.GossipFragment;
import com.rk.mbtio.CreateUserFragments.InboxFragment;
import com.rk.mbtio.CreateUserFragments.VentFragment;
import com.rk.mbtio.CreateUserFragments.ChatFragment;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public ArrayList<Fragment> fragments;
    ChatFragment chat;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ArrayList<Fragment>();

        fragments.add(VentFragment.newInstance());
        fragments.add(GossipFragment.newInstance());

        chat = ChatFragment.newInstance();

        fragments.add(InboxFragment.newInstance());
        fragments.add(chat);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = fragments.get(position);
        return f;
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