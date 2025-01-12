package com.example.usermanagmentecotracker.JihedPackage.jihedFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.usermanagmentecotracker.R;

public class ThemeFragment extends Fragment {

    private Switch switchTheme;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        // Initialize the Switch
        switchTheme = view.findViewById(R.id.switchTheme);

        // Load saved theme preference
        boolean isNightMode = loadThemePreference();
        switchTheme.setChecked(isNightMode);

        // Set up the toggle listener
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleTheme(isChecked);
            saveThemePreference(isChecked);
        });

        return view;
    }

    private void toggleTheme(boolean isNightMode) {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void saveThemePreference(boolean isNightMode) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPreferences", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isNightMode", isNightMode);
        editor.apply();
    }

    private boolean loadThemePreference() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPreferences", getContext().MODE_PRIVATE);
        return sharedPreferences.getBoolean("isNightMode", false);
    }
}
