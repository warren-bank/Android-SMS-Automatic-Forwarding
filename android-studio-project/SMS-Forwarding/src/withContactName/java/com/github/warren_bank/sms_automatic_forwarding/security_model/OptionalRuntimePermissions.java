package com.github.warren_bank.sms_automatic_forwarding.security_model;

import android.app.Activity;
import android.os.Build;

public final class OptionalRuntimePermissions {
  private static final int REQUEST_CODE = 1;

  public static void requestPermissions(Activity activity) {
    if (Build.VERSION.SDK_INT < 23)
      return;

    final String[] permissions_all = new String[]{ "android.permission.READ_CONTACTS" };
    final String[] permissions_req = RuntimePermissions.getMissingPermissions(activity, permissions_all);

    if (permissions_req == null)
      return;

    activity.requestPermissions(permissions_req, REQUEST_CODE);
  }
}
