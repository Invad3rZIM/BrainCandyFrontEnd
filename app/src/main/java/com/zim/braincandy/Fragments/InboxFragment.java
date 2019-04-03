package com.zim.braincandy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.zim.braincandy.Activities.DriverActivity;
import com.zim.braincandy.Objects.Conversation;
import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.Adapters.InboxRecyclerViewAdapter;
import com.zim.braincandy.R;
import com.zim.braincandy.Adapters.SectionsPagerAdapter;

import java.util.ArrayList;

public class InboxFragment extends Fragment {

    public InboxFragment() {
        // required empty
    }

    public static InboxFragment newInstance() {
        InboxFragment fragment = new InboxFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalSingleton.inbox = this;

        conversations = new ArrayList<Conversation>();

        if (gg != null) {
            conversations = gg.getConversations();
            gg.inbox = this;
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {


            //on enter
            if (isVisibleToUser) {
                //only execute once
                    GlobalSingleton.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    InputMethodManager inputMethodManager = (InputMethodManager) GlobalSingleton.activity.getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (GlobalSingleton.pagerAdapter.fragments.size() >= 4) {
                        ((DriverActivity) GlobalSingleton.activity).removeFragment(3);
                        GlobalSingleton.pagerAdapter.fragments.remove(3);
                        GlobalSingleton.pagerAdapter.notifyDataSetChanged();

                    }


            } else { //on becoming invisible
            }
        }
    }

    public ViewPager viewPager;
    private InboxRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView usersButton;
    private SectionsPagerAdapter pagerAdapter;
    public ArrayList<Conversation> conversations;

    private RecyclerView recyclerView;

    public void updateConversations() {

        if (gg != null) {

            ArrayList<Conversation> conversations = gg.getConversations();

            // specify an adapter
            mAdapter = new InboxRecyclerViewAdapter(gg, conversations);
            ((InboxRecyclerViewAdapter) mAdapter).setViewPager(viewPager);

            recyclerView.setAdapter(mAdapter);
        }
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (gg == null) {
            gg = ((GlobalSingleton) this.getActivity().getApplication());
        }

        View view = inflater.inflate(R.layout.fragment_view_inbox, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        conversations = gg.getConversations();

        // specify an adapter
        mAdapter = new InboxRecyclerViewAdapter(gg, conversations);
        ((InboxRecyclerViewAdapter) mAdapter).setViewPager(viewPager);

        recyclerView.setAdapter(mAdapter);

        return view;
    }

    public GlobalSingleton gg;

    public void getConversations() {
        if (gg == null)
        gg = ((GlobalSingleton) this.getActivity().getApplication());
        int max = 10;
        conversations = gg.getConversations();
     //   ((GlobalSingleton) this.getActivity().getApplication()).requestTool.getSecrets((GlobalSingleton) this.getActivity().getApplication(), u.uid, u.pin, max);
    }
}

