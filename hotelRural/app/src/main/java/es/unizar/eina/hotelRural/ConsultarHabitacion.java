package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

/** Clase para la actividad consultar habitacion
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ConsultarHabitacion extends AppCompatActivity {

    //Campos de la tabla habitación
    public static final String HAB_ID = "id";
    public static final String HAB_DESC = "descripcion";
    public static final String HAB_OCUP = "nummaxocupantes";
    public static final String HAB_PRECIO = "precioocupante";
    public static final String HAB_REC = "porcentajerecargo";

    /* Boton que al clicarlo sale de esta pantalla y vuelve a la lista de habitaciones */
    private Button btn_salir;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    /* Campos de texto en la aplicación */
    private EditText idEdit, descEdit, porcentajeEdit, precioEdit, numMaxOcupEdit;

    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_habitacion);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();

        btn_salir = findViewById(R.id.salir);

        //asociar campos a aplicacon
        idEdit = (EditText)findViewById(R.id.idForm);
        descEdit = (EditText)findViewById(R.id.descForm);
        porcentajeEdit = (EditText)findViewById(R.id.porcentajeForm);
        precioEdit = (EditText)findViewById(R.id.precioForm);
        numMaxOcupEdit = (EditText)findViewById(R.id.numMaxOcupantesForm);

        //recoger datos de la base de datos
        Cursor cursor = mDbHelper.fetchHabitacion(1); //se piden siempre los de la 1 para ver si funciona
        cursor.moveToFirst();

        //meter datos en cada casilla de texto
        idEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_ID)));
        descEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_DESC)));
        porcentajeEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_REC)));
        precioEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_PRECIO)));
        numMaxOcupEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_OCUP)));

        /** Funcion que se activa cuando el botón de salir se pulsa  */
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HabitationsList.class);
                startActivity(i);
            }
        });
    }

}
