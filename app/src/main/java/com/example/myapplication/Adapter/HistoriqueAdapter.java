package com.example.myapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Diercctory.HistoriqueDao;
import com.example.myapplication.Entity.HistoriqueTable;
import com.example.myapplication.R;
import com.example.myapplication.utils.EmailSender;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HistoriqueAdapter extends BaseAdapter {

    private Context context;
    private List<HistoriqueTable> historiqueList;
    private HistoriqueDao historiqueDao;
    private static final String TAG = "HistoriqueAdapter";

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

    // Méthode pour afficher le dialogue de modification, suppression et impression
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

                // Sauvegarder l'ancienne valeur avant la mise à jour
                float oldSoundLevel = historique.getSoundLevel();
                String oldTimestamp = historique.getTimestamp();

                // Mettre à jour les données
                historique.setSoundLevel(newSoundLevel);
                historique.setTimestamp(newTimestamp);

                // Insérer les nouvelles données
                new Thread(() -> {
                    historiqueDao.update(historique);  // Mise à jour de la base de données

                    // Envoi de l'email avec l'ancienne et la nouvelle valeur
                    String subject = "Historique Modifié";
                    String body = String.format("Ancienne valeur: %.2f dB, %s\nNouvelle valeur: %.2f dB, %s",
                            oldSoundLevel, oldTimestamp, newSoundLevel, newTimestamp);

                    try {
                        // Remplacez par l'email du destinataire
                        EmailSender.sendEmail("recipient@example.com", subject, body);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }).start();

                Toast.makeText(context, "Historique mis à jour et email envoyé", Toast.LENGTH_SHORT).show();
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

        // Bouton pour imprimer en PDF
        builder.setNeutralButton("Imprimer", (dialog, which) -> {
            // Générez le contenu à inclure dans le PDF
            String documentContent = "Historique des valeurs\n\n" +
                    "Niveau de son: " + historique.getSoundLevel() + " dB\n" +
                    "Date: " + historique.getTimestamp();

            // Appelez la méthode pour créer et enregistrer le PDF
            generatePdfInDownloads(documentContent);
        });

        builder.show();
    }

    // Méthode pour générer le PDF dans le dossier Downloads
    private void generatePdfInDownloads(String documentContent) {
        try {
            Log.d(TAG, "Démarrage de la génération du PDF");

            // Vérifier si nous avons l'autorisation d'écrire dans le dossier Download
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Lancer un intent pour permettre à l'utilisateur de choisir l'emplacement pour enregistrer le PDF
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE, "historique.pdf");

                // Demander à l'utilisateur de choisir un emplacement dans le stockage
                ((Activity) context).startActivityForResult(intent, 42);  // Code de requête arbitraire pour identifier l'action
            } else {
                // API moins de 29 : Accès direct au répertoire Downloads
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File pdfFile = new File(downloadDir, "historique.pdf");
                try (FileOutputStream fileOutputStream = new FileOutputStream(pdfFile)) {

                    com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                    com.itextpdf.text.pdf.PdfWriter.getInstance(document, fileOutputStream);

                    document.open();
                    document.add(new com.itextpdf.text.Paragraph(documentContent));
                    document.close();

                    Log.d(TAG, "PDF généré avec succès dans Downloads");
                    Toast.makeText(context, "PDF généré dans Downloads", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e(TAG, "Erreur lors de l'écriture dans le fichier", e);
                    Toast.makeText(context, "Erreur lors de la génération du PDF", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la génération du PDF", e);
            Toast.makeText(context, "Erreur lors de la génération du PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
