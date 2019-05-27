package com.example.spacup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.BaseAdapter;

public class PreferenceSetting extends PreferenceActivity implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.settings_preference);

                Preference pAppName = (Preference)findPreference("setting_activity_id");
                Preference pAppVersion = (Preference)findPreference("setting_activity_app_version");
                CheckBoxPreference cbpAutoAlarm = (CheckBoxPreference)findPreference("setting_activity_autoalarm");
                CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference)findPreference("setting_activity_alarm_reiceive");

                pAppName.setOnPreferenceClickListener(this);
                pAppVersion.setOnPreferenceClickListener(this);
                cbpAutoAlarm.setOnPreferenceClickListener(this);
                cbpAlarmReceive.setOnPreferenceClickListener(this);

        }

        @Override
        public boolean onPreferenceClick(Preference preference) {

                // 어플리케이션 이름
                if(preference.getKey().equals("setting_activity_id")) {

                }

                // 어플리케이션 버전
                else if(preference.getKey().equals("setting_activity_app_version")) {

                }

                // 자동알림
                else if(preference.getKey().equals("setting_activity_autoalarm")) {

                }

                // 알림 받기
                else if(preference.getKey().equals("setting_activity_alarm_reiceive")) {

                }

                return false;
        }
}
