package com.example.usermanagmentecotracker.jihedFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanagmentecotracker.Entity.Consommation;
import com.example.usermanagmentecotracker.R;
import com.example.usermanagmentecotracker.adaptateurs.ConsommationAdapter;

import java.util.List;

public class  ConsommationPopupFragment extends DialogFragment {

    private List<Consommation> consommations;

    public ConsommationPopupFragment(List<Consommation> consommations) {
        this.consommations = consommations;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consommation_popup, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.consommationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ConsommationAdapter adapter = new ConsommationAdapter(getContext(), consommations);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
