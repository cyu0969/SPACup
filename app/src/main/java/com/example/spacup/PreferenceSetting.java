package com.example.spacup;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.example.spacup.util.PrefUtil;
import com.example.spacup.util.ShowToast;


public class PreferenceSetting extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    public static final String TAG = "caveman";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);


        Preference pAppName = (Preference) findPreference("setting_activity_id");
        pAppName.setSummary("SPECup");

        Preference pAppVersion = (Preference) findPreference("setting_activity_app_version");
        try {
            pAppVersion.setSummary(appVersion());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Preference pAppLock = (Preference) findPreference("pref_key_passcode_toggle");
        Preference pAppLockOff = (Preference) findPreference("pref_key_change_passcode");

        Preference pAppHelp = (Preference) findPreference("setting_activity_Developer_email");
        CheckBoxPreference cbpAutoAlarm = (CheckBoxPreference) findPreference("setting_activity_autoalarm");
        CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference) findPreference("setting_activity_alarm_reiceive");


        pAppLock.setOnPreferenceClickListener(this);
        pAppLockOff.setOnPreferenceClickListener(this);

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


        // 잠금 설정
        if (preference.getKey().equals("pref_key_passcode_toggle")) {
            startActivity(PassCodeSetActivity.createIntent(getApplicationContext()));
        }

        // 잠금 끄기기
        else if (preference.getKey().equals("pref_key_change_passcode")) {
            PrefUtil.putBoolean(Constants.PREF_KEY_IS_LOCKED, false);
            PrefUtil.putInt(Constants.PREF_KEY_PASSWORD, 0);
            ShowToast.show("Unlock passcode!", this);
        }

        // 자동알림
        else if (preference.getKey().equals("setting_activity_autoalarm")) {

        }

        // 알림 받기
        else if (preference.getKey().equals("setting_activity_alarm_reiceive")) {

        }

        // 개발자에게 이메일 보내기
        else if (preference.getKey().equals("setting_activity_Developer_email")) {

            uri = Uri.parse("mailto:dhlvyou@naver.com"); //이메일과 관련된 Data는 'mailto:'으로 시작. 이후는 이메일 주소
            i = new Intent(Intent.ACTION_SENDTO, uri); //시스템 액티비티인 Dial Activity의 action값
            startActivity(i);//액티비티 실행

        }

        return false;
    }


    public String appVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        String version = pInfo.versionName;
        return version;
    }

}
