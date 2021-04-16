package com.android.mdw.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Button;

public class Main extends Activity {
  Button btnSoundInicio;
  Button btnMusicInicio;
  Button btnFin;
  Intent in;
  UIReceiver brUI;
  HeadsetReceiver brHeadset;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    btnSoundInicio = (Button) findViewById(R.id.btnIniSound);
    btnMusicInicio = (Button) findViewById(R.id.btnIniMusic);
    btnFin = (Button) findViewById(R.id.btnStop);
    in = new Intent(this, MediaManagerService.class);
    brUI = new UIReceiver(in);
    brHeadset = new HeadsetReceiver(in);
    btnSoundInicio.setOnClickListener((view) -> {
      Intent in = new Intent("MEDIA_PLAY");

      in.putExtra("TYPE_MEDIA", MediaManagerService.SOUND);
      sendBroadcast(in);
    });
    btnMusicInicio.setOnClickListener((view) -> {
      Intent in = new Intent("MEDIA_PLAY");

      in.putExtra("TYPE_MEDIA", MediaManagerService.MUSIC);
      sendBroadcast(in);
    });
    btnFin.setOnClickListener((view) -> {
      Intent in = new Intent("MEDIA_STOP");

      sendBroadcast(in);
    });
    registerHeadsetReceiver();
    registerUIReceiver();

  }

  public void registerUIReceiver() {
    IntentFilter filter = new IntentFilter("MEDIA_PLAY");
    filter.addAction("MEDIA_STOP");
    this.registerReceiver(brUI, filter);
  }

  public void registerHeadsetReceiver() {
    IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    this.registerReceiver(brHeadset, filter);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(brUI);
    unregisterReceiver(brHeadset);
  }
}