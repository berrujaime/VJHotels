package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HabitationsList extends AppCompatActivity {
    private Button btn_crear_habitaciones;

   @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.listahabitaciones);

       btn_crear_habitaciones = findViewById(R.id.crear_habitacion);

       btn_crear_habitaciones.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(getApplicationContext(), CrearHabitacion.class);
               startActivity(i);
           }
       });
   }
}
