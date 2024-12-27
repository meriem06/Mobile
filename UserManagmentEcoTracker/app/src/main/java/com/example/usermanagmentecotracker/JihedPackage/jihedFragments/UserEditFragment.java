package com.example.usermanagmentecotracker.JihedPackage.jihedFragments;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Entity.User;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.R;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserEditFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String imagePath;

    private AppDatabase db;
    private EditText editTextUserName, editTextUserEmail, editTextOldPassword, editTextNewPassword, editTextBirthDate;
    private ImageView imageViewProfile;

    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_user, container, false);

        // Initialize UI components
        editTextUserName = view.findViewById(R.id.editTextUserName);
        editTextUserEmail = view.findViewById(R.id.editTextUserEmail);
        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextBirthDate = view.findViewById(R.id.eeditTextBirthDate);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);

        ImageButton buttonEditName = view.findViewById(R.id.buttonEditName);
        ImageButton buttonEditEmail = view.findViewById(R.id.buttonEditEmail);
        ImageButton buttonEditPassword = view.findViewById(R.id.buttonEditPassword);
        ImageButton buttonEditBirthDate = view.findViewById(R.id.buttonEditBirthdate);
        ImageButton buttonChangePhoto = view.findViewById(R.id.buttonChangePhoto);

        // Initialize Room database and executor service
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();
        executorService = Executors.newSingleThreadExecutor();

        // Retrieve user ID from intent
        int userId = LoginActivity.idUserToConsommations;

        // Fetch and display user data
        fetchUserData(userId);

        // Handle name update
        buttonEditName.setOnClickListener(v -> updateName(userId));

        // Handle email update
        buttonEditEmail.setOnClickListener(v -> updateEmail(userId));

        // Handle password update
        buttonEditPassword.setOnClickListener(v -> updatePassword(userId));

        // Handle birthdate update
        buttonEditBirthDate.setOnClickListener(v -> updateBirthdate(userId));

        // Handle photo change
        buttonChangePhoto.setOnClickListener(v -> openCamera());

        return view;
    }

    // Fetch user data from the database
    private void fetchUserData(int userId) {
        executorService.execute(() -> {
            User user = db.userDao().getUserById(userId);
            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    editTextUserName.setText(user.getName());
                    editTextUserEmail.setText(user.getEmail());
                    editTextBirthDate.setText(user.getBirthdate());

                    String imagePath = user.getProfileImagePath();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        imageViewProfile.setImageURI(Uri.parse(imagePath));
                    } else {
                        imageViewProfile.setImageResource(R.drawable.ic_profile);
                    }
                });
            } else {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    // Update user's name
    private void updateName(int userId) {
        String updatedName = editTextUserName.getText().toString().trim();
        if (!updatedName.isEmpty()) {
            executorService.execute(() -> {
                db.userDao().updateUserName(userId, updatedName);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Name updated successfully", Toast.LENGTH_SHORT).show()
                );
            });
        } else {
            Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Update user's email
    private void updateEmail(int userId) {
        String updatedEmail = editTextUserEmail.getText().toString().trim();
        if (!updatedEmail.isEmpty()) {
            executorService.execute(() -> {
                db.userDao().updateUserEmail(updatedEmail, userId);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Email updated successfully", Toast.LENGTH_SHORT).show()
                );
            });
        } else {
            Toast.makeText(requireContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Update user's password
    private void updatePassword(int userId) {
        String oldPassword = editTextOldPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
            executorService.execute(() -> {
                User user = db.userDao().getUserById(userId);
                if (user != null && user.getPassword().equals(oldPassword)) {
                    db.userDao().updateUserPassword(userId, newPassword);
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show()
                    );
                }
            });
        } else {
            Toast.makeText(requireContext(), "Both fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    // Update user's birthdate
    private void updateBirthdate(int userId) {
        Calendar calendar = Calendar.getInstance();
        String currentDate = editTextBirthDate.getText().toString().trim();
        if (!currentDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = sdf.parse(currentDate);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editTextBirthDate.setText(selectedDate);
                    executorService.execute(() -> {
                        db.userDao().updateUserBirthdate(userId, selectedDate);
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "Birthdate updated successfully", Toast.LENGTH_SHORT).show()
                        );
                    });
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == requireActivity().RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                String savedImagePath = saveImageToGallery(imageBitmap);
                if (savedImagePath != null) {
                    imagePath = savedImagePath;
                    updateUserProfileImagePath(imagePath);
                    imageViewProfile.setImageBitmap(BitmapFactory.decodeFile(savedImagePath));
                }
            }
        }
    }

    private void updateUserProfileImagePath(String imagePath) {
        executorService.execute(() -> {
            db.userDao().updateImagePath(LoginActivity.idUserToConsommations, imagePath);
            requireActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show()
            );
        });
    }
    private String saveImageToGallery(Bitmap bitmap) {
        // Save image to gallery
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "profile_image_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);  // Save in the Pictures folder

        ContentResolver resolver = requireContext().getContentResolver();
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (OutputStream stream = resolver.openOutputStream(imageUri)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);  // Compress and write the image
            stream.flush();
            return imageUri.toString();  // Return the URI as a string or path
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Return null if saving fails
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Properly shut down the executor service
    }
}
