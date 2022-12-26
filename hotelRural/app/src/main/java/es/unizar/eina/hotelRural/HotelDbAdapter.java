package es.unizar.eina.hotelRural;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Clase que ayuda a acceder a la base de datos de HotelRural. Define las operaciones CRUD básicas
 * para el  HotelRural, y proporciona funciones para listar, recuperar y modificar una habitación
 * o una reserva.
 *
 * Está hecho usando cursores en vez de inner classes.
 *
 * @author Víctor Gallardo y Jaime Berruete
 */
public class HotelDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";

    //Campos de la tabla habitación
    public static final String HAB_ID = "id";
    public static final String HAB_DESC = "descripcion";
    public static final String HAB_OCUP = "nummaxocupantes";
    public static final String HAB_PRECIO = "precioocupante";
    public static final String HAB_REC = "porcentajerecargo";

    //Campos de la tabla reserva
    public static final String RES_ID = "id";
    public static final String RES_NOMBRE = "nombrecliente";
    public static final String RES_MOVIL = "movilcliente";
    public static final String RES_FENT = "fechaentrada";
    public static final String RES_FSAL = "fechasalida";

    //Base de datos
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //Nombres de las tablas
    private static final String DATABASE_TABLE = "notes";
    private static final String DATABASE_HAB = "Habitacion";
    private static final String DATABASE_RES = "Reserva";

    //Contexto para crear la base de datos
    private final Context mCtx;

    /**
     * Constructor - utiliza el Context para abrir o crear la base de datos.
     *
     * @param ctx el Context con el que se va a trabajar
     */
    public HotelDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Abre la base de datos de HotelRural. Si no se puede abrir, intenta crear una
     * instancia de la base de datos. Si no se puede crear, lanza una excepción.
     *
     * @return this (referencia a sí mismo)
     * @throws SQLException si la base de datos no se puede ni abrir ni crear
     */
    public HotelDbAdapter open() throws SQLException {
        System.out.println("Entro a open");
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        System.out.println("Salgo de open");
        return this;
    }

    /**
     * Función que cierra la conexión con la base de datos.
     */
    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Crea una nueva habitación con los valores proprocionados. Si la habitación
     * se crea correctamente devuelve su rowId, en caso contrario devuelve un -1 indicando
     * que ha fallado.
     *
     * @param id el id de la habitación
     * @param desc la descripción de la habitación
     * @param ocups número máximo de ocupantes de la habitación
     * @param precio precio por ocupante de la habitación
     * @param porcentaje porcentaje de recargo de la habitación
     *
     * @return rowId o -1 si falla
     */
    public long createHabitacion(int id, String desc, int ocups, float precio , float porcentaje) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(HAB_ID, id);
        initialValues.put(HAB_DESC, desc);
        initialValues.put(HAB_OCUP, ocups);
        initialValues.put(HAB_PRECIO, precio);
        initialValues.put(HAB_REC, porcentaje);

        return mDb.insert(DATABASE_HAB, null, initialValues);
    }

    /**
     * Actualiza los parámetros de una Habitación en la base de datos. La habitación que se
     * actualiza es la especificada utilizando el id (su clave primaria).
     *
     * @param id el id de la habitación
     * @param desc la descripción de la habitación
     * @param ocups número máximo de ocupantes de la habitación
     * @param precio precio por ocupante de la habitación
     * @param porcentaje porcentaje de recargo de la habitación
     */
    public boolean updateHabitacion(String idAntes, int id, String desc, int ocups, float precio , float porcentaje) {
        ContentValues args = new ContentValues();
        args.put(HAB_ID, id);
        args.put(HAB_DESC, desc);
        args.put(HAB_OCUP, ocups);
        args.put(HAB_PRECIO, precio);
        args.put(HAB_REC, porcentaje);

        return mDb.update(DATABASE_HAB, args, HAB_ID + "=" + idAntes, null) > 0;
    }

    public long createReserva(String nombre, String telefono, String fechaEntrada, String fechaSalida) {
        ContentValues initialValues = new ContentValues();
        //ID AUTOINCREMENT
        initialValues.put(RES_NOMBRE, nombre);
        initialValues.put(RES_MOVIL, telefono);
        initialValues.put(RES_FENT, fechaEntrada);
        initialValues.put(RES_FSAL, fechaSalida);

        return mDb.insert(DATABASE_RES, null, initialValues);
    }



    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteNote(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchHabitacion(int id){
        return mDb.query(DATABASE_HAB, new String[] {HAB_ID, HAB_DESC, HAB_OCUP, HAB_PRECIO,
                HAB_REC}, HAB_ID+"="+id, null, null, null, null);
    }
    /**
     * Devuelve un cursor sobre la lista de habitación de la base de datos
     * @param method indica el método a listar las habitaciones que puede ser:
     *               por id, por número máximo de ocupantes o por precio por ocupante.
     *
     * @return Si el método de listar es uno de los permitidos devuelve un cursor sobre la lista
     *               de habitaciones, en caso contrario devuelve null.
     */
    public Cursor fetchAllHabitacionesBy(String method) {
        if(method == "id"){
            return mDb.query(DATABASE_HAB, new String[] {HAB_ID, HAB_DESC, HAB_OCUP, HAB_PRECIO,
                    HAB_REC}, null, null, null, null, method);
        }else if(method=="nummaxocupantes" || method == "precioocupante"){
            return mDb.query(DATABASE_HAB, new String[] {HAB_ID, HAB_DESC, HAB_OCUP, HAB_PRECIO,
                    HAB_REC}, null, null, null, null, method + " DESC");
        }else{
            System.err.println("ERROR FETCHING HABS");
        }

        return null;
    }

    //Metodo que devuelve un Cursor con los datos de las resevas del sistema. Los ordena según el
    //campo method
    public Cursor fetchAllReservasBy(String method) {
        if(method == RES_NOMBRE){
            return mDb.query(DATABASE_RES, new String[] {RES_ID, RES_NOMBRE, RES_MOVIL, RES_FENT,
                    RES_FSAL}, null, null, null, null, method);
        }else if(method== RES_MOVIL || method == RES_FENT){
            return mDb.query(DATABASE_RES, new String[] {RES_ID, RES_NOMBRE, RES_MOVIL, RES_FENT,
                    RES_FSAL}, null, null, null, null, method + " DESC");
        }else{
            System.err.println("ERROR FETCHING RESERVAS");
        }

        return null;
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllNotes() {
        return null;
    }


    public boolean deleteHab(long rowId) {

        return mDb.delete(DATABASE_HAB, HAB_ID + "=" + rowId, null) > 0;
    }

}