package com.example.usermanagmentecotracker.JihedPackage.adaptateurs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.usermanagmentecotracker.JihedPackage.Directory.HistoriqueDao;
import com.example.usermanagmentecotracker.JihedPackage.Entity.HistoriqueTable;
import com.example.usermanagmentecotracker.R;

import java.util.List;

public class HistoriqueAdapter extends BaseAdapter {

    private Context context;
    private List<HistoriqueTable> historiqueList;
    private HistoriqueDao historiqueDao;

    public HistoriqueAdapter(Context context, List<HistoriqueTable> historiqueList, HistoriqueDao historiqueDao) {
        this.context = context;
        this.historiqueList = historiqueList;
        this.historiqueDao = historiqueDao;
    }

    @Override
    public int getCount() {
        return historiqueList.size();
    }

    @Override
    public Object getItem(int position) {
        return historiqueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return historiqueList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_historique, parent, false);
        }

        // Récupérer l'élément de l'historique
        HistoriqueTable historique = historiqueList.get(position);

        // Mettre à jour les vues de l'élément
        TextView soundLevelTextView = convertView.findViewById(R.id.soundLevelTextView);
        TextView timestampTextView = convertView.findViewById(R.id.timestampTextView);

        soundLevelTextView.setText(String.format("Niveau de son: %.2f dB", historique.getSoundLevel()));
        timestampTextView.setText("Date: " + historique.getTimestamp());

        // Ajouter un écouteur de clic pour afficher le dialogue
        convertView.setOnClickListener(v -> showEditDeleteDialog(historique));

        return convertView;
    }

    // Méthode pour afficher le dialogue de modification et de suppression
    private void showEditDeleteDialog(HistoriqueTable historique) {
        // Créer un dialogue avec un champ pour la modification
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Modifier ou Supprimer l'historique");

        // Créer un layout personnalisé pour le dialogue
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_delete_historique, null);
        builder.setView(dialogView);

        EditText soundLevelEditText = dialogView.findViewById(R.id.editSoundLevel);
        EditText timestampEditText = dialogView.findViewById(R.id.editTimestamp);

        // Initialiser les champs avec les valeurs actuelles
        soundLevelEditText.setText(String.valueOf(historique.getSoundLevel()));
        timestampEditText.setText(historique.getTimestamp());

        // Bouton pour modifier
        builder.setPositiveButton("Modifier", (dialog, which) -> {
            try {
                float newSoundLevel = Float.parseFloat(soundLevelEditText.getText().toString());
                String newTimestamp = timestampEditText.getText().toString();

                // Mettre à jour les données dans la base de données
                historique.setSoundLevel(newSoundLevel);
                historique.setTimestamp(newTimestamp);

                // Insérer les nouvelles données
                new Thread(() -> {
                    historiqueDao.update(historique);  // Vous devez avoir une méthode update dans votre DAO
                }).start();

                Toast.makeText(context, "Historique mis à jour", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Erreur dans les données entrées", Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton pour supprimer
        builder.setNegativeButton("Supprimer", (dialog, which) -> {
            // Supprimer l'entrée de l'historique
            new Thread(() -> {
                historiqueDao.delete(historique);  // Utilisez votre méthode delete du DAO
            }).start();
            historiqueList.remove(historique);
            notifyDataSetChanged();

            // Notifier l'utilisateur
            Toast.makeText(context, "Historique supprimé", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Annuler", (dialog, which) -> {
            // Annuler l'opération
            dialog.dismiss();
        });

        builder.show();
    }
}