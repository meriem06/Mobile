package com.example.usermanagmentecotracker.Dorrapackage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usermanagmentecotracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Statistic extends Fragment {

    private LineChart temperatureChart;
    private TextView averageTemperatureTextView;
    private TextView minTemperatureTextView;
    private TextView maxTemperatureTextView;
    private Button refreshButton;

    private List<TemperatureEntry> temperatureData;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        // Initialisation des vues
        temperatureChart = view.findViewById(R.id.chart_container);
        averageTemperatureTextView = view.findViewById(R.id.average_temperature);
        minTemperatureTextView = view.findViewById(R.id.min_temperature);
        maxTemperatureTextView = view.findViewById(R.id.max_temperature);
        refreshButton = view.findViewById(R.id.refresh_statistics_button);
        Button retourButton = view.findViewById(R.id.Retourbuttonn);
        retourButton.setOnClickListener(v -> {
            // Use the FragmentManager to pop the current fragment
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });


        // Récupération des données partagées
        temperatureData = TemperatureData.getTemperatureList();

        // Configure le bouton de rafraîchissement
        refreshButton.setOnClickListener(v -> updateStatistics());

        // Mise à jour initiale des statistiques
        updateStatistics();


        return view;
    }

    private void updateStatistics() {
        if (temperatureData.isEmpty()) {
            Toast.makeText(getContext(), "Aucune donnée disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calcul des statistiques
        double total = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < temperatureData.size(); i++) {
            TemperatureEntry entry = temperatureData.get(i);
            double temp = Double.parseDouble(entry.getTemperature());

            total += temp;
            if (temp < min) min = temp;
            if (temp > max) max = temp;

            // Ajouter les entrées pour le graphique
            entries.add(new Entry(i, (float) temp));
        }

        double average = total / temperatureData.size();

        // Mettre à jour les vues TextView
        averageTemperatureTextView.setText(String.format("%.1f°C", average));
        minTemperatureTextView.setText(String.format("%.1f°C", min));
        maxTemperatureTextView.setText(String.format("%.1f°C", max));

        // Mettre à jour le graphique
        updateChart(entries);
    }

    private void updateChart(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "Températures");
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setColor(getResources().getColor(R.color.purple));
        dataSet.setCircleColor(getResources().getColor(R.color.purple));

        LineData lineData = new LineData(dataSet);
        temperatureChart.setData(lineData);

        // Configurer les axes
        XAxis xAxis = temperatureChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        YAxis leftAxis = temperatureChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        temperatureChart.getAxisRight().setEnabled(false);

        // Rafraîchir le graphique
        temperatureChart.invalidate();
    }
}
