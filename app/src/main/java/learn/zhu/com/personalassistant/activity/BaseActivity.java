package learn.zhu.com.personalassistant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.model.PersonalAssistantDB;

/**
 * Created by zhu on 2017/4/20.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected abstract void init(Bundle savedInstanceState);

    protected abstract int getContentViewId();

    protected abstract void initToolbar(Toolbar toolbar);

    protected PersonalAssistantDB mPersonalAssistantDB;

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        //获取数据库实例
        mPersonalAssistantDB = PersonalAssistantDB.getInstance(this);

        //初始化
        init(savedInstanceState);

        //初始化toolbar
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        initToolbar(mToolbar);
    }

    protected void addToolbarBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
