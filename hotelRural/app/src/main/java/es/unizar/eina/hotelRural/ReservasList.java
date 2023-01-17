package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import es.unizar.eina.hotelRural.ui.main.SectionsPagerAdapterRes;
import es.unizar.eina.hotelRural.databinding.ActivityTabsResBinding;

/** Clase para la actividad ReservasList
 * En esta pantalla se muestra una lista de reservas que se puede ordenar por 3 opciones
 * @author Víctor Gallardo y Jaime Berruete
 */
public class ReservasList extends AppCompatActivity{

    private ActivityTabsResBinding binding;

    /** Método que se ejecuta al crearse la actividad */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTabsResBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapterRes sectionsPagerAdapter = new SectionsPagerAdapterRes(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        MaterialButton fab = binding.fab;

        /** Método que se ejecuta al pulsar el botón de crear reserva */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CrearReserva1.class);
                startActivity(i);
            }
        });
    }
}