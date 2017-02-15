package com.chengshicheng.courierquery.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chengshicheng.courierquery.Adapter.MyRecyclerAdapter;
import com.chengshicheng.courierquery.Adapter.RecycleViewDivider;
import com.chengshicheng.courierquery.CourierApp;
import com.chengshicheng.courierquery.GreenDao.OrderQuery;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.greendao.gen.OrderQueryDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengshicheng on 2017/1/9.
 */
public class Fragment2 extends BaseTabFragment {
    private MyRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private static ArrayList<OrderQuery> mDatas = new ArrayList<OrderQuery>();

    public static Fragment2 newInstance(Context context, Bundle bundle) {
        Fragment2 newFragment = new Fragment2();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment1, null);
        mAdapter = new MyRecyclerAdapter(mActivity, mDatas);
        recyclerView = (RecyclerView) view.findViewById(R.id.lv_fragment1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CourierApp.getContext());
        recyclerView.addItemDecoration(new RecycleViewDivider(
                CourierApp.getContext(), LinearLayoutManager.HORIZONTAL, 3, getResources().getColor(R.color.listviewbg)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        mDatas.clear();
        List<OrderQuery> result = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.State.eq("3")).orderDesc(OrderQueryDao.Properties.ToTop).list();
        mDatas.addAll(result);
    }

    @Override
    protected OrderQuery getChosenItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public void refreshRecyclerView() {
        initData();
        mAdapter.notifyDataSetChanged();
    }

}
