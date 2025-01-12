package com.example.usermanagmentecotracker.JihedPackage.adaptateurs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanagmentecotracker.JihedPackage.Entity.Defis;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.JihedPackage.api.DefisApi;
import com.example.usermanagmentecotracker.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
            updateDefisValidation(defis);
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
    private void updateDefisValidation(Defis defis) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginActivity.BASE_URL)  // Ensure this is correctly set
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DefisApi defisApi = retrofit.create(DefisApi.class);

        // API Call to update the `isCompleted` field of the Defis
        Call<String> call = defisApi.updateIsCompleted(defis.getIdDefi(), defis.getCompleted());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Show success toast
                    Toast.makeText(context, "Defis updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle errors from the server
                    Toast.makeText(context, "Failed to update Defis: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle communication failures
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
