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


public class Chismoso3 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        LocationManager ambur = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean petrus = ambur.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean EstaActualizaTrabajando = isMyServiceRunning(Actualiza.class, context);
        Intent intentMemoryServiceIII = new Intent(context, Actualiza.class);

        if (petrus) {
            if(!EstaActualizaTrabajando){context.startService(intentMemoryServiceIII);}
        }


    }
    private boolean isMyServiceRunning(Class<?> nombrecito,Context context) {
        ActivityManager manager = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (nombrecito.getName().equals(service.service.getClassName())) {
                Log.i("Service already","running");
                return true;
            }
        }
        Log.i("Service not","running");
        return false;
    }
}

