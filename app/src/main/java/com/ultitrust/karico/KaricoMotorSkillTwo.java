package com.ultitrust.karico;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ultitrust.karico.Adapter.KaricoPlayerEn;
import com.ultitrust.karico.Model.MusicState;

import java.io.IOException;

public class KaricoMotorSkillTwo extends AppCompatActivity {

    private ImageButton skillstwobackbtn, reverseBtn, playBtn, forwardBtn;
    private KaricoPlayerEn karicoPlayerEn;
    private Uri musicPathUri;
    private boolean isRotated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_motor_skill_two);

        skillstwobackbtn = findViewById(R.id.skillstwobackbtn);
        reverseBtn = findViewById(R.id.reverseBtn);
        playBtn = findViewById(R.id.playBtn);
        forwardBtn = findViewById(R.id.forwardBtn);
        karicoPlayerEn = new KaricoPlayerEn(this);

        playBtn.setTag(R.drawable.icons_play);


        Intent musicFolderIntent = getIntent();
        String musicPathStringUri = musicFolderIntent.getStringExtra("primaryMotorFolderPath");
        musicPathUri = Uri.parse(Uri.decode(musicPathStringUri));

        if (musicPathUri != null) {
            karicoPlayerEn.setUripaths(musicPathUri);
        }


        if (savedInstanceState != null){
            isRotated = savedInstanceState.getBoolean("isRotated");
            MusicState musicState = (MusicState) savedInstanceState.getSerializable("musicSaved");
            if (musicState != null) {
                karicoPlayerEn.setMusicState(musicState);
            }
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                karicoPlayerEn.musicLoading();
                Log.i("Karico", "Music Loaded ");
            }
        });

        thread.start();


        skillstwobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (karicoPlayerEn.getMediaPlayer() != null) {
                    if (karicoPlayerEn.getMediaPlayer().isPlaying()) {
                        karicoPlayerEn.stopMusic();
                        finish();
                    } else {
                        Log.i("Karico", "check something went wrong");
                        finish();
                    }
                } else {
                    Log.i("Karico", "check something went wrong");
                    finish();
                }
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int musiclistsize = karicoPlayerEn.getMusicList().size();
                if (musiclistsize != 0) {
                    try {
                        karicoPlayerEn.playMusic(playBtn, isRotated);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(KaricoMotorSkillTwo.this, "Loading music", Toast.LENGTH_LONG).show();
                }
            }
        });


        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    karicoPlayerEn.forwardMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        reverseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    karicoPlayerEn.reverseMusic();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (karicoPlayerEn.getMediaPlayer().isPlaying()){
            karicoPlayerEn.stopMusic();
            playBtn.setImageResource(R.drawable.icons_play);
            playBtn.setTag(R.drawable.icons_play);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (karicoPlayerEn.getMediaPlayer().isPlaying()) {
            MusicState musicState = new MusicState(0, karicoPlayerEn.getMusicList().get(karicoPlayerEn.getPosition()), karicoPlayerEn.getMediaPlayer().getCurrentPosition(), karicoPlayerEn.getMediaPlayer().getDuration());
            outState.putBoolean("isRotated", true);
            outState.putSerializable("musicSaved", musicState);
            Log.i("Karico", "saved");
        } else {
            Log.i("Karico", "not saved");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (karicoPlayerEn.getMediaPlayer().isPlaying()){
            playBtn.setImageResource(R.drawable.icons_play);
            playBtn.setTag(R.drawable.icons_play);
            karicoPlayerEn.stopMusic();
        }
    }
}
