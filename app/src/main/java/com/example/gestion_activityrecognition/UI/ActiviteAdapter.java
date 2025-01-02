package com.example.gestion_activityrecognition.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_activityrecognition.R;
import com.example.gestion_activityrecognition.DAO.ActivityDAO;
import com.example.gestion_activityrecognition.entity.Activity;

import java.text.SimpleDateFormat;
import java.util.List;

public class ActiviteAdapter extends RecyclerView.Adapter<ActiviteAdapter.ActiviteViewHolder> {

    private Context context;
    private List<Activity> activiteList;
    private ActivityDAO activiteDAO;
    private OnItemClickListener listener;

    // Interface pour gérer les clics sur les éléments
    public interface OnItemClickListener {
        void onItemClick(Activity activite);
    }

    // Constructeur mis à jour pour accepter ActiviteDAO
    public ActiviteAdapter(Context context, List<Activity> activiteList, ActivityDAO activiteDAO) {
        this.context = context;
        this.activiteList = activiteList;
        this.activiteDAO = activiteDAO;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActiviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity, parent, false);
        return new ActiviteViewHolder(v);
    }

    public void updateData(List<Activity> newActiviteList) {
        this.activiteList = newActiviteList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ActiviteViewHolder holder, int position) {
        Activity activite = activiteList.get(position);

        // Configuration des vues
        holder.textNom.setText("Activitée: " + activite.getActivityType());


        if (activite.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            holder.textDate.setText("Date : " + dateFormat.format(activite.getDate()));
        } else {
            holder.textDate.setText("Date : Non disponible");
        }

        // Gestion du clic sur l'élément entier
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(activite);
            }
        });
        // Bouton de suppression
        holder.buttonDelete.setOnClickListener(v -> {
            new Thread(() -> {
                // Supprimer l'activité de la base de données
                activiteDAO.delete(activite);
                // Mettre à jour la liste principale
                activiteList.remove(position);

                // Actualiser la RecyclerView sur le thread principal
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, activiteList.size());
                });
            }).start();
        });
        holder.buttonUpdate.setOnClickListener(v -> {
            update_activitee updateActivityFragment = update_activitee.newInstance(
                    activite.getId(),
                    activite.getActivityType(),
                    activite.getDate()
            );
            if (context instanceof androidx.fragment.app.FragmentActivity) {
                ((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, updateActivityFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return activiteList != null ? activiteList.size() : 0;
    }

    public static class ActiviteViewHolder extends RecyclerView.ViewHolder {
        TextView textNom;

        TextView textDate;
        Button buttonDelete;
        Button buttonUpdate;

        public ActiviteViewHolder(@NonNull View itemView) {
            super(itemView);
            textNom = itemView.findViewById(R.id.textView4);

            textDate =itemView.findViewById(R.id.textView7);
            buttonDelete = itemView.findViewById(R.id.button4);
            buttonUpdate = itemView.findViewById(R.id.button5);

        }
    }
}
