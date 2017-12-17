package com.romane.piedra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDayudame extends SQLiteOpenHelper {

    public final long TicksHastaEnero1970= 621355968000000000L;
    Context ctx;
    String[] histo = {"Tempo","IdTrack","Latitud","Longuitud","Vario"};
    public BDayudame(Context context) {
        super(context, "MicroHistoricLocal7.db", null, 1);
        ctx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CreaTablaHistorico = "CREATE TABLE MicroHistoricLocal (Tempo long NOT NULL primary key, IdTrack text NOT NULL, Latitud double NOT NULL, Longuitud double NOT NULL, Vario text )";
        db.execSQL(CreaTablaHistorico);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST MicroHistoricLocal");
        onCreate(db);

    }

    BDayudame ayudame;
    SQLiteDatabase MiPrimeraBase;

    /*OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO*/
    public void abrir() {
        ayudame = new BDayudame(ctx);
        MiPrimeraBase = ayudame.getWritableDatabase();
    }

    /*OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO*/
    public void cerrar() {
        MiPrimeraBase.close();
    }

    public  boolean HayRegistros(){

        Cursor cursi  = MiPrimeraBase.rawQuery("SELECT * FROM MicroHistoricLocal", null);
        boolean carine = false;
        if (cursi != null && cursi.getCount() > 0){
        carine = true;
        }
        return carine;
    }

    public long registrar( String  tracker , double lati, double longui){

        long aumento =AcomodaMillis(tracker);
        ContentValues valores = new ContentValues();

        valores.put("Tempo",aumento);
        valores.put("IdTrack",tracker);
        valores.put("Latitud", lati);
        valores.put("Longuitud", longui);
        valores.put("Vario", "");

        return  MiPrimeraBase.insert("MicroHistoricLocal", null, valores);

    }

    public Cursor actualizar( String tracker){

        Cursor melia= MiPrimeraBase.rawQuery("Select MAX(Tempo) from MicroHistoricLocal", null);
        melia.moveToFirst();
        String Carlo[] = {String.valueOf(AcomodaMillis(tracker)),melia.getString(0)};
       melia = MiPrimeraBase.rawQuery("UPDATE MicroHistoricLocal set Tempo = ?  WHERE  Tempo = ? " ,Carlo);
        return melia;
    }

    public Cursor RegistrarPendientes (){

        MiPrimeraBase = ayudame.getReadableDatabase();
        Cursor angelito = MiPrimeraBase.query("MicroHistoricLocal", histo, null, null, null, null, null);

        return angelito;
    }

    public void BorrarTodo (){
        ayudame.abrir();
        MiPrimeraBase.delete("MicroHistoricLocal", null, null);
        ayudame.cerrar();
    }
    /// Convierte un millis en Ticks --- Ademas suma el orden del trazador
    public long AcomodaMillis (String tracker){

        long aumenta =0;
        aumenta = Integer.valueOf(tracker.substring(2));
        /// Sumamos orden del track para diferenciar los tiempos y evitar lo mas que se pueda las coincidencias
        return (System.currentTimeMillis()*10000)+TicksHastaEnero1970 + aumenta;
    }

    public long CuantosHay (){
        long ahora = 0;
        Cursor melia= MiPrimeraBase.rawQuery("select count(Tempo) from MicroHistoricLocal", null);
        melia.moveToFirst();
        ahora = Long.parseLong(melia.getString(0));
        return ahora;
    }
    public Cursor RegistrarPendientes400 (){

        MiPrimeraBase = ayudame.getReadableDatabase();
        Cursor angelote= MiPrimeraBase.rawQuery("Select *  from MicroHistoricLocal where Tempo in (select Tempo from MicroHistoricLocal order by Tempo asc  limit 400 )", null);
        return angelote;
    }
    public void BorrarNregistros (int cuantos){

        String consulta = "Delete from MicroHistoricLocal where Tempo in (select Tempo from MicroHistoricLocal order by Tempo asc  limit "+String.valueOf(cuantos+5)+" )";
        ayudame.abrir();
      MiPrimeraBase.execSQL(consulta);
        ayudame.cerrar();
    }
    /// Convierte un millis en Ticks --- Ademas suma el orden del trazador


}



