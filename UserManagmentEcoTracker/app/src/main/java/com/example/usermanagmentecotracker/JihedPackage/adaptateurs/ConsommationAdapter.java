package com.example.usermanagmentecotracker.JihedPackage.adaptateurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanagmentecotracker.JihedPackage.Entity.Consommation;
import com.example.usermanagmentecotracker.R;

import java.util.List;

public class ConsommationAdapter extends RecyclerView.Adapter<ConsommationAdapter.ViewHolder> {

    private Context context;
    private List<Consommation> consommations;

    public ConsommationAdapter(Context context, List<Consommation> consommations) {
        this.context = context;
        this.consommations = consommations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consommation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Consommation consommation = consommations.get(position);
        holder.placeTextView.setText("Place : "+consommation.getPlace());
        holder.distanceTextView.setText("Distance : "+consommation.getDistance());
        holder.timeTextView.setText("Car time : "+consommation.getTime());
        holder.costTextView.setText("Cost : "+consommation.getCost());
    }

    @Override
    public int getItemCount() {
        return consommations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeTextView, distanceTextView, timeTextView, costTextView;  // Add costTextView here

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeTextView = itemView.findViewById(R.id.placeTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            costTextView = itemView.findViewById(R.id.costTextView);  // Initialize costTextView here
        }
    }
}

