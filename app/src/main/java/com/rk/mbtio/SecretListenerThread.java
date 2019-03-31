package com.rk.mbtio;

//every 5 seconds pings for new secrets that aren't blacklisted

import android.util.Log;

import org.json.JSONException;

public class SecretListenerThread extends Thread {
    GlobalSingleton gs;

    public SecretListenerThread(GlobalSingleton gs) {
        super("secret listener");
        this.gs = gs;
    }

    public void run() {
        while(true) {
            try {
                try {
                    gs.requestTool.getNewSecrets(gs.blacklist(), gs.myID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Thread.sleep(4000);  //pause for 4 seconds before running again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}