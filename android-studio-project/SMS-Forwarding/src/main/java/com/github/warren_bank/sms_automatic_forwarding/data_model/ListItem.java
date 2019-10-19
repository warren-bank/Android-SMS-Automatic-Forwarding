package com.github.warren_bank.sms_automatic_forwarding.data_model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public final class ListItem {
    public String recipient;
    public String sender;

    public ListItem(String recipient, String sender) {
        this.recipient = recipient;
        this.sender    = sender;
    }

    @Override
    public String toString() {
        return recipient;
    }

    // helpers

    public static ArrayList<ListItem> fromJson(String json) {
        ArrayList<ListItem> arrayList;
        Gson gson = new Gson();
        arrayList = gson.fromJson(json, new TypeToken<ArrayList<ListItem>>(){}.getType());
        return arrayList;
    }

    public static String toJson(ArrayList<ListItem> arrayList) {
        String json = new Gson().toJson(arrayList);
        return json;
    }

    public static ArrayList<String> get_matching_recipients(ArrayList<ListItem> arrayList, String sender) {
        ArrayList<String> recipients = new ArrayList<String>();
        ListItem item;

        for (int i=0; i < arrayList.size(); i++) {
            try {
                item = arrayList.get(i);

                // required
                if (!sender.endsWith(item.sender) && !item.sender.equals("*"))
                    continue;

                recipients.add(item.recipient);
            }
            catch(Exception e) { continue; }
        }
        return recipients;
    }
}
