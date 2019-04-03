package com.zim.braincandy.Objects;

import com.zim.braincandy.Objects.Message;

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

        //initially set preview
        if (m.size() > 0)
            setPreview(m.get(m.size() - 1).message);
        else
            setPreview("");
    }

    //returns the last message of the conversation if the convo isn't empty. else null.
    public Message getLastMessage() {
        if( messages.size() < 1) {
            return null;
        }

        return messages.get(messages.size() - 1);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message m) {
        Message last = getLastMessage();

        if (last == null) {
            messages.add(m);
        } else {
            if (last.senderID == m.senderID) {
                last.message += "\n" + m.message;
            } else {
                messages.add(m);
            }
        }

        setPreview(m.message);
    }

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    //sets the message preivew. gets called as a subset of addmessage
    private void setPreview(String str) {
        if (str.length() < 40)
            preview = str;
        else
            preview = str.substring(0, 39) + "...";
    }

    //previews the message. needs refactoring.
    public String getPreview() {
            return preview;
    }

    public int getSecretID() {
        return secretID;
    }
}
