package com.chengshicheng.courierquery.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chengshicheng.courierquery.CourierApp;
import com.chengshicheng.courierquery.Utils.DialogUtils;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.Utils.StringUtils;
import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.ArrayList;

/**
 * Created by chengshicheng on 2017/1/9.
 */
public class Fragment1 extends Fragment {
    private RecyclerView recyclerView;
    private MyRecyclerAdapter mAdapter;
    private TextView tvDot;
    private ArrayList<String> mDatas = new ArrayList<String>();

    public static Fragment1 newInstance(Context context, Bundle bundle) {
        Fragment1 newFragment = new Fragment1();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);


        mDatas.clear();
        for (int i = 0; i < 100; i++) {
            mDatas.add(i + "");
        }
        mAdapter = new MyRecyclerAdapter();

        recyclerView = (RecyclerView) view.findViewById(R.id.lv_fragment1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CourierApp.getContext());
        recyclerView.addItemDecoration(new RecycleViewDivider(
                CourierApp.getContext(), LinearLayoutManager.HORIZONTAL, 30, getResources().getColor(R.color.listviewbg)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        return view;
    }


    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    CourierApp.getContext()).inflate(R.layout.item_home, parent,
                    false));

            RandomColor randomColor = new RandomColor();
            int color = randomColor.randomColor();

            double d = Math.random() * 3;
            GradientDrawable bgShape = (GradientDrawable) holder.tvDot.getBackground();
            bgShape.setColor(color);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position));


        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            TextView tvDot;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.textview);
                tvDot = (TextView) view.findViewById(R.id.tvDot);
            }
        }
    }


}
