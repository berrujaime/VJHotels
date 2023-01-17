package es.unizar.eina.hotelRural;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import es.unizar.eina.hotelRural.ui.main.PlaceholderFragment;
import es.unizar.eina.hotelRural.ui.main.SectionsPagerAdapter;
import es.unizar.eina.hotelRural.databinding.ActivityTabsHabBinding;

/** Clase para la actividad RoomsList
 * En esta pantalla se muestra una lista de habitaciones que se puede ordenar por 3 opciones
 * @author Víctor Gallardo y Jaime Berruete
 */
public class RoomsList extends AppCompatActivity{

    private ActivityTabsHabBinding binding;

    /** Método que se ejecuta al crearse la actividad */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTabsHabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        MaterialButton fab = binding.fab;

        /** Método que se ejecuta al pulsar el botón de crear habitación */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),CrearHabitacion.class);
                startActivity(i);
            }
        });
    }
}