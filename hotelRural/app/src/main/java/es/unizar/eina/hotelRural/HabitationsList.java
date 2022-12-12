package es.unizar.eina.hotelRural;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HabitationsList extends AppCompatActivity {

    private HotelDbAdapter mDbHelper;
    private ListView HabsList;


   @Override
    protected void onCreate(Bundle savedInstanceState){


       super.onCreate(savedInstanceState);

       mDbHelper = new HotelDbAdapter(this);
       mDbHelper.open();

       setContentView(R.layout.listahabitaciones);
       HabsList = findViewById(R.id.list_ID);

   }

    //Obtiene las habitaciones para mostrarlas en la lista
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor habsCursor = mDbHelper.fetchAllHabitaciones();
        habsCursor.moveToFirst();

        ArrayList<String> habsString = new ArrayList<String>();
        while(!habsCursor.isAfterLast()){
            habsString.add(habsCursor.getString(habsCursor.getColumnIndex("id")));
            habsCursor.moveToNext();
        }
        habsCursor.close();
        /*
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { HotelDbAdapter.HAB_ID };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        HabsList.setAdapter(notes);

         */
        ArrayAdapter<String> habsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habsString);
        HabsList.setAdapter(habsAdapter);
    }
}
