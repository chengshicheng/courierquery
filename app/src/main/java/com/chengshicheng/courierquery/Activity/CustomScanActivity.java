package com.chengshicheng.courierquery.Activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chengshicheng.courierquery.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Created by chengshicheng on 2017/1/11.
 */

public class CustomScanActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener {
/*    @BindView(R.id.btn_switch) Button swichLight;
    @BindView(R.id.btn_hint1) Button hint1Show;
    @BindView(R.id.btn_hint2) Button hint2Show;
    @BindView(R.id.dbv_custom) DecoratedBarcodeView mDBV;*/

    private ImageView flash_switch;
    private DecoratedBarcodeView mDBV;
    private CaptureManager captureManager;
    private boolean isLightOn = false;

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scan);
        flash_switch = (ImageView) findViewById(R.id.btn_switch);
        mDBV = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);
        flash_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOn) {
                    mDBV.setTorchOff();
                    mDBV.setBackgroundResource(R.drawable.scan_qrcode_flash_light_off);
                } else {
                    mDBV.setTorchOn();
                    mDBV.setBackgroundResource(R.drawable.scan_qrcode_flash_light_on);
                }
            }
        });
        mDBV.setTorchListener(this);

        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

    }

    @Override
    public void onTorchOff() {
        isLightOn = false;
    }

    @Override
    public void onTorchOn() {
        isLightOn = true;

    }
}
