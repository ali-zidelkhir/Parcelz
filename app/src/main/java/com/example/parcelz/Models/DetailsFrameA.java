package com.example.parcelz.Models;

public class DetailsFrameA {
    String Title;
    String H;
    String W;
    String L;
    String Type;
    String Description;

    public DetailsFrameA(String title, String h, String w, String l, String type, String description) {
        Title = title;
        H = h;
        W = w;
        L = l;
        Type = type;
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getH() {
        return H;
    }

    public void setH(String h) {
        H = h;
    }

    public String getW() {
        return W;
    }

    public void setW(String w) {
        W = w;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
