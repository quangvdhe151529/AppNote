package com.example.appnote.model;

import java.io.Serializable;

public class Note implements Serializable {
    private int color;
    private String tieude,ghichu,dateTime, id;
    private Boolean daxoa;

    public Note() {
    }

    public Note(String id, int color, String tieude, String ghichu, String dateTime) {
        this.id = id;
        this.color = color;
        this.tieude = tieude;
        this.ghichu = ghichu;
        this.dateTime = dateTime;
    }
    public Note(String id, int color, String tieude, String ghichu, String dateTime,Boolean daxoa) {
        this.id = id;
        this.color = color;
        this.tieude = tieude;
        this.ghichu = ghichu;
        this.dateTime = dateTime;
        this.daxoa = daxoa;
    }
    public Note(int color, String tieude, String ghichu, String dateTime) {
        this.color = color;
        this.tieude = tieude;
        this.ghichu = ghichu;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getDaxoa() {
        return daxoa != null ? daxoa : false;
    }

    public void setDaxoa(Boolean daxoa) {
        this.daxoa = daxoa;
    }
}
