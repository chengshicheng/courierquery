package com.chengshicheng.courierquery.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengshicheng.courierquery.R;

/**
 * Created by chengshicheng on 2017/1/9.
 */

public class Fragment3 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

//        toolbar.setNavigationIcon(R.drawable.common_google_signin_btn_icon_dark);
//        toolbar.inflateMenu(R.menu.zhihu_toolbar_menu);//MenuItem


        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        return view;
    }
}
