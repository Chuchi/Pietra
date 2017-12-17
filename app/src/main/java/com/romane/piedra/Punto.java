package com.romane.piedra;


import com.google.android.gms.maps.model.LatLng;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by oficina on 21/07/2016.
 */
public class Punto {

    //  Constantes para la conversion de tiempos entre Java y MicroSoft
    private static final long Ticks_Hasta_1Enero1970 = 621355968000000000L;
    private static final long Millis_Hasta_1Enero1970 = 62135596800000L;
    // Provisional
    private static final long Ticks_Ajuste_seis_horas = 216000000000L;


    // Atributos de la clase punto
    public String Id;
    public long Tempo;  //// tiempo expresado en ticks,,,, MicroSoft - 18 digitos
    public double Latitud;
    public double Longuitud;


    // Construictor
    public Punto() {
        this.Id = "";
        this.Tempo = 0;
        this.Latitud = 0;
        this.Longuitud = 0;

    }

    // Constructor
    public Punto(String id, long tempo, double latitud, double longuitud) {
        this.Id = id;
        this.Tempo = tempo;
        this.Latitud = latitud;
        this.Longuitud = longuitud;

    }


    // setter y getters de la clase Punto

    public void setId(String id) {
        this.Id = id;
    }

    public void setId(long tempo) {
        this.Tempo = tempo;
    }

    public void setLatitud(double latitud) {
        this.Latitud = latitud;
    }

    public void setLonguitud(double longuitud) {
        this.Longuitud = longuitud;
    }

    public String getId() {
        return this.Id;
    }

    public long getTempo() {
        return this.Tempo;
    }

    public double getLatitud() {
        return this.Latitud;
    }

    public double getLonguitud() {
        return this.Longuitud;
    }


    /// Metodo que calcula la distancia a un punto B
    public double DistanciaLineal(Punto puntoB) {

        double lat1 = this.Latitud;
        double lat2 = puntoB.Latitud;

        double lng1 = this.Longuitud;
        double lng2 = puntoB.Longuitud;


        //double radioTierra = 3958.75;//en millas
        double radioTierra = 6371;//en kilómetros
        double dLat = ConvertDegreesToRadians(lat2 - lat1);
        double dLng = ConvertDegreesToRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(ConvertDegreesToRadians(lat1)) * Math.cos(ConvertDegreesToRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2 * 1000;


        return distancia;  /// en metros.
    }

    public static double DistanciaLinealEntreDosPuntos(Punto puntoA, Punto puntoB) {

        double lat1 = puntoA.Latitud;
        double lat2 = puntoB.Latitud;

        double lng1 = puntoA.Longuitud;
        double lng2 = puntoB.Longuitud;


        //double radioTierra = 3958.75;//en millas
        double radioTierra = 6371;//en kilómetros
        double dLat = ConvertDegreesToRadians(lat2 - lat1);
        double dLng = ConvertDegreesToRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(ConvertDegreesToRadians(lat1)) * Math.cos(ConvertDegreesToRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2 * 1000;


        return distancia;  /// en metros.
    }
    // Convierte grados en radianes

    public static double ConvertDegreesToRadians(double degrees) {

        double radians = (Math.PI / 180) * degrees;
        return (radians);
    }

    // retorna verdadero si el punto esta en un radio de 60 metros respecto del puntoK

    public Boolean EsProximo(Punto puntoK) {

        Boolean caperucita = false;
        if (this.DistanciaLineal(puntoK) < 60) {
            caperucita = true;
        }

        return caperucita;
    }


    // Retorna el punto mas cercano al toten puntoK
    public Punto ElMasProximo(Punto puntoB, List<Punto> listado) {
        double min = 2000;
        int tamaño = listado.size();
        Punto puntoMinimo = new Punto();
        Punto listear = new Punto();

        for (int p = 0; p < tamaño; p++) {

            listear = listado.get(p);

            if (listear.DistanciaLineal(puntoB) < min) {
                min = listear.DistanciaLineal(puntoB);
                puntoMinimo = listear;
            }
        }
        return puntoMinimo;

    }

    public Double DistanciaAlMasProximo( List<Punto> listado) {
        double min = 2000;
        int tamaño = listado.size();
        Punto puntoMinimo = new Punto();
        Punto listear = new Punto();

        for (int p = 0; p < tamaño; p++) {

            listear = listado.get(p);

            if (listear.DistanciaLineal(this) < min) {
                min = listear.DistanciaLineal(this);
                puntoMinimo = listear;
            }
        }
        return this.DistanciaLineal(listear);

    }
    // Retorna un string que representa el tiempo de lectura en HH:MM ,,, en Bolivia (UTC-4)

    public String TempoLectura() {
        //  Primero descontamos los ticks hasta Enero de 1970
        long millisJAVA = (this.Tempo - Ticks_Hasta_1Enero1970) / 10000;
        // Debido a un desfase,,,, hay que quitarle seis horas
        String horita = "Sin Lectura";

        //  Chequeamos si es una lectura antigua

        SimpleDateFormat horinga = new SimpleDateFormat("HH:mm");

        Date resultdate = new Date(millisJAVA);
        horita = horinga.format(resultdate);

        return horita;

    }

    public boolean ActualizadoHaceQuinceMinutos() {
        long QuinceMinutosMillis = 900000; // quince minutos en Millis
        boolean sera = false;
        //  Primero descontamos los ticks hasta Enero de 1970
        long millisJAVA = (this.Tempo - Ticks_Hasta_1Enero1970) / 10000;
        if (System.currentTimeMillis() - millisJAVA < QuinceMinutosMillis) {
            sera = true;
        }
        return sera;

    }

    public double DistanciaHito(LatLng coor) {

        double lat1 = this.Latitud;
        double lat2 = coor.latitude;

        double lng1 = this.Longuitud;
        double lng2 = coor.longitude;

        //double radioTierra = 3958.75;//en millas
        double radioTierra = 6371;//en kilómetros
        double dLat = ConvertDegreesToRadians(lat2 - lat1);
        double dLng = ConvertDegreesToRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(ConvertDegreesToRadians(lat1)) * Math.cos(ConvertDegreesToRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2 * 1000;


        return distancia;  /// en metros.
    }

    public Punto ElMasProximo(Punto puntoK, ArrayList<Punto> listado) {
        double min = 60;
        int tamaño = listado.size();
        Punto puntoMinimo = new Punto();
        Punto listear = new Punto();

        for (int p = 0; p < tamaño; p++) {

            listear = listado.get(p);

            if (listear.DistanciaLineal(puntoK) < min) {

                min = listear.DistanciaLineal(puntoK);
                puntoMinimo = listear;

            }
        }
        return puntoMinimo;

    }

    public static double DistanciaTotalRecorrida(ArrayList<Punto> listado)
    //Metodo static que calcula la disttancia total de un recorrido en metros / listado de puntos
    {
        double distancia = 0;
        int tamaño = listado.size();

        Punto ListaA = new Punto();
        Punto ListaB = new Punto();

        for (int p = 0; p < tamaño - 1; p++) {
            ListaA = listado.get(p);
            ListaB = listado.get(p + 1);
            distancia = distancia + DistanciaLinealEntreDosPuntos(ListaA, ListaB);
        }
        return distancia;
    }

    public static long TiempoTotalRecorridoMinutos(ArrayList<Punto> listado)
    //Calcula el tiempo del recorrido en MINUTOS / listado de puntos
    {
        return (HoraFinRecorridoTicks(listado) - HoraInicioRecorridoTicks(listado)) / 600000000;
    }


    public static long HoraInicioRecorridoTicks(ArrayList<Punto> listado)
    //Devuelve hora de inicio de recorrido en LONG
    {
        return listado.get(0).Tempo;
    }

    public static String HoraInicioRecorridoFECHA(ArrayList<Punto> listado)
    //Devuelve hora de inicio en formato STRING
    {
        return DameFecha2(listado.get(0).Tempo);
    }

    public static long HoraFinRecorridoTicks(ArrayList<Punto> listado)
    //Devuelve hora del final del recorrido en LONG
    {
        return listado.get(listado.size() - 1).Tempo;
    }

    public static String HoraFinRecorridoFECHA(ArrayList<Punto> listado)
    //Devuelve hora de inicio en formato STRING
    {
        return DameFecha2(listado.get(listado.size() - 1).Tempo);
    }


    public static double VelocidadMedia(ArrayList<Punto> listado)
    //Devuelve la velocidad media del recorrido en long - km/hora
    {

        if (TiempoTotalRecorridoMinutos(listado) == 0) {
            return 0;
        } else {
            return (DistanciaTotalRecorrida(listado) / 1000) / ((TiempoTotalRecorridoMinutos(listado)) / 60);
        }

    }


    // Conversor de millis a formato fecha
    // Al invocar este metodo,,, hay que pasar el formato en el que queremos la repuesta
    //MILISEGUNDOS
    public static String DameFecha(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formateador = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formateador.format(calendar.getTime());
    }

    //TICKS
    public static String DameFecha2(long tiempomicrosoftTicks) {

        long millisJAVA = (tiempomicrosoftTicks / 10000) - Millis_Hasta_1Enero1970;
        /// no descontamos el desfase horario
        ///Elegimos el formato en el que queremos nos devuelva la fecha
        SimpleDateFormat horinga = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        //La funcion CALENDAR calcula a partir de tiempo UTC,,,, y de hay lo pasa a las diferenctes frangas horarias
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisJAVA);

        return horinga.format(calendar.getTime());
    }


    public static void main(String[] args) {

        Punto uno = new Punto("a", 636120776074754847L, -17.779960, -63.147630);
        Punto dos = new Punto("b", 636120944309558182L, -17.746645, -63.161303);
        Punto tres = new Punto("c", 636120954309558182L, -17.746640, -67.16130);
        Punto alfa = new Punto("n",636120954309558182L, -19.746649, -67.161309);

        ArrayList<Punto> Marelio = new ArrayList<Punto>();
        Marelio.add(uno);
        Marelio.add(dos);
        Marelio.add(tres);
        String tracker = "AD256";
        long pepelo = System.currentTimeMillis();
        System.out.println(DameFecha2(636127150920683372L));
        System.out.println(HoraFinRecorridoTicks(Marelio));
        System.out.println(HoraInicioRecorridoFECHA(Marelio));
        System.out.println(DistanciaTotalRecorrida(Marelio));
        System.out.println(VelocidadMedia(Marelio));
        Double pep = alfa.DistanciaAlMasProximo(Marelio);
        System.out.println(tracker.substring(2));
        System.out.println( "Pepino "+ alfa.DistanciaAlMasProximo(Marelio));
    }
}















