package es.unizar.eina.hotelRural.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import es.unizar.eina.hotelRural.HabitationsList;
import es.unizar.eina.hotelRural.HotelDbAdapter;
import es.unizar.eina.hotelRural.R;

public class adapterPopUp extends AppCompatActivity {

        public static int idHab;

        private static HotelDbAdapter mDbHelper;
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tabs_hab);
            Bundle extras = getIntent().getExtras();
            mDbHelper = new HotelDbAdapter(this);
            mDbHelper.open();


            idHab = extras.getInt("idHab");


            BorrarDialogFragment borrarpop = new BorrarDialogFragment();
            borrarpop.show(getSupportFragmentManager(), "borrar_dialog");
        }



    public static class BorrarDialogFragment extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = requireActivity().getLayoutInflater();


            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.popup_borrarhab, null))
                    // Add action buttons
                    .setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            mDbHelper.deleteHab(idHab);
                            Intent i = new Intent(getActivity(), HabitationsList.class);
                            startActivity(i);
                            Context context = getContext().getApplicationContext();
                            CharSequence text = "Habitación borrada" ;
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context,text,duration);
                            toast.show();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(getActivity(), HabitationsList.class);
                            startActivity(i);
                        }
                    });
            return builder.create();
        }
    }



}

