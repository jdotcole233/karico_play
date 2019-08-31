package com.ultitrust.karico;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ultitrust.karico.Model.MusicModel;

import java.io.IOException;
import java.util.ArrayList;

public class KaricoMotorPlayer extends AppCompatActivity {

    private ImageButton playerBackBtn, upperLeftPlayerBtn, upperRightPlayerBtn, lowerLeftPlayerBtn, lowerRightPlayerBtn, centerPlayerBtn;
    private ArrayList<Uri> musicList;
    private MediaPlayer mediaPlayer;
    private Uri musicPathUri;
    private Integer position;
    private boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_motor_player);

        playerBackBtn = findViewById(R.id.playerbackbtn);
        upperLeftPlayerBtn = findViewById(R.id.upperleftplayerButton);
        upperRightPlayerBtn = findViewById(R.id.upperrightplayerButton);
        lowerLeftPlayerBtn = findViewById(R.id.lowerleftplayerButton);
        lowerRightPlayerBtn = findViewById(R.id.lowerrightplayerButton);
        centerPlayerBtn = findViewById(R.id.centerplayerButton);
        musicList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        position = 0;
        isActive = false;


        Intent folderUri = getIntent();
        String musicPathStringUri = folderUri.getStringExtra("folderPath");
        musicPathUri = Uri.parse(Uri.decode(musicPathStringUri));
        Log.i("Karico", musicPathUri + "");


        playerBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upperLeftPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KaricoMotorPlayer.this, "Upper Left Button  Tapped", Toast.LENGTH_LONG).show();
            }
        });


        upperRightPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KaricoMotorPlayer.this, "Upper Right Button Tapped", Toast.LENGTH_LONG).show();
            }
        });

        lowerLeftPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KaricoMotorPlayer.this, "Lower Left Button Tapped", Toast.LENGTH_LONG).show();
            }
        });


        lowerRightPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KaricoMotorPlayer.this, "Lower Right Button Tapped", Toast.LENGTH_LONG).show();
            }
        });


        centerPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(KaricoMotorPlayer.this, "Center Tapped", Toast.LENGTH_LONG).show();
            }
        });


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadMusic(musicPathUri);
            }
        });
        thread.start();


    }


    @Override
    public void onBackPressed() {

    }

    public void sd (ImageButton playerButton) throws IOException {
        prepareMusicPlayer(musicList.get(position));
        if (musicList != null) {
            if (playerButton != null) {
                if(playerButton.getDrawable().equals(R.drawable.icons_play)) {
                    if (musicList.size() > 0){
                        if (playMusic(mediaPlayer)){
                            isActive = true;
                            position = position + 1;
                            playerButton.setImageResource(R.drawable.icons_pause);
                        }
                    }
                } else if (playerButton.getDrawable().equals(R.drawable.icons_pause)) {

                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void prepareMusicPlayer(Uri currentMusicUri) throws IOException {
        mediaPlayer.setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        mediaPlayer.setDataSource(this, currentMusicUri);
        mediaPlayer.prepare();
    }

    public boolean playMusic(MediaPlayer mediaPlayer){
        boolean isPlaying = false;
        if(mediaPlayer != null){
            mediaPlayer.start();
            isPlaying = true;
        }
        return isPlaying;
    }

    public boolean pauseMusic(MediaPlayer mediaPlayer){
        boolean isPaused = false;
        if(mediaPlayer != null){
            mediaPlayer.pause();
            isPaused = true;
        }
        return isPaused;
    }

    public boolean stopMusic(MediaPlayer mediaPlayer){
        boolean isStopped = false;
        if(mediaPlayer != null){
            mediaPlayer.stop();
            isStopped = true;
        }

        return isStopped;
    }


    synchronized void  loadMusic(Uri musicpaths){
        DocumentFile documentsFromURIs  = null;
        if (musicpaths != null) {
            documentsFromURIs = DocumentFile.fromTreeUri(this, musicpaths);
            if (documentsFromURIs.exists()){
                if (documentsFromURIs.isDirectory()){
                    for (DocumentFile file : documentsFromURIs.listFiles()){
                        if (file.isFile()){
                            if (file.getType().equals("audio/mpeg")){
                                if (musicList != null){
                                    musicList.add(file.getUri());
                                }
                            }
                            Log.i("Karico", "folder content karico " + file.getType() + " " + file.length());
                        }
                    }
                } else if (documentsFromURIs.isFile()){
                    Toast.makeText(this,"Sorry, you cannot add single files, add a directory", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}
