package com.eup.screentranslate.util.constants;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.eup.screentranslate.ui.screen.main.MainActivity;

public class Constants {
    public static String PREFERENCE_SCREEN_TRANS = "Preference_screen_trans";
    public static final String BASE_URL_SPEAK_TEXT = "https://readspeaker.jp/";
    public static final String KEY = "AIzaSyAqIz69-o1_f8T5sHQ99scVbPheEuvCmZc";
    public static final String OPEN_SETTINGS_APP = "OPEN_SETTINGS_APP";

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;

    public static class REMOTE_KEY {
        public static String CONFIG_ANDROID = "config_android";
    }

    public static int getStatusHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0){
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationHeight(Context context){
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) return 0;
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0){
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            ((MainActivity) context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    public static int getWidthScreen(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point p = new Point();
        display.getRealSize(p);
        return p.x;
    }

    public static int getHeightScreen(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point p = new Point();
        display.getRealSize(p);
        return p.y;
    }
}
