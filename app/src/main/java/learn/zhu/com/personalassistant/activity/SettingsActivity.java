package learn.zhu.com.personalassistant.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new SettingsFragment())
                .commit();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        addToolbarBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
