package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/** Clase para la actividad modificar reserva 1
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ModificarReserva1 extends AppCompatActivity {
    //Campos de la tabla reserva
    public static final String RES_ID = "id";
    public static final String RES_NOMBRE = "nombrecliente";
    public static final String RES_MOVIL = "movilcliente";
    public static final String RES_FENT = "fechaentrada";
    public static final String RES_FSAL = "fechasalida";

    /* Boton que al clicarlo avanza en el proceso de crear una reserva */
    private Button btn_sig;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    /* Campos de texto en la aplicación */
    private EditText nombreEdit, telefonoEdit, fechaEntradaEdit, fechaSalidaEdit;

    /* Valores recogidos de los campos de texto y del spinner de la aplicacion */
    private String nombre, telefono, fechaEntrada, fechaSalida, id;

    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_reserva_1);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();

        btn_sig = findViewById(R.id.siguiente);

        //recoger datos de la base de datos
        Bundle extras = getIntent().getExtras();
        int idRes = extras.getInt("idRes");
        Cursor cursor = mDbHelper.fetchReserva(idRes);
        cursor.moveToFirst();

        //recoger datos de los campos de texto y el spinner
        nombreEdit = (EditText)findViewById(R.id.nombreForm);
        telefonoEdit = (EditText)findViewById(R.id.phoneForm);
        fechaEntradaEdit = (EditText)findViewById(R.id.fechaEntradaForm);
        fechaSalidaEdit = (EditText)findViewById(R.id.fechaSalidaForm);

        //meter datos en cada casillas de texto
        nombreEdit.setText(cursor.getString(cursor.getColumnIndex(RES_NOMBRE)));
        telefonoEdit.setText(cursor.getString(cursor.getColumnIndex(RES_MOVIL)));
        fechaEntradaEdit.setText(cursor.getString(cursor.getColumnIndex(RES_FENT)));
        fechaSalidaEdit.setText(cursor.getString(cursor.getColumnIndex(RES_FSAL)));
        id = cursor.getString(cursor.getColumnIndex(RES_ID));

        /** Funcion que se activa cuando el botón de siguiente se pulsa  */
        btn_sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recoger datos de los campos de texto y el spinner
                nombreEdit = (EditText)findViewById(R.id.nombreForm);
                telefonoEdit = (EditText)findViewById(R.id.phoneForm);
                fechaEntradaEdit = (EditText)findViewById(R.id.fechaEntradaForm);
                fechaSalidaEdit = (EditText)findViewById(R.id.fechaSalidaForm);
                //comprobar si alguno está vacío
                if(nombreEdit.getText().toString().isEmpty() || telefonoEdit.getText().toString().isEmpty() ||
                fechaEntradaEdit.getText().toString().isEmpty() || fechaSalidaEdit.getText().toString().isEmpty()){
                    System.out.println("Algun campo vacio");
                }
                else {
                    //se avanza en el proceso de reserva
                    //no comprobamos si se ha modificado alguno, ejecutamos update directamente
                    nombre = nombreEdit.getText().toString();
                    telefono = telefonoEdit.getText().toString();
                    fechaEntrada = fechaEntradaEdit.getText().toString();
                    fechaSalida = fechaSalidaEdit.getText().toString();
                    boolean haIdoBien = mDbHelper.updateReserva(id, nombre, telefono, fechaEntrada, fechaSalida);
                    if (haIdoBien) {
                        //Se va a la pantalla de modificar reserva 2
                        Intent i = new Intent(getApplicationContext(), ModificarReserva2.class);
                        i.putExtra("idRes", idRes);
                        startActivity(i);
                    } else {
                        System.out.println("Error en el update");
                    }
                }
            }
        });
    }

}
