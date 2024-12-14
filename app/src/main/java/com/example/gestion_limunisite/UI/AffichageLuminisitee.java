package com.example.gestion_limunisite.UI;

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

import com.example.gestion_limunisite.DAO.LuminositeDAO;
import com.example.gestion_limunisite.Database.AppDatabase;
import com.example.gestion_limunisite.R;
import com.example.gestion_limunisite.entity.Luminosite;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class
AffichageLuminisitee extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<Luminosite> luminositeList;
    private  LuminositeDAO luminositeDAO;
    public AffichageLuminisitee() {
        // Required empty public constructor
    }

    public static AffichageLuminisitee newInstance(String param1, String param2) {
        AffichageLuminisitee fragment = new AffichageLuminisitee();
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
        View view = inflater.inflate(R.layout.fragment_affichage_luminisitee, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getContext() != null) {
            luminositeDAO = AppDatabase.getInstance(getContext()).luminositeDAO();
        } else {
            Log.e("AffichageLuminisitee", "Le contexte est null, impossible d'initialiser le service");
        }

        myAdapter = new MyAdapter(getContext(), new ArrayList<>(), luminositeDAO);
        recyclerView.setAdapter(myAdapter);

        // Ajouter un listener pour détecter les clics
        myAdapter.setOnItemClickListener(luminosite -> {
            DetailFragement detailFragment = DetailFragement.newInstance(
                    String.valueOf(luminosite.getId()), // Passez les données nécessaires
                    luminosite.getIntensite() + "%"
            );
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        new Thread(() -> {
            List<Luminosite> luminositeList =luminositeDAO.getAll();
            getActivity().runOnUiThread(() -> {
                myAdapter.updateData(luminositeList);
            });
        }).start();
        Button button = view.findViewById(R.id.button7);
        button.setOnClickListener(v -> {
            Fragment fragmentAjouter = new Ajouter_Luminosite();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragmentAjouter)
                    .addToBackStack(null) // Ajouter à la pile arrière pour permettre de revenir
                    .commit();

        });
        Button button2 = view.findViewById(R.id.buttonstatistique);
        button2.setOnClickListener(v -> {
            Fragment fragmentStatistique = new Statistique();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragmentStatistique)
                    .addToBackStack(null) // Ajouter à la pile arrière pour permettre de revenir
                    .commit();
        });
        EditText dateInput=view.findViewById(R.id.editTextDate3);
        dateInput.setOnClickListener(v -> {
            String date = dateInput.getText().toString().trim();
            filterDataByDate(date);
        });

        return view;
    }
    private void filterDataByDate(String date) {
        new Thread(() -> {
            List<Luminosite> filteredList;
            if (date.isEmpty()) {
                // Si aucune date n'est sélectionnée, afficher toutes les données
                filteredList = luminositeDAO.getAll();
            } else {
                // Filtrer les données par date
                filteredList = luminositeDAO.getLuminositeByDate(date);
            }
            getActivity().runOnUiThread(() -> {
                myAdapter.updateData(filteredList);
            });
        }).start();
    }

}
