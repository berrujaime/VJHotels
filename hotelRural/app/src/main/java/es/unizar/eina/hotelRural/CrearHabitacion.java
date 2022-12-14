package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class CrearHabitacion extends AppCompatActivity {
    private Button btn_crear;
    private HotelDbAdapter mDbHelper;
    private EditText idEdit, descEdit, porcentajeEdit, precioEdit;
    private Spinner numMaxOcupEdit;


    private String desc;
    private float precioNoche, porcentajeRecarga;
    private int id, numMaxOcupantes;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_habitacion);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();

        btn_crear = findViewById(R.id.crear);

        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
