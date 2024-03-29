package es.unizar.eina.hotelRural.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.hotelRural.ConsultarHabitacion;
import es.unizar.eina.hotelRural.ConsultarReserva1;
import es.unizar.eina.hotelRural.HotelDbAdapter;
import es.unizar.eina.hotelRural.ModificarHabitacion;
import es.unizar.eina.hotelRural.ModificarReserva1;
import es.unizar.eina.hotelRural.R;

/**
 * Clase que configura cada fila de la lista y muestra las reservas. Permite clickar los botones tanto de editar como
 * de borrar y realiza las acciones correspondientes a estos.
 *
 *
 * @author Víctor Gallardo y Jaime Berruete
 */
public class fragmentRes extends Fragment  {
    private HotelDbAdapter mDbHelper;

    /* Listas de habitaciones según distintos métodos de listado */
    ArrayList<String> resString;
    ArrayList<Integer> resInt;
    private ListView resList;
    private View itemListView;


    private int tabPosition = -1;

    public fragmentRes(int index) {
        tabPosition = index;
    }

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
        resList=(ListView)view.findViewById(R.id.lst);
        fillData();
        resList.setAdapter(new MyListAdapter(getActivity(),R.layout.list_itemhab,resString));
        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ConsultarReserva1.class);
                System.out.println("Le vamos a añadir el id " + resInt.get(position));
                i.putExtra("idRes", resInt.get(position));
                startActivity(i);
            }
        });
    }



    /** Función que obtiene las habitaciones para mostrarlas en la lista */
    @SuppressLint("Range")
    private void fillData() {


        Cursor resCursor = null;
        System.out.println("El indice del tab es " + tabPosition);
        if(tabPosition == 0){
            resCursor = mDbHelper.fetchAllReservasBy("nombrecliente");
        }else if(tabPosition == 1){
            resCursor = mDbHelper.fetchAllReservasBy("movilcliente");
        }else if(tabPosition == 2){
            resCursor = mDbHelper.fetchAllReservasBy("fechaentrada");
        }
        resCursor.moveToFirst();

        resString = new ArrayList<String>();
        resInt = new ArrayList<Integer>();
        while(!resCursor.isAfterLast()){
            //Se muestra la palabra habitacion junto al id de la misma
            resInt.add(resCursor.getInt(resCursor.getColumnIndex("id")));
            resString.add("Reserva " + resCursor.getString(resCursor.getColumnIndex("id")));
            resCursor.moveToNext();
        }
        resCursor.close();



        ArrayAdapter<String> adapter= new ArrayAdapter<>(getActivity(), R.layout.list_itemhab, R.id.textCelda, resString);
        resList.setAdapter(adapter);




    }
    /**
     * Clase que configura cada fila de la lista y muestra la fila de la reserva correspondiente.
     *  Permite clickar los botones tanto de editar como
     * de borrar y realiza las acciones correspondientes a estos.
     *
     *
     * @author Víctor Gallardo y Jaime Berruete
     */
    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> resString;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context,resource,objects);
            layout = resource;
            resString = (ArrayList<String>) objects;
        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView)convertView.findViewById(R.id.textCelda);

                viewHolder.title.setText("Reserva "+ resInt.get(position));
                viewHolder.editButton = (ImageButton)convertView.findViewById(R.id.ButtonEdit);
                viewHolder.deleteButton = (ImageButton)convertView.findViewById(R.id.ButtonDelete);
                viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ModificarReserva1.class);
                        i.putExtra("idRes", resInt.get(position));
                        startActivity(i);
                    }
                });

                View finalConvertView = convertView;
                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), adapterPopUpRes.class);
                        i.putExtra("idRes", resInt.get(position));
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
