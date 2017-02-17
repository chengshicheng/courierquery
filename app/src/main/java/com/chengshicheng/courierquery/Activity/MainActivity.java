package com.chengshicheng.courierquery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.Utils.DialogUtils;
import com.chengshicheng.courierquery.Utils.LogUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private MenuItem searchItem;

    private Toolbar toolbar;

    private List<String> mTitleList = new ArrayList<>();//页卡标题集合

    private int requestCode = 100;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainn);
        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //隐藏Toolbar的标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View fabb = findViewById(R.id.fab);
        fabb.setOnClickListener(this);

        ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        //添加页卡标题
        mTitleList.add("全部");
        mTitleList.add("已签收");
        mTitleList.add("未签收");
        ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>();
        Bundle bundle = new Bundle();
        Fragment fragment1 = Fragment1.newInstance(
                MainActivity.this, bundle);
        Fragment fragment2 = Fragment2.newInstance(
                MainActivity.this, bundle);
        Fragment fragment3 = Fragment3.newInstance(
                MainActivity.this, bundle);

        fragmentsList.add(fragment1);
        fragmentsList.add(fragment2);
        fragmentsList.add(fragment3);
        mViewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(fragmentsList.size());//可避免切换时重复执行onCreatView()
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.menu:
                DialogUtils.ShowToast("menu");
                break;
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this,demoActivity.class);
                startActivity(intent);
                break;


            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toobar_right, menu);
        searchItem = menu.findItem(R.id.menu_search);//在菜单中找到对应控件的item

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(MainActivity.this, ChooseCompanyActivity.class);
                if (query.length() < 6 || query.length() > 50) {
                    DialogUtils.ShowToast("单号格式错误");
                } else {
                    intent.putExtra("requestType", 1);
                    intent.putExtra("requestNumber", query);
                    startActivityForResult(intent, requestCode);
//                    MenuItemCompat.collapseActionView(searchItem);//收起搜索框
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_scan) {
            Intent intent = new Intent();
            intent.setClass(this, ChooseCompanyActivity.class);
            intent.putExtra("requestType", 2);
            startActivityForResult(intent, requestCode);
        }
        return true;
    }

    private class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> mFragmentsList;

        private TabFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList) {
            super(fm);
            mFragmentsList = fragmentsList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

    /**
     * 判断(x,y)是否在view的区域内
     */
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    /***
     * 触摸事件不在toorBar中时，收起SearchView
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        if (!isTouchPointInView(toolbar, x, y)) {
            MenuItemCompat.collapseActionView(searchItem);//收起搜索框
        }
        return super.dispatchTouchEvent(ev);
        //do something
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MenuItemCompat.collapseActionView(searchItem);//收起搜索框
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}




