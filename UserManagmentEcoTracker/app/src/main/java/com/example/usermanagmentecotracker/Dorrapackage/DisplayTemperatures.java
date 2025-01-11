package com.example.usermanagmentecotracker.Dorrapackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.JihedPackage.Entity.TemperatureEntry;

import com.example.usermanagmentecotracker.R;

import java.util.List;

public class DisplayTemperatures extends Fragment {
    private TemperatureLogAdapter adapter; // Declare the adapter as a field
    private RecyclerView recyclerView;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.les_donnes, container, false);

        // Back to Home button logic
        ImageButton backHome = view.findViewById(R.id.button_home);
        backHome.setOnClickListener(v -> requireActivity().onBackPressed());

        // Initialize database
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();

        // Refresh button logic
        ImageButton refreshButton = view.findViewById(R.id.refresh);
        refreshButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Refreshing data...", Toast.LENGTH_SHORT).show();
            refreshData();
        });

        // Navigate to Statistics
        ImageButton statButton = view.findViewById(R.id.statButton);
        statButton.setOnClickListener(v -> {
            try {
                // Navigate to the Statistic fragment
                Fragment temperatureStatisticsFragment = new Statistic();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, temperatureStatisticsFragment) // Ensure correct container ID
                        .addToBackStack(null)
                        .commit();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to open statistics.", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.temperature_log_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch data asynchronously but update RecyclerView on the main thread
        new Thread(() -> {
            // Fetch data from the database on a background thread
            List<TemperatureEntry> temperatureList = db.temperatureDao().getTemperaturesByuserid(LoginActivity.idUserToConsommations);

            // Update the RecyclerView adapter on the main thread
            getActivity().runOnUiThread(() -> {
                adapter = new TemperatureLogAdapter(temperatureList);
                recyclerView.setAdapter(adapter);
            });
        }).start();

        return view;
    }

    private void refreshData() {
        // Notify the adapter that the dataset has changed
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
