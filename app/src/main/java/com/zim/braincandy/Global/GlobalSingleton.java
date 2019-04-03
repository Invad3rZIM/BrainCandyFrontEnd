package com.zim.braincandy.Global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.Window;

import com.zim.braincandy.Adapters.SectionsPagerAdapter;
import com.zim.braincandy.Fragments.ChatFragment;
import com.zim.braincandy.Fragments.GossipFragment;
import com.zim.braincandy.Fragments.InboxFragment;
import com.zim.braincandy.JSON.JsonRequestTool;
import com.zim.braincandy.Objects.Conversation;
import com.zim.braincandy.Objects.Message;
import com.zim.braincandy.Objects.Secret;
import com.zim.braincandy.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

//singleton class for all user data
public class GlobalSingleton extends Application {

    public static GlobalSingleton gs;
    public static GlobalKeyboard kb;

    public GlobalSingleton() {
        if (gs != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        init();
    }

    public static Activity activity;

    public static int lastSecretID = 0;

    public static GlobalSingleton getInstance() {
        if (gs == null) {
            gs = new GlobalSingleton();
        }

        return gs;
    }

    public static Context context;
    public static Window window;

    public final String API_URL = "https://bottled.appspot.com";

    public SharedPreferences settings;
    public ViewPager viewPager;

    public static SectionsPagerAdapter pagerAdapter;
    public static JsonRequestTool requestTool;

    //stores user specific data
    public static int myID;
    public static int secretID;

    public static HashMap<Integer, Conversation> conversations;
    public static HashMap<Integer, Secret> secrets;
    public static HashMap<Integer, Integer> secretSenders;
    public static HashMap<Integer, Secret> mySecrets;

    public static HashSet<Integer> blacklist;

    public static  GossipFragment gf;

    public int currentSecret;

    public void setViewPager(ViewPager v)
    {
        viewPager = v;
    }

    public static InboxFragment inbox;
    public static ChatFragment chat;

    public ViewPager getViewPager()
    {
        return viewPager;
    }

    public static boolean isMySecret(int i ) {
        return mySecrets.containsKey(i);
    }

    //retrieves all the conversations that are to be had...
    public ArrayList<Conversation> getConversations() {
        Collection<Conversation> values = conversations.values();

       return new ArrayList<Conversation>(values);
    }

    public static  Conversation getConversation(int ss) {

        if (conversations.containsKey(ss)) {
            Conversation c = conversations.get(ss);

            if (c != null)
                return c;
        }

        return new Conversation(0, 0, 0, new ArrayList<Message>());
    }

    public static void updateInbox() {
        if (inbox != null) {
            inbox.updateConversations();
        }
    }


    public static void updateChat() {
        if (chat != null) {
            chat.updateMessages();
        }
    }


    public static void processIncomingMessage(Message m) {
        int key = m.secretID;

        if (hasConversation(key)) {
            Conversation c = conversations.get(key);

            Message lastMessage = c.getLastMessage();
            c.addMessage(m);
            updateChat();
        } else {

            if(mySecrets.containsKey(key)) { //in the event they're responding to me...
                Secret s = mySecrets.get(key);
                Message m2 = new Message(myId,m.senderID, key, 0, s.getMessage(), s.getTime(), true);
                Message m4 = new Message(m.senderID, myId , key, m.messageIndex, m.message, m.timeStamp, false);

                ArrayList<Message> messages3 = new ArrayList<Message>();
                messages3.add(m2);
                messages3.add(m4);

                Conversation c = new Conversation(myId, m.senderID, key, messages3);
                c.imageID=mapImage();
                addConversation(c);

                updateInbox();
            }
        }
    }

    public static int myId;

    public static void setPager(ViewPager p) {
        pager = p;
    }

    public static ViewPager pager;

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

    //used to iterate through images
    public static int count = 0;

    public static int mapImage() {
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

    public static void setPagerAdapter(SectionsPagerAdapter pagerAdapter) {
        GlobalSingleton.pagerAdapter =  pagerAdapter;
    }

    public static void addBlacklist(int i) {
        blacklist.add(i);
    }

    public static JSONArray blacklist() {
        JSONArray jsArray = new JSONArray();

        for(Integer i : blacklist)
            jsArray.put(i);

        return jsArray;
    }

    public void init() {
            if (settings == null) {

                conversations = new HashMap<Integer, Conversation>();
                secrets = new HashMap<Integer, Secret>();
                mySecrets = new HashMap<Integer, Secret>();

                secretSenders = new HashMap<Integer,Integer>();
                blacklist = new HashSet<Integer>();

                kb = new GlobalKeyboard();

                genSecrets();

            }
    }

    public static void addPersonalSecret(Secret s) {
        mySecrets.put(s.getSecretID(), s);
    }

    public void genSecrets() {

    }

    public static View mainView;

    public static void addSecret(Secret s) {
            secrets.put(s.getSecretID(), s);
            secretSenders.put(s.getSecretID(), s.getSenderID());
    }

    public static void sendMessageOut(Message m) {
        requestTool.sendMessage(m);
    }

    public static boolean blacklisted(int secretID) {
        return blacklist.contains((Integer) secretID);
    }

    public static boolean hasConversation(int i) {
        return conversations.containsKey(i);
    }

    public static void addConversation(Conversation c) {
        conversations.put(c.getSecretID(), c);
    }

    //returns all the secrets in the cache.
    //come back to for blacklist later.
    public static ArrayList<Secret> getSecrets(int max) {
        ArrayList<Secret> secrets2 = new ArrayList<Secret>();

        int index = 0;

            for(Integer key: secrets.keySet()) {

                if (!blacklisted(key)) {

                    index++;
                    //remove the match from the cache
                    secrets2.add(secrets.get(key));
                }
                if (index >= max) {
                    break;
                }
            }


        for(Integer key: mySecrets.keySet()) {
            //remove the match from the cache
            secrets2.add(mySecrets.get(key));
        }

        return secrets2;
    }


    public static int getSenderFromSecret(int secret) {

        if (secretSenders.containsKey(secret) ) {
            return secretSenders.get(secret);
        }

        return 0;
    }
    public static void setRequestTool(JsonRequestTool r)
    {
        requestTool = r;
    }
}
