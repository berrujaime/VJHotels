package es.unizar.eina.hotelRural;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.hotelRural.HotelDbAdapter;
import es.unizar.eina.hotelRural.R;

public class Test extends AppCompatActivity {


    private static HotelDbAdapter mDbHelper;
    private int reserva;

    private Button btn_cajaNegra;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();


        setContentView(R.layout.pruebas);

        btn_cajaNegra = findViewById(R.id.button_CNegra);

        btn_cajaNegra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    pruebasCajaNegra();
                }catch (Throwable e) {
                    e.printStackTrace();
                } ;

            }
        });


    }

    public static void pruebasCajaNegra() throws Throwable{
        //Pruebas de habitaciones...

        //Prueba correcta
        long rowId =  mDbHelper.createHabitacion(999,"prueba",4,10.0f,15.0f);
        System.out.println("El resultado tiene que ser distinto de -1 y es  " + rowId);

        mDbHelper.deleteHab(999);

        //Pruebas no válidas
        rowId =  mDbHelper.createHabitacion(-1,"prueba",4,10.0f,15.0f);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        rowId =  mDbHelper.createHabitacion(999,"prueba",-1,10.0f,15.0f);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        rowId =  mDbHelper.createHabitacion(999,"prueba",8,10.0f,15.0f);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        rowId =  mDbHelper.createHabitacion(999,"prueba",4,-1f,15.0f);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        rowId =  mDbHelper.createHabitacion(999,"prueba",4,10.0f,-1);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        rowId =  mDbHelper.createHabitacion(999,"prueba",4,10.0f,120.0f);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        System.out.println("Pasando a las pruebas de updateHabitaciones...");


         rowId =  mDbHelper.createHabitacion(998,"prueba",4,10.0f,15.0f);

        Boolean salida =  mDbHelper.updateHabitacion("998",999,"prueba",4,10.0f,15.0f);
        System.out.println("El resultado tiene que ser true y es " + salida);

        mDbHelper.deleteHab(999);

        //Pruebas no válidas
        salida =  mDbHelper.updateHabitacion("h1d", 999,"prueba",-1,10.0f,15.0f);
        System.out.println("El resultado tiene que ser false y es  " + salida);

        salida =  mDbHelper.updateHabitacion("" , 999,"prueba",8,10.0f,15.0f);
        System.out.println("El resultado tiene que ser false y es  " + salida);

        salida =  mDbHelper.updateHabitacion("1" , -1,"prueba",8,10.0f,15.0f);
        System.out.println("El resultado tiene que ser false y es  " + salida);

        salida =  mDbHelper.updateHabitacion("1" , 1,"prueba",-1,10.0f,15.0f);
        System.out.println("El resultado tiene que ser false y es  " + salida);

        salida =  mDbHelper.updateHabitacion("1" , 1,"prueba",10,10.0f,15.0f);
        System.out.println("El resultado tiene que ser false y es  " + salida);

        salida =  mDbHelper.updateHabitacion("1" , 1,"prueba",4,-1,15.0f);
        System.out.println("El resultado tiene que ser false y es  " + salida);

        salida =  mDbHelper.updateHabitacion("1" , 1,"prueba",4,10.0f,-10.0f);
        System.out.println("El resultado tiene que ser false y es  " + salida);

        salida =  mDbHelper.updateHabitacion("1" , 1,"prueba",4,10.0f,120.0f);
        System.out.println("El resultado tiene que ser false y es  " + salida);


        System.out.println("Pasando a las pruebas de deleteHabitaciones...");


         rowId =  mDbHelper.createHabitacion(999,"prueba",4,10.0f,15.0f);

         salida =  mDbHelper.deleteHab(999);
         System.out.println("El resultado tiene que ser true y es  " + salida);

         //No válidas

        salida =  mDbHelper.deleteHab(-1);
        System.out.println("El resultado tiene que ser false y es  " + salida);
    }
}
