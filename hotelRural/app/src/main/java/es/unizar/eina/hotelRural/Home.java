package es.unizar.eina.hotelRural;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/** Clase para la actividad de la pantalla de inicio
 * @author Víctor Gallardo y Jaime Berruete
 */
public class Home extends AppCompatActivity {
/*
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
*/
    //Adaptador de la base de datos
    private HotelDbAdapter mDbHelper;

    private ListView mList;

    //Botones para pasar a la pantalla de listar reservas o listar habitaciones
    private Button btn_reserva;
    private Button btn_habitaciones;


    /** Se llama cuando se crea la actividad */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();
        //mDbHelper.createHabitacion(3,"hola", 5,120, 10.1f);
        //long id = mDbHelper.createHabitacion(7,"primera habitacion", 1,10.0f, 6);

        setContentView(R.layout.home);

        btn_habitaciones = findViewById(R.id.button_hab);
        btn_reserva = findViewById(R.id.button_res);

        /** Función que se ejecuta cuando se clica el botón de gestionar habitaciones */
        btn_habitaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Home.this, HabitationsList.class);
                startActivity(i);
            }
        });

        /** Función que se ejecuta cuando se clica el botón de gestionar habitaciones */
        btn_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent i = new Intent(Home.this, ListaReservas.class);
                Intent i = new Intent(Home.this, CrearReserva.class);
                startActivity(i);
            }
        });

        /*
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        //mList = (ListView)findViewById(R.id.list);
        fillData();

        //registerForContextMenu(mList);
        */
    }

    /*
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor notesCursor = mDbHelper.fetchAllNotes();

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        mList.setAdapter(notes);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_insert);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editNote(info.position, info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }


    protected void editNote(int position, long id) {
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

     */

}
