package com.example.usermanagmentecotracker.JihedPackage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.jihedFragments.DefisFragment;
import com.example.usermanagmentecotracker.JihedPackage.Entity.User;
import com.example.usermanagmentecotracker.JihedPackage.jihedFragments.GpsFragment;
import com.example.usermanagmentecotracker.JihedPackage.jihedFragments.HomeFragment;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.JihedPackage.jihedFragments.ThemeFragment;
import com.example.usermanagmentecotracker.R;
import com.example.usermanagmentecotracker.JihedPackage.jihedFragments.UserEditFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AppDatabase db;  // AppDatabase instance
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Toolbar
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

        // Get NavigationView and set up header
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Retrieve user data from the intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userEmail = intent.getStringExtra("userEmail");
        String defaultFragment = intent.getStringExtra("defaultFragment");

        // Set the name and email in the header
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.header_user_name);
        TextView userEmailTextView = headerView.findViewById(R.id.header_user_email);
        userNameTextView.setText(userName != null ? userName : "Unknown User");
        userEmailTextView.setText(userEmail != null ? userEmail : "No Email");

        // Fetch user data

        // Load the default fragment
        if (defaultFragment != null && defaultFragment.equals("GPSFragment")) {
            loadFragment(new GpsFragment());
        } else {
            loadFragment(new HomeFragment()); // Default fragment
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                loadFragment(new UserEditFragment());
            } else if (id == R.id.nav_settings) {
                        loadFragment(new ThemeFragment());
            } else if (id == R.id.nav_logout) {
                Intent logoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
            } else if (id == R.id.nav_defis) {
                loadFragment(new DefisFragment());
            }
         else if (id == R.id.nav_home) {
            loadFragment(new HomeFragment());
        }
            drawerLayout.closeDrawers();
            return true;
        });

        // Initialize ExecutorService for background tasks
        executorService = Executors.newSingleThreadExecutor();
    }

    // Fetch user data from the database and populate the UI
    private void fetchUserData(String userEmail) {
        // Initialize the Room database
        db = Room.databaseBuilder(this, AppDatabase.class, DatabaseName.nameOfDatabase).build();

        // Run the database query on a background thread
        executorService.execute(() -> {
            User user = db.userDao().getUserByEmail(userEmail);
            if (user != null) {
                runOnUiThread(() -> {
                    // Populate user data on the UI
                    TextView userNameTextView = findViewById(R.id.header_user_name);
                    TextView userEmailTextView = findViewById(R.id.header_user_email);
                    ImageView profileImageView = findViewById(R.id.profile_image);

                    userNameTextView.setText(user.getName());
                    userEmailTextView.setText(user.getEmail());

                    // Load the profile image if available
                    String imagePath = user.getProfileImagePath();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        profileImageView.setImageURI(Uri.parse(imagePath));
                    } else {
                        profileImageView.setImageResource(R.drawable.ic_profile); // Default image
                    }
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(HomeActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // Load a fragment into the fragment container
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the ExecutorService to avoid memory leaks
        executorService.shutdown();
    }
}
