package com.romane.piedra;

/**
 * Created by oficina on 18/07/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Chismoso extends BroadcastReceiver {

    // Este Broadcast debe de poner en marcha la MainActivity y el servicio Actualiza

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }
/*
        LocationManager ambur = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean petrus = ambur.isProviderEnabled(LocationManager.GPS_PROVIDER);

       // Intent intentMemoryService = new Intent(context, Actualiza.class);
        Intent PrendePiedra = new Intent(context,MainActivity.class);

        if (petrus) {
            context.startActivity(PrendePiedra);
          //  context.startService(intentMemoryService);

        }
*/

}

}


