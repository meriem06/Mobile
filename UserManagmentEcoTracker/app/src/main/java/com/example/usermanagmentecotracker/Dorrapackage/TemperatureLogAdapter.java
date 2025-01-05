package com.example.usermanagmentecotracker.Dorrapackage;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanagmentecotracker.R;

import java.util.List;

public class TemperatureLogAdapter extends RecyclerView.Adapter<TemperatureLogAdapter.ViewHolder> {
    private final List<TemperatureEntry> temperatureList;

    public TemperatureLogAdapter(List<TemperatureEntry> temperatureList) {
        this.temperatureList = temperatureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_temperature, parent, false); // Use a custom layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TemperatureEntry entry = temperatureList.get(position);

        // Bind data to views
        holder.temperatureText.setText("Température: " + entry.getTemperature());
        holder.dateText.setText("Date: " + entry.getDate());
        holder.recommendationText.setText(entry.getRecommendation());

        // Handle long click to show confirmation dialog before deletion
        holder.itemView.setOnLongClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Confirmer la suppression")
                    .setMessage("Êtes-vous sûr de vouloir supprimer cet élément ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // Remove the item and notify the adapter
                        temperatureList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(holder.itemView.getContext(), "Élément supprimé", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Non", (dialog, which) -> {
                        // Cancel the dialog and do nothing
                        dialog.dismiss();
                    })
                    .show();

            return true; // Indicate the event was handled
        });
        // Handle click to confirm modification
        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Modifier l'élément")
                    .setMessage("Voulez-vous modifier la température et la date ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // Navigate to EditTemperatureFragment
                        EditTemperature editTemperatureFragment = new EditTemperature();

                        // Pass data to the fragment using a Bundle
                        Bundle args = new Bundle();
                        args.putInt("position", position);
                        args.putString("temperature", entry.getTemperature());
                        args.putString("date", entry.getDate());
                        editTemperatureFragment.setArguments(args);

                        // Perform the fragment transaction
                        ((FragmentActivity) holder.itemView.getContext())
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, editTemperatureFragment) // Replace R.id.fragment_container with the actual ID of your FrameLayout
                                .addToBackStack(null) // Add to back stack for navigation
                                .commit();
                    })
                    .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }


    @Override
    public int getItemCount() {
        return temperatureList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView temperatureText;
        TextView dateText;
        TextView recommendationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureText = itemView.findViewById(R.id.text_temperature);
            dateText = itemView.findViewById(R.id.text_date);
            recommendationText = itemView.findViewById(R.id.text_recommendation);
        }
    }
}
