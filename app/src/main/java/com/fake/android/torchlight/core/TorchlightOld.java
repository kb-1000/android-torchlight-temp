package com.fake.android.torchlight.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@SuppressLint("deprecation")
class TorchlightOld extends Torchlight {
    private Camera mCamera;
    private Camera.Parameters mCameraParams;

    @Override
    public void init(Context context) {
        this.context = context;
        try {
            mCamera = Camera.open();
            mCameraParams = mCamera.getParameters();
        } catch (RuntimeException e) {
            TorchlightControl.noFlash();
            Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void _release() {
        try {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
                mCameraParams = null;
            }
        } catch (RuntimeException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void _set(boolean enable) {
        if (mCamera == null) {
            return;
        }
        if (enable) {
            mCameraParams.setFlashMode("torch");
        } else {
            mCameraParams.setFlashMode("off");
        }
        try {
            mCamera.setParameters(mCameraParams);
        } catch (RuntimeException e) {
            if (e.getMessage().startsWith("set")) {
                TorchlightControl.noFlash();
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            mCamera.stopPreview();
            return;
        }
        if (enable) {
            mCamera.startPreview();
        } else {
            mCamera.stopPreview();
        }
    }
}
