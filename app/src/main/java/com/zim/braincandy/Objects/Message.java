package com.zim.braincandy.Objects;

public class Message {

     public String message;
     public int senderID;
     public int receiverID;
     public int secretID;
     public int messageIndex;
     public String timeStamp;
     public boolean sent;


    public Message(int sid, int rid, int secretID,  int num, String text, String timestamp, boolean sent) {
        this.message = text;
        this.senderID = sid;
        this.receiverID = rid;
        this.messageIndex = num;
        this.secretID = secretID;

        this.timeStamp = timestamp;

        this.sent = sent;
    }

}
