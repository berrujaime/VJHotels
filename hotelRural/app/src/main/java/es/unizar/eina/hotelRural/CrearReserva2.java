package es.unizar.eina.hotelRural;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.unizar.eina.hotelRural.fragments.adapterPopUp;
import es.unizar.eina.hotelRural.fragments.fragmentHab;

/** Clase para la actividad crear habitacion
 * @author Víctor Gallardo y Jaime Berruete
 */
public class CrearReserva2 extends AppCompatActivity {

    /* Boton que al clicarlo avanza en el proceso de crear una reserva */
    private Button btn_sig;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    /* Campos de texto en la aplicación */
    private EditText nombreEdit, telefonoEdit, fechaEntradaEdit, fechaSalidaEdit;

    /* Valores recogidos de los campos de texto y del spinner de la aplicacion */
    private String nombre, telefono, fechaEntrada, fechaSalida;

    ArrayList<String> habsString; // String con los nombres de las habitaciones para mostrarlas en la lista
    ArrayList<Integer> habsInt; // Lista de los ids de las habitaciones mostradas
    private ListView HabsList;

    //Hashmap con las habitaciones que ha elegido el usuario
    protected static HashMap<Integer, Integer> habitacionesElegidas; // El primer campo es la posicion en la lista y el segundo el id de la hab

    /**
     * Se llama cuando se crea la actividad
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_reserva_2);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();
        habitacionesElegidas = new HashMap<Integer,Integer>();
        btn_sig = findViewById(R.id.crear);


        HabsList = (ListView) findViewById(R.id.listaHabitaciones);
        fillData();
        HabsList.setAdapter(new CrearReserva2.MyListAdapter(this,R.layout.list_crearres,habsString));

        /** Funcion que se activa cuando el botón de crear habitación se pulsa  */

    }

    ;


    /**
     * Función que obtiene las habitaciones para mostrarlas en la lista
     */
    @SuppressLint("Range")
    private void fillData() {


        // Get all of the habs ordered by "id"
        Cursor habsCursor = mDbHelper.fetchAllHabitacionesBy("id");
        habsCursor.moveToFirst();

        habsString = new ArrayList<String>();
        habsInt = new ArrayList<Integer>();
        while (!habsCursor.isAfterLast()) {
            //Se muestra la palabra habitacion junto al id de la misma
            habsInt.add(habsCursor.getInt(habsCursor.getColumnIndex("id")));
            habsString.add("Habitación " + habsCursor.getString(habsCursor.getColumnIndex("id")));
            habsCursor.moveToNext();
        }
        habsCursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_crearres, R.id.textCelda, habsString);
        HabsList.setAdapter(adapter);

    }


    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> habsString;

        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
            habsString = (ArrayList<String>) objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CrearReserva2.ViewHolder mainViewHolder = null;

            if (convertView == null) {
                //Se infla la vista con la que se va a trabajar
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                CrearReserva2.ViewHolder viewHolder = new CrearReserva2.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.textCelda);
                //Se pone el titulo correspondiente
                viewHolder.title.setText("Habitación " + habsInt.get(position));
                viewHolder.switchA = (Switch) convertView.findViewById(R.id.switch2);

                viewHolder.switchA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Se ha activado el switch
                        if(viewHolder.switchA.isChecked()){
                            habitacionesElegidas.put(position,habsInt.get(position));
                            System.out.println("El numero de habs es " + habitacionesElegidas.size());
                        }else{
                            habitacionesElegidas.remove(position);
                            System.out.println("El numero de habs es " + habitacionesElegidas.size());
                        }
                    }
                });


                convertView.setTag(viewHolder);
            } else {
                mainViewHolder = (CrearReserva2.ViewHolder) convertView.getTag();
                mainViewHolder.title.setText(getItem(position));
            }
            return convertView;
        }

    }


    public class ViewHolder {
        TextView title;
        Spinner spinner;
        Switch switchA;
    }
}
