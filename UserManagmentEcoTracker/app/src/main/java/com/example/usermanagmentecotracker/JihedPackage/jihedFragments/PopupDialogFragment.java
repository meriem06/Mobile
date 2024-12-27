package com.example.usermanagmentecotracker.JihedPackage.jihedFragments;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Consommation;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.R;

import org.w3c.dom.Text;

public class PopupDialogFragment extends DialogFragment {

    private String distance, walkingTime, carTime , placeName , cost;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the dialog
        View view = inflater.inflate(R.layout.popup_layout, container, false);

        // Initialize database
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();

        if (getArguments() != null) {
            distance = getArguments().getString("distance", "N/A");
            walkingTime = getArguments().getString("walkingTime", "N/A");
            carTime = getArguments().getString("carTime", "N/A");
            placeName = getArguments().getString("placeName", "N/A");
            cost = getArguments().getString("Cost", "N/A");


        }

        // Set the texts in the layout
        TextView distanceText = view.findViewById(R.id.popupDistanceText);
        TextView walkingTimeText = view.findViewById(R.id.popupWalkingTimeText);
        TextView carTimeText = view.findViewById(R.id.popupCarTimeText);
        TextView placeDestinationName = view.findViewById(R.id.popupPlacenameText);
        Button saveButton = view.findViewById(R.id.popupSaveButton);
        TextView costText = view.findViewById(R.id.popupCostText);



        distanceText.setText("Distance: " + distance +"KM");
        walkingTimeText.setText("Walking Time: " + walkingTime + "HOURS");
        carTimeText.setText("Car Time: " + carTime + "HOURS");
        placeDestinationName.setText("place name : " + placeName);
        costText.setText("place name : " + cost +" TND");


        saveButton.setOnClickListener(v -> {
            // Save data into the database
            new Thread(() -> {
                Consommation consommation = new Consommation();
                consommation.setDistance(distance);
                consommation.setTime(walkingTime);
                consommation.setPlace(placeName);
                consommation.setCost(cost);
                consommation.setUserId(LoginActivity.idUserToConsommations);

                db.consommationDao().insertConsommation(consommation);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                    dismiss();
                });
            }).start();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the dialog to appear like a popup
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
