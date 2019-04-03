package com.zim.braincandy.Threading;

//every 5 seconds pings for new secrets that aren't blacklisted

import com.zim.braincandy.Global.GlobalSingleton;

public class MessageListenerThread extends Thread {
    public MessageListenerThread() {
        super("message listener");
    }

    public void run() {
        while (true) {
            GlobalSingleton.requestTool.getNewMessages();

            try {
                Thread.sleep(4000);  //pause for 4 seconds before running again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}