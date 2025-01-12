package com.example.usermanagmentecotracker.Dorrapackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usermanagmentecotracker.R;

public class EditTemperature extends Fragment {
    private EditText editTemperature;
    private DatePicker datePicker;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.edit_temp, container, false);

        // Initialize views
        editTemperature = rootView.findViewById(R.id.edit_temperature);
        datePicker = rootView.findViewById(R.id.date_picker2);
        Button updateButton = rootView.findViewById(R.id.update_button);
        Button backButton = rootView.findViewById(R.id.back_button);

        // Retrieve data from the arguments
        if (getArguments() != null) {
            position = getArguments().getInt("position", -1);
            String temperature = getArguments().getString("temperature");
            String date = getArguments().getString("date");

            // Pre-fill data
            editTemperature.setText(temperature);
            if (date != null) {
                String[] dateParts = date.split("/"); // Assuming date format is "DD/MM/YYYY"
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based
                int year = Integer.parseInt(dateParts[2]);
                datePicker.updateDate(year, month, day);
            }
        }

        // Handle update button click
        updateButton.setOnClickListener(v -> {
            String newTemperature = editTemperature.getText().toString();
            int selectedDay = datePicker.getDayOfMonth();
            int selectedMonth = datePicker.getMonth() + 1; // Month is 0-based
            int selectedYear = datePicker.getYear();
            String newDate = selectedDay + "/" + selectedMonth + "/" + selectedYear;

            if (!newTemperature.isEmpty()) {
                try {
                    float temperatureValue = Float.parseFloat(newTemperature);

                    // Validate temperature range
                    if (temperatureValue < -50 || temperatureValue > 60) {
                        Toast.makeText(getContext(), "La température doit être entre -50 et 60 °C", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Update the entry in TemperatureData
                    TemperatureEntry updatedEntry = new TemperatureEntry(String.valueOf(temperatureValue), newDate);
                    TemperatureData.getTemperatureList().set(position, updatedEntry);

                    Toast.makeText(getContext(), "Température modifiée avec succès", Toast.LENGTH_SHORT).show();

                    // Navigate back using the FragmentManager
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Veuillez entrer une température valide", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Veuillez entrer une température", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle back button click
        backButton.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }
}
