package com.chengshicheng.courierquery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengshicheng.courierquery.DialogUtils;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by chengshicheng on 2017/1/9.
 */
public class Fragment1 extends Fragment implements View.OnClickListener {

    private EditText intputCode;

    private ImageView sacnImage;

    private TextView searchButton;

    private static String number;

    private int requestCode = 100;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);

        intputCode = (EditText) view.findViewById(R.id.inputCode);
        sacnImage = (ImageView) view.findViewById(R.id.scanImage);
        searchButton = (TextView) view.findViewById(R.id.searchButton);

        sacnImage.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        number = intputCode.getText().toString();
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), ChooseCompanyActivity.class);

        switch (v.getId()) {
            case R.id.searchButton:
                if (!StringUtils.isEmpty(number)) {
                    intent.putExtra("requestType", 1);
                    intent.putExtra("requestNumber", number);
                    startActivityForResult(intent, requestCode);
                } else {
                    DialogUtils.ShowToast("请输入单号");
                }
                break;
            case R.id.scanImage:
                intent.putExtra("requestType", 2);
                startActivityForResult(intent, requestCode);
                break;
            default:
                break;

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
