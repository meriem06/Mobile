package com.example.usermanagmentecotracker.JihedPackage.jihedFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.usermanagmentecotracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Consommation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticsPopupFragment extends DialogFragment {

    private static final String ARG_CONSOMMATIONS = "consommations";
    private BarChart barChart;

    // Factory method to create a new instance of the fragment with data
    public static StatisticsPopupFragment newInstance(List<Consommation> consommations) {
        StatisticsPopupFragment fragment = new StatisticsPopupFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONSOMMATIONS, (Serializable) consommations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_popup, container, false);

        // Initialize UI components
        barChart = view.findViewById(R.id.barChart);
        PieChart pieChart = view.findViewById(R.id.pieChart);
        Button buttonDone = view.findViewById(R.id.buttonDone);

        // Retrieve consommations data
        List<Consommation> consommations = getConsommationsFromArgs();
        if (consommations != null && !consommations.isEmpty()) {
            displayBarChart(consommations);
            displayPieChart(pieChart, consommations);
        } else {
            Toast.makeText(getContext(), "No data available for statistics.", Toast.LENGTH_SHORT).show();
        }

        // Handle the Done button click
        buttonDone.setOnClickListener(v -> dismiss());

        return view;
    }

    // Retrieve consommations data from arguments
    @SuppressWarnings("unchecked")
    private List<Consommation> getConsommationsFromArgs() {
        if (getArguments() != null) {
            return (List<Consommation>) getArguments().getSerializable(ARG_CONSOMMATIONS);
        }
        return Collections.emptyList();
    }

    // Configure and display the PieChart
    private void displayPieChart(PieChart pieChart, List<Consommation> consommations) {
        List<PieEntry> entries = new ArrayList<>();

        // Sort and limit to top 4 consommations
        List<Consommation> topConsommations = new ArrayList<>(consommations);
        topConsommations.sort((c1, c2) -> Float.compare(
                Float.parseFloat(c2.getDistance()), Float.parseFloat(c1.getDistance())));
        topConsommations = topConsommations.subList(0, Math.min(4, topConsommations.size()));

        // Populate PieChart entries
        for (Consommation consommation : topConsommations) {
            float distance = Float.parseFloat(consommation.getDistance());
            entries.add(new PieEntry(distance, consommation.getPlace()));
        }

        // Create PieDataSet and configure its properties
        PieDataSet dataSet = new PieDataSet(entries, "Top 4 Consumptions");
        dataSet.setColors(new int[]{
                android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light
        }, getContext());
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        dataSet.setValueTextSize(12f);

        // Bind data to the PieChart
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Top 4 Consumptions");
        pieChart.setCenterTextSize(14f);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    // Configure and display the BarChart
    private void displayBarChart(List<Consommation> consommations) {
        List<BarEntry> entries = new ArrayList<>();
        final List<String> placeNames = new ArrayList<>();

        // Populate BarChart entries and labels
        for (int i = 0; i < consommations.size(); i++) {
            Consommation consommation = consommations.get(i);
            placeNames.add(consommation.getPlace());
            float distance = Float.parseFloat(consommation.getDistance());
            entries.add(new BarEntry(i, distance));
        }

        // Create BarDataSet and configure its properties
        BarDataSet dataSet = new BarDataSet(entries, "Distances");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        dataSet.setValueTextSize(12f);

        // Bind data to the BarChart
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);
        barChart.setData(barData);

        // Configure X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if ((int) value < placeNames.size()) {
                    return placeNames.get((int) value);
                }
                return "";
            }
        });
        xAxis.setDrawGridLines(false);

        // Configure Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        barChart.getAxisRight().setEnabled(false);

        // Finalize BarChart configuration
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
