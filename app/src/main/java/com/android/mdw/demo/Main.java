package com.android.mdw.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
  private Intent in;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button btnInicio = (Button) findViewById(R.id.btnInicio);
    Button btnFin = (Button) findViewById(R.id.btnFin);

    btnInicio.setOnClickListener((listener) -> startService(in));
    btnFin.setOnClickListener((listener) -> stopService(in));

    in = new Intent(this, ElServicio.class);
  }
}