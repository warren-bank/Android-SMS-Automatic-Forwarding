package com.github.warren_bank.sms_automatic_forwarding.event;

import com.github.warren_bank.sms_automatic_forwarding.R;
import com.github.warren_bank.sms_automatic_forwarding.utils.PreferencesMgr;

import android.content.Context;
import android.telephony.SmsManager;
import java.util.ArrayList;

public final class SMSSender {

  public static void forward(Context context, ArrayList<String> recipients, String sender_phone_number, String sender_contact_name, String body) {
    SmsManager sms = SmsManager.getDefault();

    String sender = context.getString(R.string.sms_sender_heading);
    if ((sender_contact_name != null) && !sender_contact_name.isEmpty()) {
      sender += "\n  " + sender_contact_name;
    }
    sender += "\n  " + sender_phone_number;

    String template = PreferencesMgr.get_template(context);
    String message  = template.equals(context.getString(R.string.pref_template_value_prefix))
      ? (sender + "\n\n" + body)
      : (body   + "\n\n" + sender);

    ArrayList<String> parts = sms.divideMessage(message);

    // cleanup
    body    = null;
    sender  = null;
    message = null;

    String recipient;
    for (int i=0; i < recipients.size(); i++) {
      try {
        recipient = recipients.get(i);
        sms.sendMultipartTextMessage(recipient, null, parts, null, null);
      }
      catch(Exception e) { continue; }
    }
  }
}
