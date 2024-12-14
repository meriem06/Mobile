package com.example.gestion_limunisite;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.gestion_limunisite.UI.AffichageLuminisitee;
import com.example.gestion_limunisite.UI.Ajouter_Luminosite;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Fragment fragment = new AffichageLuminisitee();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();


}}
