package es.unizar.eina.hotelRural.ui.main;

import android.database.Cursor;
import android.util.Pair;

import es.unizar.eina.hotelRural.HotelDbAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/** Clase que contiene la funcionalidad de comprobar solapes.
 * Se trata de un método preventivo ya que evita que se puedan seleccionar habitaciones que están
 * ya ocupadas por otras reservas.
 * Para comprobar solapes hay que instanciar la clase y llamar al método público execute
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ComprobarSolapes {
    //Campos de la tabla reserva
    public static final String RES_FENT = "fechaentrada";
    public static final String RES_FSAL = "fechasalida";

    public static final String HAB_ID = "id";
    public static final String HAB_RES_HAB = "habitacion";

    private HotelDbAdapter mDbHelper;
    private int reserva;

    /**
     * Constructor para clase Comprobar solapes
     *
     * @param db adaptador de base de datos de la clase HotelDbAdapter
     * @param res id de la reserva que se quiere de la que se quiere
     *           comprobar solapes con otras reservas
     */
    public ComprobarSolapes(HotelDbAdapter db, int res){
        mDbHelper = db;
        reserva = res;
    }

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Función que mira si hay un intervalo de tiempo ocupado entre dos reservas
     *
     * @param fechaInicioStr fecha de inicio del primer intervalo
     * @param fechaFinStr fecha de fin del primer intervalo
     * @param inicioStr fecha de inicio del segundo intervalo
     * @param finStr fecha de fin del segundo intervalo
     *
     * @return true si hay un intervalo de tiempo que se solapa entre los dos intervalos
     *         false si hay no hay ningún intervalo de tiempo que se solapa entre los dos intervalos
     */
    private static boolean hayIntervaloOcupado(String fechaInicioStr, String fechaFinStr, String inicioStr, String finStr) throws ParseException {
        //Convierte las fechas de cadenas a objetos Date
        Date fechaInicio = FORMATO_FECHA.parse(fechaInicioStr);
        Date fechaFin = FORMATO_FECHA.parse(fechaFinStr);

        //Recorre la lista de pares de fechas y comprueba si hay algún intervalo ocupado
        Date inicio = FORMATO_FECHA.parse(inicioStr);
        Date fin = FORMATO_FECHA.parse(finStr);

        System.out.println("Fechas de reserva: " + fechaInicio + " " + fechaFin +
                    ". Fechas a las que se compara: " + inicio + " " + fin);

        //Si hay un intervalo ocupado, devuelve true
        if ((inicio.before(fechaInicio) && fin.after(fechaInicio)) || (inicio.before(fechaFin) && fin.after(fechaFin)) || (inicio.equals(fechaInicio) || fin.equals(fechaFin))) {
            return true;
        }

        //Si no se encontró ningún intervalo ocupado, devuelve false
        return false;
    }

    /**
     * Función que comprueba solapes dada una reserva en el constructor
     *
     * @return lista de identificadores de habitaciones que están disponibles ya que las fechas
     *         de la reserva que se comprueba no se solapan con ninguna otra reserva.
     */
    public List<Integer> execute(){
        List<String> habitaciones = new ArrayList<>();
        List<Integer> habitacionesDisp = new ArrayList<>();
        List<String> reservas;
        String fent, fsal, fent2, fsal2;
        List<String[]> fechasAComprobar = new ArrayList<>();
        boolean ocupado = false;

        //Se sacan las fechas de la reserva actual
        Cursor cursor = mDbHelper.fetchReserva(reserva);
        cursor.moveToFirst();
        fent = cursor.getString(cursor.getColumnIndex(RES_FENT));
        fsal = cursor.getString(cursor.getColumnIndex(RES_FSAL));
        cursor.close();

        //Se sacan todas las habitaciones que hay
        Cursor habsCursor = mDbHelper.fetchAllHabitacionesBy("id");
        while (habsCursor.moveToNext()) {
            String hab = habsCursor.getString(habsCursor.getColumnIndex(HAB_ID));
            habitaciones.add(hab);
            System.out.println("El valor de hab es "+ hab);
        }

        //Por cada habitación se comprueba si estaría disponible
        for (String habitacion : habitaciones){
            reservas = mDbHelper.fetchResevasDeHabitacion(habitacion);
            //Caso si que hay reservas que tienen habitaciones asociadas
            if(!reservas.isEmpty()){
                //Por cada reserva que tenga asociada esa habitación sin incluir la actual
                for(String res : reservas){
                    if(!res.equals(String.valueOf(reserva))){
                        cursor = mDbHelper.fetchReserva(Integer.parseInt(res));
                        cursor.moveToFirst();
                        fent2 = cursor.getString(cursor.getColumnIndex(RES_FENT));
                        fsal2 = cursor.getString(cursor.getColumnIndex(RES_FSAL));
                        //Se añaden las fechas a la lista
                        fechasAComprobar.add(new String[]{fent,fsal});
                        //Se comprueba si hay solapes
                        try {
                            ocupado = hayIntervaloOcupado(fent,fsal,fent2,fsal2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(!ocupado && !habitacionesDisp.contains(Integer.parseInt(habitacion))){
                            habitacionesDisp.add(Integer.parseInt(habitacion));
                        }
                    }
                }
            }else{
                //No hay reservas con habitaciones asociadas
                habitacionesDisp.add(Integer.parseInt(habitacion));
            }
        }
        //Se devuelven las habitaciones disponibles para reservar
        return habitacionesDisp;
    }
}
