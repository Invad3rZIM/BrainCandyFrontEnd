package com.zim.braincandy.Activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.zim.braincandy.Adapters.SectionsPagerAdapter;
import com.zim.braincandy.Fragments.ChatFragment;
import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.JSON.JsonRequestTool;
import com.zim.braincandy.R;
import com.zim.braincandy.Threading.MessageListenerThread;
import com.zim.braincandy.Threading.SecretListenerThread;

public class DriverActivity extends AppCompatActivity {

    // trust SSL Request for Google cloud server
    protected static final String TAG = "NukeSSLCerts";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public SecretListenerThread slt;
    public MessageListenerThread mlt;


    // For calls to API
    private JsonRequestTool requestTool;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void removeFragment(int index) {
        if ( index >= GlobalSingleton.pagerAdapter.fragments.size() )
            return;

        Fragment f = GlobalSingleton.pagerAdapter.fragments.get(index);

        if(f != null) {
            getSupportFragmentManager().beginTransaction().remove(f).commit();

        }

    }

    public void addChatFragment(ChatFragment cf ) {
            getSupportFragmentManager().beginTransaction().add(cf, "chat").commit();
    }



    public void init() {
        GlobalSingleton.getInstance(); //instantiate the global singleton
        GlobalSingleton.context = getApplicationContext();

        // initailize sections pager adapter and view pager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.fragmentContainer);
        mViewPager.setOffscreenPageLimit(10);
        ((ViewPager) mViewPager).setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        // init JSONRequest tool
        requestTool = new JsonRequestTool(this.getApplicationContext(), GlobalSingleton.getInstance());

        GlobalSingleton.mainView = mViewPager.getRootView();
        GlobalSingleton.activity = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        GlobalSingleton.window = getWindow();

        GlobalSingleton.setRequestTool(requestTool);
        GlobalSingleton.setPagerAdapter(mSectionsPagerAdapter);

        GlobalSingleton.setPager(mViewPager);

        requestTool.newUser();

        slt = new SecretListenerThread();
        mlt = new MessageListenerThread();

        slt.start();
        mlt.start();

    }
}

