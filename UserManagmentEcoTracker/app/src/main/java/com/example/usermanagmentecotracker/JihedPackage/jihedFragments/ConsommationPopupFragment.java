package com.example.usermanagmentecotracker.JihedPackage.jihedFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.widget.Button;
import android.widget.Toast;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Directory.ConsommationDao;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Consommation;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.R;
import com.example.usermanagmentecotracker.JihedPackage.adaptateurs.ConsommationAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsommationPopupFragment extends DialogFragment {
    private ExecutorService executorService;
    private List<Consommation> consommations;
    private ConsommationDao consommationDao;
    private RecyclerView recyclerView;
    private ConsommationAdapter adapter;
    private AppDatabase db; // Room Database instance

    public ConsommationPopupFragment(List<Consommation> consommations) {
        this.consommations = consommations;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consommation_popup, container, false);

        // Initialize database and DAO
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();
        consommationDao = db.consommationDao();

        // Executor service to handle background tasks
        executorService = Executors.newSingleThreadExecutor();

        // Initialize RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.consommationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch consommations for the user
        fetchConsommations();

        // Set up button listeners
        Button buttonDeleteAll = view.findViewById(R.id.buttonDeleteAll);
        buttonDeleteAll.setOnClickListener(v -> deleteAllConsommations());

        Button buttonSortByDistance = view.findViewById(R.id.buttonSortByDistance);
        buttonSortByDistance.setOnClickListener(v -> sortConsommationsByDistance());

        return view;
    }

    private void fetchConsommations() {
        executorService.execute(() -> {
            try {
                // Get the list of consommations for the user
                consommations = consommationDao.getConsommationsForUser(LoginActivity.idUserToConsommations);
                requireActivity().runOnUiThread(() -> {
                    // Set up the RecyclerView adapter with fetched data
                    adapter = new ConsommationAdapter(getContext(), consommations);
                    recyclerView.setAdapter(adapter);
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error loading consommations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void deleteAllConsommations() {
        executorService.execute(() -> {
            try {
                // Perform the database delete operation
                consommationDao.deleteAllConsommationsForUser(LoginActivity.idUserToConsommations);

                requireActivity().runOnUiThread(() -> {
                    consommations.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "All consommations deleted.", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error deleting consommations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void sortConsommationsByDistance() {
        executorService.execute(() -> {
            try {
                // Fetch sorted consommations by distance
                List<Consommation> sortedConsommations = consommationDao.getConsommationsSortedByDistance(LoginActivity.idUserToConsommations);

                requireActivity().runOnUiThread(() -> {
                    consommations.clear();
                    consommations.addAll(sortedConsommations);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Consommations sorted by distance.", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error sorting consommations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
