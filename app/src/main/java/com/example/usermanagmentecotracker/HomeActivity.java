package com.example.usermanagmentecotracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.usermanagmentecotracker.Database.AppDatabase;
import com.example.usermanagmentecotracker.Entity.Defis;
import com.example.usermanagmentecotracker.Entity.User;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and Toggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Get NavigationView
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Retrieve user data from the intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userEmail = intent.getStringExtra("userEmail");
        String defaultFragment = intent.getStringExtra("defaultFragment");

        // Set the name and email
        TextView userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.header_user_name);
        TextView userEmailTextView = navigationView.getHeaderView(0).findViewById(R.id.header_user_email);
        userNameTextView.setText(userName != null ? userName : "Unknown User");
        userEmailTextView.setText(userEmail != null ? userEmail : "No Email");

        // Load the default fragment
        if (defaultFragment != null && defaultFragment.equals("GPSFragment")) {
            loadFragment(new GpsFragment());
        } else {
            loadFragment(new HomeFragment()); // Replace this with your default fragment
        }

        // Handle navigation menu item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                loadFragment(new UserEditFragment());
            } else if (id == R.id.nav_settings) {
                Toast.makeText(HomeActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                Intent logoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
            } else if (id == R.id.nav_defis) {
                loadFragment(new DefisFragment());
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }


    private void showDefisFragment() {
        // Create and display a fragment with the list of defis
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DefisFragment())
                .addToBackStack(null)
                .commit();
    }
    void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
