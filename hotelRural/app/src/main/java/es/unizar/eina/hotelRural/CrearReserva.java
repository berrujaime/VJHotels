package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

/** Clase para la actividad crear habitacion
 * @author Víctor Gallardo y Jaime Berruete
 */
public class CrearReserva extends AppCompatActivity {

    /* Boton que al clicarlo avanza en el proceso de crear una reserva */
    private Button btn_sig;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    /* Campos de texto en la aplicación */
    private EditText nombreEdit, telefonoEdit, fechaEntradaEdit, fechaSalidaEdit;

    /* Valores recogidos de los campos de texto y del spinner de la aplicacion */
    private String nombre, telefono, fechaEntrada, fechaSalida;

    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_reserva_1);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();

        btn_sig = findViewById(R.id.siguiente);

        /** Funcion que se activa cuando el botón de crear habitación se pulsa  */
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

                    //se avanza en el proceso de reserva
                    nombre = nombreEdit.getText().toString();
                    telefono = telefonoEdit.getText().toString();
                    fechaEntrada = fechaEntradaEdit.getText().toString();
                    fechaSalida = fechaSalidaEdit.getText().toString();
                    //mDbHelper.createReserva(nombre, telefono, fechaEntrada, fechaSalida);
                    Intent i = new Intent(getApplicationContext(), CrearReserva2.class);
                    startActivity(i);


            }
        });
    }

}
