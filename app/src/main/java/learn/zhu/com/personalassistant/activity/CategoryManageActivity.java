package learn.zhu.com.personalassistant.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.adapt.CategoryRecyclerAdapter;
import learn.zhu.com.personalassistant.callback.RecyclerViewCallback;
import learn.zhu.com.personalassistant.model.Category;
import learn.zhu.com.personalassistant.util.SnakebarUtil;

public class CategoryManageActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    private TabLayout mTabLayout;
    private List<Category> mCategoryList;
    private FloatingActionButton mAddCategory;
    private CategoryRecyclerAdapter mAdapter;

    private int type = 1;

    /**
     * 添加分类监听器
     */
    private View.OnClickListener addCategoryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(CategoryManageActivity.this);
            View view = LayoutInflater.from(CategoryManageActivity.this).inflate(R.layout.view_add_dialog, null, false);
            final EditText categoryName = (EditText) view.findViewById(R.id.category_name);
            final EditText categoryIntroductions = (EditText) view.findViewById(R.id.category_introductions);
            builder.setTitle("添加分类");
            builder.setView(view);
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = categoryName.getText().toString();
                    String introductions = categoryIntroductions.getText().toString();
                    Category category = new Category(name, introductions, type);
                    mPersonalAssistantDB.addCategory(category);
                    mCategoryList.add(category);
                    mAdapter.notifyDataSetChanged();
                    setResult(RESULT_OK);
                }
            });
            builder.setNeutralButton("取消", null);
            builder.show();
        }
    };

    /**
     * 初始化
     */
    @Override
    protected void init(Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        mAddCategory = (FloatingActionButton)findViewById(R.id.category_add);

        mAddCategory.setOnClickListener(addCategoryOnClickListener);

        initTabLayout();
        initRecyclerView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_category_manage;
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mCategoryList = new ArrayList<>();
        List<Category> list = mPersonalAssistantDB.getCategories(type);
        for (Category category:
                list) {
            mCategoryList.add(category);
        }
        mAdapter = new CategoryRecyclerAdapter(this, mCategoryList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //设置滑动删除
        RecyclerViewCallback callback = new RecyclerViewCallback();
        callback.setOnSwipedListener(new RecyclerViewCallback.OnSwipedListener() {
            @Override
            public void onSwiped(final int position, final int direction) {
                final Category category = mCategoryList.get(position);
                //从数据库中删除
                if(mPersonalAssistantDB.deleteCategory(category.getId()) > 0) {
                    //从列表中删除
                    mCategoryList.remove(position);
                    //更新界面
                    mAdapter.notifyItemRemoved(position);
                    //显示提示
                    SnakebarUtil.show(CategoryManageActivity.this, "已删除", "撤销", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //撤销操作，重新添加
                            mPersonalAssistantDB.addCategory(category);
                            mCategoryList.add(position, category);
                            mAdapter.notifyItemInserted(position);
                        }
                    });
                } else {
                    SnakebarUtil.show(CategoryManageActivity.this, "删除失败", null, null);
                }

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 初始化toolbar
     */
    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("类别管理");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //添加返回按钮
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

    /**
     * 初始化tablayout
     */
    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        TabLayout.Tab sr = mTabLayout.newTab();
        sr.setText("收入");
        TabLayout.Tab zc = mTabLayout.newTab();
        zc.setText("支出");
        mTabLayout.addTab(sr);
        mTabLayout.addTab(zc);
        mTabLayout.setTabTextColors(Color.WHITE, ContextCompat.getColor(this,R.color.colorAccent));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String typeName = tab.getText().toString();
                type = mPersonalAssistantDB.getType(typeName).getId();
                mCategoryList.clear();
                List<Category> list = mPersonalAssistantDB.getCategories(type);
                for (Category category:
                        list) {
                    mCategoryList.add(category);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
