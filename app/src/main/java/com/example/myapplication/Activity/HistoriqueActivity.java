package com.example.myapplication.Activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.Adapter.HistoriqueAdapter;
import com.example.myapplication.Database.AppDatabase;
import com.example.myapplication.Diercctory.HistoriqueDao;
import com.example.myapplication.Entity.HistoriqueTable;
import com.example.myapplication.R;
import java.util.List;

public class HistoriqueActivity extends AppCompatActivity {

    private AppDatabase db;
    private HistoriqueDao historiqueDao;
    private ListView listView;
    private HistoriqueAdapter adapter;
    private Thread dataLoadThread; // Référence pour le thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        // Initialiser Room Database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database")
                .fallbackToDestructiveMigration()
                .build();
        historiqueDao = db.historiqueDao();

        // Référence au ListView
        listView = findViewById(R.id.historiqueListView);

        // Charger les données dans un thread d'arrière-plan
        loadHistoriqueData();
    }

    // Méthode pour charger les données de l'historique dans un thread
    private void loadHistoriqueData() {
        dataLoadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Récupérer toutes les entrées de l'historique depuis la base de données
                    List<HistoriqueTable> historiqueList = historiqueDao.getAllHistorique();

                    // Mettre à jour l'interface utilisateur sur le thread principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Vérifier si la liste est vide
                            if (historiqueList.isEmpty()) {
                                Toast.makeText(HistoriqueActivity.this, "Aucune donnée disponible", Toast.LENGTH_SHORT).show();
                            }

                            // Initialiser l'adaptateur pour afficher les données
                            adapter = new HistoriqueAdapter(HistoriqueActivity.this, historiqueList, historiqueDao);
                            listView.setAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HistoriqueActivity.this, "Erreur lors du chargement de l'historique", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        dataLoadThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Si le thread est en cours d'exécution, l'annuler
        if (dataLoadThread != null && dataLoadThread.isAlive()) {
            dataLoadThread.interrupt();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libérer toutes les ressources et fermer la base de données si nécessaire
        if (db != null) {
            db.close();
        }
    }
}
