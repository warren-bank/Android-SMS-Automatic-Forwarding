package com.github.warren_bank.sms_automatic_forwarding.data_model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public final class Contacts {
  public static String getContactName(Context context, String phoneNumber) {
    String contactName = null;

    try {
      Uri uri = Uri.withAppendedPath(
        ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
        Uri.encode(phoneNumber)
      );
      String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
      Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

      if (cursor != null) {
        if(cursor.moveToFirst()) {
          contactName = cursor.getString(0);
        }
        cursor.close();
      }
    }
    catch(Exception e) {}

    return contactName;
  }
}
