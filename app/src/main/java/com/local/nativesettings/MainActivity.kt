package com.frogobox.githubaction

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle

/**
 * A tiny trampoline that surfaces the stock AOSP Settings app on an Android TV home screen.
 *
 * Many TV firmwares ship the AOSP Settings package (com.android.settings) without any
 * MAIN + LAUNCHER activity, so PackageManager queries never return it and no home screen
 * can list it. Declaring a launcher entry of our own and forwarding to the real Settings
 * activity puts it on the home screen as an ordinary app tile.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val forward = Intent().apply {
            component = ComponentName("com.android.settings", "com.android.settings.Settings")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(forward)
        } catch (e: Exception) {
            // The component may be absent on some firmwares; fall back to the plain action.
            val fallback = Intent(android.provider.Settings.ACTION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(fallback)
            } catch (ignored: Exception) {
            }
        }
        finish()
    }
}
