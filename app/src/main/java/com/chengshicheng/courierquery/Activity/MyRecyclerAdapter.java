package com.chengshicheng.courierquery.Activity;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengshicheng.courierquery.CourierApp;
import com.chengshicheng.courierquery.GreenDao.OrderQuery;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.ResposeBean.OrderTrace;
import com.chengshicheng.courierquery.Utils.DialogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by chengshicheng on 2017/2/12.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<OrderQuery> mDatas = new ArrayList<OrderQuery>();
    private static final ArrayList<Integer> colorList = new ArrayList<Integer>(Arrays.asList(0XFF536dfe, 0xFF8BC34A, 0xFFFF9800, 0xFFF44336, 0xFF607D8B));
    private OnRecyclerViewItemClickListener mClickListener;
    private OnRecyclerViewItemLongClickListener mLongClickListener;

    public MyRecyclerAdapter(Context context, ArrayList<OrderQuery> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener onItemLongClickListener) {
        this.mLongClickListener = onItemLongClickListener;
    }


    @Override
    public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);

        MyRecyclerAdapter.MyViewHolder holder = new MyRecyclerAdapter.MyViewHolder(itemView);
        GradientDrawable bgShape = (GradientDrawable) holder.tvDot.getBackground();
        bgShape.setColor(0xFF009688);


        //随机背景颜色
//        Random rm = new Random();
//        int i = rm.nextInt(colorList.size());
//        bgShape.setColor(colorList.get(i));

        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        OrderQuery order = mDatas.get(position);

        holder.tvDot.setText(order.getOrderName().substring(0, 1));
        holder.tvRemark.setText(order.getRemark());
        if (TextUtils.isEmpty(order.getRemark())) {
            holder.tvRemark.setVisibility(View.GONE);
            holder.tvNameNum.setTextSize(16);
        } else {
            holder.tvRemark.setTextSize(16);
            holder.tvNameNum.setTextSize(12);
            holder.tvNameNum.setTextColor(mContext.getResources().getColor(R.color.greyText));
        }

        holder.tvNameNum.setText(order.getOrderName() + "  " + order.getOrderNum());
        String detail = getLatestTrace(order);
        if (TextUtils.isEmpty(detail)) {
            holder.tvDetail.setVisibility(View.GONE);
        } else {
            holder.tvDetail.setText(detail);
        }
        holder.tvState.setText(getState(order));
        final String time = getTime(order);
        if (TextUtils.isEmpty(time)) {
            holder.tvTime.setVisibility(View.GONE);
        }
        holder.tvTime.setText(time);

        //判断是否设置了监听器
        if (mClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mClickListener.onItemClick(holder.itemView, position);
                }
            });
        }        //判断是否设置了监听器
        if (mLongClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mLongClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnRecyclerViewItemClickListener mClickListener;
        TextView tvDot, tvNameNum, tvRemark, tvDetail, tvState, tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDot = (TextView) itemView.findViewById(R.id.tvDot);
            tvRemark = (TextView) itemView.findViewById(R.id.tvRemark);
            tvNameNum = (TextView) itemView.findViewById(R.id.tvNameNum);
            tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
            tvState = (TextView) itemView.findViewById(R.id.tvState);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }

        }
    }

    private String getTime(OrderQuery order) {
        ArrayList<OrderTrace> traces = new Gson().fromJson(order.getTraces2Json(), new TypeToken<List<OrderTrace>>() {
        }.getType());
        if (traces.isEmpty()) {
            return "";
        }
        return traces.get(traces.size() - 1).getAcceptTime().substring(6, 10);
    }

    private String getState(OrderQuery order) {
        String state = "";
        switch (order.getState()) {
            case "0":
                state = "未知";
                break;
            case "2":
                state = "在途中";
                break;
            case "3":
                state = "已签收";
                break;
            default:
                state = "未知";
        }
        return state;
    }

    private String getLatestTrace(OrderQuery order) {
        ArrayList<OrderTrace> traces = new Gson().fromJson(order.getTraces2Json(), new TypeToken<List<OrderTrace>>() {
        }.getType());
        if (traces.isEmpty()) {
            return "";
        }
        return traces.get(traces.size() - 1).getAcceptStation();
    }
}
