package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView name, description, startTimer, stopTimer;
    private Bundle extras;

    // TODO: pass dynamic files to play
    // TODO: design the player layout properly

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

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        startTimer = (TextView) findViewById(R.id.start_timer);
        stopTimer = (TextView) findViewById(R.id.end_timer);

        prevButton = (Button) findViewById(R.id.prevBtn);
        playButton = (Button) findViewById(R.id.playBtn);
        nextButton = (Button) findViewById(R.id.nextBtn);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                startTimer.setText(String.format("0:", String.valueOf(seekBar.getProgress() / 1000)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        stopTimer.setText(String.format("0:%s", String.valueOf(mediaPlayer.getDuration() / 1000)));

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    // pause music
                    pauseMusic();
                } else {
                    // play music
                    playMusic();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = seekBar.getProgress();
                if(mediaPlayer != null) {
                    progress-=5000;
                    progress = Math.max(progress, 0);
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = seekBar.getProgress();
                if(mediaPlayer != null) {
                    progress+=5000;
                    progress = Math.min(progress, mediaPlayer.getDuration());
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }
        });

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
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}