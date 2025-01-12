package com.example.usermanagmentecotracker.mariemPackage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Directory.LuminositeDAO;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Luminosite;
import com.example.usermanagmentecotracker.JihedPackage.api.LuminositeApi;
import com.example.usermanagmentecotracker.JihedPackage.api.RetrofitInstance;
import com.example.usermanagmentecotracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Statistique extends Fragment {

    private List<Luminosite> luminositeList = new ArrayList<>();

    public Statistique() {
        // Required empty public constructor
    }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_statistique, container, false);

         // Initialiser les graphiques
         LineChart lineChart = view.findViewById(R.id.line_chart);
         PieChart pieChart = view.findViewById(R.id.gauge_chart);
         Button returnButton = view.findViewById(R.id.button3);
         TextView descriptionTextView = view.findViewById(R.id.textViewDescription);

         // Récupérer les données en arrière-plan
         new Thread(() -> {
             final LuminositeDAO luminositeDAO;
             if (getContext() != null) {
                 luminositeDAO = AppDatabase.getInstance(getContext()).luminositeDao();
             } else {
                 Log.e("Statistique", "Le contexte est null, impossible d'initialiser le service");
                 return;
             }

             // Récupérer les données de luminosité
             luminositeList = luminositeDAO.getAll();

 // Préparer les données pour le graphique
             ArrayList<Entry> entries = new ArrayList<>();
             ArrayList<String> days = new ArrayList<>();
             SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

             for (int i = 0; i < luminositeList.size(); i++) {
                 Luminosite luminosite = luminositeList.get(i);

                 // Récupérer le jour à partir de la propriété `date`
                 Date date = luminosite.getDate(); // Assurez-vous que `date` est de type `Date`
                 String day = dayFormat.format(date);

                 // Ajouter les données au graphique
                 entries.add(new Entry(i, luminosite.getIntensite()));
                 days.add("Jour " + day);
             }

             // Calculer la moyenne et l'écart-type
             List<Float> luminositeIntensite = new ArrayList<>();
             for (Luminosite luminosite : luminositeList) {
                 luminositeIntensite.add(luminosite.getIntensite());
             }
             float average = calculateAverage(luminositeIntensite);
             float stdDev = calculateStandardDeviation(luminositeIntensite, average);
             List<Date> dates = new ArrayList<>();
             Calendar calendar = Calendar.getInstance();

             // Générer une liste de dates aléatoires sur les 15 derniers jours
             for (int i = 0; i < luminositeIntensite.size(); i++) {
                 dates.add(calendar.getTime());
                 calendar.add(Calendar.DAY_OF_MONTH, -1);
             }

             // Calcul de la régularité de la consommation lumineuse
             float regulariteLumiere = calculateConsistency(luminositeIntensite, dates);

             // Définition des nouveaux seuils
             if (regulariteLumiere < 5) {
                 descriptionTextView.setText("Consommation lumineuse stable et optimisée.");
             } else if (regulariteLumiere < 10) {
                 descriptionTextView.setText("Consommation lumineuse légèrement irrégulière, à surveiller.");
             } else {
                 descriptionTextView.setText("Consommation lumineuse instable. Optimisez l'éclairage.");
             }
             // Analyser les anomalies

              int count = 0;
             for (float intensite : luminositeIntensite) {
                 if (isAnomaly(intensite, average, stdDev)) {
                     count++;
                 }
             }
             final float anomaliesCount = count;
             // Mettre à jour l'interface utilisateur
             requireActivity().runOnUiThread(() -> {

                 // Configurer le graphique linéaire
                 LineDataSet dataSet = new LineDataSet(entries, "Intensité Luminosité");
                 dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
                 dataSet.setValueTextColor(getResources().getColor(android.R.color.black));

                 LineData lineData = new LineData(dataSet);

                 XAxis xAxis = lineChart.getXAxis();
                 xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
                 xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                 xAxis.setGranularity(1f);

                 Description description = new Description();
                 description.setText("Graphique des intensités");
                 lineChart.setDescription(description);

                 lineChart.setData(lineData);
                 lineChart.animateX(1000);
                 lineChart.invalidate();

                 // Configurer le graphique circulaire (pour l'écart-type et les anomalies)
                 float normalPercentage = 100 - (anomaliesCount / luminositeIntensite.size()) * 100;
                 float anomalyPercentage = ( anomaliesCount / luminositeIntensite.size()) * 100;

                 ArrayList<PieEntry> pieEntries = new ArrayList<>();
                 pieEntries.add(new PieEntry(anomalyPercentage, "Anomalies"));
                 pieEntries.add(new PieEntry(normalPercentage, "Normales"));

                 pieChart.setData(new com.github.mikephil.charting.data.PieData(new com.github.mikephil.charting.data.PieDataSet(pieEntries, "Distribution Anomalies")));

                 pieChart.getDescription().setEnabled(false);
                 pieChart.setUsePercentValues(true);
                 pieChart.animateY(1000);
                 pieChart.invalidate();
             });
         }).start();

         // Bouton retour pour revenir au fragment précédent
         returnButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

         return view;
     }
  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistique, container, false);

        // Initialiser les graphiques
        LineChart lineChart = view.findViewById(R.id.line_chart);
        PieChart pieChart = view.findViewById(R.id.gauge_chart);
        Button returnButton = view.findViewById(R.id.button3);
        TextView descriptionTextView = view.findViewById(R.id.textViewDescription);

        // Récupérer les données depuis l'API
        fetchLuminositeData(data -> {
            // Préparer les données pour les graphiques
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> days = new ArrayList<>();
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

            for (int i = 0; i < data.size(); i++) {
                Luminosite luminosite = data.get(i);

                // Récupérer le jour
                Date date = luminosite.getDate();
                String day = dayFormat.format(date);

                // Ajouter les données au graphique
                entries.add(new Entry(i, luminosite.getIntensite()));
                days.add("Jour " + day);
            }

            // Calculer la moyenne et l'écart-type
            List<Float> luminositeIntensite = new ArrayList<>();
            for (Luminosite luminosite : data) {
                luminositeIntensite.add(luminosite.getIntensite());
            }
            float average = calculateAverage(luminositeIntensite);
            float stdDev = calculateStandardDeviation(luminositeIntensite, average);
            List<Float> temperatureData = new ArrayList<>();
            for (int i = 0; i < luminositeIntensite.size(); i++) {
                temperatureData.add((float) (Math.random() * 15)); // Température aléatoire entre 0 et 15
            }
            float temperatureLuminsite = calculateCorrelation(luminositeIntensite,temperatureData);
            if(temperatureLuminsite>0.8)
            { descriptionTextView.setText("Optimisez l'utilisation des lumières pour réduire la consommation d'énergie.");}
            else if(temperatureLuminsite<-0.8)
            {
                descriptionTextView.setText("Surveillez le chauffage/refroidissement pour éviter des pertes d'énergie.");
            }
            else
            {
                descriptionTextView.setText("Conditions normales. Continuez à surveiller.");
            }


            // Identifier les anomalies
            long anomaliesCount = luminositeIntensite.stream()
                    .filter(intensite -> isAnomaly(intensite, average, stdDev))
                    .count();

            // Mettre à jour l'interface utilisateur
            requireActivity().runOnUiThread(() -> {
                // Configurer le graphique linéaire
                LineDataSet dataSet = new LineDataSet(entries, "Intensité Luminosité");
                dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
                dataSet.setValueTextColor(getResources().getColor(android.R.color.black));

                LineData lineData = new LineData(dataSet);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);

                Description description = new Description();
                description.setText("Graphique des intensités");
                lineChart.setDescription(description);

                lineChart.setData(lineData);
                lineChart.animateX(1000);
                lineChart.invalidate();

                // Configurer le graphique circulaire
                float normalPercentage = 100 - ((float) anomaliesCount / data.size()) * 100;
                float anomalyPercentage = ((float) anomaliesCount / data.size()) * 100;

                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                pieEntries.add(new PieEntry(anomalyPercentage, "Anomalies"));
                pieEntries.add(new PieEntry(normalPercentage, "Normales"));

                PieDataSet pieDataSet = new PieDataSet(pieEntries, "Distribution Anomalies");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                pieChart.setData(new PieData(pieDataSet));

                pieChart.getDescription().setEnabled(false);
                pieChart.setUsePercentValues(true);
                pieChart.animateY(1000);
                pieChart.invalidate();
            });
        });

        // Bouton retour pour revenir au fragment précédent
        returnButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }
*/

    // Calcul de la moyenne
    public float calculateAverage(List<Float> values) {
        float sum = 0;
        for (float value : values) {
            sum += value;
        }
        return values.size() > 0 ? sum / values.size() : 0;
    }

    // Calcul de l'écart-type
    public float calculateStandardDeviation(List<Float> values, float average) {
        float sum = 0;
        for (float value : values) {
            sum += Math.pow(value - average, 2);
        }
        return (float) Math.sqrt(sum / values.size());
    }

    // Vérification si une valeur est une anomalie
    public boolean isAnomaly(float currentValue, float average, float stdDev) {
        return Math.abs(currentValue - average) > 2 * stdDev;
    }
    public float calculateConsistency(List<Float> lightData, List<Date> dates) {
        if (lightData.size() < 2 || dates.size() < 2) {
            return 0; // Pas assez de données pour évaluer la régularité
        }

        float totalDifference = 0;
        int count = 0;

        // Calculer les variations d'intensité par rapport au jour précédent
        for (int i = 1; i < lightData.size(); i++) {
            long diffDays = (dates.get(i).getTime() - dates.get(i - 1).getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays == 1) { // Comparer uniquement les jours consécutifs
                float intensityDifference = Math.abs(lightData.get(i) - lightData.get(i - 1));
                totalDifference += intensityDifference;
                count++;
            }
        }

        // Plus la moyenne des différences est faible, plus la consommation est stable
        return count > 0 ? totalDifference / count : 0;
    }


    private void fetchLuminositeData(DataCallback<List<Luminosite>> callback) {
        // Créer une instance de Retrofit
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        LuminositeApi luminositeApi = retrofit.create(LuminositeApi.class);

        // Appeler l'API pour récupérer toutes les luminosités
        Call<List<Luminosite>> call = luminositeApi.getAll();

        // Exécuter la requête de manière asynchrone
        call.enqueue(new Callback<List<Luminosite>>() {
            @Override
            public void onResponse(Call<List<Luminosite>> call, Response<List<Luminosite>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onData(response.body());
                } else {
                    Log.e("Statistique", "Erreur de récupération des données : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Luminosite>> call, Throwable t) {
                Log.e("Statistique", "Erreur de connexion : " + t.getMessage());
            }
        });
    }

    private interface DataCallback<T> {
        void onData(T data);
    }


}