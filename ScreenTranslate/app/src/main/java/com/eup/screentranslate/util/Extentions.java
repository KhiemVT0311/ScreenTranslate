package com.eup.screentranslate.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.eup.screentranslate.model.Word;

import java.util.ArrayList;

public class Extentions {

    public static float convertDpToPixel(Context context, float dp) {
        if (context != null) {
            return dp * ((float) (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        } else {
            return dp;
        }
    }

    public static void hideKeyboard(Context context,EditText editText){
    try {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    } catch (Exception e){
        e.printStackTrace();
    }
    }

    public static void showKeybroad(Context context){
        ( (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
