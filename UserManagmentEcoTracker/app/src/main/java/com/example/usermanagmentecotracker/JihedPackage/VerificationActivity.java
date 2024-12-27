package com.example.usermanagmentecotracker.JihedPackage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Entity.User;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.JihedPackage.api.UserApi;
import com.example.usermanagmentecotracker.R;
import com.google.gson.Gson;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerificationActivity extends AppCompatActivity {

    private EditText editTextCaptchaInput;
    private Button verifyButton;
    private ImageView captchaImageView;
    private ImageView logOutButton;
    private AppDatabase db;
    private String generatedCaptcha;
    private String name, birthdate, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // Initialize views
        editTextCaptchaInput = findViewById(R.id.editTextCaptchaInput);
        captchaImageView = findViewById(R.id.captchaImageView);
        verifyButton = findViewById(R.id.buttonVerify);
        logOutButton = findViewById(R.id.logOutButton);

        // Initialize the database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DatabaseName.nameOfDatabase)
                .fallbackToDestructiveMigration()
                .build();

        // Get data from the intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        birthdate = intent.getStringExtra("birthdate");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        // Generate and display CAPTCHA
        generatedCaptcha = generateCaptcha();
        Bitmap captchaBitmap = createCaptchaImage(generatedCaptcha);
        captchaImageView.setImageBitmap(captchaBitmap);

        // Set listeners
        verifyButton.setOnClickListener(v -> verifyCaptcha());
        logOutButton.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(VerificationActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        });
    }

    private String generateCaptcha() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) { // Generate a 5-character CAPTCHA
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }
        return captcha.toString();
    }

    private Bitmap createCaptchaImage(String captchaText) {
        int width = 300, height = 100; // Define image size
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        Random random = new Random();

        // Set background color
        canvas.drawColor(Color.LTGRAY);

        // Draw random lines for noise
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);
        for (int i = 0; i < 10; i++) {
            float startX = random.nextInt(width);
            float startY = random.nextInt(height);
            float stopX = random.nextInt(width);
            float stopY = random.nextInt(height);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        // Set text properties
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setFakeBoldText(true);
        paint.setAntiAlias(true);

        // Draw each character with slight rotation and random position
        int x = 50;
        for (int i = 0; i < captchaText.length(); i++) {
            // Random rotation
            canvas.save();
            float rotation = random.nextInt(30) - 15; // Random rotation between -15 and 15 degrees
            canvas.rotate(rotation, x, 70);

            // Draw character
            char character = captchaText.charAt(i);
            canvas.drawText(String.valueOf(character), x, 70 + random.nextInt(10) - 5, paint); // Random y-offset
            canvas.restore();

            // Move to the next character position
            x += 50 + random.nextInt(10); // Random spacing between characters
        }

        // Draw additional random dots for noise
        for (int i = 0; i < 100; i++) {
            int dotX = random.nextInt(width);
            int dotY = random.nextInt(height);
            paint.setColor(random.nextBoolean() ? Color.GRAY : Color.BLACK);
            canvas.drawCircle(dotX, dotY, 2, paint);
        }

        return bitmap;
    }

    private void verifyCaptcha() {
        String userInput = editTextCaptchaInput.getText().toString().trim();

        if (userInput.isEmpty()) {
            Toast.makeText(this, "Please enter the CAPTCHA", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userInput.equals(generatedCaptcha)) {
            saveUserToDatabase();
        } else {
            Toast.makeText(this, "Incorrect CAPTCHA. Please try again.", Toast.LENGTH_SHORT).show();

            // Regenerate CAPTCHA on failure
            generatedCaptcha = generateCaptcha();
            Bitmap captchaBitmap = createCaptchaImage(generatedCaptcha);
            captchaImageView.setImageBitmap(captchaBitmap);
        }
    }

    private void saveUserToDatabase() {
        new Thread(() -> {
            User user = new User();
            user.setName(name);
            user.setBirthdate(birthdate);
            user.setEmail(email);
            user.setPassword(password);
            user.setVerified(true);

            try {
                db.userDao().insertUser(user);
                saveUserToSpringDatabase(user);
                runOnUiThread(() -> {
                    Toast.makeText(VerificationActivity.this, "Verification successful. Account created.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception e) {
                Log.e("VerificationActivity", "Error saving user: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(this, "Error creating account. Please try again.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void saveUserToSpringDatabase(User user) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginActivity.BASE_URL) // Ensure this is defined correctly in LoginActivity
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi userApi = retrofit.create(UserApi.class);

        Call<String> call = userApi.registerUser(user);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(VerificationActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(VerificationActivity.this, "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(VerificationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
