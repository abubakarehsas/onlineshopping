package com.appsinventiv.onlineshopping.Receievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appsinventiv.onlineshopping.Services.NotifyNearByAdsService;

/**
 * Created by AliAh on 27/05/2018.
 */

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, NotifyNearByAdsService.class));;

    }
}
