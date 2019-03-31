package com.rk.mbtio.CreateUserFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.rk.mbtio.GlobalSingleton;
import com.rk.mbtio.R;

import org.json.JSONException;

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
    public void onDetach() {
        super.onDetach();

        //hide keyboard when any fragment of this class has been detached
        showSoftwareKeyboard(false);
    }

    protected void showSoftwareKeyboard(boolean showKeyboard){
        final Activity activity = getActivity();
        final InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), showKeyboard ? InputMethodManager.SHOW_FORCED : InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vent, container, false);

        text = v.findViewById(R.id.secret);
        sendButton = v.findViewById(R.id.sendchat);

        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


        if (gg == null) {
            gg = ((GlobalSingleton) this.getActivity().getApplication());
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code in here!
                Log.d("test", text.getText().toString());
                String te = text.getText().toString();

                if (te.length() > 3 && !text.getText().toString().equals("Posted")) {
                    try {
                        gg.requestTool.postSecret(te, gg.myID);
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

    public GlobalSingleton gg;

}
