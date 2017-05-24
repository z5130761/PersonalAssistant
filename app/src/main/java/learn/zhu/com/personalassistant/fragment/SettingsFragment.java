package learn.zhu.com.personalassistant.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import java.io.IOException;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.activity.PersonInfoActivity;
import learn.zhu.com.personalassistant.activity.SettingsActivity;
import learn.zhu.com.personalassistant.util.BackupUtil;
import learn.zhu.com.personalassistant.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zhu on 2017/4/21.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private SwitchPreference mSwitchPreference;
    private SharedPreferences mSharedPreferences;
        //pref_update_password
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        findPreference("pref_backup").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", BackupUtil.TYPE_BACKUP);
                loginDialogFragment.setArguments(bundle);
                loginDialogFragment.show(((SettingsActivity)getActivity()).getSupportFragmentManager(), "loginDialog");
                return false;
            }
        });
        findPreference("pref_restore").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", BackupUtil.TYPE_RESTORE);
                loginDialogFragment.setArguments(bundle);
                loginDialogFragment.show(((SettingsActivity)getActivity()).getSupportFragmentManager(), "loginDialog");
                getActivity().setResult(Activity.RESULT_OK);
                return false;
            }
        });

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSwitchPreference = (SwitchPreference)findPreference(getString(R.string.lock));
        setLock();
    }

    public void setLock() {
        mSwitchPreference.setChecked(mSharedPreferences.getBoolean(getString(R.string.lock), false));
        findPreference(getString(R.string.password)).setEnabled(mSwitchPreference.isChecked());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setLock();
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
