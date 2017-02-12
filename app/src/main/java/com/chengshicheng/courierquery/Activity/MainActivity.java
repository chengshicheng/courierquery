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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.Utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ImageView menu;
    private SearchView searchView;
    private MenuItem searchItem;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragmentsList;

    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合

    private int requestCode = 100;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainn);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //隐藏Toolbar的标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mInflater = LayoutInflater.from(this);
        //添加页卡标题
        mTitleList.add("全部");
        mTitleList.add("未签收");
        mTitleList.add("已签收");
        fragmentsList = new ArrayList<Fragment>();
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
            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toobar_right, menu);
        searchItem = menu.findItem(R.id.menu_search);//在菜单中找到对应控件的item

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
                    MenuItemCompat.collapseActionView(searchItem);//收起搜索框
//                    searchView.onActionViewCollapsed();
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
            DialogUtils.ShowToast("menu_scan");
            Intent intent = new Intent();
            intent.setClass(this, ChooseCompanyActivity.class);
            intent.putExtra("requestType", 2);
            startActivityForResult(intent, requestCode);
        }
        return true;
    }

    private class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> mFragmentsList;

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public TabFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList) {
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
}




