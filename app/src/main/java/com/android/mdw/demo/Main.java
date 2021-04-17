package com.android.mdw.demo;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.widget.Button;

import java.util.Objects;

public class Main extends Activity {

    Intent in;
    UIReceiver brUI;
    HeadsetReceiver brHeadset;
    SharedPreferences sharedPreferences;
    Button btnPlayCustom;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        checkPermission();

        sharedPreferences = getSharedPreferences("mypreferences", 0);

        Button btnSoundInicio = findViewById(R.id.btnIniSound);
        Button btnMusicInicio = findViewById(R.id.btnIniMusic);
        Button btnFin = findViewById(R.id.btnStop);
        Button btnSelect = findViewById(R.id.btnSelect);
        btnPlayCustom = findViewById(R.id.btnPlaySelected);


        in = new Intent(this, MediaManagerService.class);
        brUI = new UIReceiver(in);
        brHeadset = new HeadsetReceiver(in);


        btnSelect.setOnClickListener((view) -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/mpeg");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_audio_file_title)), 1);
        });

        btnSoundInicio.setOnClickListener((view) -> sendBroadcast(MediaManagerService.SOUND));

        btnMusicInicio.setOnClickListener((view) -> sendBroadcast(MediaManagerService.MUSIC));

        btnFin.setOnClickListener((view) -> sendBroadcast(new Intent("MEDIA_STOP")));

        btnPlayCustom.setOnClickListener((view) -> startServiceCustom(sharedPreferences.getString("URI", "")));

        registerHeadsetReceiver();
        registerUIReceiver();

    }

    private void sendBroadcast(int mediaType) {
        Intent in = new Intent("MEDIA_PLAY");
        in.putExtra("TYPE_MEDIA", mediaType);
        sendBroadcast(in);
    }

    private void startServiceCustom(String uri) {


        stopService(in);
        in.putExtra("TYPE_MEDIA", MediaManagerService.CUSTOM);
        in.putExtra("URI", uri);
        startService(in);
    }

    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if ((data != null) && (data.getData() != null)) {
                Uri audioFileUri = data.getData();
                startServiceCustom(audioFileUri.toString());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("URI", audioFileUri.toString());
                editor.putString("NAME", getFileName(audioFileUri));
                editor.apply();
                btnPlayCustom.setText(sharedPreferences.getString("NAME", ""));

            }
        }
    }

    // CREDITS TO https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
    public String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                assert cursor != null;
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            assert result != null;
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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