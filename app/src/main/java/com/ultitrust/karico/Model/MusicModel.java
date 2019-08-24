package com.ultitrust.karico.Model;

public class MusicModel {

    private String folder_name,folder_size, number_of_music;

    public MusicModel(String folder_name, String folder_size, String number_of_music) {
        this.folder_name = folder_name;
        this.folder_size = folder_size;
        this.number_of_music = number_of_music;
    }


    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getFolder_size() {
        return folder_size;
    }

    public void setFolder_size(String folder_size) {
        this.folder_size = folder_size;
    }

    public String getNumber_of_music() {
        return number_of_music;
    }

    public void setNumber_of_music(String number_of_music) {
        this.number_of_music = number_of_music;
    }
}
