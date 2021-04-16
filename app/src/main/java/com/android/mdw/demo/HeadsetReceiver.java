package com.android.mdw.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class HeadsetReceiver extends BroadcastReceiver {

    private Intent in;

    public HeadsetReceiver(Intent in) {
        this.in = in;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction() == Intent.ACTION_HEADSET_PLUG)
            switch (intent.getIntExtra("state", -1)) {
                case 0:
                    Toast.makeText(context, R.string.headsetout, Toast.LENGTH_LONG).show();

                    context.stopService(in);
                    break;
                case 1:
                    Toast.makeText(context, R.string.headsetin, Toast.LENGTH_LONG).show();
                    in.putExtra("TYPE_MEDIA", MediaManagerService.MUSIC);
                    context.startService(in);
                    break;
            }
    }
}