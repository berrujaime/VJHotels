package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/** Clase para la actividad consultar habitacion
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ConsultarReserva2 extends AppCompatActivity {
    private Button btn_mensaje;
    /* Boton que al clicarlo sale de esta pantalla y vuelve a la lista de habitaciones */
    private Button btn_salir;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;


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
        List<String> elementos = mDbHelper.fetchHabitacionesReservadas("1");
        String prefix = "Habitación ";
        for (int i = 0; i < elementos.size(); i++) {
            elementos.set(i, prefix.concat(elementos.get(i)));
        }
        //recoger datos de la base de datos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, elementos);
        listView.setAdapter(adapter);

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
