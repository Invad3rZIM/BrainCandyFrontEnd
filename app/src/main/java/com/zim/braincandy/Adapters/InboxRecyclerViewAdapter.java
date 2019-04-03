package com.zim.braincandy.Adapters;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zim.braincandy.Activities.DriverActivity;
import com.zim.braincandy.Objects.Conversation;
import com.zim.braincandy.Fragments.ChatFragment;
import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.R;

import java.util.ArrayList;

public class InboxRecyclerViewAdapter extends Adapter<InboxRecyclerViewAdapter.CustomViewHolder> {

    // provide a reference to the view for each data item
    // complex data item may need more than one view per item
    // you provide access to all the iews for a data item in a view holder
    private ArrayList<Conversation> conversations;  //local datastore
    public ViewPager viewPager;
    public int myID = 5;

    public GlobalSingleton gs;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        // specify data items
        public View view;
        public String message;
        public int senderID;
        public int secretID;
        public String sig;
        public CardView card;

        public CustomViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Specify appropriate constructor for dataset
    public InboxRecyclerViewAdapter(GlobalSingleton g, ArrayList<Conversation> data) {
        conversations = data;
        gs = g;
    }

    // Create new views (invoked by the layout manager)
    public InboxRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation, parent, false);

        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        // - get element from your dataset at this position
        // -replace the contents of the view with that element
        Conversation conversation = conversations.get(position);

        holder.message = conversation.getPreview();
        holder.senderID = conversation.getSenderID();
        holder.secretID = conversation.getSecretID();

        TextView secret = holder.view.findViewById(R.id.secret);
        TextView prev = holder.view.findViewById(R.id.preview);

        ImageView img = holder.view.findViewById(R.id.signature);
        img.setImageResource(conversation.imageID);

        secret.setText(""+ conversation.getSecretID());
        prev.setText(conversation.getPreview().toString());

        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView t = v.findViewById(R.id.secret);

                int secretID = Integer.parseInt(t.getText().toString());
                gs.currentSecret = secretID;
                GlobalSingleton.lastSecretID = secretID;

                Log.d("current secret :   " , "   " + gs.currentSecret);

                ChatFragment chattyfam = ChatFragment.newInstance();

                ((DriverActivity) GlobalSingleton.activity).addChatFragment(chattyfam);
                if (gs.pagerAdapter.fragments.size() == 3)
                    gs.pagerAdapter.addFragment(gs.pagerAdapter.chat);
                else
                    gs.pagerAdapter.fragments.set(3, chattyfam);

                gs.pager.setAdapter(gs.pagerAdapter);

                gs.pager.setCurrentItem(3);
            }
        });
    }

    // get size of dataset in recycler view
    @Override
    public int getItemCount() {
        return conversations.size();
    }

    // set view pager for conversations buttons to change to chat fragment
    public void setViewPager(ViewPager vp) {
       viewPager = vp;

    };


}
