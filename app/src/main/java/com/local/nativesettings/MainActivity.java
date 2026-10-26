package com.local.nativesettings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

/**
 * A tiny trampoline that surfaces the stock AOSP Settings app on an Android TV home screen.
 *
 * Many TV firmwares ship the AOSP Settings package (com.android.settings) without any
 * MAIN + LAUNCHER activity, so PackageManager queries never return it and no home screen
 * can list it. Declaring a launcher entry of our own and forwarding to the real Settings
 * activity puts it on the home screen as an ordinary app tile.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent forward = new Intent();
        forward.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
        forward.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(forward);
        } catch (Exception e) {
            // The component may be absent on some firmwares; fall back to the plain action.
            Intent fallback = new Intent(Settings.ACTION_SETTINGS);
            fallback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(fallback);
            } catch (Exception ignored) {
            }
        }
        finish();
    }
}
