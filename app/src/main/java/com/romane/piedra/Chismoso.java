package com.romane.piedra;

/**
 * Created by oficina on 18/07/2016.
 */

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;


public class Chismoso extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        LocationManager ambur = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean petrus = ambur.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Intent intentMemoryService = new Intent(context, Actualiza.class);

        if (petrus) {
            context.startService(intentMemoryService);
        }


}

}


