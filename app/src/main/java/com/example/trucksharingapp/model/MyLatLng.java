package com.example.trucksharingapp.model;

public class MyLatLng {


    public double latitude;
    public double longitude;

    public MyLatLng(double la, double lo) {
        latitude = la;
        longitude = lo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}