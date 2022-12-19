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
public class CrearHabitacion extends AppCompatActivity {

    /* Boton que al clicarlo crea la habitacion en la base de datos */
    private Button btn_crear;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    /* Campos de texto en la aplicación */
    private EditText idEdit, descEdit, porcentajeEdit, precioEdit;

    /* Spinner que permite elegir el numero maximo de ocupantes en la aplicacion */
    private Spinner numMaxOcupEdit;

    /* Valores recogidos de los campos de texto y del spinner de la aplicacion */
    private String desc;
    private float precioNoche, porcentajeRecarga;
    private int id, numMaxOcupantes;

    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_habitacion);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();

        btn_crear = findViewById(R.id.crear);

        /** Funcion que se activa cuando el botón de crear habitación se pulsa  */
        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recoger datos de los campos de texto y el spinner
                idEdit = (EditText)findViewById(R.id.idForm);
                descEdit = (EditText)findViewById(R.id.descForm);
                porcentajeEdit = (EditText)findViewById(R.id.porcentajeForm);
                precioEdit = (EditText)findViewById(R.id.precioForm);
                numMaxOcupEdit = (Spinner)findViewById(R.id.numMaxOcupantesForm);

                //comprobar si alguno está vacío
                if(idEdit.getText().toString().isEmpty() || descEdit.getText().toString().isEmpty() ||
                porcentajeEdit.getText().toString().isEmpty() || precioEdit.getText().toString().isEmpty()){
                    System.out.println("Algun campo vacio");
                }
                else{
                    //se crea la habitacion en la base de datos
                    id = Integer.parseInt(idEdit.getText().toString());
                    desc = descEdit.getText().toString();
                    porcentajeRecarga = Float.parseFloat(porcentajeEdit.getText().toString());
                    precioNoche = Float.parseFloat(precioEdit.getText().toString());
                    numMaxOcupantes = Integer.parseInt(numMaxOcupEdit.getSelectedItem().toString());
                    mDbHelper.createHabitacion(id,desc, numMaxOcupantes,precioNoche, porcentajeRecarga);
                    Intent i = new Intent(getApplicationContext(), HabitationsList.class);
                    startActivity(i);
                }

            }
        });
    }

}
