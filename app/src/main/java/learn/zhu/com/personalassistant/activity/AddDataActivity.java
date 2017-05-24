package learn.zhu.com.personalassistant.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.model.Category;
import learn.zhu.com.personalassistant.model.Data;
import learn.zhu.com.personalassistant.model.Type;
import learn.zhu.com.personalassistant.util.DialogUtil;
import learn.zhu.com.personalassistant.util.SnakebarUtil;

public class AddDataActivity extends BaseActivity implements View.OnClickListener{

    private EditText date;
    private Spinner category;
    private EditText money;
    private EditText remark;

    private ArrayAdapter<Category> mAdapter;
    private List<Category> mList;

    private int type = Type.SR;

    private View.OnClickListener showCategoryManager = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddDataActivity.this, CategoryManageActivity.class);
            startActivityForResult(intent, 1);
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        //获取控件
        date = (EditText)findViewById(R.id.date);
        category = (Spinner)findViewById(R.id.category);
        money = (EditText)findViewById(R.id.money);
        remark = (EditText)findViewById(R.id.remark);

        //添加分类按钮事件
        findViewById(R.id.type_sr).setOnClickListener(this);
        findViewById(R.id.type_zc).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        date.setOnClickListener(this);

        initSpinner();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_data;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                //添加记录
                add();
                break;
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.type_sr:
                //选择收入
                type = Type.SR;
                loadCategoryData();
                break;
            case R.id.type_zc:
                //选择支出
                type = Type.ZC;
                loadCategoryData();
                break;
            case R.id.date:
                DialogUtil.showDatepicker(this, date);
                break;
            default:
                break;
        }
    }


    /**
     * 加载类型
     */
    private void loadCategoryData() {
        mList.clear();
        List<Category> list = mPersonalAssistantDB.getCategories(type);
        for (Category category :
                list) {
            mList.add(category);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加
     */
    private void add() {
        //获取信息
        String strRemark = remark.getText().toString();
        float fMoney = Float.valueOf(money.getText().toString());
        String strDate = date.getText().toString();
        //判断是否输入日期
        if(TextUtils.isEmpty(strDate)) {
            date.requestFocus();
            date.setError("请输入日期");
            return;
        }
        //判断是否有分类
        if(mList.size() == 0) {
            SnakebarUtil.show(this, "当前无任何分类", "前往添加", showCategoryManager);
            return;
        }

        //创建一条数据
        Data data = new Data();
        data.setRemark(strRemark);
        data.setTypeId(type);
        data.setMoney(fMoney);
        data.setDate(strDate);
        data.setCategoryId(((Category)category.getSelectedItem()).getId());
        //添加到数据库中
        if(mPersonalAssistantDB.addData(data) > 0) {
            SnakebarUtil.show(this, "添加成功", null, null);
            setResult(RESULT_OK);
        }
        else
            SnakebarUtil.show(this, "添加失败", null, null);
    }

    /**
     * 初始化toolbar
     */
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
            case R.id.category_manage:
                Intent intent = new Intent(AddDataActivity.this, CategoryManageActivity.class);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 初始化spinner
     */
    private void initSpinner() {
        mList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mList);
        loadCategoryData();
        category.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    loadCategoryData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_data_menu, menu);
        return true;
    }
}
