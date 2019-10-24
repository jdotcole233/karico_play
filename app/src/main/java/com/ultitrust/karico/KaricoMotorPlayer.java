package com.ultitrust.karico;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ultitrust.karico.Model.ColorRandomizer;
import com.ultitrust.karico.Model.MusicModel;
import com.ultitrust.karico.Model.MusicState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class KaricoMotorPlayer extends AppCompatActivity {

    private ImageButton playerBackBtn, upperLeftPlayerBtn, upperRightPlayerBtn, lowerLeftPlayerBtn, lowerRightPlayerBtn, centerPlayerBtn;
    private ArrayList<Uri> musicList, playerableList;
    private ArrayList<MusicState> _musicStates;
    private MusicState [] musicStates;
    private MediaPlayer mediaPlayer, mediaPlayerCenter, mediaPlayerRight, mediaPlayerLeft, mediaPlayerUpperRight, mediaPlayerUpperLeft;
    private Uri musicPathUri;
    private Integer position, playerNumber;
    private boolean isActive;
    boolean isPlaying = false;
    boolean isLoaded = false , isPaused = false;
    private MusicState musicState;
    private final int EXPECTED_SIZE = 5;
    private TextView displayMusicText;
    private ColorRandomizer colorRandomizer;


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
        displayMusicText = findViewById(R.id.musicplayingtext);
        colorRandomizer = new ColorRandomizer();
        String [] colorspectrum = {"#66ff66", "#cc00cc", "#ff9933", "#007acc", "#5200cc", "#660066", "#86592d", "#cc3300"};
        List<Integer> genNumbers = colorRandomizer.colorRandomizer();

        if (genNumbers != null){
            upperLeftPlayerBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorspectrum[genNumbers.get(0)])));
            upperRightPlayerBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorspectrum[genNumbers.get(1)])));
            centerPlayerBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorspectrum[genNumbers.get(2)])));
            lowerLeftPlayerBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorspectrum[genNumbers.get(3)])));
            lowerRightPlayerBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorspectrum[genNumbers.get(4)])));
        }



        upperLeftPlayerBtn.setTag(R.drawable.icons_play);
        upperRightPlayerBtn.setTag(R.drawable.icons_play);
        lowerLeftPlayerBtn.setTag(R.drawable.icons_play);
        lowerRightPlayerBtn.setTag(R.drawable.icons_play);
        centerPlayerBtn.setTag(R.drawable.icons_play);
        musicStates = new MusicState[5];
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        musicStates[0] = new MusicState(-1, null, 0, 0);
        musicStates[1] = new MusicState(-1, null, 0, 0);
        musicStates[2] = new MusicState(-1, null, 0, 0);
        musicStates[3] = new MusicState(-1, null, 0, 0);
        musicStates[4] = new MusicState(-1, null, 0, 0);


        musicList = new ArrayList<>();
        playerableList = new ArrayList<>();
        _musicStates = new ArrayList<>();
        position = -1;
        playerNumber = -1;
        isActive = false;


        Intent folderUri = getIntent();
        String musicPathStringUri = folderUri.getStringExtra("primaryMotorFolderPath");
        musicPathUri = Uri.parse(Uri.decode(musicPathStringUri));
        Log.i("Karico", musicPathUri + "");


        playerBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActive){
                    stopMusic();
                    isActive = false;
                    finish();
                } else {
                    finish();
                }
            }
        });

        upperLeftPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    resetPlayerButton(playerNumber);
                    playerNumber = 4;
                    playerButtonsHandler(playerNumber, upperLeftPlayerBtn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        upperRightPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerUpperRight = new MediaPlayer();
                try {
                    resetPlayerButton(playerNumber);
                    playerNumber = 3;
                    playerButtonsHandler(playerNumber, upperRightPlayerBtn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        lowerLeftPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerLeft = new MediaPlayer();
                try {

                    resetPlayerButton(playerNumber);
                    playerNumber = 2;
                    playerButtonsHandler(playerNumber, lowerLeftPlayerBtn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        lowerRightPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mediaPlayerRight = new MediaPlayer();
                try {
                    resetPlayerButton(playerNumber);
                    playerNumber = 1;
                    playerButtonsHandler(playerNumber, lowerRightPlayerBtn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        centerPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerCenter = new MediaPlayer();

                try {

                    resetPlayerButton(playerNumber);
                    playerNumber = 0;
                    playerButtonsHandler(playerNumber,centerPlayerBtn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadMusic(musicPathUri);
                Log.i("Karico", "Thread finished ");
                int musicsize = musicList.size();

                if (musicsize <= 0 ) {
                    isLoaded = false;
                    displayMusicText.setText("No music files loaded ");
                } else {
                    shuffleMusicForButtons(musicsize);
                    isLoaded = true;
                    displayMusicText.setText("Completed loading music files");
                }

            }
        });
        thread.start();


    }


    @Override
    public void onBackPressed() {

    }

    public void shuffleMusicForButtons(int musiclistsize) {
        int j = musiclistsize;
        for (int i = 0; i < EXPECTED_SIZE; i++) {
            j = j % musiclistsize;
            playerableList.add(i, musicList.get(j));
            j = j + 1;
        }
    }






    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void playerButtonsHandler (Integer buttonIdentNumber, ImageButton playerButton) throws IOException {

            String musicName = "";

            if (playerableList != null){
                if (playerableList.size() > 0) {
                    if (playerButton != null) {
                        if (playerButton.getTag().equals(R.drawable.icons_play)) {
                            if (_musicStates != null) {
                                int _musicStateSize = _musicStates.size();
                                if (_musicStateSize > 0) {
                                    for (int _i = 0; _i < _musicStateSize; _i++){
                                        if(_musicStates.get(_i).getButtonType() == buttonIdentNumber) {
                                            prepareMusicPlayer(_musicStates.get(_i).getMusicUri());
                                            mediaPlayer.seekTo(_musicStates.get(_i).getMusiccurrentPosition());
                                            Log.i("Karico", "music at " + buttonIdentNumber + " is " + _musicStates.get(_i).getMusiccurrentPosition() );
                                            musicName = colorRandomizer.getMusicName(KaricoMotorPlayer.this, playerableList.get(buttonIdentNumber));
                                            _musicStates.add( _i,new MusicState(buttonIdentNumber, playerableList.get(buttonIdentNumber), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
                                            break;
                                        } else {
                                            prepareMusicPlayer(playerableList.get(buttonIdentNumber));
                                            _musicStates.add( _i,new MusicState(buttonIdentNumber, playerableList.get(buttonIdentNumber), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
                                            musicName = colorRandomizer.getMusicName(KaricoMotorPlayer.this, playerableList.get(buttonIdentNumber));
                                            break;
                                        }
                                    }
                                } else {
                                    if (position > EXPECTED_SIZE){
                                        position = -1;
                                    }
                                    position++;
                                    _musicStates.add(position, new MusicState(buttonIdentNumber, playerableList.get(buttonIdentNumber), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
                                    prepareMusicPlayer(playerableList.get(buttonIdentNumber));
                                    musicName = colorRandomizer.getMusicName(KaricoMotorPlayer.this, playerableList.get(buttonIdentNumber));

                                }

                                if (playMusic()){
                                    playerButton.setImageResource(R.drawable.icons_pause);
                                    playerButton.setTag(R.drawable.icons_pause);
                                    displayMusicText.setText(musicName);
                                } else {
                                    playerButton.setImageResource(R.drawable.icons_play);
                                    playerButton.setTag(R.drawable.icons_play);
                                    displayMusicText.setText(" ");
                                }
                            }
                        } else if (playerButton.getTag().equals(R.drawable.icons_pause)) {
                            if (pauseMusic()){
                                int _musicStateSize = _musicStates.size();
                                if (_musicStateSize > 0){
                                    for (int ui = 0; ui < _musicStateSize; ui++) {
                                        if (_musicStates.get(ui).getButtonType() == buttonIdentNumber) {
                                            _musicStates.get(ui).setMusiccurrentPosition(mediaPlayer.getCurrentPosition());
                                            break;
                                        }
                                    }
                                }
                                playerButton.setImageResource(R.drawable.icons_play);
                                playerButton.setTag(R.drawable.icons_play);
                                displayMusicText.setText(" ");
                            }
                        }
                    }

                } else {
                    //player list is empty
                    Toast.makeText(this, "Music content loading....", Toast.LENGTH_LONG).show();
                    return;
                }
            }
    }




    public void resetPlayerButton(int playerNumber) {
        Log.i("Karico", "In resetplayer " + playerNumber);
        switch (playerNumber) {
            case 0:
                centerPlayerBtn.setImageResource(R.drawable.icons_play);
                break;
            case 1:
                lowerRightPlayerBtn.setImageResource(R.drawable.icons_play);
                break;
            case 2:
                lowerLeftPlayerBtn.setImageResource(R.drawable.icons_play);
                break;
            case 3:
                upperRightPlayerBtn.setImageResource(R.drawable.icons_play);
                break;
            case 4:
                upperLeftPlayerBtn.setImageResource(R.drawable.icons_play);
                break;
            default:
                break;
        }
    }


    public void prepareMusicPlayer(Uri currentMusicUri) throws IOException {
        mediaPlayer.reset();
        Log.i("Karico", currentMusicUri +  "In prepareMusic Player");
        mediaPlayer.setDataSource(this, currentMusicUri);
        mediaPlayer.prepare();

    }

    public boolean playMusic(){
        if(mediaPlayer != null){
            mediaPlayer.start();
            isPlaying = true;
        }
        return isPlaying;
    }

    public boolean pauseMusic(){
        if(mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                Log.i("Karico", "paused called ");
                mediaPlayer.pause();
                isPaused = true;
            }
        }
        return isPaused;
    }

    public boolean stopMusic(){
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
                                Log.i("Karico", "Loading music path " + file.getUri() + " " + file.getType() + " " + file.getName());
                                if (musicList != null){
                                    Log.i("Karico", "Loading music 6");
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







    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()){
            stopMusic();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            stopMusic();
            resetPlayerButton(playerNumber);
        }
    }
}
