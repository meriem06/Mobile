package com.example.usermanagmentecotracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanagmentecotracker.Entity.Defis;

import java.util.List;

public class DefisAdapter extends RecyclerView.Adapter<DefisAdapter.DefisViewHolder> {
    private List<Defis> defisList;
    private Context context;

    public DefisAdapter(List<Defis> defisList, Context context) {
        this.defisList = defisList;
        this.context = context;
    }

    @NonNull
    @Override
    public DefisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_defis_adapter, parent, false);
        return new DefisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DefisViewHolder holder, int position) {
        Defis defis = defisList.get(position);
        holder.description.setText(defis.getDescriptionDefi());
        holder.checkBox.setChecked(defis.getCompleted());

        // Change color based on completion status
        holder.itemView.setBackgroundColor(defis.getCompleted() ? Color.GREEN : Color.RED);

        // Checkbox listener
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            defis.setCompleted(isChecked);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return defisList.size();
    }

    public static class DefisViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        CheckBox checkBox;

        public DefisViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.defis_description);
            checkBox = itemView.findViewById(R.id.defis_checkbox);
        }
    }
}
