package com.canto.lucio.canto;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;

/**
 * Created by lucio on 12/10/2016.
 */

public class MediaService extends Service{

    private static final String TAG = "AudioPlayer";
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.canto_femea_coleiro);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    public void onStart(Intent intent, int startId) {
// Perform your long running operations here.
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }


}
