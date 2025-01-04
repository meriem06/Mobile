package com.example.usermanagmentecotracker.mariemPackage;

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
import androidx.room.Room;


import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Directory.LuminositeDAO;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Luminosite;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.JihedPackage.adaptateurs.MyAdapter;
import com.example.usermanagmentecotracker.JihedPackage.api.LuminositeApi;
import com.example.usermanagmentecotracker.JihedPackage.api.RetrofitInstance;
import com.example.usermanagmentecotracker.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class
AffichageLuminisitee extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<Luminosite> luminositeList;
    private LuminositeDAO luminositeDAO;
    private AppDatabase db;

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
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getContext() != null) {
            luminositeDAO = db.luminositeDao();
        } else {
            Log.e("AffichageLuminisitee", "Le contexte est null, impossible d'initialiser le service");
        }

        myAdapter = new MyAdapter(getContext(), new ArrayList<>(), luminositeDAO);
        recyclerView.setAdapter(myAdapter);

        // Ajouter un listener pour détecter les clics
        myAdapter.setOnItemClickListener(luminosite -> {
            DetailFragement detailFragment = DetailFragement.newInstance(
                    String.valueOf(luminosite.getId()),
                    luminosite.getIntensite() + "%"
            );
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Charger les données une seule fois
        //   List<Luminosite>[] luminositeListHolder = new List[1]; // Pour garder la liste récupérée
        fetchLuminositeData();
       /* new Thread(() -> {
            List<Luminosite> luminositeList = luminositeDAO.getAll();
            luminositeListHolder[0] = luminositeList; // Stocker dans une variable accessible globalement
            getActivity().runOnUiThread(() -> {
                myAdapter.updateData(luminositeList);
            });
        }).start();*/


        Button button = view.findViewById(R.id.button7);
        button.setOnClickListener(v -> {
            Fragment fragmentAjouter = new Ajouter_Luminosite();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentAjouter)
                    .addToBackStack(null)
                    .commit();
        });

        Button button2 = view.findViewById(R.id.buttonstatistique);
        button2.setOnClickListener(v -> {
            Fragment fragmentStatistique = new Statistique();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentStatistique)
                    .addToBackStack(null)
                    .commit();
        });

        Button button3 = view.findViewById(R.id.button11);
        button3.setOnClickListener(v -> {
            // Utiliser la liste déjà chargée pour le tri par date
            List<Luminosite> sortedList = luminositeList.stream()
                    .sorted(Comparator.comparing(Luminosite::getDate))
                    .collect(Collectors.toList());
            getActivity().runOnUiThread(() -> {
                myAdapter.updateData(sortedList);
            });
        });

        Button button4 = view.findViewById(R.id.button12);
        button4.setOnClickListener(v -> {
            // Utiliser la liste déjà chargée pour le tri par intensité décroissante
            List<Luminosite> sortedList = luminositeList.stream()
                    .sorted((l1, l2) -> Float.compare(l2.getIntensite(), l1.getIntensite()))
                    .collect(Collectors.toList());
            getActivity().runOnUiThread(() -> {
                myAdapter.updateData(sortedList);
            });
        });

        return view;
    }
    private void fetchLuminositeData() {
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
                    luminositeList = response.body();  // Stocker la liste de luminosité
                    myAdapter.updateData(luminositeList);  // Mettre à jour l'adaptateur avec les données récupérées
                } else {
                    Log.e("AffichageLuminisitee", "Erreur de récupération des données : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Luminosite>> call, Throwable t) {
                Log.e("AffichageLuminisitee", "Erreur de connexion : " + t.getMessage());
            }
        });
    }



}