package es.unizar.eina.hotelRural;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/** Clase para la actividad de la pantalla de inicio
 * @author Víctor Gallardo y Jaime Berruete
 */
public class Home extends AppCompatActivity {
    //Adaptador de la base de datos
    private HotelDbAdapter mDbHelper;

    private ListView mList;

    //Botones para pasar a la pantalla de listar reservas o listar habitaciones
    private Button btn_reserva, btn_habitaciones, btn_pvol, btn_pruebas;


    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();



        setContentView(R.layout.home);

        btn_habitaciones = findViewById(R.id.button_hab);
        btn_reserva = findViewById(R.id.button_res);
        btn_pvol = findViewById(R.id.button_pVol);

        btn_pruebas = findViewById(R.id.button_pruebas);

        /** Función que se ejecuta cuando se clica el botón de gestionar habitaciones */
        btn_habitaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Home.this, RoomsList.class);
                startActivity(i);
            }
        });

        /** Función que se ejecuta cuando se clica el botón de gestionar habitaciones */
        btn_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent i = new Intent(Home.this, ModificarReserva1.class);
                Intent i = new Intent(Home.this, ReservasList.class);
                startActivity(i);
            }
        });




        btn_pruebas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Test.class);
                startActivity(i);
            }
        });
    }
}
