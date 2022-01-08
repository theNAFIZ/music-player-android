package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
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
    private Button playButton;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = (TextView) findViewById(R.id.dNameID);
        description = (TextView) findViewById(R.id.dDescriptionID);

        extras = getIntent().getExtras();

        name.setText(extras.getString("name"));
        description.setText(extras.getString("description"));

        mediaPlayer = new MediaPlayer();
        mediaPlayer =  MediaPlayer.create(getApplicationContext(), R.raw.whisle);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        startTimer = (TextView) findViewById(R.id.start_timer);
        stopTimer = (TextView) findViewById(R.id.end_timer);
        playButton = (Button) findViewById(R.id.playBtn);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        stopTimer.setText(String.format("0:%s", String.valueOf(mediaPlayer.getDuration() / 1000)));

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    // pause music
                    pauseMusic();
                    playButton.setText("Play");

                } else {
                    // play music
                    playMusic();
                    playButton.setText("Pause");


                }
            }
        });

    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
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