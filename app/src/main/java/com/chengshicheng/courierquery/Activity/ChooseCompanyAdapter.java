package com.chengshicheng.courierquery.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.ResposeBean.ShipperBean;

import java.util.ArrayList;

/**
 * Created by chengshicheng on 2017/1/16.
 */
public class ChooseCompanyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ShipperBean> shipperBean = new ArrayList<ShipperBean>();

    public ChooseCompanyAdapter(Context context, ArrayList<ShipperBean> shippers) {
        this.mInflater = LayoutInflater.from(context);
        this.shipperBean = shippers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.choose_company_item,
                    null);
            holder = new ViewHolder();
            holder.comanyImage = (ImageView) convertView.findViewById(R.id.comanyImage);
            holder.comanyText = (TextView) convertView.findViewById(R.id.comanyText);
            holder.rightImage = (ImageView) convertView.findViewById(R.id.rightImage);
            holder.comanyText.setText(shipperBean.get(position).getShipperName());
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
        holder.comanyImage.setImageResource(R.drawable.aap);
        holder.comanyText.setText(shipperBean.get(position).getShipperName());
        holder.rightImage.setImageResource(R.drawable.a7o);


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (shipperBean == null) {
            return 0;
        } else {
            return shipperBean.size();
        }
    }

    public class ViewHolder {
        public ImageView comanyImage;
        public TextView comanyText;
        public ImageView rightImage;
    }
}
