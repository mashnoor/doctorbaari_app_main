package com.doctorbaari.android.utils;

import android.app.Activity;

import com.orhanobut.hawk.Hawk;

/**
 * Created by Nowfel Mashnoor on 11/26/2017.
 */

public class DBHelper {
    public static void setUserId(Activity activity, String id)
    {
        Hawk.init(activity).build();
        Hawk.put("userid", id);
    }
    public static String getUserid(Activity activity)
    {
        Hawk.init(activity).build();
        return Hawk.get("userid");
    }
    public static void setSignedInStatus(Activity activity, boolean val)
    {
        Hawk.init(activity).build();
        Hawk.put("signed", val);
    }
    public static boolean isSignedIn(Activity activity)
    {
        Hawk.init(activity).build();
        return Hawk.get("signed", false);
    }
}
