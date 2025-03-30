package com.github.warren_bank.sms_automatic_forwarding.data_model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public final class RecipientListItem {
  public String recipient;
  public String sender;

  public RecipientListItem() {
    this.recipient = "";
    this.sender    = "";
  }

  public RecipientListItem(String recipient, String sender) {
    this.recipient = recipient;
    this.sender    = sender;
  }

  @Override
  public String toString() {
    return recipient;
  }

  // helpers

  public static ArrayList<RecipientListItem> fromJson(String json) {
    ArrayList<RecipientListItem> arrayList;
    Gson gson = new Gson();
    arrayList = gson.fromJson(json, new TypeToken<ArrayList<RecipientListItem>>(){}.getType());
    return arrayList;
  }

  public static String toJson(ArrayList<RecipientListItem> arrayList) {
    String json = new Gson().toJson(arrayList);
    return json;
  }

  public static ArrayList<String> match(ArrayList<RecipientListItem> arrayList, String sender) {
    ArrayList<String> recipients = new ArrayList<String>();
    RecipientListItem item;

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
