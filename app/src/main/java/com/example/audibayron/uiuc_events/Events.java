package com.example.audibayron.uiuc_events;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

public class Events extends Activity{
    //Variables that takes from the database
    private int _id;
    private String eventName;
    private String addressName;
    private int date;
    private int month;
    private int year;
    private String details;
    private double lat;
    private double lng;
    private LatLng addressCo;

    /**
     * CONSTRUCTORS
     */
    //Empty Constructor
    public Events() {

    }

    //Main Constructor
    public Events(int _id, String eventName, String addressName, int date, int month, int year, String details,
                  double lat, double lng) {
        this._id = _id;
        this.eventName = eventName;
        this.addressName = addressName;
        this.date = date;
        this.month = month;
        this.year = year;
        this.details = details;
        this.addressCo = convertToLatLng(lat, lng);
    }

    private LatLng convertToLatLng(double lat, double lng) {
        LatLng latlng = new LatLng(lat, lng);
        return latlng;
    }

    /**
     * ACCESSOR METHODS
     */
    //Getters
    public int getId() {
        return _id;
    }

    public String getEventName() {
        return eventName;
    }

    public String getAddressName() {
        return addressName;
    }

    public LatLng getAddressCo() {
        return addressCo;
    }

    public String getDetails() {
        return details;
    }

    public int getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    //Setters
    public void setEventName(String mEventName) {
        this.eventName = mEventName;
    }

    public void setAddressName(String mAddressName) {
        this.addressName = mAddressName;
    }

    public void setAddressCo(LatLng mAddressCo) {
        this.addressCo = mAddressCo;
    }

    public void setDetails(String newDetails) {
        this.details = newDetails;
    }

    public void setDate(int mDate) {
        this.date = mDate;
    }

    public void setMonth(int mMonth) {
        this.month = mMonth;
    }

    public void setYear(int mYear) {
        this.year = mYear;
    }

}

