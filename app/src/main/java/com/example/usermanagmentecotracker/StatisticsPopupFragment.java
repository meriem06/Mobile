package com.example.usermanagmentecotracker;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.example.usermanagmentecotracker.Entity.Consommation;
import java.util.ArrayList;
import java.util.List;

public class StatisticsPopupFragment extends DialogFragment {

    private static final String ARG_CONSOMMATIONS = "consommations";
    private BarChart barChart;

    public static StatisticsPopupFragment newInstance(List<Consommation> consommations) {
        StatisticsPopupFragment fragment = new StatisticsPopupFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONSOMMATIONS, new ArrayList<>(consommations));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_popup, container, false);
        barChart = view.findViewById(R.id.barChart);

        // Get the consommations data passed to the fragment
        List<Consommation> consommations = (List<Consommation>) getArguments().getSerializable(ARG_CONSOMMATIONS);
        if (consommations != null && !consommations.isEmpty()) {
            displayChart(consommations);
        } else {
            Toast.makeText(getContext(), "No data available for statistics.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private void displayChart(List<Consommation> consommations) {
        List<BarEntry> entries = new ArrayList<>();
        final List<String> placeNames = new ArrayList<>();

        for (int i = 0; i < consommations.size(); i++) {
            Consommation consommation = consommations.get(i);
            placeNames.add(consommation.getPlace()); // Assuming you have a place name in your Consommation entity
            float distance = Float.parseFloat(consommation.getDistance()); // Assuming distance is a string in your entity
            entries.add(new BarEntry(i, distance));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Distances");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);

        // Customize the chart appearance
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // Set X-axis labels
        XAxis xAxis = barChart.getXAxis();
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

        // Customize Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        barChart.getAxisRight().setEnabled(false);

        barChart.setFitBars(true);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
