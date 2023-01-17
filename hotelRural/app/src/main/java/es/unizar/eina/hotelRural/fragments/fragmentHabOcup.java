package es.unizar.eina.hotelRural.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.AEADBadTagException;

import es.unizar.eina.hotelRural.ConsultarHabitacion;
import es.unizar.eina.hotelRural.HabitationsList;
import es.unizar.eina.hotelRural.Home;
import es.unizar.eina.hotelRural.HotelDbAdapter;
import es.unizar.eina.hotelRural.ModificarHabitacion;
import es.unizar.eina.hotelRural.R;

/**
 * Clase que gestiona la lista de habitaciones ordenadas por numocupantes. Esta clase hereda
 * de la clase Fragment
 *
 *
 * @author Víctor Gallardo y Jaime Berruete
 */
public class fragmentHabOcup extends Fragment  {
    private HotelDbAdapter mDbHelper;

    /* Listas de habitaciones según distintos métodos de listado */
    ArrayList<String> habsString;
    ArrayList<Integer> habsInt;
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
        HabsList.setAdapter(new MyListAdapter(getActivity(),R.layout.list_itemhab,habsString));
        HabsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ConsultarHabitacion.class);
                i.putExtra("idHab", habsInt.get(position));
                startActivity(i);
            }
        });
    }



    /**
     * Este método recupera la lista de habitaciones ordenadas por numocupantes y las prepara para ser mostradas
     * en la actividad correspondiente.
     *
     */
    @SuppressLint("Range")
    private void fillData() {


        // Get all of the habs ordered by "id"
        Cursor habsCursor = mDbHelper.fetchAllHabitacionesBy("nummaxocupantes");
        habsCursor.moveToFirst();

        habsString = new ArrayList<String>();
        habsInt = new ArrayList<Integer>();
        while(!habsCursor.isAfterLast()){
            //Se muestra la palabra habitacion junto al id de la misma
            habsInt.add(habsCursor.getInt(habsCursor.getColumnIndex("id")));
            habsString.add("Habitación " + habsCursor.getString(habsCursor.getColumnIndex("id")));
            habsCursor.moveToNext();
        }
        habsCursor.close();



        ArrayAdapter<String> adapter= new ArrayAdapter<>(getActivity(), R.layout.list_itemhab, R.id.textCelda, habsString);
        HabsList.setAdapter(adapter);




    }

    /**
     * Clase que configura cada fila de la lista y muestra las habitaciones. Permite clickar los botones tanto de editar como
     * de borrar y realiza las acciones correspondientes a estos.
     *
     *
     * @author Víctor Gallardo y Jaime Berruete
     */
    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> habsString;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context,resource,objects);
            layout = resource;
            habsString = (ArrayList<String>) objects;
        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView)convertView.findViewById(R.id.textCelda);

                viewHolder.title.setText("Habitación "+ habsInt.get(position));
                viewHolder.editButton = (ImageButton)convertView.findViewById(R.id.ButtonEdit);
                viewHolder.deleteButton = (ImageButton)convertView.findViewById(R.id.ButtonDelete);
                viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ModificarHabitacion.class);
                        i.putExtra("idHab", habsInt.get(position));
                        startActivity(i);
                    }
                });

                View finalConvertView = convertView;
                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), adapterPopUp.class);
                        i.putExtra("idHab", habsInt.get(position));
                        startActivity(i);
                    }
                });

                convertView.setTag(viewHolder);
            }else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.title.setText(getItem(position));
            }
            return convertView;
        }

    }



    public class ViewHolder {
        TextView title;
        ImageButton editButton;
        ImageButton deleteButton;
    }





}
