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
    double total;
    String UID;
    String Willaya;
    String Baladia;
    String Waiting;
    String Frangible;
    String Delivery_mode;
    String Cash_on;
    String Estimated_time;
    String Estimated_price;

    public DetailsFrameA(String title, String h, String w, String l, String type, String description,
                         double latitude, double longitude, double latitude_destination, double longitude_destination, double total, String UID, String willaya, String baladia, String waiting, String frangible, String delivery_mode, String cash_on, String estimated_time, String estimated_price) {
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
        this.total = total;
        this.UID = UID;
        Willaya = willaya;
        Baladia = baladia;
        Waiting = waiting;
        Frangible = frangible;
        Delivery_mode = delivery_mode;
        Cash_on = cash_on;
        Estimated_time = estimated_time;
        Estimated_price = estimated_price;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getWillaya() {
        return Willaya;
    }

    public void setWillaya(String willaya) {
        Willaya = willaya;
    }

    public String getBaladia() {
        return Baladia;
    }

    public void setBaladia(String baladia) {
        Baladia = baladia;
    }

    public String getWaiting() {
        return Waiting;
    }

    public void setWaiting(String waiting) {
        Waiting = waiting;
    }

    public String getFrangible() {
        return Frangible;
    }

    public void setFrangible(String frangible) {
        Frangible = frangible;
    }

    public String getDelivery_mode() {
        return Delivery_mode;
    }

    public void setDelivery_mode(String delivery_mode) {
        Delivery_mode = delivery_mode;
    }

    public String getCash_on() {
        return Cash_on;
    }

    public void setCash_on(String cash_on) {
        Cash_on = cash_on;
    }

    public String getEstimated_time() {
        return Estimated_time;
    }

    public void setEstimated_time(String estimated_time) {
        Estimated_time = estimated_time;
    }

    public String getEstimated_price() {
        return Estimated_price;
    }

    public void setEstimated_price(String estimated_price) {
        Estimated_price = estimated_price;
    }
}
