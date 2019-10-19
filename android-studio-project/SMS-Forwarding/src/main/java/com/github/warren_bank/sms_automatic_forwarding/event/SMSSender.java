package com.github.warren_bank.sms_automatic_forwarding.event;

import android.telephony.SmsManager;
import java.util.ArrayList;

public final class SMSSender {

    public static void forward(ArrayList<String> recipients, String sender, String body) {
        if (recipients.isEmpty())
            return;

        SmsManager sms = SmsManager.getDefault();
        String recipient;

        ArrayList<String> parts = sms.divideMessage("Forwarded from:\n  " + sender + "\n\n" + body);

        for (int i=0; i < recipients.size(); i++) {
            try {
                recipient = recipients.get(i);
                sms.sendMultipartTextMessage(recipient, null, parts, null, null);
            }
            catch(Exception e) { continue; }
        }
    }
}
