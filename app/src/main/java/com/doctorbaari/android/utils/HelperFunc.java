package com.doctorbaari.android.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Nowfel Mashnoor on 12/6/2017.
 */

public class HelperFunc {
    public static void showToast(Activity activity, String msg)
    {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
}
