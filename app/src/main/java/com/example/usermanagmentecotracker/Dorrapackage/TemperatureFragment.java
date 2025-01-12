package com.example.usermanagmentecotracker.Dorrapackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usermanagmentecotracker.JihedPackage.api.RetrofitInstance;
import com.example.usermanagmentecotracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

public class TemperatureFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;   // Sensor Manager
    private Sensor temperatureSensor;      // Temperature Sensor
    private TextView temperatureTextView;  // TextView for temperature
    private TextView tempDescriptionTextView; // TextView for temperature description

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperature, container, false);

        // Find buttons
        ImageButton tableButton = view.findViewById(R.id.button_tab);
        Button addTemperatureButton = view.findViewById(R.id.add_temperature_button);

        // Initialize the TextViews
        temperatureTextView = view.findViewById(R.id.TemperatureTextView);
        tempDescriptionTextView = view.findViewById(R.id.temp_description);

        // Initialize SensorManager and TemperatureSensor
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        } else {
            Toast.makeText(requireActivity(), "Sensor Manager not available", Toast.LENGTH_SHORT).show();
        }

        // Navigate to DisplayTemperatures Fragment
        tableButton.setOnClickListener(v -> {
            Fragment displayTemperaturesFragment = new DisplayTemperatures(); // Replace with your DisplayTemperatures fragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, displayTemperaturesFragment) // Replace R.id.fragment_container with the actual ID of your FrameLayout
                    .addToBackStack(null)
                    .commit();
        });

        // Navigate to AddTemperature Fragment
        addTemperatureButton.setOnClickListener(v -> {
            Fragment addTemperatureFragment = new AddTemperature();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addTemperatureFragment) // Replace R.id.fragment_container with the actual ID of your FrameLayout
                    .addToBackStack(null)
                    .commit();
        });

        addTemperatureButton.setOnLongClickListener(v -> {
            // Get the temperature text
            String temperature = temperatureTextView.getText().toString();

            // Parse the temperature value (e.g., "Temperature: 25°C")
            String tempValue = temperature.replace("Temperature: ", "").replace("°C", "").trim();

            try {
                double tempDouble = Double.parseDouble(tempValue);

                // Get the exact current date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = dateFormat.format(new Date());

                // Create a new TemperatureEntry
                TemperatureEntry newEntry = new TemperatureEntry(String.valueOf(tempDouble), formattedDate);

                // Add the entry to the shared data list
                List<TemperatureEntry> temperatureList = TemperatureData.getTemperatureList();
                temperatureList.add(newEntry);

                // Notify the user
                Toast.makeText(getContext(), "Temperature ajouter le : " + formattedDate, Toast.LENGTH_SHORT).show();
                // Use Retrofit to send the data to the backend
                TemperatureApi temperatureApi = RetrofitInstance.getRetrofitInstance().create(TemperatureApi.class);
                Call<Void> call = temperatureApi.sendTemperature(newEntry);

                // Make the API call asynchronously
                call.enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Temperature sent successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to send temperature: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return true; // Event handled
            } catch (NumberFormatException e) {
                // Handle parsing errors
                Toast.makeText(getContext(), "Invalid temperature format", Toast.LENGTH_SHORT).show();
                return false; // Event not handled
            }

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register the temperature sensor
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(requireActivity(), "Temperature sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener
        if (sensorManager != null && temperatureSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];

            // Display the temperature in the TextView
            @SuppressLint("DefaultLocale")
            String temperatureText = String.format("%.1f°C", temperature);
            temperatureTextView.setText(temperatureText);

            // Update the temperature description based on the value
            if (temperature > 32) {
                tempDescriptionTextView.setText("Climatisation recommandée");
            } else if (temperature < 15) {
                tempDescriptionTextView.setText("Chauffage recommandé");
            } else {
                tempDescriptionTextView.setText("Température idéale");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle changes in sensor accuracy if needed
    }
}
