package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

/** Clase para la actividad modificar habitacion
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ModificarHabitacion extends AppCompatActivity {

    //Campos de la tabla habitación
    public static final String HAB_ID = "id";
    public static final String HAB_DESC = "descripcion";
    public static final String HAB_OCUP = "nummaxocupantes";
    public static final String HAB_PRECIO = "precioocupante";
    public static final String HAB_REC = "porcentajerecargo";

    /* Boton que al clicarlo sale de esta pantalla y vuelve a la lista de habitaciones */
    private Button btn_guardar;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    /* Campos de texto en la aplicación */
    private EditText idEdit, descEdit, porcentajeEdit, precioEdit;
    private Spinner numMaxOcupEdit;

    /* Valores recogidos de los campos de texto y del spinner de la aplicacion */
    private String desc;
    private float precioNoche, porcentajeRecarga;
    private int id, numMaxOcupantes;

    /* Strings para guardar el valor y comprobar si cambia durante el transcurso de
     * esta actividad
     */
    private String idAntes, descAntes, porcentajeAntes, precioAntes, numMaxOcupAntes;

    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_habitacion);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();

        btn_guardar = findViewById(R.id.guardar);

        //asociar campos a aplicacon
        idEdit = (EditText)findViewById(R.id.idForm);
        descEdit = (EditText)findViewById(R.id.descForm);
        porcentajeEdit = (EditText)findViewById(R.id.porcentajeForm);
        precioEdit = (EditText)findViewById(R.id.precioForm);
        numMaxOcupEdit = (Spinner)findViewById(R.id.numMaxOcupantesForm);

        //recoger datos de la base de datos
        Bundle extras = getIntent().getExtras();
        int idHab = extras.getInt("idHab");
        Cursor cursor = mDbHelper.fetchHabitacion(idHab); //se piden siempre los de la 3 para ver si funciona
        cursor.moveToFirst();

        //meter datos en cada casilla de texto
        idEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_ID)));
        descEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_DESC)));
        porcentajeEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_REC)));
        precioEdit.setText(cursor.getString(cursor.getColumnIndex(HAB_PRECIO)));
        numMaxOcupEdit.setSelection(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAB_OCUP)))-1);

        idAntes = idEdit.getText().toString();
        descAntes = descEdit.getText().toString();
        porcentajeAntes = porcentajeEdit.getText().toString();
        precioAntes = precioEdit.getText().toString();
        numMaxOcupAntes = numMaxOcupEdit.getSelectedItem().toString();

        /** Funcion que se activa cuando el botón de guardar se pulsa  */
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean haIdoBien;
                //comprobar si alguno está vacío
                if(idEdit.getText().toString().isEmpty() || descEdit.getText().toString().isEmpty() ||
                        porcentajeEdit.getText().toString().isEmpty() || precioEdit.getText().toString().isEmpty()){
                    System.out.println("Algun campo vacio");
                }
                else{ //no hay ninguno vacío
                    //se mira si alguno ha cambiado
                    if(idAntes != idEdit.getText().toString() ||
                        descAntes != descEdit.getText().toString() ||
                        porcentajeAntes != porcentajeEdit.getText().toString() ||
                        precioAntes != precioEdit.getText().toString() ||
                        numMaxOcupAntes != numMaxOcupEdit.getSelectedItem().toString()){
                        //se actualiza la habitacion en la base de datos
                        id = Integer.parseInt(idEdit.getText().toString());
                        desc = descEdit.getText().toString();
                        porcentajeRecarga = Float.parseFloat(porcentajeEdit.getText().toString());
                        precioNoche = Float.parseFloat(precioEdit.getText().toString());
                        numMaxOcupantes = Integer.parseInt(numMaxOcupEdit.getSelectedItem().toString());

                        haIdoBien = mDbHelper.updateHabitacion(idAntes, id, desc, numMaxOcupantes, precioNoche, porcentajeRecarga);
                        if(haIdoBien){
                            Intent i = new Intent(getApplicationContext(), RoomsList.class);
                            startActivity(i);
                        }
                        else{
                            System.out.println("Error en el update");
                        }
                    }
                    else{
                        Intent i = new Intent(getApplicationContext(), RoomsList.class);
                        startActivity(i);
                    }
                }
            }
        });
    }

}
