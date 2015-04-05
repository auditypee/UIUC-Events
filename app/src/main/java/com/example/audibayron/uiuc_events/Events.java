package com.example.audibayron.uiuc_events;

import com.google.android.gms.maps.model.LatLng;

public class Events {
    //Variables that takes from the database
    private int _id;
    private String eventName;
    private LatLng address;
    private String details;
    private int date;
    private int month;
    private int year;

    /**
     * CONSTRUCTORS
     */

    //Empty Constructor
    public Events() {

    }
    //Main Constructor
    public Events(int mId, String mEventName, LatLng mAddress, String mDetails,
                  int mDate, int mMonth, int mYear) {
        this._id = mId;
        this.eventName = mEventName;
        this.address = mAddress;
        this.details = mDetails;
        this.date = mDate;
        this.month = mMonth;
        this.year = mYear;
    }
    //Constructor with no Details
    public Events(int mId, String mEventName, LatLng mAddress,
                  int mDate, int mMonth, int mYear){
        this._id = mId;
        this.eventName = mEventName;
        this.address = mAddress;
        this.date = mDate;
        this.month = mMonth;
        this.year = mYear;
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
    public LatLng getAddress() {
        return address;
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
    //Setters
    public void setId(int id) {
        this._id = id;
    }
    public void setEventName(String newEventName) {
        this.eventName = newEventName;
    }
    public void setAddress(LatLng newAddress) {
        this.address = newAddress;
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
