package com.rk.mbtio.CreateUserFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.rk.mbtio.Conversation;
import com.rk.mbtio.DriverActivity;
import com.rk.mbtio.GlobalSingleton;
import com.rk.mbtio.GossipRecyclerViewAdapter;
import com.rk.mbtio.InboxRecyclerViewAdapter;
import com.rk.mbtio.R;
import com.rk.mbtio.Secret;
import com.rk.mbtio.SectionsPagerAdapter;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.getSystemService;

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

        if (gg == null) {
            gg = ((GlobalSingleton) this.getActivity().getApplication());
        }

        conversations = new ArrayList<Conversation>();

        if (gg != null) {
            conversations = gg.getConversations();
            gg.inbox = this;
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

