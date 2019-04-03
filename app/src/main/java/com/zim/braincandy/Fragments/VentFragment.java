package com.zim.braincandy.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zim.braincandy.Global.GlobalKeyboard;
import com.zim.braincandy.Global.GlobalSingleton;
import com.zim.braincandy.R;

import org.json.JSONException;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class VentFragment extends Fragment {
    private EditText text;
    private Button sendButton;

    public VentFragment() {
        // required empty
    }

    public static VentFragment newInstance() {
        VentFragment fragment = new VentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // work with args
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // If we are becoming invisible, then...
        if (isVisibleToUser) {
            GlobalKeyboard.softPan();
            GlobalKeyboard.nosqueezeKeyboard(text);
        } else {
            GlobalKeyboard.hideKeyboard();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vent, container, false);

        text = v.findViewById(R.id.secret);
        sendButton = v.findViewById(R.id.sendchat);

        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code in here!
                Log.d("test", text.getText().toString());
                String te = text.getText().toString();

                if (te.length() > 3 && !text.getText().toString().equals("Posted")) {
                    try {
                        GlobalSingleton.requestTool.postSecret(te, GlobalSingleton.myID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    text.setText("");
                    text.setHint("Posted!");
                }
            }
        });
        return v;
    }
}
