package es.unizar.eina.hotelRural;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/** Clase para ejecutar las sentencias SQL en la base de datos
 * @author Víctor Gallardo y Jaime Berruete
 */
class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;

    private static final String TAG = "DatabaseHelper";

    /** Creación de la tabla habitaciones */
    private static final String DATABASE_CREATE_HAB =
            "create table Habitacion (id integer primary key, "
                + "descripcion text, nummaxocupantes integer, precioocupante float,"
                + "porcentajerecargo float);";

    /** Creación de la tabla reservas */
    private static final String DATABASE_CREATE_RES =
            "create table Reserva (id integer primary key autoincrement, "
                    + "nombrecliente text, movilcliente text, fechaentrada text,"
                    + "fechasalida text);";

    /** Creación de la tabla habitaciones reservadas */
    private static final String DATABASE_CREATE_HAB_RES =
            "CREATE TABLE HabitacionesReservadas(reserva INTEGER, habitacion INTEGER, ocupantes INTEGER," +
                    "FOREIGN KEY(reserva) REFERENCES Reserva(id) ON UPDATE CASCADE ON DELETE CASCADE," +
                    "FOREIGN KEY(habitacion) REFERENCES Habitacion(id) ON UPDATE CASCADE ON DELETE CASCADE," +
                    "PRIMARY KEY(reserva, habitacion));";

    /** Constructor
     * @param context el contexto con el que se va a trabajar */
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("Salgo del constructor");
    }

    /** Función que se ejecuta al llamar a getWritableDatabase si hay que crear la base de datos */
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Voy a crear las tablas");
        db.execSQL(DATABASE_CREATE_HAB);
        db.execSQL(DATABASE_CREATE_RES);
        db.execSQL(DATABASE_CREATE_HAB_RES);
        System.out.println("Ya las he creado");
    }

    /** Función que se ejecuta al llamar a getWritableDatabase si ya existe la base de datos.
      * Esta función borra las tablas y las vuelve a crear por si están desactualizadas.
      */
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