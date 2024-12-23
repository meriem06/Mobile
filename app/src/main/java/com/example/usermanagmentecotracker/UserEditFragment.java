package com.example.usermanagmentecotracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.usermanagmentecotracker.Database.AppDatabase;
import com.example.usermanagmentecotracker.Entity.User;
import com.example.usermanagmentecotracker.NameDatabaseJihed.DatabaseName;

public class UserEditFragment extends Fragment {

    private AppDatabase db;
    private EditText editTextUserName, editTextUserEmail, editTextOldPassword, editTextNewPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_user, container, false);

        editTextUserName = view.findViewById(R.id.editTextUserName);
        editTextUserEmail = view.findViewById(R.id.editTextUserEmail);
        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);

        ImageButton buttonEditName = view.findViewById(R.id.buttonEditName);
        ImageButton buttonEditEmail = view.findViewById(R.id.buttonEditEmail);
        ImageButton buttonEditPassword = view.findViewById(R.id.buttonEditPassword);

        db = Room.databaseBuilder(requireContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();

        String userEmail = requireActivity().getIntent().getStringExtra("userEmail");

        new Thread(() -> {
            User user = db.userDao().getUserByEmail(userEmail);
        // bech takheth el id ta3 el user
            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    editTextUserName.setText(user.getName());
                    editTextUserEmail.setText(user.getEmail());
                });
            }
        }).start();

        buttonEditName.setOnClickListener(v -> {
            String updatedName = editTextUserName.getText().toString().trim();
            if (!updatedName.isEmpty()) {
                new Thread(() -> {
                    db.userDao().updateUserName(userEmail, updatedName);
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Name updated successfully", Toast.LENGTH_SHORT).show());
                }).start();
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        buttonEditEmail.setOnClickListener(v -> {
            String updatedEmail = editTextUserEmail.getText().toString().trim();
            if (!updatedEmail.isEmpty()) {
                new Thread(() -> {
                    db.userDao().updateUserEmail(userEmail, updatedEmail);
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Email updated successfully", Toast.LENGTH_SHORT).show());
                }).start();
            } else {
                Toast.makeText(requireContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        buttonEditPassword.setOnClickListener(v -> {
            String oldPassword = editTextOldPassword.getText().toString().trim();
            String newPassword = editTextNewPassword.getText().toString().trim();

            if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                new Thread(() -> {
                    User user = db.userDao().login(userEmail, oldPassword);
                    if (user != null) {
                        db.userDao().updateUserPassword(userEmail, newPassword);
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show());
                    } else {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            } else {
                Toast.makeText(requireContext(), "Password fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
