package com.franckysolo.plugins.capacitor;

import android.Manifest;
import android.content.Intent;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin(
        requestCodes={
                MlkitBarcodescanner.REQUEST_SCAN_PERMISSION,
                MlkitBarcodescanner.REQUEST_CODE,
        },
        permissions={
            Manifest.permission.CAMERA
        }
)
public class MlkitBarcodescanner extends Plugin {

    private static final String TAG = "Barcodescanner";

    static final int REQUEST_SCAN_PERMISSION = 1000;

    static final int REQUEST_CODE = 1050;

    @PluginMethod
    public void scanBarcode(PluginCall call) {
       if (hasRequiredPermissions()) {
           saveCall(call);
           Intent intent = new Intent(getContext(), ScanActivity.class);
           startActivityForResult(call, intent, REQUEST_CODE);
       } else {
           pluginRequestPermissions(new String[]{
                   Manifest.permission.CAMERA,
           }, REQUEST_SCAN_PERMISSION);
       }
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        // Get the previously saved call
        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            return;
        }

        Log.i(TAG, "handleOnActivityResult requestCode " + requestCode);
        Log.i(TAG, "handleOnActivityResult resultCode " + resultCode);
        Log.i(TAG, "handleOnActivityResult " + data);

        if (requestCode == REQUEST_CODE) {
            // Do something with the data
            if (data.hasExtra("code")) {
                JSObject ret = new JSObject();
                Log.i(TAG, "getStringExtra " + data.getStringExtra("code"));

                ret.put("code", data.getStringExtra("code"));
                savedCall.success(ret);
            }
        }
    }
}
