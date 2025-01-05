package com.example.usermanagmentecotracker.JihedPackage.jihedFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usermanagmentecotracker.Dorrapackage.TemperatureFragment;
import com.example.usermanagmentecotracker.JihedPackage.HomeActivity;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.JihedPackage.SignUpActivity;
import com.example.usermanagmentecotracker.R;
import com.example.usermanagmentecotracker.cyrinePackage.affichage_activitee;
import com.example.usermanagmentecotracker.mariemPackage.AffichageLuminisitee;
import com.example.usermanagmentecotracker.waelPackage.SoundmeterActivity;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView gpsButton = view.findViewById(R.id.button_gps_fragment);
        ImageView soundbutton = view.findViewById(R.id.button_sound);
        ImageView lightbutton = view.findViewById(R.id.button_lum);
        ImageView speedbutton = view.findViewById(R.id.button_aceleration);
        ImageView tempbutton = view.findViewById(R.id.button_temp);



        gpsButton.setOnClickListener(v -> {
            // Load GPS Fragment
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(new GpsFragment());
            }
        });
        soundbutton.setOnClickListener(v -> {
            Intent intent_sound = new Intent(requireContext(), SoundmeterActivity.class);
            startActivity(intent_sound);
        });
        lightbutton.setOnClickListener(v -> {
            // Load GPS Fragment
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(new AffichageLuminisitee());
            }
        });
        speedbutton.setOnClickListener(v -> {
            Intent intent_speed = new Intent(requireContext(), affichage_activitee.class);
            startActivity(intent_speed);
        });
        tempbutton.setOnClickListener(v -> {
            // Load GPS Fragment
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(new TemperatureFragment());
            }
        });

        return view;
    }
}