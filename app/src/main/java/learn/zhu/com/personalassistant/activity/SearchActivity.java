package learn.zhu.com.personalassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.adapt.DataRecyclerAdapter;
import learn.zhu.com.personalassistant.model.Category;
import learn.zhu.com.personalassistant.model.Data;
import learn.zhu.com.personalassistant.model.Type;
import learn.zhu.com.personalassistant.util.DialogUtil;

public class SearchActivity extends BaseActivity {
    //控件
    private CheckBox ckCategory;
    private CheckBox ckDate;
    private CheckBox ckMoney;
    private EditText edtStartDate;
    private EditText edtEndDate;
    private EditText edtStartMoney;
    private EditText edtEndMoney;
    private Spinner spCategory;
    private RadioButton rdbSR;
    private RadioButton rdbZC;
    private Button btnSearch;
    private RecyclerView mRecyclerView;
    private AppBarLayout mAppBarLayout;

    private int mType = Type.SR;
    private List<Category> mSpinnerList;
    private ArrayAdapter<Category> mSpinnerAdapter;

    private List<Data> mDataList;
    private DataRecyclerAdapter mDataAdapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        //初始化控件
        ckCategory = (CheckBox)findViewById(R.id.ck_category);
        ckDate = (CheckBox)findViewById(R.id.ck_date);
        ckMoney = (CheckBox)findViewById(R.id.ck_money);
        edtStartDate = (EditText)findViewById(R.id.start_date);
        edtEndDate = (EditText)findViewById(R.id.end_date);
        edtStartMoney = (EditText)findViewById(R.id.start_money);
        edtEndMoney = (EditText)findViewById(R.id.end_money);
        spCategory = (Spinner)findViewById(R.id.sp_category);
        rdbSR = (RadioButton)findViewById(R.id.type_sr);
        rdbZC = (RadioButton)findViewById(R.id.type_zc);
        btnSearch = (Button)findViewById(R.id.search);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.appbar_layout);

        rdbSR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mType = Type.SR;
                else
                    mType = Type.ZC;
                loadSpinnerDate();
            }
        });

        ckCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spCategory.setEnabled(isChecked);
            }
        });
        ckDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edtStartDate.setEnabled(isChecked);
                edtEndDate.setEnabled(isChecked);
            }
        });
        ckMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edtStartMoney.setEnabled(isChecked);
                edtEndMoney.setEnabled(isChecked);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showDatepicker(SearchActivity.this, edtStartDate);
            }
        });
        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showDatepicker(SearchActivity.this, edtEndDate);
            }
        });

        initSpinner();
        initRecyclerView();
    }

    //初始化spinner
    private void initSpinner() {
        mSpinnerList = new ArrayList<>();
        mSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mSpinnerList);
        loadSpinnerDate();
        spCategory.setAdapter(mSpinnerAdapter);
    }

    //加载Spinner数据
    private void loadSpinnerDate() {
        mSpinnerList.clear();
        List<Category> list = mPersonalAssistantDB.getCategories(mType);
        for (Category item:
             list) {
            mSpinnerList.add(item);
        }
        mSpinnerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        mDataList = new ArrayList<>();
        mDataAdapter = new DataRecyclerAdapter(this, mDataList, mPersonalAssistantDB);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mDataAdapter);
    }

    private void loadRecyclerViewData(List<Data> list) {
        mDataList.clear();
        for (Data item:
                list) {
            mDataList.add(item);
        }
        mDataAdapter.notifyDataSetChanged();
    }

    //查询
    private void search() {
        int categoryId = 0;
        String startDate = null;
        String endDate = null;
        int startMoney = -1;
        int endMoney = -1;

        //判断类别
        if(ckCategory.isChecked())
            categoryId = ((Category)spCategory.getSelectedItem()).getId();
        //判断日期范围
        if(ckDate.isChecked()) {
            startDate = edtStartDate.getText().toString();
            endDate = edtEndDate.getText().toString();
        }
        //判断钱范围
        if(ckMoney.isChecked()) {
            startMoney = edtStartMoney.getText() == null ? 0 : Integer.valueOf(edtStartMoney.getText().toString());
            endMoney = edtEndMoney.getText() == null ? 0 : Integer.valueOf(edtEndMoney.getText().toString());
        }
        List<Data> list = mPersonalAssistantDB.getDatas(categoryId, mType, startDate, endDate, startMoney, endMoney, "date desc");
        loadRecyclerViewData(list);
        mAppBarLayout.setExpanded(false);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        addToolbarBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.settings_menu, menu);
        return true;
    }
}
