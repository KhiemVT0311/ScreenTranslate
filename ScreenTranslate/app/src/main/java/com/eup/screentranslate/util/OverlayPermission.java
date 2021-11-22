package com.eup.screentranslate.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class OverlayPermission {

    /**
     * Does this app have permission to display Views as an overlay above all other apps?
     *
     * @param context context
     * @return true if overlay drawing is permitted, false otherwise
     */
    public static boolean hasRuntimePermissionToDrawOverlay(@NonNull Context context) {
        //noinspection SimplifiableIfStatement
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Runtime permissions are required. Check for the draw overlay permission.
            return Settings.canDrawOverlays(context);
        } else {
            // No runtime permissions required. We're all good.
            return true;
        }
    }

    /**
     * Starting with Android M, a runtime permission is required to be able to display UI elements
     * as an overlay above all other apps.  This method creates and returns an Intent that prompts
     * the user for this permission.
     *
     * @param context context
     * @return Intent to launch permission prompt
     */
    @RequiresApi(Build.VERSION_CODES.M)
    @NonNull
    public static Intent createIntentToRequestOverlayPermission(@NonNull Context context) {
        return new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName())
        );
    }

    private OverlayPermission() {
        // Utility class.
    }
}
