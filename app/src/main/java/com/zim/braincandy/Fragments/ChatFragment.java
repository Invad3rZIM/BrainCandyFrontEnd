package com.zim.braincandy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zim.braincandy.Activities.DriverActivity;
import com.zim.braincandy.Global.GlobalKeyboard;
import com.zim.braincandy.Objects.Conversation;
import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.Adapters.ChatRecyclerViewAdapter;
import com.zim.braincandy.Objects.Message;
import com.zim.braincandy.R;
import com.zim.braincandy.Adapters.SectionsPagerAdapter;

import java.util.ArrayList;

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

       GlobalSingleton.chat = this;

        conversation = GlobalSingleton.getConversation(GlobalSingleton.secretID);
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

                GlobalKeyboard.softResize();
                GlobalKeyboard.nosqueezeKeyboard(toSend);

                }


            } else { //on becoming invisible
            }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_view_chats, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        toSend = (EditText) view.findViewById(R.id.message);

        toSend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                              @Override
                                              public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                  boolean handled = false;
                                                  if (actionId == EditorInfo.IME_ACTION_SEND) {
                                                      handled = true;

                                                      // Add your code in here
                                                      if (toSend.getText().length() > 0 ) {
                                                          String msg = toSend.getText().toString();
                                                          toSend.setText(""); //clear out the text

                                                          int theirID = conversation.getReceiverID();

                                                          if (theirID == GlobalSingleton.myId) {
                                                              theirID = conversation.getSenderID();
                                                          }

                                                          conversation = GlobalSingleton.getConversation(GlobalSingleton.lastSecretID);

                                                          Message m = new Message(GlobalSingleton.myId, theirID, conversation.getSecretID(), mAdapter.getMessageCount() + 1, msg, "timestamp filler", true);
                                                          GlobalSingleton.sendMessageOut(m);

                                                          conversation.addMessage(m);




                                                          GlobalSingleton.updateInbox();
                                                          mAdapter.update();
                                                      }

                                                  }
                                                  return handled;
                                              }
                                          });


        if(GlobalSingleton.lastSecretID != 0) {
            conversation = GlobalSingleton.getConversation(GlobalSingleton.lastSecretID);
            messages = conversation.getMessages();
        }

        // specify an adapter
        mAdapter = new ChatRecyclerViewAdapter(messages);
        ((ChatRecyclerViewAdapter) mAdapter).setViewPager(viewPager);

        recyclerView.setAdapter(mAdapter);

        mAdapter.update();
        return view;
    }

    public void updateMessages() {
        if(mAdapter != null)
            mAdapter.update();
    }
}

