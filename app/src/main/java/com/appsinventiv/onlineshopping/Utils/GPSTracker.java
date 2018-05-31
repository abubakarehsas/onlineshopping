package com.appsinventiv.onlineshopping.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

/**
 * Created by AliAh on 28/05/2018.
 */

public class GPSTracker implements LocationListener {
Context context;
    public GPSTracker(Context context) {
        this.context=context;
    }
    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CommonUtils.showToast("Permision not granted");
            return null;
        }
        LocationManager lm= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGpsEnabled){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,this);
            Location l=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;
        }else {
            CommonUtils.showToast("Please enable gps");
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
