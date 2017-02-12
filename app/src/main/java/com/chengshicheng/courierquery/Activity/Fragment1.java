package com.chengshicheng.courierquery.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengshicheng.courierquery.CourierApp;
import com.chengshicheng.courierquery.GreenDao.GreenDaoHelper;
import com.chengshicheng.courierquery.GreenDao.OrderQuery;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.Utils.DialogUtils;
import com.chengshicheng.greendao.gen.OrderQueryDao;

import java.util.ArrayList;

/**
 * Created by chengshicheng on 2017/1/9.
 */
public class Fragment1 extends Fragment implements OnRecyclerViewItemClickListener {
    private static Context context;
    private RecyclerView recyclerView;
    private OrderQueryDao mOrderDao;
    private ArrayList<OrderQuery> mDatas = new ArrayList<OrderQuery>();

    public static Fragment1 newInstance(Context context, Bundle bundle) {
        Fragment1.context = context;
        Fragment1 newFragment = new Fragment1();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);
        init();
        MyRecyclerAdapter mAdapter = new MyRecyclerAdapter(context, mDatas);
        recyclerView = (RecyclerView) view.findViewById(R.id.lv_fragment1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CourierApp.getContext());
        recyclerView.addItemDecoration(new RecycleViewDivider(
                CourierApp.getContext(), LinearLayoutManager.HORIZONTAL, 3, getResources().getColor(R.color.listviewbg)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        return view;
    }

    private void init() {
        mOrderDao = GreenDaoHelper.getDaoSession().getOrderQueryDao();
        mDatas.addAll(mOrderDao.loadAll());
    }

    @Override
    public void onItemClick(View view, int position) {
        DialogUtils.ShowToast("" + position);

    }
}
