package com.example.usermanagmentecotracker.JihedPackage.jihedFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usermanagmentecotracker.JihedPackage.HomeActivity;
import com.example.usermanagmentecotracker.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView gpsButton = view.findViewById(R.id.button_gps_fragment);
        gpsButton.setOnClickListener(v -> {
            // Load GPS Fragment
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(new GpsFragment());
            }
        });

        return view;
    }
}