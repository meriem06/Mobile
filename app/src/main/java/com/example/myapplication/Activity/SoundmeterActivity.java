package com.example.myapplication.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.myapplication.Database.AppDatabase;
import com.example.myapplication.Diercctory.HistoriqueDao;
import com.example.myapplication.Entity.HistoriqueTable;
import com.example.myapplication.R;
import com.github.anastr.speedviewlib.PointerSpeedometer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SoundmeterActivity extends AppCompatActivity {

    private PointerSpeedometer speedometer;
    private TextView maxSoundTV;
    private static final int SAMPLE_RATE = 44100;
    private boolean isListening = true;
    private float currentMaxSound = 0;
    private final Handler maxSoundHandler = new Handler();
    private static final int UPDATE_INTERVAL = 10000; // 10 secondes
    private AppDatabase db;
    private HistoriqueDao historiqueDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_meter);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database")
                .fallbackToDestructiveMigration()  // Permet de supprimer les anciennes versions si nécessaire
                .build();

        historiqueDao = db.historiqueDao();

        initUI();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            startSoundMeter();
        }

        scheduleMaxSoundUpdate();
    }

    private void initUI() {
        speedometer = findViewById(R.id.speedometer);
        maxSoundTV = findViewById(R.id.maxsoundTV);

        Button resetButton = findViewById(R.id.resetButton);
        Button viewHistoryButton = findViewById(R.id.viewHistoryButton);
        Button deleteAllButton = findViewById(R.id.deleteAllButton);  // Référence pour le bouton DeleteAll

        // Réinitialiser les valeurs
        resetButton.setOnClickListener(view -> resetValues());

        // Voir l'historique
        viewHistoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(SoundmeterActivity.this, HistoriqueActivity.class);
            startActivity(intent);
        });

        // Supprimer toutes les données
        deleteAllButton.setOnClickListener(view -> {
            // Supprimer toutes les données de la base de données
            deleteAllHistorique();
        });
    }

    // Méthode pour supprimer toutes les données de l'historique
    private void deleteAllHistorique() {
        new Thread(() -> {
            try {
                // Supprimer toutes les entrées de la table
                historiqueDao.deleteAll();
                runOnUiThread(() -> Toast.makeText(SoundmeterActivity.this, "Toutes les données ont été supprimées", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(SoundmeterActivity.this, "Erreur lors de la suppression des données", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }



    private void resetValues() {
        isListening = false;
        speedometer.speedTo(0);
        maxSoundTV.setText("Maximum(1s): 0.00");
        currentMaxSound = 0;

        new Handler().postDelayed(() -> {
            isListening = true;
            startSoundMeter();
        }, 5000);
    }

    private void startSoundMeter() {
        new Thread(() -> {
            int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                runOnUiThread(() -> Toast.makeText(SoundmeterActivity.this, "Erreur de configuration du buffer audio", Toast.LENGTH_SHORT).show());
                return;
            }

            AudioRecord audioRecord = null;
            try {
                audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, bufferSize);

                short[] buffer = new short[bufferSize];
                audioRecord.startRecording();

                while (isListening) {
                    int readSize = audioRecord.read(buffer, 0, bufferSize);
                    if (readSize > 0) {
                        double rms = calculateRMS(buffer, readSize);
                        float decibel = (float) (20 * Math.log10(rms));

                        if (decibel > currentMaxSound) {
                            currentMaxSound = decibel;
                        }

                        runOnUiThread(() -> updateUI(decibel));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(SoundmeterActivity.this, "Erreur d'enregistrement audio", Toast.LENGTH_SHORT).show());
            } finally {
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                }
            }
        }).start();
    }

    private double calculateRMS(short[] buffer, int readSize) {
        long sum = 0;
        for (int i = 0; i < readSize; i++) {
            sum += buffer[i] * buffer[i];
        }
        return Math.sqrt(sum / (double) readSize);
    }

    private void updateUI(float decibel) {
        if (!isFinishing() && !isDestroyed()) {
            speedometer.speedTo(decibel);
            maxSoundTV.setText(String.format(" %.2f dB", decibel));
        }
    }

    private void scheduleMaxSoundUpdate() {
        maxSoundHandler.postDelayed(() -> {
            if (isListening) {
                saveMaxSoundToDatabase();
                currentMaxSound = 0;
                maxSoundTV.setText(String.format(" %.2f dB", currentMaxSound));
                scheduleMaxSoundUpdate();
            }
        }, UPDATE_INTERVAL);
    }

    private void saveMaxSoundToDatabase() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        HistoriqueTable entry = new HistoriqueTable(currentMaxSound, timestamp);

        new Thread(() -> historiqueDao.insert(entry)).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSoundMeter();
            } else {
                Toast.makeText(this, "Permission audio nécessaire", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isListening = false;
        maxSoundHandler.removeCallbacksAndMessages(null);
    }
}
