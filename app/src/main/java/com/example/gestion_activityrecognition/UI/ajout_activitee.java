package com.example.gestion_activityrecognition.UI;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gestion_activityrecognition.BuildConfig;
import com.example.gestion_activityrecognition.DAO.ActivityDAO;
import com.example.gestion_activityrecognition.Database.ActivityDatabase;
import com.example.gestion_activityrecognition.R;
import com.example.gestion_activityrecognition.Utils.EmailSender;
import com.example.gestion_activityrecognition.entity.Activity;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import logger.LogFragment;

public class ajout_activitee extends Fragment {
    private Button toggleTrackingButton;
    private static final String TAG = "AjoutActiviteeFragment";
    private LottieAnimationView lottieAnimation;
    private TextView textView;
    private ActivityDAO activityRecognitionDAO;
    private boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    private LogFragment mLogFragment;
    private Button saveButton;
    private boolean activityTrackingEnabled;
    private List<ActivityTransition> activityTransitionList;
    private final String TRANSITIONS_RECEIVER_ACTION = BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION";
    private PendingIntent mActivityTransitionsPendingIntent;
    private TransitionsReceiver mTransitionsReceiver;
    private ActivityDatabase database;

    private static String toActivityString(int activity) {
        switch (activity) {
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.IN_VEHICLE:
                return "VEHICLE";
            default:
                return "UNKNOWN";
        }
    }

    private static String toTransitionType(int transitionType) {
        switch (transitionType) {
            case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                return "ENTER";
            case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "UNKNOWN";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ajout_activitee, container, false);
        toggleTrackingButton = rootView.findViewById(R.id.buttonSave3);
        saveButton = rootView.findViewById(R.id.buttonSave);
        Button returnButton = rootView.findViewById(R.id.buttonReturn);
        lottieAnimation = rootView.findViewById(R.id.lottieAnimation);
        textView = rootView.findViewById(R.id.activityTextView);
        lottieAnimation.setAnimation(R.raw.waiting);

        activityTrackingEnabled = false;
        database = ActivityDatabase.getInstance(requireContext());
        activityRecognitionDAO = database.activityDAO();

        activityTransitionList = new ArrayList<>();

        // Add activity transitions to track
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        // Create PendingIntent to listen for activity transitions
        Intent intent = new Intent(TRANSITIONS_RECEIVER_ACTION);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_MUTABLE;
        }
        mActivityTransitionsPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, flags);

        // Create and register the BroadcastReceiver for activity transitions
        mTransitionsReceiver = new TransitionsReceiver();

        toggleTrackingButton.setOnClickListener(v -> toggleActivityRecognition());

        saveButton.setOnClickListener(v -> {
            String activityType = textView.getText().toString();
            if (!TextUtils.isEmpty(activityType)) {
                saveActivityToDatabase(); // Save activity
                printToScreen("Activity saved manually: " + activityType);
            } else {
                printToScreen("No activity type to save.");
            }
        });

        returnButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        requireContext().registerReceiver(mTransitionsReceiver, new IntentFilter(TRANSITIONS_RECEIVER_ACTION));
    }

    @Override
    public void onStop() {
        requireContext().unregisterReceiver(mTransitionsReceiver);
        super.onStop();
    }

    private void toggleActivityRecognition() {
        if (activityRecognitionPermissionApproved()) {
            if (activityTrackingEnabled) {
                disableActivityTransitions();
            } else {
                enableActivityTransitions();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            } else {
                enableActivityTransitions(); // Enable transitions if permission already granted
            }
        }
    }

    private void enableActivityTransitions() {
        ActivityTransitionRequest request = new ActivityTransitionRequest(activityTransitionList);
        Task<Void> task = ActivityRecognition.getClient(requireContext())
                .requestActivityTransitionUpdates(request, mActivityTransitionsPendingIntent);

        task.addOnSuccessListener(result -> {
            activityTrackingEnabled = true;
            printToScreen("Transitions API successfully registered.");
        }).addOnFailureListener(e -> {
            printToScreen("Transitions API could NOT be registered: " + e);
            Log.e(TAG, "Transitions API could NOT be registered: " + e);
        });
    }

    private void disableActivityTransitions() {
        ActivityRecognition.getClient(requireContext())
                .removeActivityTransitionUpdates(mActivityTransitionsPendingIntent)
                .addOnSuccessListener(result -> {
                    activityTrackingEnabled = false;
                    printToScreen("Transitions successfully unregistered.");
                }).addOnFailureListener(e -> {
                    printToScreen("Transitions could not be unregistered: " + e);
                    Log.e(TAG, "Transitions could not be unregistered: " + e);
                });
    }

    private boolean activityRecognitionPermissionApproved() {
        return runningQOrLater
                ? PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                : true;
    }

    private void printToScreen(@NonNull String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }

    private void saveActivityToDatabase() {
        String activityType = textView.getText().toString();
        long timestamp = System.currentTimeMillis();
        new Thread(() -> {
            Activity activity = new Activity();
            activity.setActivityType(activityType);
            activity.setDate(new Date(timestamp));
            activityRecognitionDAO.insert(activity);
            Log.d(TAG, "Activity saved to database: " + activityType);
        }).start();

    }

    public class TransitionsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive(): " + intent);
            if (!TextUtils.equals(TRANSITIONS_RECEIVER_ACTION, intent.getAction())) {
                printToScreen("Received an unsupported action in TransitionsReceiver: action = " + intent.getAction());
                return;
            }
            if (ActivityTransitionResult.hasResult(intent)) {
                ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
                for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                    String activityType = toActivityString(event.getActivityType());
                    String transitionType = toTransitionType(event.getTransitionType());
                    String info = "Transition: " + activityType + " (" + transitionType + ") at " +
                            new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
                    textView.setText(activityType);
                    printToScreen(info);
                    String subject = "Activity detected";
                    String body = "You are currently: " + activityType + " (" + transitionType + ") at " +
                            new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
                    saveActivityToDatabase();
                    try {
                        EmailSender.sendEmail("recipient@example.com", subject, body);
                    } catch (MessagingException e) {
                        Log.e("EmailSend", "Messaging exception: " + e.getMessage());
                        // Affichez un message d'erreur à l'utilisateur si nécessaire
                    } catch (Exception e) {
                        Log.e("EmailSend", "General exception: " + e.getMessage());
                        // Affichez un message d'erreur à l'utilisateur si nécessaire
                    }




                    // Set the Lottie animation based on detected activity
                    try {
                        switch (activityType) {
                            case "WALKING":
                                lottieAnimation.setAnimation(R.raw.walking);
                                break;
                            case "STILL":
                                lottieAnimation.setAnimation(R.raw.still);
                                break;

                            default:
                                lottieAnimation.setAnimation(R.raw.waiting);
                                break;
                        }
                        lottieAnimation.playAnimation();
                    } catch (Exception e) {
                        Log.e(TAG, "Error setting Lottie animation: " + e.getMessage());
                        printToScreen("Error setting Lottie animation: " + e.getMessage());
                    }
                }
            } else {
                Log.d(TAG, "No transition result in intent.");
            }
        }
    }
}
