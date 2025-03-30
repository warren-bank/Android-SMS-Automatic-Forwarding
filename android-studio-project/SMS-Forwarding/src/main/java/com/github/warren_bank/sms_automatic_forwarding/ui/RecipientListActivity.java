package com.github.warren_bank.sms_automatic_forwarding.ui;

import com.github.warren_bank.sms_automatic_forwarding.R;
import com.github.warren_bank.sms_automatic_forwarding.data_model.Preferences;
import com.github.warren_bank.sms_automatic_forwarding.data_model.RecipientListItem;
import com.github.warren_bank.sms_automatic_forwarding.security_model.RuntimePermissions;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class RecipientListActivity extends Activity {
  private CheckBox inputEnable;
  private ListView listView;

  private ArrayList<RecipientListItem>    listItems;
  private ArrayAdapter<RecipientListItem> listAdapter;

  // ---------------------------------------------------------------------------------------------
  // Lifecycle Events:
  // ---------------------------------------------------------------------------------------------

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preference);

    inputEnable = (CheckBox) findViewById(R.id.input_enable);
    listView    = (ListView) findViewById(R.id.listview);

    listItems   = Preferences.getRecipientListItems(RecipientListActivity.this);
    listAdapter = new ArrayAdapter<RecipientListItem>(RecipientListActivity.this, android.R.layout.simple_list_item_1, listItems);
    listView.setAdapter(listAdapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showEditDialog(position);
        return;
      }
    });

    if (RuntimePermissions.isEnabled(RecipientListActivity.this)) {
      inputEnable.setChecked(Preferences.isEnabled(RecipientListActivity.this));
      inputEnable.setEnabled(true);
      inputEnable.setClickable(true);

      inputEnable.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Preferences.setEnabled(RecipientListActivity.this, inputEnable.isChecked());
        }
      });
    }
    else {
      inputEnable.setChecked(false);
      inputEnable.setEnabled(false);
      inputEnable.setClickable(false);

      if (Preferences.isEnabled(RecipientListActivity.this)) {
        Preferences.setEnabled(RecipientListActivity.this, false);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    RuntimePermissions.onRequestPermissionsResult(RecipientListActivity.this, requestCode, permissions, grantResults);
  }

  // ---------------------------------------------------------------------------------------------
  // ActionBar:
  // ---------------------------------------------------------------------------------------------

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getActionBar().setDisplayShowHomeEnabled(false);
    getMenuInflater().inflate(R.menu.activity_preference, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    switch(menuItem.getItemId()) {
      case R.id.menu_add: {
        showEditDialog(-1);
        return true;
      }
      default: {
        return super.onOptionsItemSelected(menuItem);
      }
    }
  }

  // ---------------------------------------------------------------------------------------------
  // Add/Edit Dialog:
  // ---------------------------------------------------------------------------------------------

  private void showEditDialog(final int position) {
    final boolean isAdd = (position < 0);

    final RecipientListItem listItem = (isAdd)
      ? new RecipientListItem()
      : listItems.get(position)
    ;

    final Dialog dialog = new Dialog(RecipientListActivity.this, R.style.app_theme);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_listitem);

    final EditText inputRecipient = (EditText) dialog.findViewById(R.id.input_recipient);
    final EditText inputSender    = (EditText) dialog.findViewById(R.id.input_sender);

    final Button buttonDelete = (Button) dialog.findViewById(R.id.button_delete);
    final Button buttonSave   = (Button) dialog.findViewById(R.id.button_save);

    inputRecipient.setText(listItem.recipient);
    inputSender.setText(listItem.sender);

    if (isAdd) {
      buttonDelete.setText(R.string.label_button_cancel);
    }

    buttonDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!isAdd) {
          listItems.remove(position);
          listAdapter.notifyDataSetChanged();
        }
        dialog.dismiss();
      }
    });

    buttonSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String new_recipient   = inputRecipient.getText().toString().trim();
        final String new_sender      = inputSender.getText().toString().trim();

        final boolean same_recipient = new_recipient.equals(listItem.recipient);
        final boolean same_sender    = new_sender.equals(listItem.sender);

        if (new_recipient.equals("") || new_sender.equals("")) {
          Toast.makeText(RecipientListActivity.this, getResources().getString(R.string.error_missing_required_value), Toast.LENGTH_SHORT).show();
          return;
        }

        if (same_recipient && same_sender) {
          // no change
          dialog.dismiss();
          return;
        }

        if (!same_recipient) {
          listItem.recipient = new_recipient;
        }
        if (!same_sender) {
          listItem.sender = new_sender;
        }

        if (isAdd) {
          if (!listItems.add(listItem)) {
            Toast.makeText(RecipientListActivity.this, getResources().getString(R.string.error_add_listitem), Toast.LENGTH_SHORT).show();
            return;
          }          
        }

        listAdapter.notifyDataSetChanged();
        dialog.dismiss();

        Preferences.setRecipientListItems(RecipientListActivity.this, listItems);
      }
    });

    dialog.show();
  }
}
