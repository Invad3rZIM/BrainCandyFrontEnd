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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rk.mbtio.Conversation;
import com.rk.mbtio.GlobalSingleton;
import com.rk.mbtio.ChatRecyclerViewAdapter;
import com.rk.mbtio.GlobalStuff;
import com.rk.mbtio.Message;
import com.rk.mbtio.R;
import com.rk.mbtio.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Random;

public class ChatFragment extends Fragment {

    public ChatFragment() {
        // required empty
    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (gg == null) {
            gg = ((GlobalSingleton) this.getActivity().getApplication());
            gg.chat = this;
        }

        conversation = gg.getConversation(gg.secretID);
        messages = new ArrayList<Message>();
    }

    public ViewPager viewPager;
    public ArrayList<Message> messages;

    private ChatRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView usersButton;
    private SectionsPagerAdapter pagerAdapter;
    public Conversation conversation;
    public int secretID;

    public Button send;
    public EditText toSend;


    private RecyclerView recyclerView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (gg == null) {
            gg = ((GlobalSingleton) this.getActivity().getApplication());
        }

        View view = inflater.inflate(R.layout.fragment_view_chats, container, false);

        send = view.findViewById(R.id.send);
        toSend = view.findViewById(R.id.message);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        EditText message = (EditText) view.findViewById(R.id.message);
        Log.d("immediate after secret " , "   " + gg.secretID);

        if(GlobalStuff.lastSecretID != 0) {
            conversation = gg.getConversation(GlobalStuff.lastSecretID);
            messages = conversation.getMessages();
        }

        // specify an adapter
        mAdapter = new ChatRecyclerViewAdapter(gg, messages);
        ((ChatRecyclerViewAdapter) mAdapter).setViewPager(viewPager);

        recyclerView.setAdapter(mAdapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code in here
                if (toSend.getText().length() > 0 ){
                    String msg = toSend.getText().toString();
                    toSend.setText(""); //clear out the text

                    int theirID = conversation.getReceiverID();

                    if (theirID == GlobalStuff.myId) {
                        theirID = conversation.getSenderID();
                    }

                    Log.d("yyyy   ", conversation.getSecretID() + "   " + conversation.getReceiverID() + "  " + conversation.getSenderID());

                    conversation = gg.getConversation(GlobalStuff.lastSecretID);

                    Message m = new Message(GlobalStuff.myId, theirID, conversation.getSecretID(), mAdapter.getMessageCount() + 1, msg, "timestamp filler", true);
                    gg.sendMessageOut(m);

                    messages.add(m);

                    mAdapter.update();
                }
            }
        });

        mAdapter.update();


        return view;
    }

    public void updateMessages() {
        if(mAdapter != null)
            mAdapter.update();
    }

    public GlobalSingleton gg;
}

