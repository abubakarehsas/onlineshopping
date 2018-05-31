package com.appsinventiv.onlineshopping.Services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.appsinventiv.onlineshopping.Model.AdDetails;
import com.appsinventiv.onlineshopping.Model.User;
import com.appsinventiv.onlineshopping.Receievers.BroadCastReceiver;
import com.appsinventiv.onlineshopping.Utils.BuildNotification;
import com.appsinventiv.onlineshopping.Utils.CommonUtils;
import com.appsinventiv.onlineshopping.Utils.SharedPrefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by AliAh on 27/05/2018.
 */

public class NotifyNearByAdsService extends Service implements LocationListener {
    DatabaseReference mDatabase;
    private BroadcastReceiver receiver;

    private static final int HANDLER_DELAY = 60 * 1000;//1 min
    LocationManager m_locationManager;
    ArrayList<AdDetails> adsArrayList = new ArrayList<>();
    ArrayList<String> userNotifiedAdsArrayList;
    Double lat, lon;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Location l = getLocation();
                if (l != null) {
                    lat = l.getLatitude();
                    lon = l.getLongitude();
                } else
                    return;
                getDataFromServer();
//                CommonUtils.showToast(lat + "  " + lon);
                handler.postDelayed(this, HANDLER_DELAY);
            }
        }, 0);


        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addDataScheme("package");
        receiver = new BroadCastReceiver();
        registerReceiver(receiver, intentFilter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void getDataFromServer() {
        mDatabase.child("users").child(SharedPrefs.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getNearbyAdsNotified() != null) {
                            userNotifiedAdsArrayList = user.getNearbyAdsNotified();
                        } else {
                            userNotifiedAdsArrayList = new ArrayList<>();
                        }
                        getAds();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getAds() {
        mDatabase.child("ads").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    AdDetails adDetails = dataSnapshot.getValue(AdDetails.class);
                    if (adDetails != null) {
                        adsArrayList.add(adDetails);
                        if (!adDetails.getUsername().equals(SharedPrefs.getUsername())) {
                            Double distance = CommonUtils.KilometerDistanceBetweenPoints(lat, lon, adDetails.getLattitude(), adDetails.getLongitude());
                            if (distance <= 5) {
                                if (!userNotifiedAdsArrayList.contains(adDetails.getAdId())) {
                                    showPopUp(adDetails.getAdId());
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CommonUtils.showToast("Permision not granted");
            return null;
        }
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGpsEnabled) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;
        } else {
            CommonUtils.showToast("Please enable gps");
        }
        return null;
    }


    private void showPopUp(String adId) {

        if (!userNotifiedAdsArrayList.contains(adId)) {
            BuildNotification.showNotification(getApplicationContext(), "Listings nearby", "There are some products listed nearby");

        }
        userNotifiedAdsArrayList.add(adId);
        mDatabase.child("users").child(SharedPrefs.getUsername()).child("nearbyAdsNotified").setValue(userNotifiedAdsArrayList);
    }


    @Override
    public void onDestroy() {
        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        super.onDestroy();
        Intent myIntent = new Intent(getApplicationContext(), NotifyNearByAdsService.class);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, myIntent, 0);

        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.add(Calendar.SECOND, 10);

        alarmManager1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return Service.START_STICKY;
        }
//        this.m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent myIntent = new Intent(getApplicationContext(), NotifyNearByAdsService.class);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, myIntent, 0);

        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.add(Calendar.SECOND, 10);

        alarmManager1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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
