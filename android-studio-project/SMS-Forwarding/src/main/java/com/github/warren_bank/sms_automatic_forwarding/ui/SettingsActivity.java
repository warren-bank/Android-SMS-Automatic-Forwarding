package com.github.warren_bank.sms_automatic_forwarding.ui;

import com.github.warren_bank.sms_automatic_forwarding.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class SettingsActivity extends PreferenceActivity {
  public static Intent getStartIntent(Context context) {
    return new Intent(context, SettingsActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
  }

  public static class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = super.onCreateView(inflater, container, savedInstanceState);

      // fix for Android 15+ edge-to-edge layout enforcement
      if (view != null)
        view.setFitsSystemWindows(true);

      return view;
    }
  }
}
