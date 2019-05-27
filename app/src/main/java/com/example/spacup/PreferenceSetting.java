package com.example.spacup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toolbar;

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


        Preference pAppHelp = (Preference) findPreference("setting_activity_Developer_email");
        CheckBoxPreference cbpAutoAlarm = (CheckBoxPreference) findPreference("setting_activity_autoalarm");
        CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference) findPreference("setting_activity_alarm_reiceive");

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

        // 자동알림
        if (preference.getKey().equals("setting_activity_autoalarm")) {

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

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}
