package es.unizar.eina.hotelRural;

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

public class HabitationsList extends AppCompatActivity {
    private Button btn_crear_habitaciones;

    private HotelDbAdapter mDbHelper;
    private ListView HabsList;


   @Override
    protected void onCreate(Bundle savedInstanceState){


       super.onCreate(savedInstanceState);

       mDbHelper = new HotelDbAdapter(this);
       mDbHelper.open();
       mDbHelper.createHabitacion(2,"desc", 5,(float)5.3, (float)3.4);
       mDbHelper.createHabitacion(5,"desc", 5,(float)5.3, (float)3.4);

       setContentView(R.layout.listahabitaciones);

       btn_crear_habitaciones = findViewById(R.id.crear_habitacion);

       btn_crear_habitaciones.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(getApplicationContext(), CrearHabitacion.class);
               startActivity(i);
           }
       });

        //Se configuran los tabs

       TabHost mTabHost = (TabHost) findViewById(R.id.tabs);



       mDbHelper = new HotelDbAdapter(this);
       mDbHelper.open();
       HabsList = (ListView)findViewById(R.id.list_ID);
       fillData();
       registerForContextMenu(HabsList);
   }

    //Obtiene las habitaciones para mostrarlas en la lista
    private void fillData() {


        // Get all of the notes from the database and create the item list
        Cursor habsCursor = mDbHelper.fetchAllHabitaciones();
        habsCursor.moveToFirst();

        ArrayList<String> habsString = new ArrayList<String>();
        while(!habsCursor.isAfterLast()){
            //Se muestra la palabra habitacion junto al id de la misma
            habsString.add("Habitación " + habsCursor.getString(habsCursor.getColumnIndex("id")));
            habsCursor.moveToNext();
        }
        habsCursor.close();



        //ArrayAdapter<String> habsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habsString);
        //HabsList.setAdapter(habsAdapter);

        ArrayAdapter<String> habsAdapter = new ArrayAdapter<String>(this,R.layout.list_itemhab, R.id.textCelda, habsString);
        HabsList.setAdapter(habsAdapter);

    /*
        Cursor notesCursor = mDbHelper.fetchAllHabitaciones();

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { HotelDbAdapter.HAB_ID };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.textCelda };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter habs =
                new SimpleCursorAdapter(this, R.layout.list_itemhab, notesCursor, from, to);
        HabsList.setAdapter(habs);
        */

    }
}
