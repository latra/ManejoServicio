package com.android.mdw.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Main extends Activity {
  private Intent soundService;
  private Intent musicService;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button btnSound =  findViewById(R.id.btnSound);
    Button btnMusic =  findViewById(R.id.btnMusic);
    Button btnStop =
            findViewById(R.id.btnStop);

    btnSound.setOnClickListener((listener) -> startService(soundService));
    btnMusic.setOnClickListener((listener) -> startService(musicService));
    btnStop.setOnClickListener((listener) -> {
      stopService(soundService);
      stopService(musicService);
    });
    soundService = new Intent(this, SoundService.class);
    musicService = new Intent(this, MusicService.class);
  }
}