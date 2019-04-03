package com.zim.braincandy.Adapters;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zim.braincandy.Objects.Conversation;
import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.Objects.Message;
import com.zim.braincandy.R;
import com.zim.braincandy.Objects.Secret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GossipRecyclerViewAdapter extends Adapter<GossipRecyclerViewAdapter.SecretViewHolder> {

    // provide a reference to the view for each data item
    // complex data item may need more than one view per item
    // you provide access to all the iews for a data item in a view holder
    private ArrayList<Secret> secrets;
    public ViewPager viewPager;
    public int myID = 5;

    public GlobalSingleton gs;

    // add secret at runtime
    public void addSecret(Secret s, GlobalSingleton x ) {
        gs = x;
        secrets.add(s);
        notifyDataSetChanged();
    }

    public ArrayList<Secret> removeSecret(int secretID) {
        for(Secret s : secrets) {
            if (s.getSecretID() == secretID) {
                secrets.remove(s);
                break;
            }
        }

        return secrets;
    }

    public static class SecretViewHolder extends RecyclerView.ViewHolder {
        // specify data items
        public View view;
        public String message;
        public int senderID;
        public CardView card;
        public int mySecret = 0;

        public SecretViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public ArrayList<Secret>refresh() {
        secrets = GlobalSingleton.getSecrets(10);

        Collections.shuffle(secrets);
        notifyDataSetChanged();

        return secrets;
    }

    // Specify appropriate constructor for dataset
    public GossipRecyclerViewAdapter(GlobalSingleton g, ArrayList<Secret> data) {
        secrets = data;
        gs = g;
    }

    // Create new views (invoked by the layout manager)
    public GossipRecyclerViewAdapter.SecretViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.secret, parent, false);

            SecretViewHolder vh = new SecretViewHolder(v);
            return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SecretViewHolder holder, int position) {
        // - get element from your dataset at this position
        // -replace the contents of the view with that element
         Secret secret = secrets.get(position);
         //Log.d("sss", "" + secret.getSenderID());
        // set text


        holder.message = secret.getMessage();
        holder.senderID = secret.getSenderID();

        TextView t = holder.view.findViewById(R.id.secret_label);
        TextView c = holder.view.findViewById(R.id.senderid);

        LinearLayout d = holder.view.findViewById(R.id.bj);
        TextView timestamp = holder.view.findViewById(R.id.timestamp);

        if(gs.isMySecret(secret.getSecretID())) {
            CardView cc = holder.view.findViewById(R.id.card2);
            int myColor = Color.parseColor("#6DD3CE");
            cc.setBackgroundColor(myColor);

            int myColor2= Color.parseColor("#773344");
            c.setBackgroundColor(myColor);
            t.setBackgroundColor(myColor);
            timestamp.setBackgroundColor(myColor);
            d.setBackgroundColor(myColor);
            timestamp.setTextColor(myColor2);
            t.setTextColor(myColor2);
            secret.createdbyme=1;
            holder.mySecret = 1;
        } else {
            CardView cc = holder.view.findViewById(R.id.card2);
            int myColor = Color.parseColor("#FF8A5B");
            int myColor2= Color.parseColor("#FFFFFF");
            cc.setBackgroundColor(myColor);
            c.setBackgroundColor(myColor);
            t.setBackgroundColor(myColor);
            timestamp.setBackgroundColor(myColor);
            d.setBackgroundColor(myColor);
            t.setTextColor(myColor2);
            timestamp.setTextColor(myColor2);
            holder.mySecret=0;;
        }

        c.setText(""+ secret.getSecretID());
        t.setText(secret.getMessage());
        timestamp.setText(secret.getTime());

        viewholders.put(holder.view, holder);

            holder.view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (viewholders.get(v).mySecret == 1) //no self conversations
                        return;

                    TextView t = v.findViewById(R.id.secret_label);
                    TextView c = v.findViewById(R.id.senderid);

                    int secretID = Integer.parseInt(c.getText().toString());
                    int senderID = GlobalSingleton.getSenderFromSecret(secretID);

                    Message mes = new Message(senderID, myID, secretID, 0, t.getText().toString(), "timestamp filler", false);
                    ArrayList<Message> messages = new ArrayList<Message>();
                    messages.add(mes);

                    Conversation convo = new Conversation(GlobalSingleton.myId, senderID, secretID, messages);
                    convo.imageID = GlobalSingleton.mapImage();
                    GlobalSingleton.addConversation(convo);
                    GlobalSingleton.updateInbox();
                    GlobalSingleton.addBlacklist(secretID);
                    GlobalSingleton.gf.removeSecret(secretID);

                    notifyDataSetChanged(); /* Important */
                }
            });
        }

    public static HashMap<View, SecretViewHolder> viewholders = new HashMap<View, SecretViewHolder>();

    // get size of dataset in recycler view
    @Override
    public int getItemCount() {
        return secrets.size();
    }

    // set view pager for conversations buttons to change to chat fragment
    public void setViewPager(ViewPager vp) {
        viewPager = vp;
    };
}
