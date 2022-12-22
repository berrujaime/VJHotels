package es.unizar.eina.hotelRural.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.unizar.eina.hotelRural.HabitationsList;
import es.unizar.eina.hotelRural.Home;
import es.unizar.eina.hotelRural.HotelDbAdapter;
import es.unizar.eina.hotelRural.ModificarHabitacion;
import es.unizar.eina.hotelRural.R;


public class fragmentHab extends Fragment implements AdapterView.OnItemClickListener {
    private HotelDbAdapter mDbHelper;

    /* Listas de habitaciones según distintos métodos de listado */
    private ListView HabsList;
    private View itemListView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceFor){
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        itemListView = inflater.inflate(R.layout.list_itemhab,container,false);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        mDbHelper = new HotelDbAdapter(getActivity());
        mDbHelper.open();
        HabsList=(ListView)view.findViewById(R.id.lst);
        fillData();

        ImageButton edit_hab = (ImageButton)itemListView.findViewById(R.id.ButtonEdit);

        edit_hab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ModificarHabitacion.class);
                startActivity(i);
            }
        });
        HabsList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(position == 0){
            Toast.makeText(getActivity(),"Paris",Toast.LENGTH_SHORT).show();
        }else if(position == 1){
            Toast.makeText(getActivity(),"Paris",Toast.LENGTH_SHORT).show();
        }
    }


    /** Función que obtiene las habitaciones para mostrarlas en la lista */
    @SuppressLint("Range")
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



        ArrayAdapter<String> adapter= new ArrayAdapter<>(getActivity(), R.layout.list_itemhab, R.id.textCelda, habsString);
        HabsList.setAdapter(adapter);




    }



}
