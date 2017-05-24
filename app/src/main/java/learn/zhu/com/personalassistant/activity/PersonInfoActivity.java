package learn.zhu.com.personalassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import learn.zhu.com.personalassistant.preference.AvatarPreference;
import learn.zhu.com.personalassistant.fragment.PersonInfoFragment;
import learn.zhu.com.personalassistant.R;

public class PersonInfoActivity extends BaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new PersonInfoFragment())
                .commit();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("个人信息");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK)
                    mAvatarPreference.setPicture(data);
                break;
            default:
                break;
        }
    }
    private AvatarPreference mAvatarPreference;
    public void setAvatarPreference(AvatarPreference avatarPreference) {
        mAvatarPreference = avatarPreference;
    }
}
