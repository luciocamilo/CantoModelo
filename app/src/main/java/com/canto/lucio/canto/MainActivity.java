package com.canto.lucio.canto;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl{
    private static final String TAG = "AudioPlayer";

    public static final String AUDIO_FILE_NAME = "Canto de Fêmea para Esquentar Coleiro";

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String audioFile;
    private ToggleButton btnBackground;
    private Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        audioFile = this.getIntent().getStringExtra(AUDIO_FILE_NAME);
        ((TextView)findViewById(R.id.now_playing_text)).setText(audioFile);
        btnBackground = (ToggleButton) findViewById(R.id.toggleButton2);
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnBackground.isChecked()) {
                    mediaPlayer.stop();
                    mediaController.hide();
                    if(!isMyServiceRunning())
                        startService(new Intent(MainActivity.this, MediaService.class));
                }else{
                    stopService(new Intent(MainActivity.this, MediaService.class));
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.canto_femea_coleiro);
                    mediaPlayer.setOnPreparedListener(MainActivity.this);
                    mediaPlayer.setLooping(true);
                    mediaController = new MediaController(MainActivity.this);
                    mediaPlayer.start();
                    mediaController.show(0);
                }
            }
        });
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.canto_femea_coleiro);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setLooping(true);
        mediaController = new MediaController(this);

        if(!isMyServiceRunning()) {
            mediaPlayer.start();
            mediaController.show(0);
        }else{
            btnBackground.setChecked(true);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                finish();
            }
            mediaController.hide();
            mediaPlayer.release();
            mediaPlayer = null;
            finish();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        mediaController.show(0);
        return false;
    }

    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        if(mediaPlayer!=null){
            mediaPlayer.pause();
        }
        super.onPause();
    }

    public int getDuration() {
        if(mediaPlayer!=null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if(mediaPlayer!=null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        if(mediaPlayer!=null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
    //--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        if (!isMyServiceRunning()) {
            Log.d(TAG, "onPrepared");
            mediaController.setMediaPlayer(this);
            mediaController.setAnchorView(findViewById(R.id.main_audio_view));

            handler.post(new Runnable() {
                public void run() {
                    mediaController.setEnabled(true);
                    mediaController.show(0);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja Finalizar a Aplicação?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MediaService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        int i = 0;
        super.onDestroy();
    }
}