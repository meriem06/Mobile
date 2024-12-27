package com.example.usermanagmentecotracker.JihedPackage.jihedFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Defis;
import com.example.usermanagmentecotracker.JihedPackage.adaptateurs.DefisAdapter;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.R;

import java.util.ArrayList;
import java.util.List;

public class DefisFragment extends Fragment {

    private RecyclerView recyclerView;
    private DefisAdapter defisAdapter;
    private List<Defis> defisList = new ArrayList<>();
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_defis, container, false);


        recyclerView = view.findViewById(R.id.recycler_defis);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Button addDefisButton = view.findViewById(R.id.button_add_defis);
        addDefisButton.setOnClickListener(v -> showAddDefisDialog());

        db = Room.databaseBuilder(getContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();


        loadDefis();

        return view;
    }

    private void loadDefis() {
        new Thread(() -> {
            defisList = db.defisDao().getAllDefis();
            getActivity().runOnUiThread(() -> {
                defisAdapter = new DefisAdapter(defisList, getContext());
                recyclerView.setAdapter(defisAdapter);
            });
        }).start();
    }

    private void showAddDefisDialog() {
        // Create a dialog view
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_defis, null);
        builder.setView(dialogView);

        // Initialize views in the dialog
        EditText defisDescriptionInput = dialogView.findViewById(R.id.edittext_defis_description);
        Button addDefisButton = dialogView.findViewById(R.id.button_add_defis_popup);

        AlertDialog dialog = builder.create();

        // Handle the "Add" button click
        addDefisButton.setOnClickListener(v -> {
            String description = defisDescriptionInput.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(getContext(), "Description cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add the new defis to the database
            new Thread(() -> {
                Defis newDefis = new Defis(description, 1, false); // Replace 1 with actual user ID if needed
                db.defisDao().insertDefis(newDefis);

                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Defis added successfully!", Toast.LENGTH_SHORT).show();
                    loadDefis(); // Reload the list
                    dialog.dismiss();
                });
            }).start();
        });

        dialog.show();
    }
}
