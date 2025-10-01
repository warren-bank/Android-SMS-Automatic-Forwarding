package com.github.warren_bank.sms_automatic_forwarding.data_model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
    ArrayList<String> allRecipients = new ArrayList<String>();
    RecipientListItem item;
    RecipientListItemCriteria criteria;

    for (int i=0; i < arrayList.size(); i++) {
      try {
        item = arrayList.get(i);
        criteria = new RecipientListItemCriteria(item);

        criteria.match(sender, allRecipients);
      }
      catch(Exception e) { continue; }
    }

    allRecipients = sanitizeMatches(sender, allRecipients);
    return allRecipients;
  }

  private static ArrayList<String> sanitizeMatches(String sender, ArrayList<String> allRecipients) {
    // remove duplicates from recipients list:
    HashSet<String> uniqueRecipients = new HashSet<String>(allRecipients);
    allRecipients = new ArrayList<String>(uniqueRecipients);

    // remove sender from recipients list:
    allRecipients.remove(sender);

    return allRecipients;
  }

  /*
   * ---------------------------------------------------------------------------
   * Parser that supports more advanced values in 'recipient' and 'sender' fields.
   * Fully backwards compatible with older single-value field values.
   *
   * 'recipient' field:
   *   - supports: comma-separated list of values
   *   - example:
   *       8001190000,8002290000,8003390000
   *
   * 'sender' field:
   *   - supports: <whitelist>!<blacklist>
   *   - where:
   *       * both <whitelist> and <blacklist> are a comma-separated list of values
   *       * <whitelist> is required
   *       * <blacklist> is optional
   *       * the character '!' denotes the start of <blacklist>
   *   - special case:
   *       * <whitelist> is '*'
   *         ..all other values in its comma-separated list are ignored
   *   - examples:
   *       8001190000,8002290000,8003390000
   *       *!8001190000,8002290000,8003390000
   *       0000!8001190000,8002290000,8003390000
   *       0000!90000
   * ---------------------------------------------------------------------------
   */

  private static class RecipientListItemCriteria {
    private String[] recipients;
    private String[] sender_whitelist;
    private String[] sender_blacklist;

    private static String[] splitCSV(String list) {
      return splitCSV(list, ',');
    }

    private static String[] splitCSV(String list, char delimiter) {
      return splitCSV(list, String.valueOf(delimiter));
    }

    private static String[] splitCSV(String list, String delimiter) {
      if (list == null) return null;

      String regex = "\\s*" + delimiter + "\\s*";
      return list.trim().split(regex);
    }

    public RecipientListItemCriteria(RecipientListItem item) {
      parseRecipient(item.recipient);
      parseSender(item.sender);
    }

    private void parseRecipient(String recipient) {
      this.recipients = splitCSV(recipient);
    }

    private void parseSender(String sender) {
      String[] parts = splitCSV(sender, "[!/#]");

      if (parts == null)
        return;
      if (parts.length > 0) {
        String whitelist = parts[0];
        if (!whitelist.isEmpty()) {
          ArrayList<String> wl_list = new ArrayList<String>();
          String[] wl_array = splitCSV(whitelist);
          for (String wl_value : wl_array) {
            if (wl_value.isEmpty())
              continue;
            if (wl_value.equals("*")) {
              wl_list.clear();
              wl_list.add(wl_value);
              break;
            }
            wl_value = wl_value.replace("*", "");
            wl_list.add(wl_value);
          }
          this.sender_whitelist = wl_list.toArray(new String[0]);
        }
      }
      if (parts.length > 1) {
        String blacklist = parts[1];
        if (!blacklist.isEmpty()) {
          this.sender_blacklist = splitCSV(blacklist);
        }
      }
    }

    // mutates 'allRecipients' list
    private void match(String sender, ArrayList<String> allRecipients) {
      if ((this.recipients == null) || (this.sender_whitelist == null)) return;

      // is sender in blacklist?
      if (this.sender_blacklist != null) {
        for (String bl_value : this.sender_blacklist) {
          if (sender.endsWith(bl_value)) {
            return;
          }
        }
      }

      // is sender in whitelist?
      for (String wl_value : this.sender_whitelist) {
        if (wl_value.equals("*") || sender.endsWith(wl_value)) {
          allRecipients.addAll(
            Arrays.asList(this.recipients)
          );
          return;
        }
      }
    }
  }

}
