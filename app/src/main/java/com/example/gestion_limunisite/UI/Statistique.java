package com.example.gestion_limunisite.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.gestion_limunisite.DAO.LuminositeDAO;
import com.example.gestion_limunisite.Database.AppDatabase;
import com.example.gestion_limunisite.R;
import com.example.gestion_limunisite.entity.Luminosite;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Statistique#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistique extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
     private  LuminositeDAO luminositeDAO;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Statistique() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Statistique.
     */
    // TODO: Rename and change types and number of parameters
    public static Statistique newInstance(String param1, String param2) {
        Statistique fragment = new Statistique();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistique, container, false);

        // Initialiser le LineChart
        LineChart lineChart = view.findViewById(R.id.line_chart);

        // Utiliser un thread pour récupérer les données
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Assurez-vous que le service est bien initialisé
                if (getContext() != null) {
                    luminositeDAO = AppDatabase.getInstance(getContext()).luminositeDAO();
                } else {
                    Log.e("Statistique", "Le contexte est null, impossible d'initialiser le service");
                    return;
                }

                // Récupérer les données d'intensité de la luminosité
                List<Luminosite> luminositeList = luminositeDAO.getAll();

                // Convertir les données d'intensité en Entry pour le graphique
                ArrayList<Entry> entries = new ArrayList<>();
                ArrayList<String> months = new ArrayList<>(); // Pour les labels des mois
                for (int i = 0; i < luminositeList.size(); i++) {
                    Luminosite luminosite = luminositeList.get(i);
                    entries.add(new Entry(i, luminosite.getIntensite())); // Intensité en pourcentage
                    months.add("Mois " + (i + 1)); // Assigner le nom du mois (vous pouvez personnaliser ici)
                }

                // Mettre à jour l'interface utilisateur sur le thread principal
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Créer un LineDataSet
                        LineDataSet dataSet = new LineDataSet(entries, "Intensité Luminosité");
                        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
                        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));

                        // Créer le LineData
                        LineData lineData = new LineData(dataSet);

                        // Configurer le graphique
                        lineChart.setData(lineData);

                        // Personnalisation de l'axe des X (Mois)
                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Positionner les labels en bas
                        xAxis.setGranularity(1f); // Un label pour chaque mois

                        // Personnalisation de l'axe des Y (Intensité)
                        YAxis yAxis = lineChart.getAxisLeft();
                        yAxis.setAxisMinimum(0f); // Minimum de l'axe Y à 0
                        yAxis.setAxisMaximum(100f); // Maximum de l'axe Y à 100%

                        // Personnalisation supplémentaire (optionnelle)
                        Description description = new Description();
                        description.setText("Graphique des intensités");
                        lineChart.setDescription(description);

                        lineChart.animateX(1000); // Animation
                        lineChart.invalidate(); // Rafraîchir le graphique
                    }
                });
            }
        }).start();
        Button returnButton = view.findViewById(R.id.button3);
        returnButton.setOnClickListener(v-> {
            requireActivity().getSupportFragmentManager().popBackStack();

        });



        return view;
    }



}