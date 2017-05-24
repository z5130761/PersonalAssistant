package learn.zhu.com.personalassistant.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.adapt.DataRecyclerAdapter;
import learn.zhu.com.personalassistant.callback.RecyclerViewCallback;
import learn.zhu.com.personalassistant.model.Data;
import learn.zhu.com.personalassistant.model.Type;
import learn.zhu.com.personalassistant.util.DialogUtil;
import learn.zhu.com.personalassistant.util.Utility;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    //请求码
    private final int UPDATE_DATA_CODE = 0;
    private final int PERSON_INFO_CODE = 1;

    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private TextView mUserName;
    private TextView mZcMonth;
    private TextView mSrMonth;
    private TextView mTotalMoney;
    private ProgressBar mProgressBar;
    private TextView mHeaderTotalMoney;
    private FloatingActionButton mFloatingActionButton;
    private CircleImageView mCircleImageView;

    private DataRecyclerAdapter mAdapter;
    private List<Data> mDatas;

    /**
     * 进度条View单击事件
     */
    private View.OnClickListener totalMoneyOnClickListene = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final EditText editText = new EditText(MainActivity.this);
            editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            //加载预算金额
            final float totalMoney = getTotalMoney();                               //原本预算的钱
            editText.setText(String.valueOf(totalMoney));

            DialogUtil.showNormalDialog(MainActivity.this, "设置预算金额", editText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //设置预算金额
                    float money = Float.valueOf(editText.getText().toString());     //新设置的钱
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                    editor.putFloat(getString(R.string.total_money), money);
                    editor.apply();
                    //设置菜单头部的预算金额
                    mHeaderTotalMoney.setText(String.valueOf(money));
                    //重新计算值
                    float preMoney = Float.valueOf(mTotalMoney.getText().toString());//原来剩余的钱
                    //新设置的值 - 原来总共的钱和剩余的钱的差值
                    float newMoney = money - (totalMoney - preMoney);
                    mTotalMoney.setText(String.valueOf(newMoney));
                    mProgressBar.setMax((int)money);
                    mProgressBar.setProgress((int)newMoney);
                }
            }, null, true);
        }
    };

    /**
     * 获取预算金额
     * @return
     */
    private float getTotalMoney() {
        return Utility.getFloat(MainActivity.this, getString(R.string.total_money));
    }

    /**
     * 初始化
     * @param savedInstanceState
     */
    @Override
    protected void init(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT > 21) {
            //如果是高版本的进行状态栏沉浸适配
            View decorView = getWindow().getDecorView();
            //设置成全屏
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN           //全屏隐藏状态栏
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION    //隐藏导航栏
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE             //与前面一起用表示主体内容占用隐藏的空间
            );
            //设置状态栏透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //设置导航栏透明
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //检查权限，请求网络权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        //获取控件
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mZcMonth = (TextView)findViewById(R.id.zc_month);
        mSrMonth = (TextView)findViewById(R.id.sr_month);
        mTotalMoney = (TextView)findViewById(R.id.tv_total_money);
        mProgressBar = (ProgressBar)findViewById(R.id.pb_total_money);
        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.add_data);

        mFloatingActionButton.setOnClickListener(this);
        //设置余额的单击事件
        ((View)mProgressBar.getParent()).setOnClickListener(totalMoneyOnClickListene);

        //设置的默认值
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        //设置月份
        setMonth();

        //密码锁
        lock();
        initNavigation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //权限请求被拒绝，程序退出
                    Toast.makeText(this, "请给予足够的权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置月份
     */
    private void setMonth() {
        Calendar calendar = Calendar.getInstance();
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        ((TextView)findViewById(R.id.left_month)).setText(month + "月收入");
        ((TextView)findViewById(R.id.right_month)).setText(month + "月支出");
    }

    /**
     * 获取布局id
     * @return
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    /**
     * 单击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_info_img:
                Intent intent = new Intent(MainActivity.this, PersonInfoActivity.class);
                startActivityForResult(intent, PERSON_INFO_CODE);

                break;
            case R.id.add_data:
                //添加数据
                startActivityForResult(new Intent(MainActivity.this, AddDataActivity.class), UPDATE_DATA_CODE);
                break;
            default:
                break;
        }
    }

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * 初始化toolbar
     */
    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("个人助理");
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        toggle.syncState();
    }

    /**
     * 菜单项事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.search:
                //查询
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mDatas = new ArrayList<>();
        mAdapter = new DataRecyclerAdapter(this, mDatas, mPersonalAssistantDB);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //创建滑动回调函数
        RecyclerViewCallback callback = new RecyclerViewCallback();
        callback.setOnSwipedListener(new RecyclerViewCallback.OnSwipedListener() {
            @Override
            public void onSwiped(int position, int direction) {
                Data data = mDatas.get(position);
                mPersonalAssistantDB.deleteData(data.getId());
                mDatas.remove(position);
                mAdapter.notifyItemRemoved(position);

                if(data.getTypeId() == Type.ZC) {
                    subMoney(mZcMonth, -data.getMoney());
                }
                else
                    subMoney(mSrMonth, data.getMoney());
            }
        });
        //创建移动监听器
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        //加载数据
        loadDatas();
    }

    /**
     * 计算删除记录后的钱
     * @param view 更改的view
     * @param money 删掉的钱
     */
    public void subMoney(TextView view, float money) {
        //设置收入和支出
        float preMoney = Float.valueOf(view.getText().toString());
        float currentMoney = preMoney - money;
        view.setText(String.valueOf(currentMoney));

        //设置进度条
        float surplusMoney = Float.valueOf(mTotalMoney.getText().toString());
        surplusMoney -= money;
        mTotalMoney.setText(String.valueOf(surplusMoney));
        mProgressBar.setProgress((int)surplusMoney);
    }

    /**
     * 初始化头像
     */
    private void initHeaderAvatar() {
        String imagePath = Utility.getString(this, getString(R.string.avatar));
        if(!imagePath.equals("未设置")) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mCircleImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 初始化侧滑菜单头
     */
    private void initNavigationHeader() {
        View view = mNavigationView.getHeaderView(0);
        //头像
        mCircleImageView = (CircleImageView) view.findViewById(R.id.user_info_img);
        initHeaderAvatar();
        mCircleImageView.setOnClickListener(this);
        //用户名
        mUserName = (TextView)view.findViewById(R.id.username);
        mUserName.setText(Utility.getString(this, getString(R.string.user_name)));
        //预算额度
        mHeaderTotalMoney = (TextView)view.findViewById(R.id.header_total_money);
        mHeaderTotalMoney.setText(String.valueOf(getTotalMoney()));
    }

    /**
     * 初始化侧滑菜单
     */
    private void initNavigation() {
        initNavigationHeader();

        //创建菜单单击事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.statistical_information:
                        //报表
                        startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
                        break;
                    case R.id.category_manage:
                        //分类管理
                        startActivity(new Intent(MainActivity.this, CategoryManageActivity.class));
                        break;
                    case R.id.settings:
                        startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), PERSON_INFO_CODE);
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadDatas() {
        float sr = 0;
        float zc = 0;
        //加载列表数据
        mDatas.clear();
        Calendar calendar = Calendar.getInstance();

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.getActualMaximum(Calendar.DATE));

        String beginDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-" + String.format(Locale.CHINA, "%2d", Integer.valueOf(day));
        List<Data> list = mPersonalAssistantDB.getDatas(0, 0, beginDate, endDate, -1, -1, "date desc");
        for (Data data :
                list) {
            mDatas.add(data);
            if(data.getTypeId() == Type.ZC)
                zc -= data.getMoney();
            else
                sr += data.getMoney();
        }
        mAdapter.notifyDataSetChanged();
        mZcMonth.setText(String.valueOf(zc));
        mSrMonth.setText(String.valueOf(sr));

        //加载进度条
        float totalMoney = getTotalMoney();
        mProgressBar.setMax((int)totalMoney);
        float surplusMoney = totalMoney + sr + zc;
        mProgressBar.setProgress((int)surplusMoney);
        mTotalMoney.setText(String.valueOf(surplusMoney));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UPDATE_DATA_CODE:
                if(resultCode == RESULT_OK) {
                    initHeaderAvatar();
                    loadDatas();
                }
                break;
            case PERSON_INFO_CODE:
                if(resultCode == RESULT_OK) {
                    loadDatas();
                    String userName = Utility.getString(this, getString(R.string.user_name));
                    mUserName.setText(userName);
                    initHeaderAvatar();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(mPersonalAssistantDB != null)
            mPersonalAssistantDB.close();
        super.onDestroy();
    }

    /**
     * 密码锁
     */
    private void lock() {
        if(!Utility.getBoolean(this, getString(R.string.lock))) {
            //没有设置密码锁
            initRecyclerView();
            return;
        }
        final EditText editText = new EditText(this);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isCorrect = false;
                String inputPassword = editText.getText().toString();
                //判断密码是否正确
                if(Utility.getString(MainActivity.this, getString(R.string.password)).equals(inputPassword)) {
                    isCorrect = true;
                    //密码正确加载数据
                    initRecyclerView();
                } else {
                    editText.setError("密码错误");
                }
                DialogUtil.setButtonExit(dialog, isCorrect);
            }
        };
        DialogUtil.showNormalDialog(this, "请输入密码", editText, onClickListener,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }, false);
    }
}
