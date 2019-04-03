package com.zim.braincandy.JSON;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.Objects.Message;
import com.zim.braincandy.Objects.Secret;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonRequestTool {

    private Context context;
    private JSONArray statusArr;

    // API URL
    private final String URL = "https://bottled.appspot.com";

    public JsonRequestTool(Context con, GlobalSingleton g) {
        context = con;
    }

    // test POST for send message
    public void newUser() {
        if (GlobalSingleton.myId == 0 ) {

        JSONObject data = new JSONObject();

        JSONRequestObj("/users/new", "POST", data, new JsonRequestTool.VolleyObjCallback() {
            @Override
            public void onSuccess(JSONObject results) {
                Log.d("HELLO HELLO HELLO    ", results.toString());

                try {
                    int id = results.getInt("Uid");
                    GlobalSingleton.myID = id;
                    GlobalSingleton.myId = id;

                    GlobalSingleton.addBlacklist(id); //this way you never receive your own chats

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        }
    }


    // test POST for send message
    public void postSecret(String secret, int myid) throws JSONException {

        JSONObject data = new JSONObject();

        data.put("secret", secret);
        data.put("sid", myid);


        JSONRequestObj("/secrets/new", "POST", data, new JsonRequestTool.VolleyObjCallback() {
            @Override
            public void onSuccess(JSONObject results) {
                try {
                    int secretID = results.getInt("SecretID");
                    int senderID = results.getInt("SenderID");
                    String body = results.getString("Body");
                    String timeStamp = results.getString("TimeStamp");

                    Secret s = new Secret(body, timeStamp,senderID, secretID );
                    s.createdbyme = 1;

                    GlobalSingleton.addPersonalSecret(s);
                    GlobalSingleton.gf.refreshSecrets();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // test POST for send message
    public void getNewSecrets(JSONArray js, int myid) throws JSONException {

        JSONObject data = new JSONObject();

        data.put("blacklist", js);
        data.put("sid", myid);

        JSONRequestObj("/secrets/get", "POST", data, new JsonRequestTool.VolleyObjCallback() {
            @Override
            public void onSuccess(JSONObject results) {
                try {
                    JSONArray cast = results.getJSONArray("Secrets");
                    for (int i = 0; i < cast.length(); i++) {
                        JSONObject item = cast.getJSONObject(i);

                        int secretID = item.getInt("SecretID");
                        int senderID = item.getInt("SenderID");
                        String body = item.getString("Body");
                        String timeStamp = item.getString("TimeStamp");

                        Secret s = new Secret(body, timeStamp,senderID, secretID );
                        s.createdbyme = 0;

                        GlobalSingleton.addSecret(s);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getNewMessages() {
        JSONObject data = new JSONObject();

        try {
            data.put("uid", GlobalSingleton.myId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONRequestObj("/messages/getnew", "POST", data, new JsonRequestTool.VolleyObjCallback() {
            @Override
            public void onSuccess(JSONObject results) {
                try {
                    JSONArray cast = results.getJSONArray("Messages");
                    for (int i = 0; i < cast.length(); i++) {
                        JSONObject item = cast.getJSONObject(i);

                        int secretID = item.getInt("SecretID");
                        int senderID = item.getInt("From");
                        int num = item.getInt("Num");
                        int receiverID = item.getInt("To");
                        String body = item.getString("Body");
                        String timeStamp = item.getString("TimeStamp");

                        Message m2 = new Message(senderID, receiverID, secretID, num, body, timeStamp, false);

                        GlobalSingleton.processIncomingMessage(m2);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // test POST for send message
    public void sendMessage(Message m) {

        JSONObject data = new JSONObject();

        Log.d("predata :   " , " DD     " + data.toString());
        try {
            data.put("sid", m.senderID);
            data.put("rid", m.receiverID);
            data.put("num", m.messageIndex);
            data.put("message", m.message);
            data.put("secretid", m.secretID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONRequestObj("/messages/send", "POST", data, new JsonRequestTool.VolleyObjCallback() {
            @Override
            public void onSuccess(JSONObject results) {

                Log.d("JSON", results.toString());
            }
        });
    }

    // test POST for get messages
    public void getMessages(int sid, int pin) {
        JSONObject data = new JSONObject();

        try {
            data.put("sid", sid);
            data.put("pin", pin);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONRequestObj("/messages/get", "POST", data, new JsonRequestTool.VolleyObjCallback() {
            @Override
            public void onSuccess(JSONObject results) {
                Log.d("JSON", results.toString());
            }
        });
    }

    // JSON request
    public void JSONRequestObj(String in_url, final String requestType, JSONObject data, final VolleyObjCallback callback) {

        String url = URL + in_url;
        int type = -1;


        if (requestType.equals("GET"))
            type = Request.Method.GET;
        else if (requestType.equals("POST"))
            type = Request.Method.POST;


        JsonObjectRequest jRequest = new JsonObjectRequest
                (type, url, data, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
               //         Log.i("JSON", response.toString());
                        if (callback != null)
                          callback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("JSON", error+ "\nJSON response error");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
            //    params.put("secret-key", KEY);

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        JSONSingleton.getInstance(context).addToRequestQueue(jRequest);
    }

    // JSON request
    public JSONArray JSONRequestArr(String in_url, final String requestType, JSONArray data, final VolleyArrCallback callBack) {
        String url = in_url;
        int type = -1;

        if (requestType.equals("GET"))
            type = Request.Method.GET;
        else if (requestType.equals("POST"))
            type = Request.Method.POST;


        JsonArrayRequest jRequest = new JsonArrayRequest
                (type, url, data, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i < response.length(); i++) {
                            try {
                                Log.i("JSON", response.get(i).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        statusArr = response;
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                //    params.put("secret-key", KEY);

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        JSONSingleton.getInstance(context).addToRequestQueue(jRequest);

        return statusArr;

    }

    public interface VolleyObjCallback {
        void onSuccess(JSONObject results);
    }

    public interface VolleyArrCallback {
        void onSuccess(JSONArray jArr);
    }
}
