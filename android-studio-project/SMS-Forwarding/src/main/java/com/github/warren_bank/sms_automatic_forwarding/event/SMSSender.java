package com.github.warren_bank.sms_automatic_forwarding.event;

import android.telephony.SmsManager;
import java.util.ArrayList;

public final class SMSSender {

  public static void forward(ArrayList<String> recipients, String sender, String sender_contact_name, String body) {
    if (recipients.isEmpty())
      return;

    SmsManager sms = SmsManager.getDefault();
    String recipient;

    String preface = "Forwarded from:" + "\n  " + sender;

    if ((sender_contact_name != null) && !sender_contact_name.isEmpty())
      preface += "\n  " + sender_contact_name;

    ArrayList<String> parts = sms.divideMessage(preface + "\n\n" + body);

    for (int i=0; i < recipients.size(); i++) {
      try {
        recipient = recipients.get(i);
        sms.sendMultipartTextMessage(recipient, null, parts, null, null);
      }
      catch(Exception e) { continue; }
    }
  }
}
