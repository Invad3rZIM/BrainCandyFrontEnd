package com.zim.braincandy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.zim.braincandy.Global.GlobalKeyboard;
import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.Adapters.GossipRecyclerViewAdapter;
import com.zim.braincandy.R;
import com.zim.braincandy.Objects.Secret;
import com.zim.braincandy.Adapters.SectionsPagerAdapter;

import java.util.ArrayList;

public class GossipFragment extends Fragment {

    public GossipFragment() {
        // required empty
    }

    public static GossipFragment newInstance() {
        GossipFragment fragment = new GossipFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // work with args
        }
    }

    public ViewPager viewPager;
    private GossipRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView usersButton;
    private SectionsPagerAdapter pagerAdapter;
    private ArrayList<Secret> secrets;

    public SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;


    public void refresh() {
        secrets = GlobalSingleton.getSecrets(10);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        GlobalSingleton.gf = this;

        View view = inflater.inflate(R.layout.fragment_view_secrets, container, false);

        swipeContainer = (SwipeRefreshLayout)  view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSecrets();
            }
        });

        swipeContainer.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Secret> secrets = ((GlobalSingleton) getActivity().getApplication()).getSecrets(10);

        // specify an adapter
        mAdapter = new GossipRecyclerViewAdapter(gg, secrets);
        ((GossipRecyclerViewAdapter) mAdapter).setViewPager(viewPager);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    //called whenever its time to update secret list
    public void refreshSecrets() {
        secrets = mAdapter.refresh();
        swipeContainer.setRefreshing(false);

    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {
                GlobalKeyboard.hideKeyboard();
                GlobalKeyboard.softPan();
            } else {
            }
        }
    }

    //called when it's time to cleanup blacklisted secrets
    public void removeSecret(int secretID) {
        secrets = mAdapter.removeSecret(secretID);
    }

    public GlobalSingleton gg;

    public void getSecrets() {
        if (gg == null)
        gg = ((GlobalSingleton) this.getActivity().getApplication());
        gg.gf = this;
        gg.setViewPager(viewPager);
        gg.setPagerAdapter(pagerAdapter);
        int max = 10;
     //   ((GlobalSingleton) this.getActivity().getApplication()).requestTool.getSecrets((GlobalSingleton) this.getActivity().getApplication(), u.uid, u.pin, max);
    }
}

