package com.rk.mbtio.CreateUserFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.rk.mbtio.DriverActivity;
import com.rk.mbtio.GlobalSingleton;
import com.rk.mbtio.GossipRecyclerViewAdapter;
import com.rk.mbtio.R;
import com.rk.mbtio.Secret;
import com.rk.mbtio.SectionsPagerAdapter;

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

    public Button refresh;
    private RecyclerView recyclerView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (gg == null) {
            gg = ((GlobalSingleton) this.getActivity().getApplication());
            gg.gf = this;
        }
        View view = inflater.inflate(R.layout.fragment_view_secrets, container, false);

        refresh = view.findViewById(R.id.refresh);
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

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSecrets();
            }
        });
        return view;
    }

    //called whenever its time to update secret list
    public void refreshSecrets() {
        secrets = ((GlobalSingleton) getActivity().getApplication()).getSecrets(10);

        mAdapter = new GossipRecyclerViewAdapter(gg, secrets);
        ((GossipRecyclerViewAdapter) mAdapter).setViewPager(viewPager);
        recyclerView.setAdapter(mAdapter);
    }

    //called when it's time to cleanup blacklisted secrets
    public void removeSecret(int secretID) {
        mAdapter.removeSecret(secretID);

        for(Secret s : secrets) {
            if (s.getSecretID() == secretID) {
                secrets.remove(s);
                break;
            }
        }
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

