package com.vackosar.searchbasedlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class WifiButton extends Colorful implements View.OnClickListener {
    final MainActivity mainActivity;
    final ColorService colorService = new ColorService();
    private final WifiManager wifiManager;
    private final TextView textView;
    BroadcastReceiver RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
            Log.d("DEBUG", "recieved Broadcast");
            if (wifiManager != null) {
                if (wifiManager.isWifiEnabled()) {
                    setActivatedColor();
                } else {
                    setNormalColor();
                }
            }
        }
    };

    public WifiButton(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        textView = (TextView) mainActivity.findViewById(R.id.wifiButton);
        wifiManager = (WifiManager) mainActivity.getSystemService(Context.WIFI_SERVICE);
        setVisibleIfAvailable();
        if (isAvailable()) {
            if(wifiManager.isWifiEnabled()) {
                colorService.setActive(textView);
            }
            else {
                colorService.setNormal(textView);
            }
        }
        textView.setOnClickListener(this);
        IntentFilter filter = new IntentFilter( );
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mainActivity.registerReceiver(RECEIVER, filter);
    }

    @Override
    public void onClick(View v) {
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
        }
    }

    private boolean isAvailable () {
        return wifiManager == null;
    }

    public void setVisibleIfAvailable () {
        if (wifiManager == null) {
            colorService.setInvisible(textView);
        } else {
            colorService.setVisible(textView);
        }
    }

    @Override
    public View getView() {
        return textView;
    }

    public void unregisterReceiver () {
        mainActivity.unregisterReceiver(RECEIVER);
    }
}