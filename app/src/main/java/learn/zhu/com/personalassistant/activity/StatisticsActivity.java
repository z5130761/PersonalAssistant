package learn.zhu.com.personalassistant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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

import com.github.lzyzsd.randomcolor.RandomColor;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.model.Category;
import learn.zhu.com.personalassistant.model.Data;
import learn.zhu.com.personalassistant.model.Type;
import learn.zhu.com.personalassistant.util.DialogUtil;

/**
 * Created by zhu on 2017/4/28.
 */

public class StatisticsActivity extends BaseActivity {

    //控件
    private PieChart mPieChart;
    private CheckBox ckCategory;
    private CheckBox ckDate;
    private EditText edtStartDate;
    private EditText edtEndDate;
    private Spinner spCategory;
    private RadioButton rdbSR;
    private Button btnSearch;
    private AppBarLayout mAppBarLayout;

    private int mType = Type.SR;
    private List<Category> mSpinnerList;
    private ArrayAdapter<Category> mSpinnerAdapter;

    private boolean isSearchAllCategory;
    private Hashtable<String, Float> mPieData;

    private float totalMoney = 0f;

    @Override
    protected void init(Bundle savedInstanceState) {
        //初始化控件
        ckCategory = (CheckBox)findViewById(R.id.ck_category);
        ckDate = (CheckBox)findViewById(R.id.ck_date);
        edtStartDate = (EditText)findViewById(R.id.start_date);
        edtEndDate = (EditText)findViewById(R.id.end_date);
        rdbSR = (RadioButton)findViewById(R.id.type_sr);
        btnSearch = (Button)findViewById(R.id.search);
        spCategory = (Spinner)findViewById(R.id.sp_category);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.appbar_layout);

        mPieChart = (PieChart)findViewById(R.id.pie_chart);
        mPieChart.setNoDataText("还未进行统计查询");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

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
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showDatepicker(StatisticsActivity.this, edtStartDate);
            }
        });
        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showDatepicker(StatisticsActivity.this, edtEndDate);
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

        initSpinner();
        initPieChart();
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

    //初始化饼图控件
    private void initPieChart() {
        mPieData = new Hashtable<>();
        //mPieChart.setHoleColorTransparent(true);
        mPieChart.setHoleRadius(60f);  //半径
        mPieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        // 设置描述文字
        Description description = new Description();
        description.setText("收入/支出统计");
        description.setTextSize(20);
        description.setXOffset(10);
        description.setYOffset(15);
        description.setTextColor(Color.GRAY);
        mPieChart.setDescription(description);

        // mChart.setDrawYValues(true);
        mPieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        mPieChart.setDrawHoleEnabled(true);

        mPieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // 可以手动旋转
        mPieChart.setRotationEnabled(true);

        // 百分比显示
        mPieChart.setUsePercentValues(true);

        // 饼状图中间的文字
        mPieChart.setCenterText("收入统计");

        //设置Label大小
        mPieChart.setEntryLabelTextSize(7);
        mPieChart.setEntryLabelColor(Color.BLACK);

        // 设置比例图
        Legend mLegend = mPieChart.getLegend();
        // 设置比例图的位置
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        mLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        mLegend.setDrawInside(false);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(0f);
        mLegend.setYOffset(0f);
        //mLegend.setForm(Legend.LegendForm.LINE);  //设置比例图的形状，默认是方形

        // 设置动画
        mPieChart.animateXY(1000, 1000);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_statistics;
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

    //设置饼图数据
    private PieData getPieData() {
        //yValues用来表示封装每个饼块的实际数据
        ArrayList<PieEntry> yValues = new ArrayList<>();

        //添加数据

        if(mPieData.size() > 0)
            if(isSearchAllCategory) {
                //添加所有的类别
                for (Category category:
                        mSpinnerList) {

                    float money = mPieData.get(category.getName()) == null ? 0 : mPieData.get(category.getName());
                    float persent = money / totalMoney;
                    if(money > 0)
                        yValues.add(new PieEntry(persent, String.format(Locale.CHINA,"%s(%.2f)", category.getName(), persent*100), money));
                }
            } else {
                //只有指定的类别
                Category category = ((Category)spCategory.getSelectedItem());
                float money = mPieData.get(category.getName());
                yValues.add(new PieEntry(100, String.format(Locale.CHINA,"%s(100)", category.getName()), money));
            }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, null);

        //设置值的样式颜色
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setSliceSpace(1.5f); //设置个饼状图之间的距离

        // 设置饼图颜色
        if(yValues.size() > 0) {
            RandomColor randomColor = new RandomColor();
            int[] randomColors = randomColor.randomColor(yValues.size());
            pieDataSet.setColors(randomColors);
        }

        // 设置选中态多出的长度
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px);

        //设置显示为百分比
        pieDataSet.setValueFormatter(new PercentFormatter());

        pieDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf(entry.getData()) + "元";
            }
        });

        return new PieData(pieDataSet);
    }

    //查询
    public void search() {
        String startDate = null;
        String endDate = null;
        float total = 0f;
        totalMoney = 0;
        mPieData.clear();
        String groupBy = "money desc";

        //判断日期范围
        if(ckDate.isChecked()) {
            startDate = edtStartDate.getText().toString();
            endDate = edtEndDate.getText().toString();
        }

        //判断类别
        if(ckCategory.isChecked()) {
            //查询指定类别
            isSearchAllCategory = false;
            int categoryId = ((Category)spCategory.getSelectedItem()).getId();
            List<Data> list = mPersonalAssistantDB.getDatas(categoryId, mType, startDate, endDate, -1, -1, groupBy);
            for (Data data:
                    list) {
                total += data.getMoney();
            }
            totalMoney = total;
            //判断是否有数据
            if(list.size() > 0)
                mPieData.put(((Category)spCategory.getSelectedItem()).getName(), total);
        } else {
            //查询所有类别
            isSearchAllCategory = true;
            for (Category category:
                 mSpinnerList) {
                float perCategoryTotal = 0f;
                //从数据库中读取指定分类id的数据
                List<Data> dataList = mPersonalAssistantDB.getDatas(category.getId(), mType, startDate, endDate, -1, -1, groupBy);
                if(dataList.size() <= 0)
                    //当前类别无数据继续查询下一个类别
                    continue;
                //计算总金额
                for (Data data:
                        dataList) {
                    perCategoryTotal += data.getMoney();
                }
                totalMoney += perCategoryTotal;
                mPieData.put(category.getName(), perCategoryTotal);
            }
        }

        mPieChart.setData(getPieData());
        mPieChart.setCenterText("总计：" + String.valueOf(totalMoney));
        mPieChart.invalidate();
        //收起appbar
        mAppBarLayout.setExpanded(false);
    }
}
