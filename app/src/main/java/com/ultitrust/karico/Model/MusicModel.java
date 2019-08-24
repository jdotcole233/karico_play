package com.ultitrust.karico.Model;

public class MusicModel {

    private String folder_name;
    private Double folder_size;
    private Integer number_of_music;

    public MusicModel(String folder_name, Double folder_size, Integer number_of_music) {
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

    public Double getFolder_size() {
        return folder_size;
    }

    public void setFolder_size(Double folder_size) {
        this.folder_size = folder_size;
    }

    public Integer getNumber_of_music() {
        return number_of_music;
    }

    public void setNumber_of_music(Integer number_of_music) {
        this.number_of_music = number_of_music;
    }
}
