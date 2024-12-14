package com.example.gestion_limunisite.UI;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestion_limunisite.DAO.LuminositeDAO;
import com.example.gestion_limunisite.Database.AppDatabase;
import com.example.gestion_limunisite.R;
import com.example.gestion_limunisite.entity.Luminosite;

import java.util.Date;

public class Ajouter_Luminosite extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView lightPercentageTextView;
    private static final float MAX_LUX = 1000; // Référence pour la luminosité maximale en lux
    private float currentLightLevel; // Pour stocker la valeur actuelle de la luminosité
     LuminositeDAO luminositeDAO;

    public Ajouter_Luminosite() {
        // Constructeur public requis
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View view = inflater.inflate(R.layout.fragment_ajouter__luminosite, container, false);

        // Initialize views
        lightPercentageTextView = view.findViewById(R.id.lightPercentageTextView);
        Button saveButton = view.findViewById(R.id.button);
        Button returnButton = view.findViewById(R.id.button2);

        // Initialize SensorManager
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Initialize LuminositeDAO
        luminositeDAO = AppDatabase.getInstance(getContext()).luminositeDAO();

        if (lightSensor == null) {
            Log.e("Sensor", "Light sensor not available");
        }

        // Save button action
        saveButton.setOnClickListener(v -> {
            float lightPercentage = Math.min((currentLightLevel / MAX_LUX) * 100, 100); // Convert to percentage
            boolean isNormal = lightPercentage <= 50; // Threshold at 50%

            // Create a new Luminosite object
            Luminosite luminosite = new Luminosite();
            luminosite.setIntensite(lightPercentage);
            luminosite.setNormal(isNormal);
            luminosite.setDate(new Date());
            luminosite.setUserId(0);
            // Insert into database
            new Thread(() -> {
                long id = luminositeDAO.insert(luminosite);
                if (id > 0) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Luminosity saved successfully!", Toast.LENGTH_SHORT).show();
                        Log.d("RoomDatabase", "Insert success: ID = " + id);
                        requireActivity().getSupportFragmentManager().popBackStack();
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failed to save luminosity!", Toast.LENGTH_SHORT).show();
                        Log.e("RoomDatabase", "Error inserting into database.");
                    });
                }
            }).start();
        });

        // Return button action
        returnButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Désenregistrer le gestionnaire de capteurs pour éviter les fuites de mémoire
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            currentLightLevel = sensorEvent.values[0];
            float lightPercentage = Math.min((currentLightLevel / MAX_LUX) * 100, 100);
            lightPercentageTextView.setText("Luminosité: " + String.format("%.2f", lightPercentage) + " %");
            Log.d("LightSensor", "Light level: " + currentLightLevel + " lux, Luminosité: " + lightPercentage + " %");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Peut être utilisé pour gérer des changements de précision
    }
}
