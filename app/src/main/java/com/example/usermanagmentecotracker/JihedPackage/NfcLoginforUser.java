package com.example.usermanagmentecotracker.JihedPackage;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.usermanagmentecotracker.JihedPackage.Entity.User;
import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.HomeActivity;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NfcLoginforUser extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
TextView nfc_text ;
    // Create an ExecutorService for background tasks
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_loginfor_user);

        // Initialize NFC adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        }
        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE
        );




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter[] intentFiltersArray = new IntentFilter[]{tagDetected};
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String cardId = bytesToHex(tag.getId()); // Convert card ID to string
            handleNfcLogin(cardId);
        }
    }

    private void handleNfcLogin(final String cardId) {
        // Run database query and insertion on background thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Get the instance of AppDatabase
                AppDatabase db = AppDatabase.getInstance(NfcLoginforUser.this);

                // Query the database for the user by card ID
                User user = db.userDao().getUserByCardId(cardId);

                // If user exists, log them in; otherwise, register a new user
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            // User exists, log them in
                            Toast.makeText(NfcLoginforUser.this, "Welcome back, " + user.getName(), Toast.LENGTH_SHORT).show();
                            // Proceed to the next activity (e.g., MainActivity)
                            Intent intent = new Intent(NfcLoginforUser.this, HomeActivity.class);
                            startActivity(intent);

                        } else {
                            // User doesn't exist; create a new user
                            User newUser = new User("Test User", "01-01-2000", "password", "test@example.com", cardId);
                            new Thread(() -> {
                                db.userDao().insertUser(newUser); // Insert the new user with NFC card ID
                                runOnUiThread(() -> {
                                    Toast.makeText(NfcLoginforUser.this, "User registered with NFC", Toast.LENGTH_SHORT).show();
                                });
                            }).start();
                        }
                    }
                });
            }
        });
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
