package com.example.usermanagmentecotracker;

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

import com.example.usermanagmentecotracker.Database.AppDatabase;
import com.example.usermanagmentecotracker.Entity.User;
import com.example.usermanagmentecotracker.NameDatabaseJihed.DatabaseName;

import java.io.IOException;
import java.io.OutputStream;
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

        // Retrieve user email from intent
        String userEmail = requireActivity().getIntent().getStringExtra("userEmail");

        // Fetch and display user data
            fetchUserData(userEmail);
        // Handle birthdate update
        buttonEditBirthDate.setOnClickListener(v -> updateBirthdate(userEmail));

        // Handle photo change
        buttonChangePhoto.setOnClickListener(v -> openCamera());

        return view;
    }

    public void fetchUserData(String userEmail) {
        executorService.execute(() -> {
            User user = db.userDao().getUserByEmail(userEmail);
            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    // Populate user data
                    editTextUserName.setText(user.getName());
                    editTextUserEmail.setText(user.getEmail());
                    editTextBirthDate.setText(user.getBirthdate());

                    // Load the image if it exists
                    String imagePath = user.getProfileImagePath();  // Get the stored image path from the database
                    if (imagePath != null && !imagePath.isEmpty()) {
                        imageViewProfile.setImageURI(Uri.parse(imagePath));  // Set the image using the stored URI
                    } else {
                        // If no image path is found, you can set a default image or leave it empty
                        imageViewProfile.setImageResource(R.drawable.ic_profile); // Example of a default image
                    }
                });
            } else {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void updateBirthdate(String userEmail) {
        String updatedBirthDate = editTextBirthDate.getText().toString().trim();
        if (!updatedBirthDate.isEmpty()) {
            executorService.execute(() -> {
                db.userDao().updateUserBirthdate(userEmail, updatedBirthDate);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Birthdate updated successfully", Toast.LENGTH_SHORT).show()
                );
            });
        } else {
            Toast.makeText(requireContext(), "Birthdate cannot be empty", Toast.LENGTH_SHORT).show();
        }
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
                // Save the image to gallery and get the image URI (as a string)
                String savedImagePath = saveImageToGallery(imageBitmap);

                // Set the image path in the database
                if (savedImagePath != null) {
                    imagePath = savedImagePath;  // Save the path to the static variable
                    updateUserProfileImagePath(imagePath);
                    imageViewProfile.setImageBitmap(BitmapFactory.decodeFile(savedImagePath));  // Display the new image
                }
            }
        }
    }

    private void updateUserProfileImagePath(String savedImagePath) {
        String userEmail = requireActivity().getIntent().getStringExtra("userEmail");

        // Update only the image path in the database
        executorService.execute(() -> {
            db.userDao().updateImagePath(userEmail, savedImagePath);  // Only update image path
            requireActivity().runOnUiThread(() -> {
                // Show a success message or do any other actions if needed
                Toast.makeText(requireContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
            });
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
