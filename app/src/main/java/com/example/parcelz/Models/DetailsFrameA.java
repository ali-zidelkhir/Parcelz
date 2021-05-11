package com.example.parcelz.Models;

public class DetailsFrameA {
    String Title;
    String H;
    String W;
    String L;
    String Type;
    String Description;
    double Latitude;
    double Longitude;
    double Latitude_destination;
    double Longitude_destination;
    String UID;

    public DetailsFrameA(String title, String h, String w, String l, String type, String description, double latitude,
                         double longitude, double latitude_destination, double longitude_destination, String UID) {
        Title = title;
        H = h;
        W = w;
        L = l;
        Type = type;
        Description = description;
        Latitude = latitude;
        Longitude = longitude;
        Latitude_destination = latitude_destination;
        Longitude_destination = longitude_destination;
        this.UID = UID;
    }

    public double getLatitude_destination() {
        return Latitude_destination;
    }

    public void setLatitude_destination(double latitude_destination) {
        Latitude_destination = latitude_destination;
    }

    public double getLongitude_destination() {
        return Longitude_destination;
    }

    public void setLongitude_destination(double longitude_destination) {
        Longitude_destination = longitude_destination;
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

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

}
