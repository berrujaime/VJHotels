package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.hotelRural.HotelDbAdapter;
import es.unizar.eina.hotelRural.R;

public class Test extends AppCompatActivity {


    private static HotelDbAdapter mDbHelper;
    private int reserva;

    private Button btn_cajaNegra;
    private Button btn_pvol;
    private Button btn_home;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mDbHelper = new HotelDbAdapter(this);
        mDbHelper.open();


        setContentView(R.layout.pruebas);

        btn_cajaNegra = findViewById(R.id.button_CNegra);

        btn_pvol = findViewById(R.id.button_pVol);

        btn_home = findViewById(R.id.button_home);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Test.this, Home.class);
                startActivity(i);
            }
        });



        btn_cajaNegra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pruebasCajaNegra();

            }
        });

        btn_pvol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mDbHelper = new HotelDbAdapter(Test.this);
                    mDbHelper.open();
                    //mDbHelper.deleteAll();
                    //Creamos 100 habitaciones
                    for(int i=0; i<100; i++){
                        mDbHelper.createHabitacion(i,"desc "+i, 6,72, 7.2f);
                    }
                    //Creamos 1000 reservas
                    for(int i=0; i<1000; i++){
                        mDbHelper.createReserva("res "+i,"666666666", "04/01/2023", "05/01/2023");
                    }
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(Test.this,"Habitaciones y reservas creadas",duration);

                    toast.show();

            }
        });


    }

    public static void pruebasCajaNegra() {
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


        try {
            salida = mDbHelper.updateHabitacion("h1d", 999, "prueba", -1, 10.0f, 15.0f);
            System.out.println("El resultado tiene que ser false y es  " + salida);
        }catch (Throwable e){
            e.printStackTrace();
        }
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



        //Pruebas de reservas
        System.out.println("Empezando pruebas de reservas...");

        System.out.println("CreateReserva...");
        rowId = mDbHelper.createReserva("test1","68523","27/03/12","30/03/12");

        System.out.println("El resultado tiene que ser distinto de -1 y es " + rowId);

        mDbHelper.deleteRes(rowId);

        //Pruebas no válidas

        rowId = mDbHelper.createReserva("","68523","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);

        rowId = mDbHelper.createReserva("test1","","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);

        rowId = mDbHelper.createReserva("test1","9h3","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);

        rowId = mDbHelper.createReserva("test1","6847","27","30/03/12");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);

        rowId = mDbHelper.createReserva("test1","6847","27/03/1212","30/03/12");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);

        rowId = mDbHelper.createReserva("test1","6847","aa/bb/cc","30/03/12");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);

        rowId = mDbHelper.createReserva("test1","6847","27/03/12","30");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);

        rowId = mDbHelper.createReserva("test1","6847","27/03/12","30/03/1212");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);

        rowId = mDbHelper.createReserva("test1","6847","27/03/12","aa/bb/cc");
        System.out.println("El resultado tiene que ser -1 y es " + rowId);


        System.out.println("UpdateReserva...");

        rowId = mDbHelper.createReserva("test1","68523","27/03/12","30/03/12");

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","6853","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser true y es " + salida);

        salida = mDbHelper.updateReserva("","test1","6853","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva("8h3","test1","6853","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"","6853","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","9h3","27/03/12","30/03/12");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","9346","27","30/03/12");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","9346","27/03/1212","30/03/12");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","9346","aa/bb/cc","30/03/12");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","9346","27/03/12","30");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","9346","27/03/12","30/03/1212");
        System.out.println("El resultado tiene que ser false y es " + salida);

        salida = mDbHelper.updateReserva(String.valueOf(rowId),"test1","9346","27/03/12","aa/bb/cc");
        System.out.println("El resultado tiene que ser false y es " + salida);




        System.out.println("BorrarReserva...");
        salida =mDbHelper.deleteRes(rowId);
        System.out.println("El resultado tiene que ser true y es  " + salida);

        //No válidas

        salida =  mDbHelper.deleteRes(-1);
        System.out.println("El resultado tiene que ser false y es  " + salida);


        System.out.println("Empezando pruebas de HabitacionesReservadas");
        System.out.println("CreateHabitacionesReservadas...");

        rowId = mDbHelper.createReserva("test1","68523","27/03/12","30/03/12");

        salida = mDbHelper.updateReserva(String.valueOf(998),"test1","6853","27/03/12","30/03/12");

        mDbHelper.createHabitacion(999,"test1",5,10.0f,15.0f);

        rowId = mDbHelper.createHabitacionesReservadas(998,999,2);
        System.out.println("El resultado tiene que ser distinto de -1 y es  " + rowId);

        //No válidas
        rowId = mDbHelper.createHabitacionesReservadas(-1,999,2);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        rowId = mDbHelper.createHabitacionesReservadas(998,-1,2);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        rowId = mDbHelper.createHabitacionesReservadas(998,999,0);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);

        mDbHelper.deleteRes(998);
        mDbHelper.deleteHab(999);



        rowId = mDbHelper.createHabitacionesReservadas(998,999,10);
        System.out.println("El resultado tiene que ser -1 y es  " + rowId);


        System.out.println("Pasando a deleteHabsRes...");

        salida = mDbHelper.deleteHabsRes(998);
        System.out.println("El resultado tiene que ser true y es  " + salida);

        salida = mDbHelper.deleteHabsRes(-1);
        System.out.println("El resultado tiene que ser false y es  " + salida);

    }
}
