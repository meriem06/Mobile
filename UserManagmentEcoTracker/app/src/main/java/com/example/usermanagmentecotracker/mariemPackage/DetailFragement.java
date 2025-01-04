package com.example.usermanagmentecotracker.mariemPackage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.usermanagmentecotracker.JihedPackage.Directory.LuminositeDAO;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Luminosite;
import com.example.usermanagmentecotracker.JihedPackage.api.LuminositeApi;
import com.example.usermanagmentecotracker.JihedPackage.api.RetrofitInstance;
import com.example.usermanagmentecotracker.R;

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
         /*   luminositeDAO = AppDatabase.getInstance(getContext()).luminositeDAO();

            // Run the database query on a background thread
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Luminosite luminosite = luminositeDAO.getLuminositeById(Long.parseLong(id));

                requireActivity().runOnUiThread(() -> {
                    // Update UI on the main thread
                    TextView textIntensite = view.findViewById(R.id.textView8);
                    TextView textDate = view.findViewById(R.id.textView10);
                    TextView textNormal = view.findViewById(R.id.textView11);

                    textIntensite.setText(luminosite.getIntensite() + "%");
                    textDate.setText(luminosite.getDate().toString());
                    textNormal.setText(luminosite.isNormal() + "");
                });
            });*/
            // Vérifier si l'ID est valide
            if (id != null && !id.isEmpty()) {
                long luminositeId = Long.parseLong(id);

                // Créer une instance de Retrofit
                Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
                LuminositeApi luminositeApi = retrofit.create(LuminositeApi.class);

                // Appeler l'API pour récupérer la luminosité par ID
                Call<Luminosite> call = luminositeApi.getLuminositeById(luminositeId);

                call.enqueue(new Callback<Luminosite>() {
                    @Override
                    public void onResponse(Call<Luminosite> call, Response<Luminosite> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Luminosite luminosite = response.body();

                            // Mettre à jour l'interface utilisateur avec les données reçues
                            TextView textIntensite = view.findViewById(R.id.textView8);
                            TextView textDate = view.findViewById(R.id.textView10);
                            TextView textNormal = view.findViewById(R.id.textView11);

                            textIntensite.setText(luminosite.getIntensite() + "%");
                            textDate.setText(luminosite.getDate().toString());
                            textNormal.setText(luminosite.isNormal() ? "Normal" : "Anormal");
                        } else {
                            Toast.makeText(requireContext(), "Échec de la récupération des données", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Luminosite> call, Throwable t) {
                        Toast.makeText(requireContext(), "Erreur de connexion : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireContext(), "ID non valide", Toast.LENGTH_SHORT).show();
            }
        }

        Button button = view.findViewById(R.id.button6);
        button.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

}