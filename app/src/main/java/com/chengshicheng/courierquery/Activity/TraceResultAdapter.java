package com.chengshicheng.courierquery.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chengshicheng.courierquery.Utils.DensityUtils;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.ResposeBean.OrderTrace;

import java.util.ArrayList;

/**
 * 物流查询结果页面适配器
 * Created by chengshicheng on 2017/1/21.
 */
public class TraceResultAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrderTrace> traceList;

    public TraceResultAdapter(Context context, ArrayList<OrderTrace> traces) {
        this.context = context;
        this.traceList = traces;
    }

    @Override
    public int getCount() {
        return traceList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public OrderTrace getItem(int position) {
        return traceList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final OrderTrace trace = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trace, parent, false);
            holder.tvAcceptTime = (TextView) convertView.findViewById(R.id.tvAcceptTime);
            holder.tvAcceptStation = (TextView) convertView.findViewById(R.id.tvAcceptStation);
            holder.tvTopLine = (TextView) convertView.findViewById(R.id.tvTopLine);
            holder.tvDot = (TextView) convertView.findViewById(R.id.tvDot);
            convertView.setTag(holder);
        }

        if (position == 0) {
            // 第一行头的竖线不显示
            holder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色蓝色
            holder.tvAcceptTime.setTextColor(context.getResources().getColor(R.color.tabBlue));
            holder.tvAcceptStation.setTextColor(context.getResources().getColor(R.color.tabBlue));
            //第一行圆点设置大一点
            holder.tvDot.getLayoutParams().width = DensityUtils.dp2px(context, 10);
            holder.tvDot.getLayoutParams().height = DensityUtils.dp2px(context, 10);
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
        } else if (position != 0) {
            holder.tvTopLine.setVisibility(View.VISIBLE);
            holder.tvAcceptTime.setTextColor(context.getResources().getColor(R.color.greyText));
            holder.tvAcceptStation.setTextColor(context.getResources().getColor(R.color.greyText));
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }

        holder.tvAcceptTime.setText(trace.getAcceptTime());
        holder.tvAcceptStation.setText(trace.getAcceptStation().replace("到达：", ""));
        return convertView;
    }

    static class ViewHolder {
        public TextView tvAcceptTime, tvAcceptStation;
        public TextView tvTopLine, tvDot;
    }
}
