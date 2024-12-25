package com.example.usermanagmentecotracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.usermanagmentecotracker.Database.AppDatabase;
import com.example.usermanagmentecotracker.Entity.User;
import com.example.usermanagmentecotracker.NameDatabaseJihed.DatabaseName;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText editTextName, editTextBirthdate, editTextEmail, editTextPassword;
    private Button signUpButton;
    private AppDatabase db;
    private String verificationCode;
    private ImageView logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextBirthdate = findViewById(R.id.editTextBirthdate);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        signUpButton = findViewById(R.id.buttonSignUp);
        logOutButton = findViewById(R.id.logOutButton);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DatabaseName.nameOfDatabase)
                .fallbackToDestructiveMigration() // Reset database on schema change
                .build();

        signUpButton.setOnClickListener(v -> handleRegistration());

        logOutButton.setOnClickListener(v -> {
            // Navigate back to the LoginActivity
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleRegistration() {
        String name = editTextName.getText().toString().trim();
        String birthdate = editTextBirthdate.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty() || birthdate.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        verificationCode = generateVerificationCode();

        String subject = "Your Verification Code";
        String messageBody = "Your verification code is: " + verificationCode;

        /*EmailSender emailSender = new EmailSender(email, subject, messageBody);
        emailSender.execute();*/

        Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("birthdate", birthdate);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("verificationCode", verificationCode);
        startActivity(intent);
    }

    private String generateVerificationCode() {
        int code = (int) (Math.random() * 9000) + 1000; // Generates a 4-digit code
        return String.valueOf(code);
    }
}
