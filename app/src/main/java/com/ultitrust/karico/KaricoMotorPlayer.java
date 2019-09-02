package com.ultitrust.karico;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.Toast;

import com.ultitrust.karico.Model.MusicModel;
import com.ultitrust.karico.Model.MusicState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class KaricoMotorPlayer extends AppCompatActivity {

    private ImageButton playerBackBtn, upperLeftPlayerBtn, upperRightPlayerBtn, lowerLeftPlayerBtn, lowerRightPlayerBtn, centerPlayerBtn;
    private ArrayList<Uri> musicList;
    private MusicState [] musicStates;
    private MediaPlayer mediaPlayer, mediaPlayerCenter, mediaPlayerRight, mediaPlayerLeft, mediaPlayerUpperRight, mediaPlayerUpperLeft;
    private Uri musicPathUri;
    private Integer position, playerNumber;
    private boolean isActive;
    boolean isPlaying = false;
    boolean isPaused = false;


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

        upperLeftPlayerBtn.setTag(R.drawable.icons_play);
        upperRightPlayerBtn.setTag(R.drawable.icons_play);
        lowerLeftPlayerBtn.setTag(R.drawable.icons_play);
        lowerRightPlayerBtn.setTag(R.drawable.icons_play);
        centerPlayerBtn.setTag(R.drawable.icons_play);
        musicStates = new MusicState[4];
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());


        musicList = new ArrayList<>();
        position = 0;
        playerNumber = -1;
        isActive = false;


        Intent folderUri = getIntent();
        String musicPathStringUri = folderUri.getStringExtra("folderPath");
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
//                mediaPlayerUpperLeft = new MediaPlayer();
                try {
                    resetPlayerButton(playerNumber);
                    playerNumber = 4;
                    playerButtonsHandler(playerNumber, upperLeftPlayerBtn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(KaricoMotorPlayer.this, "Upper Left Button  Tapped", Toast.LENGTH_LONG).show();
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
                Toast.makeText(KaricoMotorPlayer.this, "Upper Right Button Tapped", Toast.LENGTH_LONG).show();
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
                Toast.makeText(KaricoMotorPlayer.this, "Lower Left Button Tapped", Toast.LENGTH_LONG).show();
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
                Toast.makeText(KaricoMotorPlayer.this, "Lower Right Button Tapped", Toast.LENGTH_LONG).show();
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
                Toast.makeText(KaricoMotorPlayer.this, "Center Tapped", Toast.LENGTH_LONG).show();
            }
        });


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadMusic(musicPathUri);
                Log.i("Karico", "Thread finished ");

            }
        });
        thread.start();


    }


    @Override
    public void onBackPressed() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void playerButtonsHandler (Integer buttonIdentNumber, ImageButton playerButton) throws IOException {
            if (musicList != null) {
                if (position < musicList.size()) {
                    Log.i("Karico", "L123 " + musicStates.length);
                    if (musicStates.length > 0) {
                        Log.i("Karico", "L12 " + buttonIdentNumber);

                            if (musicStates[buttonIdentNumber] == null) {
                                Log.i("Karico", "L123 " + buttonIdentNumber);
                                if (musicStates[buttonIdentNumber].getMusiccurrentPosition() == musicStates[buttonIdentNumber].getMusicDuration()){
                                    Log.i("Karico", "L124 " + buttonIdentNumber);
                                    musicStates[buttonIdentNumber] = null;
                                }
                                prepareMusicPlayer(musicStates[buttonIdentNumber].getMusicUri());
                                Log.i("Karico", "L12 " + musicStates[buttonIdentNumber].getMusiccurrentPosition());
                                mediaPlayer.seekTo(musicStates[buttonIdentNumber].getMusiccurrentPosition());
                            } else {
                            Log.i("Karico", "L123 else sle" + buttonIdentNumber);
                            prepareMusicPlayer(musicList.get(position));
                        }
                    } else {
                        Log.i("Karico", "L123 else" + buttonIdentNumber);
                        prepareMusicPlayer(musicList.get(position));
                    }
                }

                if (playerButton != null) {
                    if(playerButton.getTag().equals(R.drawable.icons_play)) {
                        if (musicList.size() > 0 || position < musicList.size()){
                            if (playMusic()) {
                                isActive = true;
                                Log.i("Karico", position + " number");
//                                if (mediaPlayer.isPlaying()){
//                                    MusicState musicState = new MusicState(buttonIdentNumber, musicList.get(position),
//                                            mediaPlayer.getCurrentPosition(), 0);
//                                    musicStates[buttonIdentNumber] = musicState;

//                                }

//                                if (musicState.isInserted()) {
//                                    position++;
//                                    musicState.setInserted(false);
//                                }
                                playerButton.setImageResource(R.drawable.icons_pause);
                                playerButton.setTag(R.drawable.icons_pause);
                                Log.i("Karico ", "position " + position + " " + musicList.size());
                            } else {
                                isActive = false;
                                playerButton.setImageResource(R.drawable.icons_play);
                                playerButton.setTag(R.drawable.icons_play);
                            }
                        } else {
                            Toast.makeText(this, "Loading music", Toast.LENGTH_LONG).show();
                        }
                    } else if ( playerButton.getTag().equals(R.drawable.icons_pause)) {
                        if (pauseMusic()) {
                            isActive = false;
                            isPaused = false;
                            if (musicStates.length > 0){
                                musicStates[buttonIdentNumber]
                                        .setMusiccurrentPosition(mediaPlayer.getCurrentPosition());
                            }
                            playerButton.setImageResource(R.drawable.icons_play);
                            playerButton.setTag(R.drawable.icons_play);
                        }
                    }
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
//            if (mediaPlayer.isPlaying()){
                Log.i("Karico", "paused called ");
                mediaPlayer.pause();
                isPaused = true;
//            }
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

}
