package com.romane.piedra;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {

    SharedPreferences propias;
    // Cambiar por verdaderas coordenadas
    Double LatitudHito1 = -17.847509;
    Double LonguitudHito1 =-63.111670;
   EditText Eto;
    TextView TV1,TV2,TV3,TV4,TV5,TV6,TV10;
    String info;
    SharedPreferences tuyas;
    Timer temporero = new Timer();
    TimerTask rutina = new TimerTask() {
        @Override
        public void run() {
            BDayudame carion = new BDayudame(MainActivity.this);
           carion.abrir();
            long aruba = carion.CuantosHay();
            info = Long.toString(aruba);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Eto.setText(String.valueOf(info));
                    TV1.setText(tuyas.getString(getResources().getString(R.string.PropiedadREFRESH),""));
                    TV2.setText(tuyas.getString(getResources().getString(R.string.PropiedadNombreUbicacion1),""));
                 //   TV3.setText(tuyas.getString(getResources().getString(R.string.PropiedadLatitudHito1),""));
                    TV4.setText(tuyas.getString(getResources().getString(R.string.PropiedadLonguitudHito2),""));

                    if(EstaFuncionando(Actualiza.class)){
                            TV1.setBackgroundColor(Color.YELLOW);
                        }else{
                            TV1.setBackgroundColor(Color.RED);

                    }

                    TV6.setText(tuyas.getString("prova",""));

                    ///PRUEBA - Cambia el texto dependiendo del mensaje recibido

                    TV3.setText("F. Local " + tuyas.getInt(getResources().getString(R.string.PropiedadFrecuenciaActualizacionEnLocal),5000));
                    TV4.setText("F. ANube " + tuyas.getInt(getResources().getString(R.string.PropiedadFrecuenciaSubirDatosANube),5000));
                    TV5.setText("F. ANube " + tuyas.getInt(getResources().getString(R.string.PropiedadFrecuenciaScaneo),900));
                    TV10.setText(tuyas.getString(getResources().getString(R.string.PropiedadURL),"ll"));


                }
                });

           carion.cerrar();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tuyas= PreferenceManager.getDefaultSharedPreferences(this);
        if (tuyas.getString(getResources().getString(R.string.PropiedadTracker), "1").equals("1")) {
            //Verificamos que la aplicacion tiene numero de trazador asignado,,, de lo contrario abrimos
            //Introducimos la direccion inicial del URL via Shared Preferences
            SharedPreferences.Editor edita = tuyas.edit();
            edita.putString(getResources().getString(R.string.PropiedadURL),"http://sareta.somee.com/MicroMundoGPS/Galileo.asmx");
            edita.putString(getResources().getString(R.string.PropiedadNAMESPACE),"Tormenta");
            edita.apply();

            //la actividad de bienvenida para la personalizacion de la actividad


            Intent Area = new Intent("android.intent.action.Iniciax");
            startActivity(Area);
        }else {
            setContentView(R.layout.activity_main);
            Eto = (EditText) findViewById(R.id.Eto);
            TV1 = (TextView)findViewById(R.id.TV1);
            TV2 = (TextView)findViewById(R.id.TV2);
            TV3 = (TextView)findViewById(R.id.TV3);
            TV4 = (TextView)findViewById(R.id.TV4);
            TV5 = (TextView)findViewById(R.id.TV5);
            TV6 = (TextView)findViewById(R.id.TV6);
            TV10 = (TextView)findViewById(R.id.TV10);

          //  Chismoso pepon = new Chismoso();

            temporero.scheduleAtFixedRate(rutina, 2000, 500); // Temporizador para periodo de pruebas

        // Se inicializa el AlarmManager enviando una alarma cada 60 segundos para que se revise el estado de la aplicacion Actualiza.
        }
    }

    private boolean EstaFuncionando(Class<?> nombreApp) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (nombreApp.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public  void Ataca(String orden){
        if (orden.equals("negro")){
            TV10.setText("negrito");
        }
        if (orden.equals("rojo")){
            TV10.setText("rojito");
        }
        if (orden.equals("verde")){
            TV10.setText("verdecito");
        }

    }
}
