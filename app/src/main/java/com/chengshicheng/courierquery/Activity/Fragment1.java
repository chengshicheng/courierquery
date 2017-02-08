package com.chengshicheng.courierquery.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chengshicheng.courierquery.Utils.DialogUtils;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.Utils.StringUtils;

/**
 * Created by chengshicheng on 2017/1/9.
 */
public class Fragment1 extends Fragment {
    private ListView listView;
    private String[] data = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};

    public static Fragment1 newInstance(Context context, Bundle bundle) {
        Fragment1 newFragment = new Fragment1();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, data);

        listView = (ListView) view.findViewById(R.id.lv_fragment1);
        listView.setAdapter(adapter);
        return view;
    }

}
