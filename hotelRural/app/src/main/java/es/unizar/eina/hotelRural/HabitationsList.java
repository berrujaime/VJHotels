package es.unizar.eina.hotelRural;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



import android.database.Cursor;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/** Clase para la actividad listar habitaciones
 * @author Víctor Gallardo y Jaime Berruete
 */
public class HabitationsList extends TabActivity {

    /* Boton que al clicarlo lleva a la actividad de crear una habitación */
    private Button btn_crear_habitaciones;

    /* Adaptador de la base de datos */
    private HotelDbAdapter mDbHelper;

    /* Listas de habitaciones según distintos métodos de listado */
    private ListView HabsList;
    private ListView OcupsList;
    private ListView PrecioList;

    /** Se llama cuando se crea la actividad
     * Esta función crea las tablas con las listas de habitaciones ordenadas según los distintos
     * métodos.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){


       super.onCreate(savedInstanceState);

       mDbHelper = new HotelDbAdapter(this);
       mDbHelper.open();
       //mDbHelper.createHabitacion(2,"desc", 5,(float)5.3, (float)3.4);
       //mDbHelper.createHabitacion(5,"desc", 5,(float)5.3, (float)3.4);

       setContentView(R.layout.listahabitaciones);

        TabHost tabHost = getTabHost();
        tabHost.addTab(
                tabHost.newTabSpec("tab1ID").setIndicator("ID", null).setContent(R.id.tabID)
        );
        tabHost.addTab(
                tabHost.newTabSpec("tab2Ocup").setIndicator("MaxOcup").setContent(R.id.tabMaxOcup)
        );
        tabHost.addTab(
                tabHost.newTabSpec("tab3Precio").setIndicator("Precio").setContent(R.id.tabPrecio)
        );


        btn_crear_habitaciones = findViewById(R.id.crear_habitacion);

       btn_crear_habitaciones.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //Intent i = new Intent(getApplicationContext(), CrearHabitacion.class);
               Intent i = new Intent(getApplicationContext(), ConsultarHabitacion.class);
               startActivity(i);
           }
       });


       HabsList = (ListView)findViewById(R.id.list_ID);
       OcupsList = (ListView)findViewById(R.id.list_Ocups);
       PrecioList = (ListView)findViewById(R.id.list_Precio);
       fillData();
       registerForContextMenu(HabsList);
       registerForContextMenu(OcupsList);
       registerForContextMenu(PrecioList);
   }

    /** Función que obtiene las habitaciones para mostrarlas en la lista */
    private void fillData() {


        // Get all of the habs ordered by "id"
        Cursor habsCursor = mDbHelper.fetchAllHabitacionesBy("id");
        habsCursor.moveToFirst();

        ArrayList<String> habsString = new ArrayList<String>();
        while(!habsCursor.isAfterLast()){
            //Se muestra la palabra habitacion junto al id de la misma
            habsString.add("Habitación " + habsCursor.getString(habsCursor.getColumnIndex("id")));
            habsCursor.moveToNext();
        }
        habsCursor.close();

        ArrayAdapter<String> habsAdapter = new ArrayAdapter<String>(this,R.layout.list_itemhab, R.id.textCelda, habsString);
        HabsList.setAdapter(habsAdapter);


        // Get all of the habs ordered by "maxocupantes"
        habsCursor = mDbHelper.fetchAllHabitacionesBy("nummaxocupantes");
        habsCursor.moveToFirst();

        ArrayList<String> habsOcupsString = new ArrayList<String>();
        while(!habsCursor.isAfterLast()){
            //Se muestra la palabra habitacion junto al id de la misma
            habsOcupsString.add("Habitación " + habsCursor.getString(habsCursor.getColumnIndex("id")));
            habsCursor.moveToNext();
        }
        habsCursor.close();

        habsAdapter = new ArrayAdapter<String>(this,R.layout.list_itemhab, R.id.textCelda, habsOcupsString);
        OcupsList.setAdapter(habsAdapter);



        // Get all of the habs ordered by "precioocupante"
         habsCursor = mDbHelper.fetchAllHabitacionesBy("precioocupante");
        habsCursor.moveToFirst();

        ArrayList<String> habsPrecioString = new ArrayList<String>();
        while(!habsCursor.isAfterLast()){
            //Se muestra la palabra habitacion junto al id de la misma
            habsPrecioString.add("Habitación " + habsCursor.getString(habsCursor.getColumnIndex("id")));
            habsCursor.moveToNext();
        }
        habsCursor.close();

        habsAdapter = new ArrayAdapter<String>(this,R.layout.list_itemhab, R.id.textCelda, habsPrecioString);
        PrecioList.setAdapter(habsAdapter);

    }
}
