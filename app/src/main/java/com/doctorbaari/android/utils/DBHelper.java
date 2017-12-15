package com.doctorbaari.android.utils;

import android.app.Activity;

import com.doctorbaari.android.models.Advertise;
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
    public static void saveAdvertises(Activity activity, Advertise[] advertises)
    {
        Hawk.init(activity).build();
        Hawk.put("advertises", advertises);
    }
    public static Advertise[] getAdvertises(Activity activity)
    {
        Hawk.init(activity).build();
        return Hawk.get("advertises", new Advertise[]{});
    }
}
