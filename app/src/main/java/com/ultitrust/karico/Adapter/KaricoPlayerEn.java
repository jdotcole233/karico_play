package com.ultitrust.karico.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ultitrust.karico.Model.MusicState;
import com.ultitrust.karico.R;

import java.io.IOException;
import java.util.ArrayList;

public class KaricoPlayerEn {
    private ArrayList<Uri> musicList;
    private Context context;
    private MediaPlayer mediaPlayer;
    private Boolean isPlaying, isPaused = false;
    private int position = 0;
    private Uri uripaths;

    public KaricoPlayerEn(Context context){
        this.context = context;
        musicList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        uripaths = null;
    }


    public void setUripaths(Uri uripaths) {
        this.uripaths = uripaths;
    }


    public ArrayList<Uri> getMusicList() {
        return musicList;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void prepareMusicPlayer(Uri currentMusicUri) throws IOException {
        mediaPlayer.reset();
        Log.i("Karico", currentMusicUri +  "In prepareMusic Player");
        mediaPlayer.setDataSource(context, currentMusicUri);
        mediaPlayer.prepare();

    }

    public boolean playMusic(ImageButton imageButton) throws IOException{
        if(mediaPlayer != null){
            if (musicList.size() > 0 && position < musicList.size()) {
                if (imageButton.getTag().equals(R.drawable.icons_play)) {
                    prepareMusicPlayer(musicList.get(position));
                    imageButton.setImageResource(R.drawable.icons_pause);
                    imageButton.setTag(R.drawable.icons_pause);
                    mediaPlayer.start();
                    isPlaying = true;
                } else if (imageButton.getTag().equals(R.drawable.icons_pause)) {
                    if (pauseMusic()) {
                        imageButton.setImageResource(R.drawable.icons_play);
                        imageButton.setTag(R.drawable.icons_play);
                    }
                }

            }
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


    public boolean forwardMusic() throws IOException {
        boolean isforwarded = false;
        if (mediaPlayer != null) {
            position = position + 1;
            Log.i("Karico", "forwarded");
        }

        return isforwarded;
    }

    public boolean reverseMusic() throws IOException {
        boolean isreversed = false;
        if (mediaPlayer != null) {
            if (position > 0) {
                position = position - 1;
                Log.i("Karico", "reversed");

            } else {
                position = 0;
                Log.i("Karico", "reversed 0");

            }
            isreversed = true;
        }
        return isreversed;
    }



    public  void musicLoading() {
        if (uripaths != null) {
            loadMusic(uripaths);
        }
    }


    synchronized public void  loadMusic(Uri musicpaths){
        DocumentFile documentsFromURIs  = null;
        if (musicpaths != null) {
            documentsFromURIs = DocumentFile.fromTreeUri(context, musicpaths);
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
                    Toast.makeText(context,"Sorry, you cannot add single files, add a directory", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}
