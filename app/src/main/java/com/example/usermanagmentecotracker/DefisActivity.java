package com.example.usermanagmentecotracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.usermanagmentecotracker.Database.AppDatabase;
import com.example.usermanagmentecotracker.Entity.Defis;
import com.example.usermanagmentecotracker.NameDatabaseJihed.DatabaseName;

import java.util.ArrayList;
import java.util.List;

public class DefisActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DefisAdapter defisAdapter;
    private List<Defis> defisList = new ArrayList<>();
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defis);

        recyclerView = findViewById(R.id.recycler_defis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button buttonAddDefis = findViewById(R.id.button_add_defis);

        // Initialize the database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();

        // Load defis from the database
        loadDefis();

        // Handle "Add Defis" button click
        buttonAddDefis.setOnClickListener(v -> addNewDefis());
    }

    private void loadDefis() {
        // Fetch all defis from the database
        new Thread(() -> {
            defisList = db.defisDao().getAllDefis(); // Ensure this method exists in your DAO
            runOnUiThread(() -> {
                defisAdapter = new DefisAdapter(defisList, this);
                recyclerView.setAdapter(defisAdapter);
            });
        }).start();
    }

    private void addNewDefis() {
        // Add a default new defis
        new Thread(() -> {
            Defis newDefis = new Defis("New Defis Description", 1, false); // Replace userId with actual value
            db.defisDao().insertDefis(newDefis);
            runOnUiThread(() -> {
                Toast.makeText(this, "Defis Added!", Toast.LENGTH_SHORT).show();
                loadDefis(); // Reload to reflect changes
            });
        }).start();
    }
}
