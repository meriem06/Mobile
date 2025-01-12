// LoginActivity.java
package com.example.usermanagmentecotracker.JihedPackage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Entity.User;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.JihedPackage.api.UserApi;
import com.example.usermanagmentecotracker.JihedPackage.NfcLoginforUser;
import com.example.usermanagmentecotracker.R;
import com.example.usermanagmentecotracker.JihedPackage.SignUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://192.168.100.137:3333/";  // Change to your server's IP
    public static int idUserToConsommations;
    public static User userToSpringBoot;

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    UserApi userApi;
    private TextView buttonGoToSignUp , texttonfc;
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
        texttonfc = findViewById(R.id.texttonfc);
        // Config Retrofit ****************************************************************************
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);
        // Config Retrofit ****************************************************************************

        // Initialize database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();

        // Login button click listener
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
                userToSpringBoot = user;
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

                    Toast.makeText(this, "Invalid credentials or account not verified", Toast.LENGTH_SHORT).show();

                }
            }).start();
        });

        // Set up listener for "Sign Up" button
        buttonGoToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        texttonfc.setOnClickListener(v -> {
            Intent intent4 = new Intent(LoginActivity.this, NfcLoginforUser.class);
            startActivity(intent4);
        });
    }
    //fonction vers le spring boot
    private void authenticateUser(String email, String password) {
        userApi.login(email, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    idUserToConsommations = user.getId();

                    if (user.isVerified()) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                        intent.putExtra("userName", user.getName());
                        intent.putExtra("userEmail", user.getEmail());
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Account not verified", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
