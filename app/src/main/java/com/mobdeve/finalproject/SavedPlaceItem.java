package com.mobdeve.finalproject;

import com.google.android.gms.maps.model.LatLng;

public class SavedPlaceItem {

    private float latitude;
    private float longitude;
    private String placeName;
    private int placeID;

    public SavedPlaceItem(int placeID, String placeName, float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
        this.placeID = placeID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }
}
