package com.example.gestion_activityrecognition.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.gestion_activityrecognition.DAO.ActivityDAO;
import com.example.gestion_activityrecognition.Database.ActivityDatabase;
import com.example.gestion_activityrecognition.R;
import com.example.gestion_activityrecognition.entity.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class update_activitee extends Fragment {
    private ActivityDAO activityDAO;
    private int id;

    public static update_activitee newInstance(int id, String activityType, Date date) {
        update_activitee fragment = new update_activitee();
        Bundle args = new Bundle();
        args.putInt("ARG_ID", id);
        args.putString("ARG_ACTIVITY_TYPE", activityType);
        if (date != null) {
            args.putString("ARG_DATE", new SimpleDateFormat("dd/MM/yyyy").format(date));
        } else {
            args.putString("ARG_DATE", ""); // Default value if date is null
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDAO = ActivityDatabase.getInstance(getContext()).activityDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_activitee, container, false);

        Spinner spinnerActivityType = view.findViewById(R.id.spinnerActivity);
        EditText editDate = view.findViewById(R.id.editTextDate2);
        Button buttonUpdate = view.findViewById(R.id.button9);
        Button buttonCancel = view.findViewById(R.id.button10);

        // Set the adapter for the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.activity_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivityType.setAdapter(adapter);

        if (getArguments() != null) {
            id = getArguments().getInt("ARG_ID");
            String activityType = getArguments().getString("ARG_ACTIVITY_TYPE");
            String date = getArguments().getString("ARG_DATE", "");

            // Set initial values in the components
            setSpinnerValue(spinnerActivityType, activityType);
            editDate.setText(date.isEmpty() ? "" : date);
        }

        buttonUpdate.setOnClickListener(v -> {
            String activityType = spinnerActivityType.getSelectedItem().toString();
            String dateString = editDate.getText().toString().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false); // Strict parsing
            Date date;

            try {
                if (!dateString.isEmpty()) {
                    date = sdf.parse(dateString);

                    // Validate the date to be in the same month and year
                    Calendar enteredDate = Calendar.getInstance();
                    enteredDate.setTime(date);

                    Calendar currentDate = Calendar.getInstance(); // Current system date

                    if (enteredDate.get(Calendar.YEAR) != currentDate.get(Calendar.YEAR) ||
                            enteredDate.get(Calendar.MONTH) != currentDate.get(Calendar.MONTH)) {
                        throw new IllegalArgumentException("The date must be in the same month and year as the system date.");
                    }
                } else {
                    // Keep the existing date if no date is provided
                    String existingDate = getArguments().getString("ARG_DATE", "");
                    if (!existingDate.isEmpty()) {
                        date = sdf.parse(existingDate);
                    } else {
                        date = new Date(); // Default to current date
                    }
                }

                Activity updatedActivity = new Activity(id, activityType, date);

                new Thread(() -> {
                    activityDAO.updateActivity(updatedActivity);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    });
                }).start();

            } catch (ParseException e) {
                Toast.makeText(requireContext(), "Error in date format (expected DD/MM/YYYY)", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancel.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        if (spinner.getAdapter() != null) {
            for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
                if (spinner.getAdapter().getItem(i).toString().equals(value)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }
}