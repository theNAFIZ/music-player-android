package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView name, description, startTimer, stopTimer;
    private Bundle extras;
    private Thread thread;

    // TODO: pass dynamic files to play

    private MediaPlayer mediaPlayer;
    private Button prevButton;
    private Button playButton;
    private Button nextButton;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = (TextView) findViewById(R.id.dTitleID);
        description = (TextView) findViewById(R.id.dDescriptionID);

        extras = getIntent().getExtras();

        name.setText(extras.getString("name"));
        description.setText(extras.getString("description"));

        mediaPlayer = new MediaPlayer();
        mediaPlayer =  MediaPlayer.create(getApplicationContext(), R.raw.whisle);
        int duration = mediaPlayer.getDuration();

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        startTimer = (TextView) findViewById(R.id.start_timer);
        stopTimer = (TextView) findViewById(R.id.end_timer);

        prevButton = (Button) findViewById(R.id.prevBtn);
        playButton = (Button) findViewById(R.id.playBtn);
        nextButton = (Button) findViewById(R.id.nextBtn);

        stopTimer.setText(new SimpleDateFormat("mm:ss").format(duration));
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                int currentPosition = mediaPlayer.getCurrentPosition();

                startTimer.setText(dateFormat.format(new Date(currentPosition)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
            }
        });

        playButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            playButton.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            updateThread();
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.release();
            mediaPlayer = null;

            thread.interrupt();
            thread = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int progress = mediaPlayer.getCurrentPosition();
        switch (view.getId()) {
            case R.id.prevBtn:
                if(mediaPlayer != null) {
                    progress-=5000;
                    progress = Math.max(progress, 0);
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
                break;

            case R.id.playBtn:
                if (mediaPlayer.isPlaying()) {
                    // pause music
                    pauseMusic();
                } else {
                    // play music
                    playMusic();
                }
                break;

            case R.id.nextBtn:
                if(mediaPlayer != null) {
                    progress+=5000;
                    progress = Math.min(progress, mediaPlayer.getDuration());
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
                break;
        }
    }

    public void updateThread() {
        thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int newPosition = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(newPosition);

                                startTimer.setText(String.valueOf(new SimpleDateFormat("mm:ss").format(new Date(newPosition))));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}