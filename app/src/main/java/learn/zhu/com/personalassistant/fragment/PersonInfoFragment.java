package learn.zhu.com.personalassistant.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.activity.MainActivity;
import learn.zhu.com.personalassistant.activity.PersonInfoActivity;
import learn.zhu.com.personalassistant.model.Category;
import learn.zhu.com.personalassistant.model.Data;
import learn.zhu.com.personalassistant.util.BackupUtil;

/**
 * Created by zhu on 2017/4/21.
 */

public class PersonInfoFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private final String KEY[] = new String[] { "pref_user_name", "pref_age",
            "pref_email", "pref_sex", "pref_born_date", "pref_location"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_personal_info);

        //加载值
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        for (String key:
             KEY) {
            Preference pref = findPreference(key);

            pref.setSummary(sharedPreferences.getString(key, "未设置"));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        pref.setSummary(sharedPreferences.getString(key, "未设置"));
    }

    @Override
    public void onResume() {
        super.onResume();
        //注册监听设置更改
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //取消注册
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
