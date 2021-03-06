package com.zim.braincandy.Adapters;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.Objects.Message;
import com.zim.braincandy.R;

import java.util.ArrayList;


public class ChatRecyclerViewAdapter extends Adapter<ChatRecyclerViewAdapter.CustomViewHolder> {

    // provide a reference to the view for each data item
    // complex data item may need more than one view per item
    // you provide access to all the iews for a data item in a view holder
    private ArrayList<Message> messages = new ArrayList<Message>();

    public ViewPager viewPager;
    public int myID = 5;

    // add secret at runtime
    public void update() {
        notifyDataSetChanged();
    }

    //gets the newest message in the chat
    public int getMessageCount() {
        if (messages != null)
            return messages.size();
        return 0;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public int sent = 0; //0 = not your message, 1 = ismessage

        public CustomViewHolder(View v, int type) {
            super(v);
            view = v;
            sent = type;
        }
    }

    // Specify appropriate constructor for dataset
    public ChatRecyclerViewAdapter(ArrayList<Message> data) {
        messages = data;

        if (messages == null)
            messages = new ArrayList<Message>();
    }



    // Create new views (invoked by the layout manager)
    public ChatRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);

        CustomViewHolder vh = new CustomViewHolder(v, 0);
        return vh;
    }

    public Message message;


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        // - get element from your dataset at this position
        // -replace the contents of the view with that element
        message = messages.get(position);

    RelativeLayout r = holder.view.findViewById(R.id.layout);
        TextView text = holder.view.findViewById(R.id.message);


        //a rid message
        if (!message.sent ) {
            CardView card = holder.view.findViewById(R.id.card);
            int myColor =  ContextCompat.getColor(GlobalSingleton.context, R.color.rchatbg);
            card.setBackgroundColor(myColor);


            (   (TextView) holder.view.findViewById(R.id.message)).setGravity(Gravity.LEFT);


            text.setBackgroundColor(myColor);

        } else { //a sid message

            CardView card = holder.view.findViewById(R.id.card);

            int myColor =  ContextCompat.getColor(GlobalSingleton.context, R.color.schatbg);
            card.setBackgroundColor(myColor);
            (   (TextView) holder.view.findViewById(R.id.message)).setGravity(Gravity.RIGHT);


    //        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)card.getLayoutParams();
      //      params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
       //     params.addRule(RelativeLayout.LEFT_OF, R.id.id_to_be_left_of);

        //card.setLayoutParams(params); //causes layout updat
            //  params.addRule(RelativeLayout.LEFT_OF, R.id.id_to_be_left_of);

          //  text.setLayoutParams(params); //causes layout update

            text.setBackgroundColor(myColor);
        }


        text.setText("" + message.message);

        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //empty method for tapping on chats
            }
        });
    }

    // get size of dataset in recycler view
    @Override
    public int getItemCount() {
        return messages.size();
    }

    // set view pager for conversations buttons to change to chat fragment
    public void setViewPager(ViewPager vp) {
       viewPager = vp;

    };


}
