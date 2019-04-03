package com.zim.braincandy.Global;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class GlobalKeyboard {
    public static GlobalKeyboard ks;

    public GlobalKeyboard() {

        ks = this;
    }

    public static void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) GlobalSingleton.activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = GlobalSingleton.activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(GlobalSingleton.activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void squeezeKeyboard() {

    }

    public static void softPan() {
        GlobalSingleton.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    public static void softResize() {
        GlobalSingleton.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }
    //focuses on the editText box
    public static void nosqueezeKeyboard(EditText e) {
        e.requestFocus();
        softResize();
        InputMethodManager imm = (InputMethodManager)GlobalSingleton.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(e, InputMethodManager.SHOW_FORCED);

    }
}
