package com.ultitrust.karico;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ultitrust.karico.Adapter.KaricoPlayerEn;
import com.ultitrust.karico.Model.ColorRandomizer;
import com.ultitrust.karico.Model.MusicState;

import java.io.IOException;
import java.util.List;

public class KaricoMotorSkillTwo extends AppCompatActivity {

    private ImageButton skillstwobackbtn, reverseBtn, playBtn, forwardBtn;
    private KaricoPlayerEn karicoPlayerEn;
    private Uri musicPathUri;
    private boolean isRotated = false;
    private TextView musictext;
    private ColorRandomizer colorRandomizer;
    private String musicTitle;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_motor_skill_two);

        skillstwobackbtn = findViewById(R.id.skillstwobackbtn);
        reverseBtn = findViewById(R.id.reverseBtn);
        playBtn = findViewById(R.id.playBtn);
        forwardBtn = findViewById(R.id.forwardBtn);
        musictext = findViewById(R.id.musictext);
        karicoPlayerEn = new KaricoPlayerEn(this);
        String [] colorspectrum = {"#66ff66", "#cc00cc", "#ff9933", "#007acc", "#5200cc",
                "#660066", "#86592d", "#cc3300", "#e69900", "#adad85", "#2e2e1f",
                "#00ff00", "#004d00", "#000080", "#9999ff", "#ff33ff"};
        colorRandomizer = new ColorRandomizer();
        List<Integer> genNumbers = colorRandomizer.colorRandomizer();

        if (genNumbers != null){
            reverseBtn.setColorFilter(Color.WHITE);
            reverseBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorspectrum[genNumbers.get(0)])));
            playBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorspectrum[genNumbers.get(1)])));
            playBtn.setColorFilter(Color.WHITE);
            forwardBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorspectrum[genNumbers.get(2)])));
            forwardBtn.setColorFilter(Color.WHITE);
        }

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
//                Log.i("Karico", "Music Loaded ");
                musictext.setText("Music Loaded ");
            }
        });

        thread.start();


        skillstwobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (karicoPlayerEn.getMediaPlayer() != null) {
                    if (karicoPlayerEn.getMediaPlayer().isPlaying()) {
                        karicoPlayerEn.stopMusic();
                        musictext.setText(" ");
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
                        if(karicoPlayerEn.playMusic(playBtn, isRotated)) {
                            musicTitle = colorRandomizer.getMusicName(KaricoMotorSkillTwo.this, karicoPlayerEn.getUripaths());
                            Log.i("Karico", "track info " + karicoPlayerEn.getUripaths());
                            musictext.setText(musicTitle);
                        } else {
                            musictext.setText(" ");
                        }
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
                    musicTitle = colorRandomizer.getMusicName(KaricoMotorSkillTwo.this, karicoPlayerEn.getUripaths());
                    musictext.setText(musicTitle);
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
                    musicTitle = colorRandomizer.getMusicName(KaricoMotorSkillTwo.this, karicoPlayerEn.getUripaths());
                    musictext.setText(musicTitle);
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
            musictext.setText(" ");

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
            musictext.setText(" ");

        }
    }
}
