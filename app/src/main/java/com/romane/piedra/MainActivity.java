package com.romane.piedra;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    SharedPreferences propias;
    // Cambiar por verdaderas coordenadas
    Double LatitudHito1 = -17.847509;
    Double LonguitudHito1 = -63.111670;
    RelativeLayout RL100;
    ImageView IMV10;
    TextView TV10, TV11, TV12;
    TextClock CLOCK10;
    Typeface sare;


    String info;
    Boolean pepsi = true;
    int BanderinRepos1 = 0;
    int BanderinRepos2 = 0;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
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
                    TV12.setText(info);
                    if (EstaFuncionando(Actualiza.class)) {
                        if ((tuyas.getBoolean(getResources().getString(R.string.PropiedadEstaEnReposo), false)) && BanderinRepos1 == 0) {
                            RL100.setKeepScreenOn(false);
                            BanderinRepos1 = 1;
                            BanderinRepos2 = 0;
                        }
                        if ((!tuyas.getBoolean(getResources().getString(R.string.PropiedadEstaEnReposo), false)) && BanderinRepos2 == 0) {
                            mWakeLock.acquire(20000);
                            RL100.setKeepScreenOn(true);
                            BanderinRepos2 = 1;
                            BanderinRepos1 = 0;
                        }

                        if (BanderinRepos1 == 1) {
                            CLOCK10.setTextColor(getResources().getColor(R.color.Ciano));
                        }
                        if (BanderinRepos2 == 1) {
                            CLOCK10.setTextColor(getResources().getColor(R.color.Verde));
                        }

                    } else {
                        CLOCK10.setTextColor(getResources().getColor(R.color.RojoClaro));
                        RL100.setKeepScreenOn(true);
                    }

                    if (tuyas.getString(getResources().getString(R.string.PropiedadPasarIU), "0").equals("1")) {
                        IMV10.setVisibility(View.VISIBLE);
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 500);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);
                        SharedPreferences.Editor edita = tuyas.edit();
                        edita.putString(getResources().getString(R.string.PropiedadPasarIU), "0");
                        edita.apply();
                    } else {
                        IMV10.setVisibility(View.INVISIBLE);
                    }


                }
            });

            carion.cerrar();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Service");


        super.onCreate(savedInstanceState);

        mWakeLock.acquire(20000);

        tuyas = PreferenceManager.getDefaultSharedPreferences(this);
        if (tuyas.getString(getResources().getString(R.string.PropiedadTracker), "1").equals("1")) {
            //Verificamos que la aplicacion tiene numero de trazador asignado,,, de lo contrario abrimos
            //Introducimos la direccion inicial del URL via Shared Preferences
            SharedPreferences.Editor edita = tuyas.edit();
            edita.putString(getResources().getString(R.string.PropiedadURL), "http://sareta.somee.com/MicroMundoGPS/Galileo.asmx");
            edita.putString(getResources().getString(R.string.PropiedadNAMESPACE), "Tormenta");
            edita.apply();

            //la actividad de bienvenida para la personalizacion de la actividad


            Intent Area = new Intent("android.intent.action.Iniciax");
            startActivity(Area);
        } else {
            setContentView(R.layout.activity_main);
            RL100 = (RelativeLayout) findViewById(R.id.RL100);
            IMV10 = (ImageView) findViewById(R.id.IMV10);
            TV10 = (TextView) findViewById(R.id.TV10);
            TV11 = (TextView) findViewById(R.id.TV11);
            TV12 = (TextView) findViewById(R.id.TV12);
            CLOCK10 = (TextClock) findViewById(R.id.CLOCK10);

            String saira = "Verno/saira.ttf";
            sare = Typeface.createFromAsset(getAssets(), saira);

            CLOCK10.setTypeface(sare);

            // Si el servicio Actualiza no esta trabajando y el servicio de Ubicacion esta dispopnible y trabajando,,,, inicia el servicio Actualiza

            ActivaActulizaSiProcede();

            temporero.scheduleAtFixedRate(rutina, 2000, 1000); // Temporizador para periodo de pruebas

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

    public void Ataca(String orden) {
        if (orden.equals("negro")) {
            TV10.setText("negrito");
        }
        if (orden.equals("rojo")) {
            TV10.setText("rojito");
        }
        if (orden.equals("verde")) {
            TV10.setText("verdecito");
        }

    }

    private boolean isMyServiceRunning(Class<?> nombrecito, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (nombrecito.getName().equals(service.service.getClassName())) {
                Log.i("Service already", "running");
                return true;
            }
        }
        Log.i("Service not", "running");
        return false;
    }

    public void ActivaActulizaSiProcede() {

        LocationManager ambur = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean petrus = ambur.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean EstaActualizaTrabajando = isMyServiceRunning(Actualiza.class, this);
        Intent intentMemoryServiceII = new Intent(this, Actualiza.class);

                if(petrus) {
                        if (!EstaActualizaTrabajando) {this.startService(intentMemoryServiceII);
                        }
               }else
                    {
                        this.stopService(intentMemoryServiceII);
                    }
    }

}
