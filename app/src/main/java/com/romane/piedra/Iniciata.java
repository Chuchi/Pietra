package com.romane.piedra;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Iniciata extends AppCompatActivity implements View.OnClickListener{

    private static final long Ticks_Hasta_1Enero1970 = 621355968000000000L;

    Button BTN1;
    TextView TV2;
    EditText ET1,ET2;
    TextInputLayout Cortina3;
    SharedPreferences preferidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciata);
        BTN1 = (Button)findViewById(R.id.BTN1);
        ET1 = (EditText)findViewById(R.id.ET1);
        ET2 = (EditText)findViewById(R.id.ET2);
        TV2 = (TextView) findViewById(R.id.TV2);
        Cortina3=(TextInputLayout)findViewById(R.id.Cortina3);

        String camino = "Verno/Roboto-Light.ttf";

        preferidas =  PreferenceManager.getDefaultSharedPreferences(this);

        Typeface TF = Typeface.createFromAsset(getAssets(),camino);

        TV2.setTypeface(TF);
        ET1.setTypeface(TF);
        ET2.setTypeface(TF);
        BTN1.requestFocus();


        TextWatcher Kant = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 >3){
                    Cortina3.setVisibility(View.VISIBLE);
                    ET2.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        TextWatcher Kunt = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2> 5){

                    BTN1.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(BTN1.getWindowToken(), 0);}
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        BTN1.setOnClickListener(this);


        ET1.addTextChangedListener(Kant);
        ET2.addTextChangedListener(Kunt);

    }

    @Override
    public void onClick(View view) {

        BTN1.setVisibility(View.INVISIBLE);
        PreguntaAzure horitinga = new PreguntaAzure();
        horitinga.execute(ET1.getText().toString(),ET2.getText().toString());
    }

    private class PreguntaAzure extends AsyncTask<String, Integer, String> {
        String resultado = "";

        protected String doInBackground(String... params) {

            long[] merco;

            final String METHOD_NAME = "IniciaAplicacionTrazador";
            final String SOAP_ACTION = preferidas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1")+"/IniciaAplicacionTrazador";

            SoapObject request = new SoapObject(preferidas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1"), METHOD_NAME);

            request.addProperty("trazador", params[0]);
            request.addProperty("pass", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(preferidas.getString(getResources().getString(R.string.PropiedadURL), "1"));
            int paco = 0;

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                resultado = response.toString();
            }
            catch (Exception e) {
                merco = new long[] {};
            }
            return resultado;
        }


        protected void onPostExecute(String result) {
            if (result.equals("1")) {

                SharedPreferences.Editor editorial = preferidas.edit();
                Double LatitudHito1 = -0.0;
                Double LonguitudHito1 =-0.0;
                Double LatitudHito2 = 0.0;
                Double LonguitudHito2 =0.0;
                Double LatitudHito3 = 0.0;
                Double LonguitudHito3 =0.0;

                editorial.putString(getResources().getString(R.string.PropiedadTracker),ET1.getText().toString());
                editorial.putString(getResources().getString(R.string.PropiedadVersion), "A");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito1),String.valueOf(LatitudHito1));
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito1),String.valueOf(LonguitudHito1));
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito2),String.valueOf(LatitudHito2));
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito2),String.valueOf(LonguitudHito2));
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito3),String.valueOf(LatitudHito3));
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito3),String.valueOf(LonguitudHito3));
                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion1),"Ubicacion1");
                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion2),"Ubicacion2");
                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion3),"Ubicacion3");
                editorial.putInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito1),10);
                editorial.putInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito2),10);
                editorial.putInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito3),10);

                editorial.putInt(getResources().getString(R.string.PropiedadRadioEnMetrosRelojes),300);

                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj1),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj1),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj2),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj2),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj3),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj3),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj4),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj4),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj5),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj5),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj6),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj6),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj7),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj7),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj8),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj8),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj9),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj9),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudReloj10),"2");
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudReloj10),"2");
                // Introduce por defecto una frecuencia de 2.5 minutos para actualizar datos en local
                editorial.putInt(getResources().getString(R.string.PropiedadFrecuenciaActualizacionEnLocal),150003);
                // Introduce por defecto una frecuencia de 10 minutos para subir datos a nube
                editorial.putInt(getResources().getString(R.string.PropiedadFrecuenciaSubirDatosANube),600004);
                // Introduce por defecto una frecuencia de 0.9 seg  para el scaneo
                editorial.putInt(getResources().getString(R.string.PropiedadFrecuenciaScaneo),900);
                // Introduce por defecto una frecuencia de 12 horas para subir datos a nube
                editorial.putInt(getResources().getString(R.string.PropiedadFrecuenciaRecibirOrdenes),43200000);
                // Introduce por defecto una frecuencia de 1 hora actualizacion de seguridad (Guardia)
                editorial.putInt(getResources().getString(R.string.PropiedadFrecuenciaGuardia),3600000);

                editorial.putString("prova","cerca");
                 // Estas shared preferences son para la identificacion de mensajes llegados via FCM
                editorial.putString(getResources().getString(R.string.PropiedadColorin),"Nada");



                editorial.apply();

                TareaRegistrarEnServidorLaClaveFCMTrazador calmita = new TareaRegistrarEnServidorLaClaveFCMTrazador();
                calmita.execute(preferidas.getString(getResources().getString(R.string.PropiedadTracker),""));

            }
            else {
                Toast.makeText(Iniciata.this, "Proceso FALLIDO", Toast.LENGTH_LONG).show();

                Iniciata.this.finish();
            }
        }
    }

    public class TareaRegistrarEnServidorLaClaveFCMTrazador extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String msg = "";
            String ClaveFCM = FirebaseInstanceId.getInstance().getToken();


            //Nos registramos en nuestro servidor
            boolean registrado = RegistroClaveFcmTrazadorEnServidor(params[0], ClaveFCM);

            if (registrado) {
                msg = "Exitoso";
            }
            return msg;
        }

        protected void onPostExecute(String result) {
            if (result.equals("Exitoso")) {
                Toast.makeText(Iniciata.this, "PROCESO EXITOSO", Toast.LENGTH_LONG).show();

                Iniciata.this.finish();
            } else {
                Toast.makeText(Iniciata.this, "PROCESO FALLIDO", Toast.LENGTH_LONG).show();

                Iniciata.this.finish();
            }

        }
    }


    public  boolean RegistroClaveFcmTrazadorEnServidor(String traza, String clafcm)
    // Registra el RegistroID por primera vez en el servidor
    {
        boolean reg = false;

        final String METHOD_NAME = "RegistroClaveFCMTrazador";
        final String SOAP_ACTION = preferidas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1")+"/RegistroClaveFCMTrazador";

        SoapObject request = new SoapObject(preferidas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1"), METHOD_NAME);

        request.addProperty("trazador", traza);
        request.addProperty("clavefcm", clafcm);

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(preferidas.getString(getResources().getString(R.string.PropiedadURL), "1"));

        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapPrimitive resultadoxml = (SoapPrimitive) envelope.getResponse();
            String res = resultadoxml.toString();


        } catch (Exception e) {
            //   Toast.makeText(MapsActivity.this, "Mal", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
