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
import es.unizar.eina.hotelRural.ui.main.SectionsPagerAdapterRes;
import es.unizar.eina.hotelRural.databinding.ActivityTabsResBinding;

public class ReservasList extends AppCompatActivity{

    private ActivityTabsResBinding binding;




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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(),CrearReserva.class);
                startActivity(i);
            }
        });
    }
}