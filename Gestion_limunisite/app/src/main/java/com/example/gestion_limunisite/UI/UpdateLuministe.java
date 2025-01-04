package com.example.gestion_limunisite.UI;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestion_limunisite.Api.LuminositeApi;
import com.example.gestion_limunisite.Api.RetrofitInstance;
import com.example.gestion_limunisite.DAO.LuminositeDAO;
import com.example.gestion_limunisite.Database.AppDatabase;
import com.example.gestion_limunisite.R;
import com.example.gestion_limunisite.entity.Luminosite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateLuministe extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private float currentLightLevel;
    private TextView textViewIntensite;

      LuminositeDAO luminositeDAO;
    int id;
    private static final float MAX_LUX = 1000;

    public static UpdateLuministe newInstance(int id, float intensite, boolean isNormal, Date date) {
        UpdateLuministe fragment = new UpdateLuministe();
        Bundle args = new Bundle();
        args.putInt("ARG_ID", id);
        args.putFloat("ARG_INTENSITE", intensite);
        args.putBoolean("ARG_IS_NORMAL", isNormal);
        if (date != null) {
            args.putString("ARG_DATE", new SimpleDateFormat("dd/MM/yyyy").format(date));
        } else {
            args.putString("ARG_DATE", ""); // Valeur par défaut si la date est null
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) requireActivity().getSystemService(requireContext().SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        luminositeDAO = AppDatabase.getInstance(getContext()).luminositeDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_luministe, container, false);

        textViewIntensite = view.findViewById(R.id.textView6);
        EditText editDate = view.findViewById(R.id.editTextDate2);
        RadioButton radioIsNormal = view.findViewById(R.id.radioButton2);
        RadioButton radioIsNotNormal = view.findViewById(R.id.radioButton3);
        Button buttonIntensite = view.findViewById(R.id.button8);
        Button buttonUpdate = view.findViewById(R.id.button9);
        Button Annuler = view.findViewById(R.id.button10);

        // Variable pour suivre l'état du clic
        final boolean[] isButtonIntensiteClicked = {false};
        final float[] intensite = {0}; // Pour stocker l'intensité initiale
        final float[] newIntensity = {0}; // Pour stocker la nouvelle intensité si modifiée

        if (getArguments() != null) {
            id = getArguments().getInt("ARG_ID");
            intensite[0] = getArguments().getFloat("ARG_INTENSITE"); // Récupération de l'intensité initiale
            boolean isNormal = getArguments().getBoolean("ARG_IS_NORMAL");
            String date = getArguments().getString("ARG_DATE", "");

            // Affichage des valeurs initiales dans les composants
            textViewIntensite.setText(String.valueOf(intensite[0]));
            editDate.setText(date.isEmpty() ? "" : date);
            radioIsNormal.setChecked(isNormal);
            radioIsNotNormal.setChecked(!isNormal);
        }

        buttonIntensite.setOnClickListener(v -> {
            try {
                newIntensity[0] = Float.parseFloat(textViewIntensite.getText().toString());
                isButtonIntensiteClicked[0] = true; // Marquer que le bouton a été cliqué
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Erreur : Entrez une intensité valide", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUpdate.setOnClickListener(v -> {
            boolean isNormal = radioIsNormal.isChecked();

            String dateString = editDate.getText().toString().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false); // Parsing strict
            Date date;

            try {
                if (!dateString.isEmpty()) {
                    date = sdf.parse(dateString);

                    // Validation de la date pour qu'elle soit dans le même mois et la même année
                    Calendar enteredDate = Calendar.getInstance();
                    enteredDate.setTime(date);

                    Calendar currentDate = Calendar.getInstance(); // Date système actuelle

                    if (enteredDate.get(Calendar.YEAR) != currentDate.get(Calendar.YEAR) ||
                            enteredDate.get(Calendar.MONTH) != currentDate.get(Calendar.MONTH)) {
                        throw new IllegalArgumentException("La date doit être dans le même mois et la même année que la date système.");
                    }
                } else {
                    // Conserver la date existante si aucune date n'est fournie
                    String existingDate = getArguments().getString("ARG_DATE", "");
                    if (!existingDate.isEmpty()) {
                        date = sdf.parse(existingDate);
                    } else {
                        date = new Date(); // Par défaut, la date actuelle
                    }
                }

                // Déterminer l'intensité à utiliser
                float intensityToUse = isButtonIntensiteClicked[0] ? newIntensity[0] : intensite[0];

                Luminosite updatedLuminosite = new Luminosite(id, intensityToUse, isNormal, date,1);

             /*   new Thread(() -> {
                    luminositeDAO.updateLuminosite(updatedLuminosite);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Mise à jour réussie !", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    });
                }).start();*/
                Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
                LuminositeApi luminositeApi = retrofit.create(LuminositeApi.class);

                Call<Void> call = luminositeApi.updateLuminosite(updatedLuminosite);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Mise à jour réussie !", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Log.e("Update Luminosite", "Échec de mise à jour : " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Update Luminosite", "Erreur de connexion : " + t.getMessage());
                    }
                });

            } catch (ParseException e) {
                Toast.makeText(requireContext(), "Erreur dans le format de la date (JJ/MM/AAAA attendu)", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Erreur dans l'intensité lumineuse", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Annuler.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            currentLightLevel = event.values[0];
            float lightPercentage = Math.min((currentLightLevel / MAX_LUX) * 100, 100);
            textViewIntensite.setText(String.format("%.2f", lightPercentage));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
