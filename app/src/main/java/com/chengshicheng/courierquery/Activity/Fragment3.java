package com.chengshicheng.courierquery.Activity;

import android.content.Context;
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
    public static Fragment3 newInstance(Context context, Bundle bundle) {
        Fragment3 newFragment = new Fragment3();
        newFragment.setArguments(bundle);
        return newFragment;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, null);

        return view;
    }
}
