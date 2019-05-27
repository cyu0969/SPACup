package com.example.spacup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
                Preference pAppHelp = (Preference)findPreference("setting_activity_Developer_email");
                CheckBoxPreference cbpAutoAlarm = (CheckBoxPreference)findPreference("setting_activity_autoalarm");
                CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference)findPreference("setting_activity_alarm_reiceive");

                pAppName.setOnPreferenceClickListener(this);
                pAppVersion.setOnPreferenceClickListener(this);
                pAppHelp.setOnPreferenceClickListener(this);
                cbpAutoAlarm.setOnPreferenceClickListener(this);
                cbpAlarmReceive.setOnPreferenceClickListener(this);

        }

        @Override
        public boolean onPreferenceClick(Preference preference) {

            Intent i; //시스템액티비티를 부를 Intent 참조변수
            Uri uri;  //시스템 액티비티의 세부 종류를 구분하는 Data는 Uri객체로 제공. 이를 위한 참조변수

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

                // 개발자에게 이메일 보내기
                else if(preference.getKey().equals("setting_activity_Developer_email")) {

                    uri= Uri.parse("mailto:abcd@naver.com"); //이메일과 관련된 Data는 'mailto:'으로 시작. 이후는 이메일 주소
                    i= new Intent(Intent.ACTION_SENDTO, uri); //시스템 액티비티인 Dial Activity의 action값
                    startActivity(i);//액티비티 실행

                }

                return false;
        }
}
