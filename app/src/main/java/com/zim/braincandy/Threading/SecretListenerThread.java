package com.zim.braincandy.Threading;

//every 5 seconds pings for new secrets that aren't blacklisted

import com.zim.braincandy.Global.GlobalSingleton;

import org.json.JSONException;

public class SecretListenerThread extends Thread {

    public SecretListenerThread() {
    }

    public void run() {
        while(true) {
            try {
                try {
                    GlobalSingleton.requestTool.getNewSecrets(GlobalSingleton.blacklist(), GlobalSingleton.myID);
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