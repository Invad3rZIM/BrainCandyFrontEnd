package com.rk.mbtio;

//every 5 seconds pings for new secrets that aren't blacklisted

import org.json.JSONException;

public class MessageListenerThread extends Thread {
    GlobalSingleton gs;

    public MessageListenerThread(GlobalSingleton gs) {
        super("message listener");
        this.gs = gs;
    }

    public void run() {
        while (true) {
            gs.requestTool.getNewMessages();

            try {
                Thread.sleep(4000);  //pause for 4 seconds before running again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}