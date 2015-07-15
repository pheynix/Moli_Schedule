package com.pheynix.moli_schedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private TabLayout tabs;
    private MainContentPageAdapter mPageAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DBUtil dbUtil;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbUtil = new DBUtil(this);

        if (isFirstLaunch()){
            initCategory();
        }
        
        initializeView();

        initializeDrawer();


    }



    private void initializeView() {
        //设置新的Toolbar
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(mToolbar);

        //NavigationView
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.id_navigationView);

        //设置Viewpager和tabs
        mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
        tabs = (TabLayout) findViewById(R.id.id_tabs);

        tabs.addTab(tabs.newTab());
        tabs.addTab(tabs.newTab());
//        在tab中删除记录页
//        tabs.addTab(tabs.newTab());

        mPageAdapter = new MainContentPageAdapter(getSupportFragmentManager(),this);

        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(this);
        tabs.setupWithViewPager(mViewPager);

        fragmentManager = getSupportFragmentManager();

    }


    //设置汉堡包图标和菜单事件点击的响应逻辑
    private void initializeDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                不需要设置为check
//                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.id_drawer_share:
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.id_drawer_settings:
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.id_drawer_about:
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.id_contact_us:
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                }

                mDrawerLayout.closeDrawers();

                return true;
            }
        });

        mDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void initCategory() {
        Category category_normal = new Category("普通日程",0,0,"-1",0);
        dbUtil.addCategory(category_normal);

        Category category_new = new Category("新建日程",0,0,"-1",0);
        dbUtil.addCategory(category_new);
    }

    //是否第一次启动此应用，用SharePreferences的方式保存
    public boolean isFirstLaunch() {

        SharedPreferences sharedPreferences = getSharedPreferences("pre_first_launch", Context.MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch",true);

        if (isFirstLaunch){
            sharedPreferences.edit().putBoolean("isFirstLaunch",false).commit();
        }

        return isFirstLaunch;
    }


    //add onPageChangeListener
    //滑动过程调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    //滑动前调用
    @Override
    public void onPageSelected(int position) {
        //重复代码，考虑重构
        if (position == 1){
            if (fragmentManager.findFragmentByTag("SummaryDailyFragment") == null){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_fragment_summary, new SummaryDailyFragment(), "SummaryDailyFragment");
                transaction.commit();
                mViewPager.setCurrentItem(1);
            }
        }
        if (position == 0){
            if (fragmentManager.findFragmentByTag("SummaryDailyFragment") == null){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragmentManager.findFragmentByTag("SummaryDailyFragment"));
                transaction.commit();
            }
        }
    }

    //0,1,2 == idle,dragging,setting，滑动状态
    @Override
    public void onPageScrollStateChanged(int state) {
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //在SummaryFragment才让OptionMenu生效
//        if (isSummaryFragment){
//
//        }

        switch (item.getItemId()){
            case R.id.action_daily:
                if (fragmentManager.findFragmentByTag("SummaryDailyFragment") == null){
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_fragment_summary, new SummaryDailyFragment(), "SummaryDailyFragment");
                    transaction.commit();
                }
                mViewPager.setCurrentItem(1);
                break;
            case R.id.action_weekly:
                if (fragmentManager.findFragmentByTag("SummaryWeeklyFragment") == null){
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_fragment_summary, new SummaryWeeklyFragment(), "SummaryWeeklyFragment");
                    transaction.commit();
                }
                mViewPager.setCurrentItem(1);
                break;
            case R.id.action_monthly:
                if (fragmentManager.findFragmentByTag("SummaryMonthlyFragment") == null){
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_fragment_summary,new SummaryMonthlyFragment(),"SummaryMonthlyFragment");
                    transaction.commit();
                }
                mViewPager.setCurrentItem(1);
                break;
        }

        return true;
    }

}
