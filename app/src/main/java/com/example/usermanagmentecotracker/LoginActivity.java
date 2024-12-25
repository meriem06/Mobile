// LoginActivity.java
package com.example.usermanagmentecotracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.usermanagmentecotracker.Database.AppDatabase;
import com.example.usermanagmentecotracker.Entity.User;
import com.example.usermanagmentecotracker.NameDatabaseJihed.DatabaseName;

public class LoginActivity extends AppCompatActivity {
    public static int idUserToConsommations;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView buttonGoToSignUp;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonGoToSignUp = findViewById(R.id.buttonGoToSignUp); // Sign Up button

        // Initialize database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Authenticate user
            new Thread(() -> {
                User user = db.userDao().login(email, password);
                idUserToConsommations = user.getId();

                if (user != null && user.isVerified()) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                        // Pass user data to HomeActivity
                        intent.putExtra("userName", user.getName());
                        intent.putExtra("userEmail", user.getEmail());
                        intent.putExtra("userBirthdate", user.getBirthdate());
                        intent.putExtra("userPassword", user.getPassword());
                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Invalid credentials or account not verified", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });

        // Set up listener for "Sign Up" button
        buttonGoToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
