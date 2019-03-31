package com.rk.mbtio;

public class Secret {
    private String time;
    private String message;
    private int senderID;
    private int secretID;
    public int createdbyme = 0;

    public Secret(String message, String time, int senderID, int secretID) {
        this.time = time;
        this.message = message;
        this.senderID = senderID;
        this.secretID = secretID;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public int getSenderID() {
        return senderID;
    }

    public String toString() {
        String m = message +"\n\n" + time;

        return m;
    }

    public int getSecretID() {
        return this.secretID;
    }
}
