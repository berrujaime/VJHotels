package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/** Clase para la actividad consultar habitacion
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ConsultarReserva2 extends AppCompatActivity {
    public static final String HAB_PRECIO = "precioocupante";
    public static final String HAB_REC = "porcentajerecargo";
    public static final String HAB_OCUP = "nummaxocupantes";

    private Button btn_mensaje;
    /* Boton que al clicarlo sale de esta pantalla y vuelve a la lista de habitaciones */
    private Button btn_salir;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    private double calcularPrecio(HotelDbAdapter mDbHelper, List<Pair<Integer,Integer>> elementos){
        String precio, recargo;
        int ocup;
        double precioTotal=0.0, precioD, recD;
        //recoger datos de la base de datos
        for(Pair<Integer,Integer> elemento : elementos){
            Cursor cursor = mDbHelper.fetchHabitacion(elemento.first);
            cursor.moveToFirst();
            precio = cursor.getString(cursor.getColumnIndex(HAB_PRECIO));
            recargo = cursor.getString(cursor.getColumnIndex(HAB_REC));
            ocup = elemento.second;
            precioD = Double.parseDouble(precio);
            if(ocup>1){
                recD = Double.parseDouble(recargo);
                precioD = precioD*ocup;
                precioD = precioD + precioD*recD/100.0; //Se aplica recargo
            }
            precioTotal = precioTotal + precioD;
        }
        return precioTotal;
    }

    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_reserva_2);
        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();

        btn_salir = findViewById(R.id.btnSalir);
        btn_mensaje = findViewById(R.id.btnMensaje);

        ListView listView = findViewById(R.id.listaHabitaciones);
        //aquí habría que pasarle la reserva que se haya pulsado, ponemos la 1 para probar
        List<Pair<Integer,Integer>> elementos = mDbHelper.fetchHabitacionesReservadas("1");
        List<String> habitaciones = new ArrayList<>();
        List<String>  ocupantes = new ArrayList<>();
        List<String> mostrar = new ArrayList<>();
        String prefix = "Habitación ";
        for (Pair<Integer, Integer> pair : elementos) {
            habitaciones.add(prefix.concat(pair.first.toString()));
            ocupantes.add(pair.second.toString().concat(" ocupantes"));
        }
        int i=0;
        for (String hab : habitaciones) {
            mostrar.add(hab.concat(" - ").concat(ocupantes.get(i)));
            i++;
        }
        //recoger datos de la base de datos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mostrar);
        listView.setAdapter(adapter);
        double precio = calcularPrecio(mDbHelper, elementos);
        TextView campoPrecio = (TextView)findViewById(R.id.textPrecio);
        campoPrecio.setText("Precio total: " + String.format("%.2f", precio) + "€");

        /** Funcion que se activa cuando el botón de salir se pulsa  */
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //esto tendrá que llevar a ListaReservas
                Intent i = new Intent(getApplicationContext(), HabitationsList.class);
                startActivity(i);
            }
        });
    }

}
