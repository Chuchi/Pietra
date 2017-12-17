package com.romane.piedra;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
    SharedPreferences Camile = PreferenceManager.getDefaultSharedPreferences(this);

            if (remoteMessage.getData().size() > 0) {

                Map<String, String> MensajeLiteral = remoteMessage.getData();
                String mensajito = "";
                String codigo = "";
                SharedPreferences.Editor edito = Camile.edit();
                mensajito = MensajeLiteral.get("msg");
              //Las acciones a tomar dependen del contenido de los Maps
                if (MensajeLiteral.get("Amalaya")!= null){edito.putString(getResources().getString(R.string.PropiedadURL),MensajeLiteral.get("Amalaya"));}
                if (MensajeLiteral.get("Amalayo")!= null){edito.putString(getResources().getString(R.string.PropiedadNAMESPACE),MensajeLiteral.get("Amalayo"));}
                if (MensajeLiteral.get("FrecuenciaLocal")!= null)
                {
                    edito.putInt(getResources().getString(R.string.PropiedadFrecuenciaActualizacionEnLocal),Integer.parseInt(MensajeLiteral.get("FrecuenciaLocal")));
                    /// ademas paramos y reanudamos el servicio Actualiza
                    Intent intentMemoryService = new Intent(this, Actualiza.class);
                         this.stopService(intentMemoryService);
                         this.startService(intentMemoryService);
                }

                if (MensajeLiteral.get("FrecuenciaANube")!= null)
                {
                    edito.putInt(getResources().getString(R.string.PropiedadFrecuenciaSubirDatosANube),Integer.parseInt(MensajeLiteral.get("FrecuenciaANube")));
                    /// ademas paramos y reanudamos el servicio Actualiza
                    Intent intentMemoryService = new Intent(this, Actualiza.class);
                    this.stopService(intentMemoryService);
                    this.startService(intentMemoryService);
                }
                if (MensajeLiteral.get("FrecuenciaScaneo")!= null)
                {
                    edito.putInt(getResources().getString(R.string.PropiedadFrecuenciaScaneo),Integer.parseInt(MensajeLiteral.get("FrecuenciaScaneo")));
                    /// ademas paramos y reanudamos el servicio Actualiza
                    Intent intentMemoryService = new Intent(this, Actualiza.class);
                    this.stopService(intentMemoryService);
                    this.startService(intentMemoryService);
                }
                if (MensajeLiteral.get("FrecuenciaRecibirOrdenes")!= null)
                {
                    edito.putInt(getResources().getString(R.string.PropiedadFrecuenciaRecibirOrdenes),Integer.parseInt(MensajeLiteral.get("FrecuenciaRecibirOrdenes")));
                    /// ademas paramos y reanudamos el servicio Actualiza
                    Intent intentMemoryService = new Intent(this, Actualiza.class);
                    this.stopService(intentMemoryService);
                    this.startService(intentMemoryService);
                }
                if (MensajeLiteral.get("FrecuenciaGuardia")!= null)
                {
                    edito.putInt(getResources().getString(R.string.PropiedadFrecuenciaGuardia),Integer.parseInt(MensajeLiteral.get("FrecuenciaGuardia")));
                    /// ademas paramos y reanudamos el servicio Actualiza
                    Intent intentMemoryService = new Intent(this, Actualiza.class);
                    this.stopService(intentMemoryService);
                    this.startService(intentMemoryService);
                }
                if (MensajeLiteral.get("Reseteame")!= null)
                {
                    ///  paramos y reanudamos el servicio Actualiz
                    Intent intentMemoryService = new Intent(this, Actualiza.class);
                    this.stopService(intentMemoryService);
                    this.startService(intentMemoryService);
                }

                if (MensajeLiteral.get("PararServicio")!= null)
                {
                    ///  paramos y reanudamos el servicio Actualiz
                    Intent intentMemoryService = new Intent(this, Actualiza.class);
                    this.stopService(intentMemoryService);
                }

                if (MensajeLiteral.get("IniciarServicio")!= null)
                {
                    ///  paramos y reanudamos el servicio Actualiz
                    Intent intentMemoryService = new Intent(this, Actualiza.class);
                    this.startService(intentMemoryService);
                }



                if (MensajeLiteral.get("msg")!= null){mensajito = MensajeLiteral.get("msg");}

                // Desencadena el envio inmediato de la ubicacion
                edito.putString(getResources().getString(R.string.PropiedadREFRESH),"Metale");
                EnviaNotificacion(mensajito);
                edito.apply();

            }


          if (remoteMessage.getNotification() != null) {
       //    EnviaNotificacion(remoteMessage.getNotification().getBody());
           }
        }


    private void EnviaNotificacion(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cuadroxx)
                .setContentTitle("GPS Links")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 2000, 1000 })
                .setSound(defaultSoundUri)
                .setColor(2563)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}