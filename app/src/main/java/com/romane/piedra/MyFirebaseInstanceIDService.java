package com.romane.piedra;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
       SharedPreferences perfidia = getSharedPreferences("Valentia", Context.MODE_PRIVATE);
        String ClaveFCM = FirebaseInstanceId.getInstance().getToken();
        boolean amauta =RegistroClaveFcmEnServidor(perfidia.getString(getResources().getString(R.string.PropiedadTracker),""),ClaveFCM);


    }


    public  boolean RegistroClaveFcmEnServidor(String traza, String clafcm)
    // Registra el RegistroID por primera vez en el servidor
    {
        SharedPreferences estitas= PreferenceManager.getDefaultSharedPreferences(this);
        boolean reg = false;
        final String METHOD_NAME = "RegistroClaveFCMTrazador";
        final String SOAP_ACTION = estitas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1")+"/RegistroClaveFCMTrazador";

        SoapObject request = new SoapObject(estitas.getString(getResources().getString(R.string.PropiedadNAMESPACE), "1"), METHOD_NAME);
        request.addProperty("trazador", traza);
        request.addProperty("clavefcm", clafcm);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(estitas.getString(getResources().getString(R.string.PropiedadURL), "1"));

        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapPrimitive resultadoxml = (SoapPrimitive) envelope.getResponse();
            String res = resultadoxml.toString();


        } catch (Exception e) {
            //   Toast.makeText(MapsActivity.this, "Mal", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}// Get updated InstanceID token.
