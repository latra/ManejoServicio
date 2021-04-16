package com.android.mdw.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UIReceiver extends BroadcastReceiver {
    private Intent in;
    public  UIReceiver(Intent in){
        this.in = in;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("MEDIA_STOP"))
            context.stopService(in);
        else {
            in.putExtra("TYPE_MEDIA", intent.getIntExtra("TYPE_MEDIA", MediaManagerService.STOP));
            context.startService(in);
        }
    }
}