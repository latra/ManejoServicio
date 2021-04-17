package com.android.mdw.demo;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class MediaManagerService extends Service {
    public final static int STOP = -1;

    public final static int MUSIC = 0;
    public final static int SOUND = 1;
    public static final int CUSTOM = 2;
    private MediaPlayer playerTrain;
    private MediaPlayer playerMusic;
    private MediaPlayer playerCustom;

    public void setPlayerCustom(String uri) {
        Uri myUri = Uri.parse(uri);

        playerCustom = new MediaPlayer();
        try {
            playerCustom.setDataSource(getApplicationContext(), Uri.parse(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            playerCustom.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {

        Toast.makeText(this, R.string.creaserv, Toast.LENGTH_LONG).show();
        playerTrain = MediaPlayer.create(this, R.raw.train);
        playerMusic = MediaPlayer.create(this, R.raw.music);
        playerTrain.setLooping(true);
        playerMusic.setLooping(true);
    }

    @Override
    public void onDestroy() {
        Toast.makeText( this, R.string.finaserv, Toast.LENGTH_LONG).show();
        playerTrain.stop();
        playerMusic.stop();
        playerCustom.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        switch (intent.getIntExtra("TYPE_MEDIA", 0)) {
            case MUSIC:
                Toast.makeText(this, R.string.initmusicsrv, Toast.LENGTH_LONG).show();
                playerMusic.start();
                break;
            case SOUND:
                Toast.makeText(this, R.string.initsoundsrv, Toast.LENGTH_LONG).show();
                playerTrain.start();
                break;
            case CUSTOM:
                setPlayerCustom(intent.getStringExtra("URI"));

                Toast.makeText(this, R.string.initcustomsrv, Toast.LENGTH_LONG).show();
                playerCustom.start();
        }
        return startid;
    }

}
