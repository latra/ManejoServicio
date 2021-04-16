package com.android.mdw.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;

public class Main extends Activity {
  Button btnSoundInicio;
  Button btnMusicInicio;
  Button btnFin;
  BroadcastReceiver br = new UIReciever();


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    btnSoundInicio = (Button) findViewById(R.id.btnIniSound);
    btnMusicInicio = (Button) findViewById(R.id.btnIniMusic);
    btnFin = (Button) findViewById(R.id.btnStop);

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

    registerReceiver();

  }

  public void registerReceiver() {
    IntentFilter filter = new IntentFilter("MEDIA_PLAY");
    filter.addAction("MEDIA_STOP");
    this.registerReceiver(br, filter);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(br);
  }
}