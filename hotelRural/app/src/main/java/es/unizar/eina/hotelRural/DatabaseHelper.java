package es.unizar.eina.hotelRural;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;

    private static final String TAG = "DatabaseHelper";

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, "
                    + "title text not null, body text not null);";

    private static final String DATABASE_CREATE_HAB =
            "create table Habitacion (id integer primary key, "
                + "descripcion text, nummaxocupantes integer, precioocupante float,"
                + "porcentajerecargo float);";

    private static final String DATABASE_CREATE_RES =
            "create table Reserva (id integer primary key, "
                    + "nombrecliente text, movilcliente text, fechaentrada text,"
                    + "fechasalida text);";

    //Esta tabla indica las habitaciones reservadas para una reserva dada.
   /* private static final String DATABASE_CREATE_HAB_RES =
            "create table HabitacionesReservadas (reserva integer, FOREIGN KEY('reserva') REFERENCES " + DATABASE_CREATE_RES + "('id')" +
                    ", habitacion integer FOREIGN KEY('habitacion') REFERENCES " + DATABASE_CREATE_HAB + "('id')" +
                    ", ocupantes integer);";
*/
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("Salgo del constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        System.out.println("Voy a creer las tablas");

        //db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE_HAB);
        db.execSQL(DATABASE_CREATE_RES);
        //db.execSQL(DATABASE_CREATE_HAB_RES);
        System.out.println("Ya las he creado");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS Habitacion");
        db.execSQL("DROP TABLE IF EXISTS Reserva");
        db.execSQL("DROP TABLE IF EXISTS HabitacionesReservadas");
        onCreate(db);
    }
}