package com.chengshicheng.courierquery.Activity;

import android.content.Intent;
import android.os.Build;
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
import com.chengshicheng.courierquery.ResposeBean.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by chengshicheng on 2017/1/9.
 */
public class Fragment1 extends Fragment implements View.OnClickListener {

    private EditText intputCode;

    private ImageView sacnImage;

    private TextView searchButton;

    private static String number;


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
                    startActivity(intent);
                } else {
                    DialogUtils.ShowToast("别瞎几把点啊");
                }
                break;
            case R.id.scanImage:
                DialogUtils.ShowToast("paizhao");
                intent.putExtra("requestType", 2);
                startActivity(intent);
                break;
            default:efaut:
            break;

        }


    }

    /**
     * Fragment跳转到扫描二维码界面
     * https://github.com/journeyapps/zxing-android-embedded
     */
    private void toCustomScanActivity() {
//         zxinging自带的扫描界面
//        IntentIntegrator.forSupportFragment(Fragment1.this).initiateScan();

        //重定义zxing的扫描界面 CustomScanActivity
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(Fragment1.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("");
        integrator.setCaptureActivity(CustomScanActivity.class);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
