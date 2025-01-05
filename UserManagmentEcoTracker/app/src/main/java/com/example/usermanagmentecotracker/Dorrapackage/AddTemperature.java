package com.example.usermanagmentecotracker.Dorrapackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usermanagmentecotracker.R;

public class AddTemperature extends Fragment {
    private EditText inputTemperature;
    private DatePicker datePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.add_temp, container, false);

        // Initialize views
        datePicker = rootView.findViewById(R.id.date_picker);
        inputTemperature = rootView.findViewById(R.id.input_temperature);
        ImageButton backButton = rootView.findViewById(R.id.back_button);
        Button submitButton = rootView.findViewById(R.id.submit_button);

        // Back Button Logic
        backButton.setOnClickListener(v -> {
            // Use the FragmentManager to pop the current fragment
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        // Submit Button Logic
        submitButton.setOnClickListener(v -> {
            // Get the temperature input
            String temperatureInput = inputTemperature.getText().toString();

            // Validate temperature input
            if (!temperatureInput.isEmpty()) {
                try {
                    float temperature = Float.parseFloat(temperatureInput);

                    // Check if the temperature is within the valid range
                    if (temperature < -50 || temperature > 50) {
                        Toast.makeText(getContext(), "La température doit être entre -50 et 60 °C", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get the selected date from the DatePicker
                    String date = getSelectedDate();

                    // Create a new TemperatureEntry object with the input and date
                    TemperatureEntry entry = new TemperatureEntry(String.valueOf(temperature), date);

                    // Add the entry to the shared data list
                    TemperatureData.addTemperatureEntry(entry);

                    // Show success message
                    Toast.makeText(getContext(), "Température ajoutée avec succès", Toast.LENGTH_SHORT).show();

                    // Use the FragmentManager to pop the current fragment
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Veuillez entrer une température valide", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Show error message if temperature input is empty
                Toast.makeText(getContext(), "Veuillez entrer une température", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private String getSelectedDate() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Months are zero-based
        int year = datePicker.getYear();
        return day + "/" + month + "/" + year;
    }
}
