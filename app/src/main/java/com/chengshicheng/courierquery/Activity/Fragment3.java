package com.chengshicheng.courierquery.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.chengshicheng.courierquery.Utils.StringUtils;
import com.chengshicheng.greendao.gen.OrderQueryDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengshicheng on 2017/1/9.
 */
public class Fragment3 extends Fragment implements OnRecyclerViewItemClickListener, OnRecyclerViewItemLongClickListener {
    private RecyclerView recyclerView;
    private OrderQueryDao mOrderDao;
    private MyRecyclerAdapter mAdapter;
    private static ArrayList<OrderQuery> mDatas = new ArrayList<OrderQuery>();
    private LocalBroadcastManager broadcastManager;
    private static OrderQuery chosenOrder;

    public static Fragment3 newInstance(Context context, Bundle bundle) {
        Fragment3 newFragment = new Fragment3();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);
        initDatas();
        receiveRefreshBroadcast();
        mAdapter = new MyRecyclerAdapter(getActivity(), mDatas);
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

    private void initDatas() {
        mDatas.clear();
        mOrderDao = GreenDaoHelper.getDaoSession().getOrderQueryDao();
        List<OrderQuery> result = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.State.notEq("3")).orderDesc(OrderQueryDao.Properties.ToTop).list();
        mDatas.addAll(result);
    }

    /**
     * 注册广播接收器
     */
    private void receiveRefreshBroadcast() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StringUtils.refreshAction);
        MyLocalBroadCastReceiver mRerfreshReceiver = new MyLocalBroadCastReceiver();
        broadcastManager.registerReceiver(mRerfreshReceiver, intentFilter);
    }

    private class MyLocalBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refreshRecyclerView();
        }
    }

    private void refreshRecyclerView() {
        initDatas();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        chosenOrder = mDatas.get(position);
        Intent intent = new Intent();
        intent.setClass(getActivity(), TraceResultActivity.class);
        intent.putExtra("expCode", chosenOrder.getOrderCode());
        intent.putExtra("expName", chosenOrder.getOrderName());
        intent.putExtra("expNO", chosenOrder.getOrderNum().toString());
        //正常查询快递requestCode为100.从主界面直接点进去，为101
        startActivityForResult(intent, 101);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        chosenOrder = mDatas.get(position);
        String title = chosenOrder.getOrderName() + "  " + chosenOrder.getOrderNum();
        final String[] items = {"置顶", "删除", "修改备注", "复制单号"};

        if (chosenOrder.getToTop()) {
            items[0] = "取消置顶";
        }

        Dialog dialog = DialogUtils.createListDialog(getActivity(), title, items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handleLongClick(which);
                    }
                });
        dialog.show();
    }


    /***
     * 长按弹出窗口的点击事件
     *
     * @param which
     */
    private void handleLongClick(final int which) {
        long orderNum = chosenOrder.getOrderNum();
        final OrderQuery query = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.OrderNum.eq(orderNum)).unique();
        switch (which) {
            case 0:
                query.setToTop(!query.getToTop());
                mOrderDao.insertOrReplace(query);
                sendBroadCastToRefresh();
                break;
            case 1:
                mOrderDao.delete(query);
                sendBroadCastToRefresh();
                break;
            case 2:
                Dialog dialog = DialogUtils.createInputDialog(getActivity(), "输入备注", R.layout.dialog_input_remark, query.getRemark(), new AlertDialogListener() {
                    @Override
                    public void OnPositive(String text) {
                        chosenOrder.setRemark(text);
                        mOrderDao.insertOrReplace(query);
                        sendBroadCastToRefresh();
                    }
                });
                dialog.show();
                break;
            case 3:
                StringUtils.toCopy(String.valueOf(orderNum), getActivity());
                DialogUtils.ShowToast("复制成功");
                break;
            default:
                break;
        }
    }

    /****
     * 通知其他Fragment界面刷新列表
     */
    private void sendBroadCastToRefresh() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        Intent intent = new Intent(StringUtils.refreshAction);
        localBroadcastManager.sendBroadcast(intent);
    }

}
