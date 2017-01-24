package com.chengshicheng.courierquery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.Utils.DialogUtils;
import com.chengshicheng.courierquery.Utils.StringUtils;

public class MainActivity extends BaseActivity {
    private EditText intputCode;

    private ImageView sacnImage;
    private ImageView menu;

    private TextView searchButton;

    private static String number;

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
        intputCode = (EditText) findViewById(R.id.inputCode);
        sacnImage = (ImageView) findViewById(R.id.scanImage);
        searchButton = (TextView) findViewById(R.id.searchButton);

        sacnImage.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        number = intputCode.getText().toString();
        Intent intent = new Intent();
        intent.setClass(this, ChooseCompanyActivity.class);

        switch (v.getId()) {
            case R.id.searchButton:
                if (StringUtils.isEmpty(number)) {
                    DialogUtils.ShowToast("请输入单号");
                } else if (number.length() < 6 || number.length() > 50) {
                    DialogUtils.ShowToast("单号格式错误");
                } else {
                    intent.putExtra("requestType", 1);
                    intent.putExtra("requestNumber", number);
                    startActivityForResult(intent, requestCode);
                }
                break;
            case R.id.scanImage:
                intputCode.setText("");
                intent.putExtra("requestType", 2);
                startActivityForResult(intent, requestCode);
                break;
            case R.id.menu:
                DialogUtils.ShowToast("menu");
                break;
            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toobar_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                //Action you want
                DialogUtils.ShowToast("menu_search");
                return true;
            case R.id.menu_scan:
                //Action you want
                DialogUtils.ShowToast("menu_scan");
                return true;
            default:
                return super.onOptionsItemSelected(item);
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


}
