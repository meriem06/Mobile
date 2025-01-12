package com.example.usermanagmentecotracker.mariemPackage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Directory.LuminositeDAO;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Luminosite;
import com.example.usermanagmentecotracker.JihedPackage.api.LuminositeApi;
import com.example.usermanagmentecotracker.JihedPackage.api.RetrofitInstance;
import com.example.usermanagmentecotracker.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragement extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LuminositeDAO luminositeDAO;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragement newInstance(String param1, String param2) {
        DetailFragement fragment = new DetailFragement();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_fragement, container, false);

        if (getArguments() != null) {
            String id = getArguments().getString(ARG_PARAM1);

            if (id != null && !id.isEmpty()) {
                try {
                    long luminositeId = Long.parseLong(id);
                    luminositeDAO = AppDatabase.getInstance(getContext()).luminositeDao();

                    // Utilisation d'un thread d'arrière-plan pour éviter le blocage de l'UI
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        List<Luminosite> allLuminosites = luminositeDAO.getAll();
                        Log.d("DetailFragment", "Toutes les données : " + allLuminosites.toString());
                    });
                    executor.execute(() -> {
                        Log.d("DetailFragment", "Requête avec ID : " + luminositeId);
                        Luminosite luminosite = luminositeDAO.getLuminositeById(luminositeId);
                        Log.d("DetailFragment", "Donnée récupérée : " + (luminosite != null ? luminosite.toString() : "null"));



                        // Mise à jour de l'interface sur le thread principal
                        requireActivity().runOnUiThread(() -> {
                            if (luminosite != null) {
                                TextView textIntensite = view.findViewById(R.id.textView8);
                                TextView textDate = view.findViewById(R.id.textView10);
                                TextView textNormal = view.findViewById(R.id.textView11);

                                textIntensite.setText(luminosite.getIntensite() + "%");
                                textDate.setText(luminosite.getDate().toString());
                                textNormal.setText(luminosite.isNormal() ? "Normal" : "Anormal");
                            } else {
                                Toast.makeText(requireContext(), "Aucune donnée trouvée", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "ID invalide", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "ID manquant", Toast.LENGTH_SHORT).show();
            }
        }

        // Bouton retour
        Button button = view.findViewById(R.id.button6);
        button.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

}