package com.rk.mbtio;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.rk.mbtio.CreateUserFragments.ChatFragment;
import com.rk.mbtio.CreateUserFragments.GossipFragment;
import com.rk.mbtio.CreateUserFragments.InboxFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

//singleton class for all user data
public class GlobalSingleton extends Application {
    public final String API_URL = "https://bottled.appspot.com";

    public SharedPreferences settings;
    public ViewPager viewPager;

    public com.rk.mbtio.SectionsPagerAdapter pagerAdapter;
    public JsonRequestTool requestTool;
    public GlobalSingleton g;

    //stores user specific data
    public int myID;

    public int secretID;

    public HashMap<Integer, Conversation> conversations;
    public HashMap<Integer, Secret> secrets;
    public HashMap<Integer, Integer> secretSenders;
    public HashMap<Integer, Secret> mySecrets;

    public HashSet<Integer> blacklist;

    public GossipFragment gf;

    public int currentSecret;

    public void setViewPager(ViewPager v)
    {
        viewPager = v;
    }

    public InboxFragment inbox;
    public ChatFragment chat;

    public ViewPager getViewPager()
    {
        return viewPager;
    }

    public boolean isMySecret(int i ) {
        return mySecrets.containsKey(i);
    }

    //retrieves all the conversations that are to be had...
    public ArrayList<Conversation> getConversations() {
        Collection<Conversation> values = conversations.values();

       return new ArrayList<Conversation>(values);
    }

    public Conversation getConversation(int ss) {

        if (conversations.containsKey(ss)) {
            Conversation c = conversations.get(ss);

            if (c != null)
                return c;
        }

        return new Conversation(0, 0, 0, new ArrayList<Message>());
    }

    public void updateInbox() {
        if (inbox != null) {
            inbox.updateConversations();
        }
    }


    public void updateChat() {
        if (chat != null) {
            chat.updateMessages();
        }
    }


    public void processIncomingMessage(Message m) {
        int key = m.secretID;

        if (hasConversation(key)) {
            Conversation c = conversations.get(key);
            c.addMessage(m);
            updateChat();
        } else {

            if(mySecrets.containsKey(key)) { //in the event they're responding to me...
                Secret s = mySecrets.get(key);
                Message m2 = new Message(GlobalStuff.myId,m.senderID, key, 0, s.getMessage(), s.getTime(), true);
                Message m4 = new Message(m.senderID, GlobalStuff.myId , key, m.messageIndex, m.message, m.timeStamp, false);

                ArrayList<Message> messages3 = new ArrayList<Message>();
                messages3.add(m2);
                messages3.add(m4);

                Conversation c = new Conversation(GlobalStuff.myId, m.senderID, key, messages3);
                c.imageID=mapImage();
                addConversation(c);

                updateInbox();
            }
        }
    }


    public void setPager(ViewPager p) {
        this.pager = p;
    }

    public ViewPager pager;

    public ArrayList<Conversation> getConversations(int max) {
        ArrayList<Conversation> conversations = new ArrayList<Conversation>();

        int index = 0;

        for(Integer key: this.conversations.keySet()) {

            index++;

            //remove the match from the cache
            conversations.add(this.conversations.get(key));

            if (index >= max) {
                break;
            }
        }
        return conversations;
    }

    public int count = 0;

    public int mapImage() {
        Random gen = new Random();

        count++;

        if (count > 9)
            count = 0;

        switch(count) {
            case 0:return R.drawable.icon0;
            case 1: return R.drawable.icon1;
            case 2: return R.drawable.icon2;
            case 3:return R.drawable.icon3;
            case 4: return R.drawable.icons4;
            case 5: return R.drawable.icons5;
            case 6: return R.drawable.icons6;
            case 7: return R.drawable.icons7;
            case 8: return R.drawable.icons8;
        }

        return R.drawable.icons9;
    }

    public SectionsPagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    public void setPagerAdapter(SectionsPagerAdapter pagerAdapter) {
        this.pagerAdapter =  pagerAdapter;
    }

    public void addBlacklist(int i) {
        blacklist.add(i);
    }

    public JSONArray blacklist() {
        JSONArray jsArray = new JSONArray();

        for(Integer i : blacklist)
            jsArray.put(i);

        return jsArray;
    }

    public void init() {
            if (settings == null) {
                settings = getApplicationContext().getSharedPreferences("PREFERENCES", 0);

                conversations = new HashMap<Integer, Conversation>();
                secrets = new HashMap<Integer, Secret>();
                mySecrets = new HashMap<Integer, Secret>();

                secretSenders = new HashMap<Integer,Integer>();
                blacklist = new HashSet<Integer>();

                genSecrets();

                for(Integer i : secretSenders.keySet()) {
                    Log.d("" , "key "+i + "  val" + secretSenders.get(i));
                }
            }
    }

    public void addPersonalSecret(Secret s) {
        mySecrets.put(s.getSecretID(), s);
    }

    public void genSecrets() {

     //   Random gen = new Random();

  /*      for (int i = 0; i < 10; i++) {
            Secret s = new Secret("heyyyyyy" + gen.nextInt(22), "Today", gen.nextInt(), gen.nextInt());
            secrets.put(s.getSenderID(), s);
            secretSenders.put(s.getSecretID(), s.getSenderID());
        }


        for (int i = 0; i < 10; i++) {
            ArrayList<Message> m = new ArrayList<Message>();
            m.add(new Message(92, 91, 0, "something a little \n\nlonger to filter through", false));

            for(int o = 0; o < 4; o++ ) {
                if (o % 2 == 0) {

                    m.add(new Message(92, 91, 0, "I'm trying to get this done. well shiiitittt...", true));
                }else {
                    m.add(new Message(92, 91, 0, "something short", false));
                }
            }*/
            //Conversation c = new Conversation(gen.nextInt(22),gen.nextInt(), gen.nextInt(), m);
            //c.imageID = mapImage();
            //conversations.put(c.secretID, c);
      //  }

    }

    public void addSecret(Secret s) {
            secrets.put(s.getSecretID(), s);
            secretSenders.put(s.getSecretID(), s.getSenderID());
    }

    public void sendMessageOut(Message m) {
        requestTool.sendMessage(m);
    }

    public boolean blacklisted(int secretID) {
        return blacklist.contains((Integer) secretID);
    }

    public boolean hasConversation(int i) {
        return conversations.containsKey(i);
    }

    public void addConversation(Conversation c) {
        conversations.put(c.getSecretID(), c);
    }

    //returns all the secrets in the cache.
    //come back to for blacklist later.
    public ArrayList<Secret> getSecrets(int max) {
        ArrayList<Secret> secrets = new ArrayList<Secret>();

        int index = 0;

            for(Integer key: this.secrets.keySet()) {

                if (!blacklisted(key)) {

                    index++;
                    //remove the match from the cache
                    secrets.add(this.secrets.get(key));
                }
                if (index >= max) {
                    break;
                }
            }


        for(Integer key: this.mySecrets.keySet()) {
            //remove the match from the cache
            secrets.add(this.mySecrets.get(key));
        }

        return secrets;
    }


    public int getSenderFromSecret(int secret) {

        if (secretSenders.containsKey(secret) ) {
            return secretSenders.get(secret);
        }

        return 0;
    }
    public void setRequestTool(JsonRequestTool r)
    {
        requestTool = r;
    }
}
