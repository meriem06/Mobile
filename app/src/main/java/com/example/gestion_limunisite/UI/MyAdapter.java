package com.example.gestion_limunisite.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_limunisite.R;
import com.example.gestion_limunisite.DAO.LuminositeDAO;
import com.example.gestion_limunisite.entity.Luminosite;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ProductViewHolder> {

    private Context context;
    private List<Luminosite> luminositeList;
    private LuminositeDAO luminositeDAO;
    private OnItemClickListener listener;

    // Interface pour gérer les clics sur les éléments
    public interface OnItemClickListener {
        void onItemClick(Luminosite luminosite);
    }

    // Constructeur mis à jour pour accepter LuminositeDAO
    public MyAdapter(Context context, List<Luminosite> luminositeList, LuminositeDAO luminositeDAO) {
        this.context = context;
        this.luminositeList = luminositeList;
        this.luminositeDAO = luminositeDAO;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.luminosite_activity, parent, false);
        return new ProductViewHolder(v);
    }

    public void updateData(List<Luminosite> newLuminositeList) {
        this.luminositeList = newLuminositeList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Luminosite luminosite = luminositeList.get(position);

        // Configuration des vues
        double intensite = luminosite.getIntensite();
        String intensiteFormatted = String.format("%.2f", intensite) + "%";
        holder.text1.setText(intensiteFormatted);

        holder.text2.setText(luminosite.isNormal() ? "IS Normal" : "IS NOT Normal");

        if (luminosite.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            holder.text3.setText("Date : " + dateFormat.format(luminosite.getDate()));
        } else {
            holder.text3.setText("Date : Not Available");
        }

        // Gestion du clic sur l'élément entier
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(luminosite);
            }
        });

        // Bouton pour supprimer
        holder.button.setOnClickListener(v -> {
            new Thread(() -> {
                // Suppression de l'élément dans la base de données
                luminositeDAO.deleteLuminosite(luminosite);
                luminositeList.remove(position);
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, luminositeList.size());
                });
            }).start();
        });

        // Bouton pour mettre à jour
        holder.buttonupdate.setOnClickListener(v -> {
            UpdateLuministe updateLuministeFragment = UpdateLuministe.newInstance(
                    luminosite.getId(),
                    luminosite.getIntensite(),
                    luminosite.isNormal(),
                    luminosite.getDate()
            );
            if (context instanceof androidx.fragment.app.FragmentActivity) {
                ((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, updateLuministeFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return luminositeList != null ? luminositeList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        Button button;
        Button buttonupdate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.textIntensite);
            text2 = itemView.findViewById(R.id.textView4);
            text3 = itemView.findViewById(R.id.textView7);
            button = itemView.findViewById(R.id.button4);
            buttonupdate = itemView.findViewById(R.id.button5);
        }
    }
}
