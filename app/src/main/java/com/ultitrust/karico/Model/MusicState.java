package com.ultitrust.karico.Model;

import android.net.Uri;

import java.io.Serializable;

public class MusicState implements Serializable {
    private int buttonType;
    private Uri musicUri;
    private int musiccurrentPosition;
    private int musicDuration;
    private boolean isInserted = false;



    public MusicState(int buttonType, Uri musicUri, int musiccurrentPosition, int musicDuration) {
        this.buttonType = buttonType;
        this.musicUri = musicUri;
        this.musiccurrentPosition = musiccurrentPosition;
        this.musicDuration = musicDuration;
        isInserted = true;
    }


    public int getButtonType() {
        return buttonType;
    }

    public void setButtonType(int buttonType) {
        this.buttonType = buttonType;
    }

    public Uri getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(Uri musicUri) {
        this.musicUri = musicUri;
    }

    public int getMusiccurrentPosition() {
        return musiccurrentPosition;
    }

    public void setMusiccurrentPosition(int musiccurrentPosition) {
        this.musiccurrentPosition = musiccurrentPosition;
    }

    public int getMusicDuration() {
        return musicDuration;
    }

    public void setMusicDuration(int musicDuration) {
        this.musicDuration = musicDuration;
    }

    public boolean isInserted() {
        return isInserted;
    }

    public void setInserted(boolean inserted) {
        isInserted = inserted;
    }
}
