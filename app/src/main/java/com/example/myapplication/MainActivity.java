package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Activity.SoundmeterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Active la gestion des barres système

        setContentView(R.layout.activity_main);

        // Trouver le bouton ajouté dans le layout
        Button buttonGoToSoundmeter = findViewById(R.id.buttonGoToSoundmeter);

        // Action du bouton : lancement de SoundmeterActivity
        buttonGoToSoundmeter.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SoundmeterActivity.class);
            startActivity(intent);
        });

        // Gestion des espaces pour les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
