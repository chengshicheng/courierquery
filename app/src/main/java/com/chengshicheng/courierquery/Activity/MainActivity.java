package com.chengshicheng.courierquery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.Utils.DialogUtils;
import com.chengshicheng.courierquery.Utils.LogUtil;

public class MainActivity extends BaseActivity {

    private ImageView menu;
    private SearchView searchView;
    private MenuItem searchItem;

    private int requestCode = 100;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //隐藏Toolbar的标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);

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
    protected void onResume() {
        super.onResume();
        if (searchView != null && !searchView.isShown()) {
            MenuItemCompat.collapseActionView(searchItem);//收起搜索框
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}


//    private FragmentTabHost mTabHost;
//
//    private LayoutInflater mLayoutInflater;
//
//    private final Class mFragmentArray[] = {Fragment1.class, Fragment2.class,
//            Fragment3.class};
//
//    private final String[] mTextArray = {"查快递", "寄快递", "更多"};
//
//    private final int mImageArray[] = {R.drawable.fragment1_selector, R.drawable.fragment2_selector,
//            R.drawable.fragment3_selector};
//    private void initViews() {
//        mLayoutInflater = LayoutInflater.from(this);
//        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//        mTabHost.getTabWidget().setDividerDrawable(null);//去除tab之间的分割线
//        int count = mFragmentArray.length;
//        for (int i = 0; i < count; i++) {
//            // 给每个Tab按钮设置内容
//            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
//                    .setIndicator(getTabItemView(i));
//            // 将Tab按钮添加进Tab选项卡中
//            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
//        }
//    }
//
//    private View getTabItemView(int index) {
//        View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
//        TextView textView = (TextView) view.findViewById(R.id.textview);
//        textView.setText(mTextArray[index]);
//        imageView.setImageResource(mImageArray[index]);
//
//        return view;
//    }




