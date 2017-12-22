package com.romane.piedra;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Actualiza extends Service {

    private final static double distanciaminima= 12d;

    private static final long Ticks_Hasta_1Enero1970 = 621355968000000000L;
    final long PermanenciaOchoHoras1 = 3000000L; //Casi ocho horas en milisegundos
    final long PermanenciaOchoHoras2 = 3000000L; //Casi ocho horas en milisegundos
    final long PermanenciaOchoHoras3 = 3000000L; //Casi ocho horas en milisegundos
    Boolean NotificacionLlegadaHito1Enviada = true;//Indicador de que la notificacion1 de llegada ha sido enviada
    Boolean NotificacionLlegadaHito2Enviada = true;//Indicador de que la notificacion2 de llegada ha sido enviada
    Boolean NotificacionLlegadaHito3Enviada = true;//Indicador de que la notificacion2 de llegada ha sido enviada
    long TiempoReposo=System.currentTimeMillis();
    int BanderinReposo1=0;
    int BanderinReposo2=0;

    Timer tempora1 ,tempora2 , tempora3, tempora4, tempora5;

    String latitude = "12";
    String longuitude = "13";
    long perico = 0;
    int ActualizameYa =0;
    int numeroavisos = 0;
    Punto Anterior= new Punto("1",2,0,0);
    Punto Nuevo = new Punto ("2",2,12,13);



    private Punto Hito1 ;
    private Punto Hito2 ;
    private Punto Hito3 ;

    ArrayList <Punto> ListadoRelojes;

    SharedPreferences tuyas;

    public Actualiza() {
    }

    private LocationManager locManager;
    private LocationListener locListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        tuyas=PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor emil = tuyas.edit();
        emil.putString(getResources().getString(R.string.PropiedadREFRESH),"EnEspera");
        emil.apply();;

        CreaciondePuntos(); //Instanciamos todos los puntos de referencia

            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locListener = new LocationListener() {

                public void onLocationChanged(Location location) {
                    Nuevo = new Punto("1", 1, Redondear(location.getLatitude()), Redondear(location.getLongitude()));
                    latitude = String.valueOf(Nuevo.getLatitud());
                    longuitude = String.valueOf(Nuevo.getLonguitud());
                // actualizamos estas coordenadas en las sharedpreferences
                }
                public void onProviderDisabled(String provider) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };

            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, locListener);

            tempora1 = new Timer();
            tempora1.scheduleAtFixedRate(RutinaRegistrosEnLocal1, 7001, tuyas.getInt(getResources().getString(R.string.PropiedadFrecuenciaActualizacionEnLocal),140003));
            tempora2 = new Timer();
            tempora2.scheduleAtFixedRate(RutinaSubidaANube, 8712, tuyas.getInt(getResources().getString(R.string.PropiedadFrecuenciaSubirDatosANube),600000));
            tempora3 = new Timer();
            tempora3.scheduleAtFixedRate(RutinaNotificaciones, 9023, tuyas.getInt(getResources().getString(R.string.PropiedadFrecuenciaScaneo),900));
            tempora4 = new Timer();
            tempora4.scheduleAtFixedRate(RutinaGuardiaEnReposo, 75423, tuyas.getInt(getResources().getString(R.string.PropiedadFrecuenciaGuardia),3600000));
            tempora5 = new Timer();
            tempora5.scheduleAtFixedRate(RutinaBuscarOrdenesEnServidor, 10231, tuyas.getInt(getResources().getString(R.string.PropiedadFrecuenciaRecibirOrdenes),43200000));
        SharedPreferences.Editor editorial =tuyas.edit();
        Toast.makeText(this, "Va bueno ,,, starting", Toast.LENGTH_LONG).show();
      //  editorial.putInt(getResources().getString(R.string.PropiedadRadioEnMetrosRelojes),120);
        editorial.apply();
        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        tempora1.cancel();
        tempora1.purge();
        tempora2.cancel();
        tempora2.purge();
        tempora3.cancel();
        tempora3.purge();
        tempora4.cancel();
        tempora4.purge();
        tempora5.cancel();
        tempora5.purge();


        super.onDestroy();

    }

    private class ActualizaRapidin extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... params) {

            String resul = "empanao";

            final String METHOD_NAME = "RegistroMicroHistorico";
            final String SOAP_ACTION =tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1")+"/RegistroMicroHistorico";

            SoapObject request = new SoapObject(tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1"), METHOD_NAME);
            request.addProperty("iden", params[0]);
            request.addProperty("latitude", params[1]);
            request.addProperty("longuitude", params[2]);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(tuyas.getString(getResources().getString(R.string.PropiedadURL), "1"));
            int paco = 0;

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                resul = response.toString();

            } catch (Exception e) {
                resul = "Calimero";
            }
            return resul;
        }


        protected void onPostExecute(String result) {

        }
    }
    private class SubeLocalANube extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... params) {


            String resul = "empanao";

            final String METHOD_NAME = "RegistroPuntosPendientes";
            final String SOAP_ACTION =tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1")+"/RegistroPuntosPendientes";

            SoapObject request = new SoapObject(tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1"), METHOD_NAME);
            request.addProperty("venas",params[0]);
            request.addProperty("trazador",params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(tuyas.getString(getResources().getString(R.string.PropiedadURL), "1"));

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                resul = response.toString();

            } catch (Exception e) {
                resul = "0";
            }
            return resul;
        }


        protected void onPostExecute(String result) {
            int cuantitos = 0;
            cuantitos =Integer.valueOf(result);
            if (cuantitos >0) {

                BDayudame amaranto = new BDayudame(Actualiza.this);
                amaranto.abrir();
                amaranto.BorrarTodo();
                amaranto.cerrar();
                perico = 0;

            }
        }
    }
    private class SubeLocalANube400 extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... params) {


            String resul = "empanao";

            final String METHOD_NAME = "RegistroPuntosPendientes";
            final String SOAP_ACTION = tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1")+"/RegistroPuntosPendientes";

            SoapObject request = new SoapObject(tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1"), METHOD_NAME);
            request.addProperty("venas",params[0]);
            request.addProperty("trazador",params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(tuyas.getString(getResources().getString(R.string.PropiedadURL), "1"));

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                resul = response.toString();

            } catch (Exception e) {
                resul = "0";
            }
            return resul;
        }


        protected void onPostExecute(String result) {
            int cuantitos = 0;
            cuantitos =Integer.valueOf(result);
            if (cuantitos >0) {

                BDayudame amaranto = new BDayudame(Actualiza.this);
                amaranto.abrir();
                amaranto.BorrarNregistros(cuantitos);

                perico = 0;
                // SI aun quedan mas de 700 registros,,, vuelve a pedir SubeLocalANube700
                if (amaranto.CuantosHay() > 400){
                    String Cadenorra = CreadorDeCadenas(amaranto.RegistrarPendientes400());

                    SubeLocalANube400 camion = new SubeLocalANube400();
                    camion.execute(Cadenorra, tuyas.getString(getResources().getString(R.string.PropiedadTracker),""));
                }
                amaranto.cerrar();
            }
        }
    }
    private class GuardaEnLocal extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... params) {
            String granada = "malito";
            BDayudame amaranto = new BDayudame(Actualiza.this);
            amaranto.abrir();

            long ep = amaranto.registrar(tuyas.getString(getResources().getString(R.string.PropiedadTracker),""),Nuevo.getLatitud(),Nuevo.getLonguitud());
             if (ep >0) {
                 granada = String.valueOf(ep) + " registro creado";

                 Anterior=Nuevo;
                 ActualizameYa=0;

             }else{
                 granada = "Error en Base Local";
             }
                 amaranto.cerrar();

             return granada;
        }

        protected void onPostExecute(String result) {
            Anterior=Nuevo;
            ActualizameYa=0;
        }
    }
    private class ActualizaEnLocal extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... params) {
            String granada = "malito";
            BDayudame amaranto = new BDayudame(Actualiza.this);
            amaranto.abrir();

            Cursor Mijail = amaranto.actualizar(tuyas.getString(getResources().getString(R.string.PropiedadTracker),""));

                perico ++;
                Anterior=Nuevo;
                ActualizameYa=0;

            amaranto.cerrar();
            return granada;
        }

        protected void onPostExecute(String result) {

        }
    }
    private class BuscaOrdenesEnNube extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... params) {


            String resul = "empanao";

            final String METHOD_NAME = "TrazadorBuscaOrdenes";
            final String SOAP_ACTION = tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1")+"/TrazadorBuscaOrdenes";

            SoapObject request = new SoapObject(tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1"), METHOD_NAME);
            request.addProperty("trazador",params[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(tuyas.getString(getResources().getString(R.string.PropiedadURL), "1"));

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject ic = (SoapObject)response;
                SharedPreferences.Editor editorial = tuyas.edit();
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito1),ic.getProperty(0).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito1),ic.getProperty(1).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito2),ic.getProperty(2).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito2),ic.getProperty(3).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito3),ic.getProperty(4).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito3),ic.getProperty(5).toString());

                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion1),ic.getProperty(6).toString());
                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion2),ic.getProperty(7).toString());
                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion3),ic.getProperty(8).toString());

                editorial.putString(getResources().getString(R.string.PropiedadLatitudInicioVuelta),ic.getProperty(9).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudInicioVuelta),ic.getProperty(10).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudFinVuelta),ic.getProperty(11).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudFinVuelta),ic.getProperty(12).toString());

                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj1),ic.getProperty(13).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj1),ic.getProperty(14).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj2),ic.getProperty(15).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj2),ic.getProperty(16).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj3),ic.getProperty(17).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj3),ic.getProperty(18).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj4),ic.getProperty(19) .toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj4),ic.getProperty(20).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj5),ic.getProperty(21).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj5),ic.getProperty(22).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj6),ic.getProperty(23).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj6),ic.getProperty(24).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj7),ic.getProperty(25).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj7),ic.getProperty(26).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj8),ic.getProperty(27).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj8),ic.getProperty(28).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj9),ic.getProperty(29).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj9),ic.getProperty(30).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj10),ic.getProperty(31).toString());
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj10),ic.getProperty(32).toString());
                editorial.apply();

            } catch (Exception e) {
                resul = "0";
            }
            return resul;
        }


        protected void onPostExecute(String result) {


            }
        }
    private class EnvioMensaje extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... params) {

            String resul = "1";

            final String METHOD_NAME = "SolicitudEnvioASociosNotificacionesFCM";
            final String SOAP_ACTION = tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1")+"/SolicitudEnvioASociosNotificacionesFCM";

            SoapObject request = new SoapObject(tuyas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1"), METHOD_NAME);
            request.addProperty("trazatore", params[0]);
            request.addProperty("mensajito", params[1]);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(tuyas.getString(getResources().getString(R.string.PropiedadURL), "1"));
            int paco = 0;

            try {
                transporte.call(SOAP_ACTION, envelope);


            } catch (Exception e) {
                resul = "0";
            }
            return resul;
        }
        protected void onPostExecute(String result) {
            if (result.equals("1")){
            }

        }
    }

    public double Redondear(double numero) {
        return Math.rint(numero * 1000000) / 1000000;
    }
    public String CreadorDeCadenas(Cursor pendientes) {
        String cadena = "";

        if (pendientes.moveToFirst()) {
            do {
                String Tiempo = String.valueOf(pendientes.getLong(0));
                String Idu = (pendientes.getString(1));
                String latitu = String.valueOf(pendientes.getDouble(2));
                String longuitu = String.valueOf(pendientes.getDouble(3));
                cadena =cadena + Tiempo + "$"+latitu + "$"+longuitu +"$#";

            }
            while (pendientes.moveToNext());
        }

return cadena;

}
    public boolean EsProximoAHitos (){
        boolean sera=false;
        int tamaño =ListadoRelojes.size();

        for (int p = 0; p < tamaño; p++) {
            if (Nuevo.DistanciaLineal(ListadoRelojes.get(p))<tuyas.getInt(getResources().getString(R.string.PropiedadRadioEnMetrosRelojes),1)){

                sera=true;
                break;
                  }
        }
          return sera ;
        // Devuelve true si el trazador se aproxima a cualquier hito una distancia inferior a PropiedadRadioEnMetros
      }
    public int NivelCargaBateria()    {
        try
        {
            IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent battery = this.registerReceiver(null, batIntentFilter);
            int nivelBateria = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            return nivelBateria;
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),
                    "Error al obtener estado de la batería",
                    Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
    TimerTask RutinaRegistrosEnLocal1 = new TimerTask() {
        @Override
        public void run() {
                //// Filtro para permitir la actualizacion,,,, que la lectura sea correcta y no este a menos de 12 metros de la ultima actualizacion
            if (!latitude.equals("12") && !EnReposo()) {
                GuardaEnLocal capote = new GuardaEnLocal();
                capote.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker),""), String.valueOf(Nuevo.getLatitud()), String.valueOf(Nuevo.getLonguitud()));
            } else {
                ActualizameYa++;
                if (ActualizameYa > 2) { /// Actualiza una posicion sin movimiento a los cinco minutos de reposo
                    if (!longuitude.equals("13") && !String.valueOf(Anterior.getLonguitud()).equals("0")) { /// No actualiza si persiste el punto por defecto (12,13)
                        if (perico == 0) {
                            GuardaEnLocal capilla = new GuardaEnLocal();
                            capilla.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker),""), String.valueOf(Nuevo.getLatitud()), String.valueOf(Nuevo.getLonguitud()));
                            ActualizameYa = 0;
                        } else {
                            ActualizaEnLocal yambae = new ActualizaEnLocal();

                            yambae.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker),""));
                        }
                        ActualizameYa = 0;
                    }
                }
            }
        }
    };
    TimerTask RutinaSubidaANube = new TimerTask() {
        @Override
        public void run() {
            BDayudame amaranto = new BDayudame(Actualiza.this);
            amaranto.abrir();
            // SI hay mas de 500 registros en la base local ,,, subiremos los datos por paquetes
            if (amaranto.CuantosHay()==1 && EnReposo()) {
                ///// Nada ////// Dejarlo pasar sin subir a la nube
                    }else {
                             if (amaranto.CuantosHay() < 400) {
                                 String Cadenita = CreadorDeCadenas(amaranto.RegistrarPendientes());

                                 SubeLocalANube taxi = new SubeLocalANube();
                                 taxi.execute(Cadenita, tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""));
                                  } else {
                                         String Cadenota = CreadorDeCadenas(amaranto.RegistrarPendientes400());

                                         SubeLocalANube400 trufi = new SubeLocalANube400();
                                         trufi.execute(Cadenota, tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""));
                                            }
            }
            amaranto.cerrar();
        }
    };
    TimerTask RutinaNotificaciones = new TimerTask() {
        @Override
        public void run() {        // En la proximidad a los hitos,,, se aumentara la frecuencia de lecturas.
            if (HaCambiadoNuevoDesdeOnCreate()) {

                if (EnReposo()) if (EsProximoAHitos()) {
                    GuardaEnLocal capote = new GuardaEnLocal();
                    capote.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""), String.valueOf(Nuevo.getLatitud()), String.valueOf(Nuevo.getLonguitud()));
                }

                if (Nuevo.DistanciaLineal(Hito1) < tuyas.getInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito1), 0) && System.currentTimeMillis() > tuyas.getLong(getResources().getString(R.string.PropiedadTiempoDescansoDeNotificacionesHito1), 1)) {
                    // NotificacionLlegadaHito1Enviada = false;
                    EnvioMensaje LlegadaHITO1 = new EnvioMensaje();
                    LlegadaHITO1.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""), tuyas.getString(getResources().getString(R.string.PropiedadTracker), "ER") + "  en " + tuyas.getString(getResources().getString(R.string.PropiedadNombreUbicacion1), ""));

                    SharedPreferences.Editor editorialo = tuyas.edit();
                    editorialo.putLong(getResources().getString(R.string.PropiedadTiempoDescansoDeNotificacionesHito1), System.currentTimeMillis() + PermanenciaOchoHoras1);
                    editorialo.apply();
                }

                if (Nuevo.DistanciaLineal(Hito2) < tuyas.getInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito2), 0) && System.currentTimeMillis() > tuyas.getLong(getResources().getString(R.string.PropiedadTiempoDescansoDeNotificacionesHito2), 1)) {
                    // NotificacionLlegadaHito2Enviada = false;
                    EnvioMensaje LlegadaHITO2 = new EnvioMensaje();
                    LlegadaHITO2.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""), tuyas.getString(getResources().getString(R.string.PropiedadTracker), "ER") + "  en " + tuyas.getString(getResources().getString(R.string.PropiedadNombreUbicacion2), ""));

                    SharedPreferences.Editor editorialo = tuyas.edit();
                    editorialo.putLong(getResources().getString(R.string.PropiedadTiempoDescansoDeNotificacionesHito2), System.currentTimeMillis() + PermanenciaOchoHoras2);
                    editorialo.apply();
                }
                if (Nuevo.DistanciaLineal(Hito3) < tuyas.getInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito3), 0) && System.currentTimeMillis() > tuyas.getLong(getResources().getString(R.string.PropiedadTiempoDescansoDeNotificacionesHito3), 1)) {
                    // NotificacionLlegadaHito3Enviada = false;
                    EnvioMensaje LlegadaHITO3 = new EnvioMensaje();
                    LlegadaHITO3.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""), tuyas.getString(getResources().getString(R.string.PropiedadTracker), "ER") + "  en " + tuyas.getString(getResources().getString(R.string.PropiedadNombreUbicacion3), ""));

                    SharedPreferences.Editor editorialo = tuyas.edit();
                    editorialo.putLong(getResources().getString(R.string.PropiedadTiempoDescansoDeNotificacionesHito3), System.currentTimeMillis() + PermanenciaOchoHoras3);
                    editorialo.apply();
                }


                if (NivelCargaBateria() < 20 && numeroavisos < 2) {
                    EnvioMensaje bateriabaja = new EnvioMensaje();
                    bateriabaja.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""), tuyas.getString(getResources().getString(R.string.PropiedadTracker), "Error") + " con Bateria Muy Baja");
                    numeroavisos++;
                }
                // Ponemos a cero el contador cuando la carga de la bateria supera el 60%
                if (NivelCargaBateria() > 60) {
                    numeroavisos = 0;
                }
                /// Envia una actualizacion inmediata a la nube a petidion del propietario,,, via Notificacion
                if (tuyas.getString(getResources().getString(R.string.PropiedadREFRESH), "").equals("Metale")) {
                    ActualizaRapidin cemelo = new ActualizaRapidin();
                    cemelo.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""), latitude, longuitude);
                    SharedPreferences.Editor emil = tuyas.edit();
                    emil.putString(getResources().getString(R.string.PropiedadREFRESH), "EnEspera");
                    emil.apply();
                }
            }
                /// Primero chequeamos si el vehiculo ha esta parado por mas de diez minutos,,,,

                    SharedPreferences.Editor emil = tuyas.edit();
                    if (EnReposoMasDeDiezMinutos()) {
                             emil.putBoolean(getResources().getString(R.string.PropiedadEstaEnReposo), true);
                        }else{
                            emil.putBoolean(getResources().getString(R.string.PropiedadEstaEnReposo), false);}
                    emil.apply();

        }
    };
    TimerTask RutinaGuardiaEnReposo = new TimerTask() {
            @Override
            public void run() {
                // Subir lo que haya en local
                BDayudame amaranto = new BDayudame(Actualiza.this);
                amaranto.abrir();

                if (amaranto.CuantosHay() < 400) {
                    String Cadenita = CreadorDeCadenas(amaranto.RegistrarPendientes());

                    SubeLocalANube taxi = new SubeLocalANube();
                    taxi.execute(Cadenita, tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""));
                } else {
                    String Cadenota = CreadorDeCadenas(amaranto.RegistrarPendientes400());
                    SubeLocalANube400 trufi = new SubeLocalANube400();
                    trufi.execute(Cadenota, tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""));
                }
                amaranto.cerrar();
            }
        };
    TimerTask RutinaBuscarOrdenesEnServidor = new TimerTask() {
        @Override
        public void run() {
            BuscaOrdenesEnNube mirada = new BuscaOrdenesEnNube();
            mirada.execute(tuyas.getString(getResources().getString(R.string.PropiedadTracker), ""));
            CreaciondePuntos();
        }
    };


    public boolean EnReposo(){
       return Nuevo.DistanciaLineal(Anterior) < distanciaminima;
    }
    public boolean EnReposoMasDeDiezMinutos(){
        if(!EnReposo()){TiempoReposo= System.currentTimeMillis();}
        return System.currentTimeMillis()-TiempoReposo >100000;
    }
    public boolean HaCambiadoNuevoDesdeOnCreate(){


                 return !Nuevo.getId().equals("2");
    }
    public void CreaciondePuntos (){

        ListadoRelojes = new ArrayList<Punto>();

        Hito1 =new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudHito1), "5")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudHito1),"5")));
        Hito2 =new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudHito2), "5")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudHito2),"5")));
        Hito3=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudHito3), "5")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudHito3),"5")));

        Punto Reloj1=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj1), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj1),"2")));
        Punto Reloj2=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj2), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj2),"2")));
        Punto Reloj3=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj3), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj3),"2")));
        Punto Reloj4=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj4), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj4),"2")));
        Punto Reloj5=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj5), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj5),"2")));
        Punto Reloj6=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj6), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj6),"2")));
        Punto Reloj7=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj7), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj7),"2")));
        Punto Reloj8=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj8), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj8),"2")));
        Punto Reloj9=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj9), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj9),"2")));
        Punto Reloj10=new Punto("1", 0, Double.parseDouble(tuyas.getString(getResources().getString(R.string.PropiedadLatitudReloj10), "2")),Double.parseDouble( tuyas.getString(getResources().getString(R.string.PropiedadLonguitudReloj10),"2")));
        // Punto pelota = new Punto ("0",1,-17.847509,-63.111670);
        ListadoRelojes.add(Reloj1);  ListadoRelojes.add(Reloj2);  ListadoRelojes.add(Reloj8);  ListadoRelojes.add(Reloj4);  ListadoRelojes.add(Reloj5);  ListadoRelojes.add(Reloj6);  ListadoRelojes.add(Reloj7);  ListadoRelojes.add(Reloj10);  ListadoRelojes.add(Reloj9);  ListadoRelojes.add(Reloj3);
    }


}
