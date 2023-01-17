package es.unizar.eina.hotelRural;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //Campos de la tabla HabitacionesReservadas
    public static final String HAB_RES_RES = "reserva";
    public static final String HAB_RES_HAB = "habitacion";
    public static final String HAB_RES_OCUP = "ocupantes";

    public static final String sequence_sqlite = "sqlite_sequence";


    //Base de datos
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //Nombres de las tablas
    private static final String DATABASE_TABLE = "notes";
    private static final String DATABASE_HAB = "Habitacion";
    private static final String DATABASE_RES = "Reserva";
    private static final String DATABASE_HAB_RES = "HabitacionesReservadas";

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
        mDb.execSQL("PRAGMA foreign_keys=ON;");
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
     * Función que crea una nueva habitación con los valores proprocionados. Si la habitación
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

        if(id < 0 || ocups < 0 || precio < 0.0f || porcentaje < 0.0f || porcentaje > 100.0 || ocups > 6) {
            return -1;
        }else{
            ContentValues initialValues = new ContentValues();
            initialValues.put(HAB_ID, id);
            initialValues.put(HAB_DESC, desc);
            initialValues.put(HAB_OCUP, ocups);
            initialValues.put(HAB_PRECIO, precio);
            initialValues.put(HAB_REC, porcentaje);
            return mDb.insert(DATABASE_HAB, null, initialValues);
        }

    }

    /**
     * Función que comprueba si la String que se le pasa representa un número entero o no
     *
     * @param cadena String que se va a comprobar si es un número entero o no
     *
     * @return true si representa un número entero, false si no lo hace
     */
    public static boolean isNumeric(String cadena) {
        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Función que actualiza los parámetros de una Habitación en la base de datos. La habitación que se
     * actualiza es la especificada utilizando el id (su clave primaria).
     *
     * @param id el id de la habitación
     * @param desc la descripción de la habitación
     * @param ocups número máximo de ocupantes de la habitación
     * @param precio precio por ocupante de la habitación
     * @param porcentaje porcentaje de recargo de la habitación
     *
     * @return false si no se pueden actualizar los parámetros de una habitacióncon los parámetros
     * dados al método. true si se puede.
     */
    public boolean updateHabitacion(String idAntes, int id, String desc, int ocups, float precio , float porcentaje) {
        if(idAntes.length() == 0){
            return false;
        }else if(Integer.parseInt(idAntes) <= 0) {
            return false;
        }else if(isNumeric(idAntes) != true || id <= 0 || ocups < 0 || precio < 0.0f || porcentaje < 0.0f || porcentaje > 100.0 || ocups > 6 ){
            return false;
        }else{
            ContentValues args = new ContentValues();
            args.put(HAB_ID, id);
            args.put(HAB_DESC, desc);
            args.put(HAB_OCUP, ocups);
            args.put(HAB_PRECIO, precio);
            args.put(HAB_REC, porcentaje);

            return mDb.update(DATABASE_HAB, args, HAB_ID + "=" + idAntes, null) > 0;
        }

    }

    /**
     * Función para insertar una reserva en la base de datos.
     *
     * @param nombre nombre del cliente que hace la reserva
     * @param telefono teléfono del cliente que hace la reserva
     * @param fechaEntrada fecha de entrada de la reserva
     * @param fechaSalida fecha de salida de la reserva
     *
     * @return -1 si no se puede insertar la reserva. 0 si se puede crear la reserva.
     */
    public long createReserva(String nombre, String telefono, String fechaEntrada, String fechaSalida) {
        Pattern p = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        Matcher matcher  = p.matcher(fechaEntrada);
        Matcher matcherFSal  = p.matcher(fechaSalida);

        if(nombre.length() == 0 || telefono.length() == 0 || fechaEntrada.length() != 10 || fechaSalida.length() != 10 ){
            return -1;
        }else if(isNumeric(telefono) == false){
            return -1;
        }else if(matcher.find() == false || matcherFSal.find() == false){
            return -1;

        }else {

            ContentValues initialValues = new ContentValues();
            //ID AUTOINCREMENT
            initialValues.put(RES_NOMBRE, nombre);
            initialValues.put(RES_MOVIL, telefono);
            initialValues.put(RES_FENT, fechaEntrada);
            initialValues.put(RES_FSAL, fechaSalida);

            return mDb.insert(DATABASE_RES, null, initialValues);
        }
    }

    /**
     * Función para insertar a la base de datos una tupla que contiene información sobre una
     * habitación que ha sido asociada a una reserva existente con el número de ocupantes.
     *
     * @param resId el id de la reserva
     * @param habId el id de la habitación
     * @param numOcups número de ocupantes de la habitación reservada
     *
     * @return -1 si no se puede inserta la habitación reservada, 0 si se inserta
     */
    public long createHabitacionesReservadas(Integer resId, Integer habId, Integer numOcups) {
        if(resId <=0 || habId <= 0 || numOcups <= 0 || numOcups > 6){
            return -1;
        }else{
            ContentValues initialValues = new ContentValues();
            //ID AUTOINCREMENT
            initialValues.put(HAB_RES_RES, resId);
            initialValues.put(HAB_RES_HAB, habId);
            initialValues.put(HAB_RES_OCUP,numOcups);
            return mDb.insert(DATABASE_HAB_RES, null, initialValues);
        }

    }

    /**
     * Función para actualizar los valores de una reserva en la base de datos.
     *
     * @param id el id de la reserva
     * @param nombre nombre del cliente que hace la reserva
     * @param telefono teléfono del cliente que hace la reserva
     * @param fechaEntrada fecha de entrada de la reserva
     * @param fechaSalida fecha de salida de la reserva
     *
     * @return -1 si no se puede actualizar la reserva, 0 si se actualiza
     */
    public boolean updateReserva(String id, String nombre, String tel, String fent, String fsal){
        Pattern p = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        Matcher matcher  = p.matcher(fent);
        Matcher matcherFSal  = p.matcher(fsal);

        if(id.length() <= 0 || nombre.length() == 0 || tel.length() == 0 || fent.length() != 10 || fsal.length() != 10 ){
            return false;
        }else if(isNumeric(tel) == false || isNumeric(id) == false){
            return false;
        }else if(matcher.find() == false || matcherFSal.find() == false){
            return false;

        }else {

            ContentValues args = new ContentValues();
            args.put(RES_ID, id);
            args.put(RES_NOMBRE, nombre);
            args.put(RES_MOVIL, tel);
            args.put(RES_FENT, fent);
            args.put(RES_FSAL, fsal);

            return mDb.update(DATABASE_RES, args, RES_ID + "=" + id, null) > 0;
        }
    }

    /**
     * Función que devuelve un cursor sobre una reserva dado su identificador
     *
     * @param id identificador de la reserva
     *
     * @return Cursor sobre la reserva
     */
    public Cursor fetchReserva(int id){
        return mDb.query(DATABASE_RES, new String[] {RES_ID, RES_FENT, RES_FSAL, RES_MOVIL, RES_NOMBRE}, RES_ID+"="+id, null, null, null, null);
    }

    /**
     * Función que devuelve un cursor sobre una habitación dado su identificador
     *
     * @param id identificador de la habitación
     *
     * @return Cursor sobre la habitación
     */
    public Cursor fetchHabitacion(int id){
        return mDb.query(DATABASE_HAB, new String[] {HAB_ID, HAB_DESC, HAB_OCUP, HAB_PRECIO,
                HAB_REC}, HAB_ID+"="+id, null, null, null, null);
    }

    /**
     * Función que devuelve un cursor sobre la lista de habitación de la base de datos
     *
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

    /**
     * Función para obtener una lista de habitaciones reservadas junto a sus ocupantes dado el
     * identificador de una reserva.
     *
     * @param id identificador de una reserva
     *
     * @return lista de pares de enteros que representan [id de la habitación,ocupantes]
     */
    public List<Pair<Integer, Integer>> fetchHabitacionesReservadas(String id) {
        List<Pair<Integer, Integer>> habitaciones = new ArrayList<>();
        // Especifica la consulta SQL
        String query = "SELECT " + HAB_RES_HAB + "," + HAB_RES_OCUP + " FROM " + DATABASE_HAB_RES + " WHERE " + HAB_RES_RES + "=" + id;

        // Ejecuta la consulta y obtiene el cursor con los resultados
        Cursor cursor = mDb.rawQuery(query, null);

        // Recorre el cursor y procesa los resultados
        while (cursor.moveToNext()) {
            int hab = cursor.getInt(cursor.getColumnIndex(HAB_RES_HAB));
            int ocup = cursor.getInt(cursor.getColumnIndex(HAB_RES_OCUP));
            habitaciones.add(new Pair<Integer, Integer>(hab, ocup));
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        return habitaciones;
    }

    /**
     * Función para obtener una lista de reservas que tienen asociada una habitación en concreto.
     *
     * @param id identificador de una habitación
     *
     * @return lista de enteros que representan identificadores de reservas que tienen asociada
     * a la habitación dada como el parámetro id
     */
    public List<String> fetchResevasDeHabitacion(String id) {
        List<String> reservas = new ArrayList<>();
        // Especifica la consulta SQL
        String query = "SELECT " + HAB_RES_RES + " FROM " + DATABASE_HAB_RES + " WHERE " + HAB_RES_HAB + "=" + id;

        // Ejecuta la consulta y obtiene el cursor con los resultados
        Cursor cursor = mDb.rawQuery(query, null);

        // Recorre el cursor y procesa los resultados
        while (cursor.moveToNext()) {
            String res = cursor.getString(cursor.getColumnIndex(HAB_RES_RES));
            reservas.add(res);
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        return reservas;
    }



    /**
     * Función que devuelve un cursor sobre la lista de reservas de la base de datos
     *
     * @param method indica el método a listar las habitaciones que puede ser:
     *               por nombre del cliente, por teléfono móvil del ciente o por fecha de entrada.
     *
     * @return Si el método de listar es uno de los permitidos devuelve un cursor sobre la lista
     *               de reservas, en caso contrario devuelve null.
     */
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
     * Función que borra una habitación de la base de datos.
     *
     * @param rowId identificador de la habitación
     *
     * @return true si la ha eliminado, 0 si no se ha podido eliminar
     */
    public boolean deleteHab(long rowId) {

        if(rowId <= 0 ){
            return false;
        }else{
            return mDb.delete(DATABASE_HAB, HAB_ID + "=" + rowId, null) > 0;
        }


    }

    /**
     * Función que borra una reserva de la base de datos.
     *
     * @param rowId identificador de la reserva
     *
     * @return true si la ha eliminado, 0 si no se ha podido eliminar
     */
    public boolean deleteRes(long rowId) {

        if(rowId <= 0 ){
            return false;
        }else{
            return mDb.delete(DATABASE_RES, RES_ID + "=" + rowId, null) > 0;
        }

    }

    /**
     * Función que borra las habitaciones asociadas a una reserva
     *
     * @param rowId identificador de la reserva
     *
     * @return true si la ha eliminado, 0 si no se ha podido eliminar
     */
    public boolean deleteHabsRes(long rowId) {

        if(rowId <= 0 ){
            return false;
        }else{
            return mDb.delete(DATABASE_HAB_RES, HAB_RES_RES + "=" + rowId, null) > 0;
        }

    }

    /**
     * Función que busca la última reserva creada en la base de datos
     *
     * @return Cursor que apunta a la última reserva creada en la base de datos
     */
    public Cursor fetchLastReserva() {
            return mDb.query(sequence_sqlite, new String[] {"seq"}, "name='Reserva'", null, null, null, null);
    }

    /**
     * Función que borra todas las tablas de la base de datos
     */
    public void deleteAll(){
        mDb.delete(DATABASE_HAB_RES, null, null);
        mDb.delete(DATABASE_HAB, null, null);
        mDb.delete(DATABASE_RES, null, null);

    }
}