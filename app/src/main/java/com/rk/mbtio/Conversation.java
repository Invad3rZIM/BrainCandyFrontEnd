package com.rk.mbtio;

import java.util.ArrayList;

public class Conversation {

   private ArrayList<Message> messages;
   private String preview;

   private int senderID;
   private int receiverID;
   private int secretID;
   public int imageID;

   //come back here in a moment
    public Conversation(int sid, int rid, int secretID, ArrayList<Message> m) {
        this.senderID = sid;
        this.receiverID = rid;
        this.secretID = secretID;

        messages = m;

        if (m.size() > 0) {
            String s = m.get(m.size() - 1).message;

            if (s.length() < 40)
                preview = s;
            else

            preview = s.substring(0, 39) + "...";
        }
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message m) {
        messages.add(m);

        if (m.message.length() < 40)
            preview = m.message;
        else

            preview = m.message.substring(0, 39) + "...";

    }

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public String getPreview() {
        return preview;
    }

    public int getSecretID() {
        return secretID;
    }
}
