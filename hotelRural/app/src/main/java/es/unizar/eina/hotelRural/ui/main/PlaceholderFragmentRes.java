package es.unizar.eina.hotelRural.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import es.unizar.eina.hotelRural.databinding.FragmentTabsHabBinding;

import es.unizar.eina.hotelRural.fragments.fragmentRes;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentRes extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static PageViewModel pageViewModel;
    private FragmentTabsHabBinding binding;

    public static Fragment newInstance(int index) {
        Fragment fragment=null;
        System.out.printf("El valor de index es %d\n", index);
        fragment = new fragmentRes(index);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentTabsHabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}