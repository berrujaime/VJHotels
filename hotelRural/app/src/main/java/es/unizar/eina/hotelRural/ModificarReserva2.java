package es.unizar.eina.hotelRural;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unizar.eina.hotelRural.ui.main.ComprobarSolapes;

/** Clase para la actividad modificar reserva 2
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ModificarReserva2 extends AppCompatActivity {

    public static final String HAB_OCUP = "nummaxocupantes";

    /* Boton que al clicarlo completa la creacion del a reserva */
    private Button btn_guardar;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    /* Campos de texto en la aplicación */
    private EditText nombreEdit, telefonoEdit, fechaEntradaEdit, fechaSalidaEdit;

    /* Valores recogidos de los campos de texto y del spinner de la aplicacion */
    private String nombre, telefono, fechaEntrada, fechaSalida;

    ArrayList<String> habsString; // String con los nombres de las habitaciones para mostrarlas en la lista
    ArrayList<Integer> habsInt; // Lista de los ids de las habitaciones mostradas
    ArrayList<Integer> habsOcup; //Lista de los ocupantes de cada habitacion. Estan ordenados en el mismo orden que habsInt.
    private ListView HabsList;

    //Id de la reserva que se esta creando
    private Integer idReserva;

    //Hashmap con las habitaciones que ha elegido el usuario
    /*
    * Se utiliza un único hashMap en el que se va a almacenar el id, numero de ocupantes
    * Este numero de ocupantes se actualizará cuando el usuario lo modifique en el Spinner
    * Si el usuario deselecciona la habitación se borrará del hashmap*/
    protected static HashMap<Integer, Integer> habitacionesElegidas;

    private int preSelectedRooms = 0;
    private int idRes = -1;

    /**
     * Se llama cuando se crea la actividad
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_reserva_2);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();
        habitacionesElegidas = new HashMap<Integer,Integer>();
        btn_guardar = findViewById(R.id.guardar);


        HabsList = (ListView) findViewById(R.id.listaHabitaciones);
        fillData();
        HabsList.setAdapter(new ModificarReserva2.MyListAdapter(this,R.layout.list_crearres,habsString));

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(habitacionesElegidas.size() == 0){
                    //Hay que seleccionar una habitación
                    Context context = ModificarReserva2.this;
                    CharSequence text = "Seleccione al menos una habitación" ;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context,text,duration);

                    View view = toast.getView();
                    view.setBackgroundColor(Color.parseColor("#FF0000"));
                    toast.show();
                }else{

                    //Se elimina la anterior entrada que habia de habitacionesReservadas
                    mDbHelper.deleteHabsRes(idRes);
                    //Se obtienen el id de la reserva y se van añadiendo las habitacionesReservadas

                    for(Map.Entry<Integer,Integer> set:habitacionesElegidas.entrySet()){
                        mDbHelper.createHabitacionesReservadas(idRes,set.getKey(),set.getValue());
                    }
                    Context context = ModificarReserva2.this;
                    CharSequence text = "Reserva modificada con id " + idRes ;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context,text,duration);

                    toast.show();
                    Intent i = new Intent(getApplicationContext(), ReservasList.class);
                    startActivity(i);
                }
            }
        });

        /** Funcion que se activa cuando el botón de crear habitación se pulsa  */

    }

    /**
     * Función que obtiene las habitaciones para mostrarlas en la lista
     */
    @SuppressLint("Range")
    private void fillData() {
        /*Se hace un control de errores preventivo listando solo las habitaciones que no tendrían
          problema de solapamiento.
          */
        habsInt = new ArrayList<Integer>();
        habsString = new ArrayList<String>();
        habsOcup = new ArrayList<Integer>();
        Bundle extras = getIntent().getExtras();
        idRes = extras.getInt("idRes");
        String prefix = "Habitación ";
        //Obtenemos las habitaciones que se seleccionaron
        List<Pair<Integer,Integer>> elementos = mDbHelper.fetchHabitacionesReservadas(String.valueOf(idRes));
        for (Pair<Integer, Integer> pair : elementos) {
            habsString.add(prefix.concat(pair.first.toString()));
            habsOcup.add(pair.second);
            habsInt.add(pair.first);
            preSelectedRooms++;
            habitacionesElegidas.put(pair.first,pair.second);
        }

        //Obtenemos el resto de habitaciones disponibles

        ComprobarSolapes func = new ComprobarSolapes(mDbHelper,idRes);
        ArrayList<Integer> habsInt_conc = (ArrayList<Integer>) func.execute();


        System.out.println("El numero de habitacioens disponibles es " + habsInt.size());
        for (int habitacion : habsInt_conc) {
            //Se muestra la palabra habitacion junto al id de la misma
            habsString.add("Habitación " + String.valueOf(habitacion));
            //Se obtiene los ocupantes de cada habitación para usarlos mas tarde en el spinner.
            habsInt.add(habitacion);
            Cursor cursor = mDbHelper.fetchHabitacion(habitacion);
            cursor.moveToFirst();
            habsOcup.add(cursor.getInt(cursor.getColumnIndex(HAB_OCUP)));
            cursor.close();
        }

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
            ModificarReserva2.ViewHolder mainViewHolder = null;

            if (convertView == null) {
                //Se infla la vista con la que se va a trabajar
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ModificarReserva2.ViewHolder viewHolder = new ModificarReserva2.ViewHolder();

                //Creacion y características
                viewHolder.title = (TextView) convertView.findViewById(R.id.textCelda);
                //Se pone el titulo correspondiente
                viewHolder.title.setText("Habitación " + habsInt.get(position));
                viewHolder.switchA = (Switch) convertView.findViewById(R.id.switch2);
                //Si se seleccionaron anteriormente se pone a true el switch
                if (position < preSelectedRooms){
                    viewHolder.switchA.setChecked(true);
                }else{
                    viewHolder.switchA.setChecked(false);
                }

                viewHolder.spinner = (Spinner) convertView.findViewById(R.id.spinnerocups);

                //Comportamiento


                viewHolder.switchA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Se ha activado el switch
                        if(viewHolder.switchA.isChecked()){
                            Integer numMaxOcupantes = Integer.parseInt(viewHolder.spinner.getSelectedItem().toString());
                            habitacionesElegidas.put(habsInt.get(position), numMaxOcupantes);
                        }else{
                            habitacionesElegidas.remove(habsInt.get(position));
                        }
                    }
                });

                /* La parte que hay a continuación sirve para validar la capacidad de las habitaciones dinámicamente*/
                //Se configura el spinner para que su número de ocupantes correspondientes
                String opcionesPop[] = new String[habsOcup.get(position)];
                //Se crea un array con los ocupantes que puede tener esa hab
                for(int i=0; i < habsOcup.get(position); i++){
                    opcionesPop[i]= Integer.toString(i+1);
                }
                //Se añaden al adapter para que se muestren
                ArrayAdapter<String> adapterOcups = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,opcionesPop);
                viewHolder.spinner.setAdapter(adapterOcups);
                //Se preselecciona el numero de ocupantes
                if(position < preSelectedRooms){
                    int spinnerPos = adapterOcups.getPosition(String.valueOf(habsOcup.get(position)));
                    viewHolder.spinner.setSelection(spinnerPos);
                }

                viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    int posicionFila = position;
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        //Si se cambia un número de ocupantes de una habitacion seleccionada ya, se actualiza el valor
                        if(habitacionesElegidas.get(habsInt.get(posicionFila)) != null){
                            Integer numMaxOcupantes = Integer.parseInt(viewHolder.spinner.getSelectedItem().toString());
                            System.out.println("Entro aqui y el num ocupantes nuevo es " + numMaxOcupantes);
                            habitacionesElegidas.put(habsInt.get(posicionFila),numMaxOcupantes);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });






                convertView.setTag(viewHolder);
            } else {
                mainViewHolder = (ModificarReserva2.ViewHolder) convertView.getTag();
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
