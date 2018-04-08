package com.abdul.hackathon;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by abduljama on 4/8/18.
 */

public class Deliveries {
    LatLng pickup;
    LatLng drop;

    String pick_name;
    String drop_name;


    public Deliveries(LatLng pickup, LatLng drop, String pick_name, String drop_name) {
        this.pickup = pickup;
        this.drop = drop;
        this.pick_name = pick_name;
        this.drop_name = drop_name;
    }

    public Deliveries() {

    }

    public String getPick_name() {
        return pick_name;
    }

    public void setPick_name(String pick_name) {
        this.pick_name = pick_name;
    }

    public String getDrop_name() {
        return drop_name;
    }

    public void setDrop_name(String drop_name) {
        this.drop_name = drop_name;
    }

    public LatLng getPickup() {
        return pickup;
    }

    public void setPickup(LatLng pickup) {
        this.pickup = pickup;
    }

    public LatLng getDrop() {
        return drop;
    }

    public void setDrop(LatLng drop) {
        this.drop = drop;
    }
}
