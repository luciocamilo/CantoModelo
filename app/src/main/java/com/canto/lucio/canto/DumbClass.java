package com.canto.lucio.canto;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
/**
 * Created by lucio on 12/10/2016.
 */

public class DumbClass extends Activity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl{




   // public class MainActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl{
        private static final String TAG = "AudioPlayer";

        public static final String AUDIO_FILE_NAME = "Canto de Fêmea para Esquentar Coleiro";

        private MediaPlayer mediaPlayer;
        private MediaController mediaController;
        private String audioFile;

        private Handler handler = new Handler();

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            audioFile = this.getIntent().getStringExtra(AUDIO_FILE_NAME);
            ((TextView)findViewById(R.id.now_playing_text)).setText(audioFile);

            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.canto_femea_coleiro);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setLooping(true);
            mediaController = new MediaController(this){
                @Override
                public void hide() {
                }
            };


            mediaPlayer.start();
            mediaController.show();

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
            mediaController.show();
            return false;
        }

        //--MediaPlayerControl methods----------------------------------------------------
        public void start() {
            mediaPlayer.start();
        }

        public void pause() {
            if(mediaPlayer!=null){
                mediaPlayer.pause();
                mediaPlayer.stop();
                finish();
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
            Log.d(TAG, "onPrepared");
            mediaController.setMediaPlayer(this);
            mediaController.setAnchorView(findViewById(R.id.main_audio_view));

            handler.post(new Runnable() {
                public void run() {
                    mediaController.setEnabled(true);
                    mediaController.show();
                }
            });
        }

    }
