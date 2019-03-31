package com.rk.mbtio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rk.mbtio.CreateUserFragments.*;
import com.rk.mbtio.DriverFragments.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CreateUserActivity extends AppCompatActivity {

    // trust SSL Request for Google cloud server
    protected static final String TAG = "NukeSSLCerts";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public GlobalSingleton gs;

    public SecretListenerThread slt;
    public MessageListenerThread mlt;


    // For calls to API
    private JsonRequestTool requestTool;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // trust ssl
        //      nuke();

        init();


    }

    public void init() {
        // initailize sections pager adapter and view pager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.fragmentContainer);
        mViewPager.setOffscreenPageLimit(10);
        ((ViewPager) mViewPager).setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        (  (GlobalSingleton) getApplication()).init();
        // init JSONRequest tool
        requestTool = new JsonRequestTool(this.getApplicationContext(), (GlobalSingleton) getApplication());
        if (gs == null) {
           gs =  ((GlobalSingleton) getApplication());
        }

        (  (GlobalSingleton) getApplication()).setRequestTool(requestTool);
        ((GlobalSingleton) getApplication()).setPagerAdapter(mSectionsPagerAdapter);

        ((GlobalSingleton) getApplication()).setPager(mViewPager);

        requestTool.newUser();

        slt = new SecretListenerThread((GlobalSingleton) getApplication());
        mlt = new MessageListenerThread((GlobalSingleton) getApplication());

        slt.start();
        mlt.start();

    }
}

