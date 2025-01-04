package com.example.gestion_limunisite;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.gestion_limunisite.DAO.UserDAO;
import com.example.gestion_limunisite.Database.AppDatabase;
import com.example.gestion_limunisite.UI.AffichageLuminisitee;
import com.example.gestion_limunisite.UI.Ajouter_Luminosite;
import com.example.gestion_limunisite.entity.User;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserDAO UserDAO = AppDatabase.getInstance(this).userDAO();
        // Vérifiez si l'utilisateur par défaut existe
        new Thread(() -> {
            User defaultUser = new User();
            defaultUser.setId(0); // Assurez-vous que l'ID est cohérent
            defaultUser.setName("Default User");
            defaultUser.setEmail("default@example.com");
            defaultUser.setPassword("defaultPassword");
            defaultUser.setBirthdate(String.valueOf(new Date()));
            defaultUser.setVerified(false);

            // Insérer l'utilisateur uniquement s'il n'existe pas
            UserDAO.insertUser(defaultUser);
        }).start();
            Fragment fragment = new AffichageLuminisitee();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();


}}
