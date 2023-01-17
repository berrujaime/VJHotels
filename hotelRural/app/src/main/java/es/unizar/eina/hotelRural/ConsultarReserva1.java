package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.hotelRural.ui.main.ComprobarSolapes;
import es.unizar.eina.send.SendAbstraction;
import es.unizar.eina.send.SendAbstractionImpl;

/** Clase para la actividad consultar reserva 1
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ConsultarReserva1 extends AppCompatActivity {
    //Campos de la tabla reserva
    public static final String RES_NOMBRE = "nombrecliente";
    public static final String RES_MOVIL = "movilcliente";
    public static final String RES_FENT = "fechaentrada";
    public static final String RES_FSAL = "fechasalida";

    private Button btn_sig;

    // Adaptador de la base de datos
    private HotelDbAdapter mDbHelper;

    private EditText nombreEdit, telEdit, fentEdit, fsalEdit;

    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_reserva_1);
        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();

        //asociar campos a aplicacon
        nombreEdit = (EditText)findViewById(R.id.idForm);
        telEdit = (EditText)findViewById(R.id.editTextPhone);
        fentEdit = (EditText)findViewById(R.id.editTextDate);
        fsalEdit = (EditText)findViewById(R.id.editTextDate2);
        btn_sig = findViewById(R.id.sig);

        //recoger datos de la base de datos
        Bundle extras = getIntent().getExtras();
        int idRes = extras.getInt("idRes");
        Cursor cursor = mDbHelper.fetchReserva(idRes);
        cursor.moveToFirst();

        //meter datos en cada casilla de texto
        nombreEdit.setText(cursor.getString(cursor.getColumnIndex(RES_NOMBRE)));
        telEdit.setText(cursor.getString(cursor.getColumnIndex(RES_MOVIL)));
        fentEdit.setText(cursor.getString(cursor.getColumnIndex(RES_FENT)));
        fsalEdit.setText(cursor.getString(cursor.getColumnIndex(RES_FSAL)));

        /** Funcion que se activa cuando el botón de salir se pulsa  */
        btn_sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Lleva al usuario a la pantalla ConsultarReserva2 pasándole el id de la reserva
                consultada
                */
                Intent i = new Intent(getApplicationContext(), ConsultarReserva2.class);
                i.putExtra("idRes",idRes);
                startActivity(i);
            }
        });
    }

}
