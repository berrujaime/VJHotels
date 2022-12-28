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

public class ComprobarSolapes {
    //Campos de la tabla reserva
    public static final String RES_FENT = "fechaentrada";
    public static final String RES_FSAL = "fechasalida";

    private HotelDbAdapter mDbHelper;
    private int reserva;

    public ComprobarSolapes(HotelDbAdapter db, int res){
        mDbHelper = db;
        reserva = res;
    }

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    private static boolean hayIntervaloOcupado(String fechaInicioStr, String fechaFinStr, List<String[]> paresFechas) throws ParseException {
        // Convierte las fechas de cadenas a objetos Date
        Date fechaInicio = FORMATO_FECHA.parse(fechaInicioStr);
        Date fechaFin = FORMATO_FECHA.parse(fechaFinStr);

        // Recorre la lista de pares de fechas y comprueba si hay algún intervalo ocupado
        for (String[] parFechas : paresFechas) {
            Date inicio = FORMATO_FECHA.parse(parFechas[0]);
            Date fin = FORMATO_FECHA.parse(parFechas[1]);

            //
            System.out.println("Fechas de reserva: " + fechaInicio + " " + fechaFin +
                    ". Fechas a las que se compara: " + inicio + " " + fin);
            //

            // Si hay un intervalo ocupado, devuelve true
            if ((inicio.before(fechaInicio) && fin.after(fechaInicio)) || (inicio.before(fechaFin) && fin.after(fechaFin)) || (inicio.equals(fechaInicio) || fin.equals(fechaFin))) {
                return true;
            }
        }

        // Si no se encontró ningún intervalo ocupado, devuelve false
        return false;
    }

    //este es el método comprobar solapes como tal
    //tendrá que recibir mdbhelper y la reserva que se han guardado como atrib privados
    public boolean execute(){
        List<Pair<Integer,Integer>> elementos = mDbHelper.fetchHabitacionesReservadas(String.valueOf(reserva));
        List<String> habitaciones = new ArrayList<>();
        List<String> reservas = new ArrayList<>();
        String fent, fsal;
        List<String[]> fechasAComprobar = new ArrayList<>();

        //Se crea la lista de habitaciones asociadas a la reserva
        for (Pair<Integer, Integer> pair : elementos) {
            habitaciones.add(pair.first.toString());
        }

        //Por cada habitación asociada a la reserva
        for (String habitacion : habitaciones){
            reservas = mDbHelper.fetchResevasDeHabitacion(habitacion);
            if(!reservas.isEmpty()){
                //Por cada reserva que tenga asociada esa habitación sin incluir la actual
                for(String res : reservas){
                    if(!res.equals(String.valueOf(reserva))){
                        Cursor cursor = mDbHelper.fetchReserva(Integer.parseInt(res));
                        cursor.moveToFirst();
                        fent = cursor.getString(cursor.getColumnIndex(RES_FENT));
                        fsal = cursor.getString(cursor.getColumnIndex(RES_FSAL));
                        //Se añaden las fechas a la lista
                        fechasAComprobar.add(new String[]{fent,fsal});
                    }
                }
            }
        }
        //se sacan las fechas de la reserva actual
        Cursor cursor = mDbHelper.fetchReserva(reserva);
        cursor.moveToFirst();
        fent = cursor.getString(cursor.getColumnIndex(RES_FENT));
        fsal = cursor.getString(cursor.getColumnIndex(RES_FSAL));
        //se comprueba si hay solapes
        boolean ocupado = false;
        try {
            ocupado = hayIntervaloOcupado(fent,fsal,fechasAComprobar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //false si no hay solapes, true si hay solapes
        return ocupado;
    }
}
