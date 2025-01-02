package com.example.gestion_activityrecognition.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_activityrecognition.DAO.ActivityDAO;
import com.example.gestion_activityrecognition.Database.ActivityDatabase;
import com.example.gestion_activityrecognition.R;
import com.example.gestion_activityrecognition.entity.Activity;
import com.example.gestion_activityrecognition.entity.Activity;

import java.util.ArrayList;
import java.util.List;

public class affichage_activitee extends Fragment {

    private RecyclerView recyclerView;
    private ActiviteAdapter myAdapter;
    private ActivityDAO activiteDAO;

    public affichage_activitee() {
        // Required empty public constructor
    }

    public static affichage_activitee newInstance(String param1, String param2) {
        affichage_activitee fragment = new affichage_activitee();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle any arguments passed to the fragment
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affichage_activitee, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getContext() != null) {
            activiteDAO = ActivityDatabase.getInstance(getContext()).activityDAO();
        } else {
            Log.e("AffichageActivite", "Le contexte est null, impossible d'initialiser le DAO");
        }

        myAdapter = new ActiviteAdapter(getContext(), new ArrayList<>(), activiteDAO);
        recyclerView.setAdapter(myAdapter);



        // Charger les données dans un thread séparé
        new Thread(() -> {
            List<Activity> activiteList = activiteDAO.getAll();
            getActivity().runOnUiThread(() -> myAdapter.updateData(activiteList));
        }).start();

        Button ajouterButton = view.findViewById(R.id.buttonAdd);
        ajouterButton.setOnClickListener(v -> {
            Fragment ajouterFragment = new ajout_activitee();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, ajouterFragment)
                    .addToBackStack(null)
                    .commit();
        });

        EditText dateInput = view.findViewById(R.id.editTextDateFilter);
        dateInput.setOnClickListener(v -> {
            String date = dateInput.getText().toString().trim();
            filterDataByDate(date);
        });

        return view;
    }

    private void filterDataByDate(String date) {
        new Thread(() -> {
            List<Activity> filteredList;
            if (date.isEmpty()) {
                // Si aucune date n'est sélectionnée, afficher toutes les activités
                filteredList = activiteDAO.getAll();
            } else {
                // Filtrer les activités par date
                filteredList = activiteDAO.getActiviteeByDate(date);
            }
            getActivity().runOnUiThread(() -> myAdapter.updateData(filteredList));
        }).start();
    }
}
