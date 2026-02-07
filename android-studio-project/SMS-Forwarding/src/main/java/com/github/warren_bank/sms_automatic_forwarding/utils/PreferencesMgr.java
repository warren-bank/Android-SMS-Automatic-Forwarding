package com.github.warren_bank.sms_automatic_forwarding.utils;

import com.github.warren_bank.sms_automatic_forwarding.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesMgr {

  // ---------------------------------------------------------------------------
  // internal:

  private static SharedPreferences getPrefs(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  // ---------------------------------------------------------------------------
  // internal generic getters:

  private static String getPrefString(Context context, int pref_key_id, String default_value) {
    SharedPreferences prefs = getPrefs(context);
    String pref_key = context.getString(pref_key_id);

    return prefs.getString(pref_key, default_value);
  }

  // ---------------------------------------------------------------------------
  // public getters:

  public static String get_template(Context context) {
    return getPrefString(
      context,
      /* pref_key_id   */ R.string.pref_template_key,
      /* default_value */ context.getString(R.string.pref_template_default_value)
    );
  }

}
