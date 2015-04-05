package com.example.audibayron.uiuc_events;

import com.google.android.gms.maps.model.LatLng;

public class Events {
    //Variables that takes from the database
    private int _id;
    private String eventName;
    private String addressName;
    private LatLng addressCo;
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
    public Events(int mId, String mEventName, String mAddressName, LatLng mAddressCo, String mDetails,
                  int mDate, int mMonth, int mYear) {
        this._id = mId;
        this.eventName = mEventName;
        this.addressName = mAddressName;
        this.addressCo = mAddressCo;
        this.details = mDetails;
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
