package com.android.mdw.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UIReciever extends BroadcastReceiver {
    private Intent in;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (in == null)
            in = new Intent(context, MediaManagerService.class);
        if (intent.getAction().equals("MEDIA_STOP"))
            context.stopService(in);
        else {
            in.putExtra("TYPE_MEDIA", intent.getIntExtra("TYPE_MEDIA", MediaManagerService.STOP));
            context.startService(in);
        }
    }
}