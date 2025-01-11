package com.example.usermanagmentecotracker.cyrinePackage;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.airbnb.lottie.LottieAnimationView;

import com.example.usermanagmentecotracker.JihedPackage.Database.AppDatabase;
import com.example.usermanagmentecotracker.JihedPackage.Directory.ActivityDAO;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Activity;
import com.example.usermanagmentecotracker.JihedPackage.LoginActivity;
import com.example.usermanagmentecotracker.JihedPackage.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.JihedPackage.loggor.LogFragment;
import com.example.usermanagmentecotracker.R;
import com.github.mikephil.charting.BuildConfig;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;

import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ajout_activitee extends Fragment {
    private Button toggleTrackingButton;
    private static final String TAG = "AjoutActiviteeFragment";
    private LottieAnimationView lottieAnimation;
    private TextView textView;
    private ActivityDAO activityRecognitionDAO;
    private boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    private LogFragment mLogFragment;
    private Button buttonSave3;
    private boolean activityTrackingEnabled;
    private Button saveButton;
    private List<ActivityTransition> activityTransitionList;
    private final String TRANSITIONS_RECEIVER_ACTION = BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION";
    private PendingIntent mActivityTransitionsPendingIntent;
    private TransitionsReceiver mTransitionsReceiver;
    private TextView logTextView;
    private AppDatabase database;
    private static String toActivityString(int activity) {
        switch (activity) {
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
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
        mLogFragment = (LogFragment) getChildFragmentManager().findFragmentById(R.id.log_fragment);
        toggleTrackingButton =rootView.findViewById(R.id.buttonSave3);
        saveButton = rootView.findViewById(R.id.buttonSave);
        Button returnButton = rootView.findViewById(R.id.buttonReturn);
        Button toggleTrackingButton = rootView.findViewById(R.id.buttonSave3);
        lottieAnimation = rootView.findViewById(R.id.lottieAnimation);
        textView=rootView.findViewById(R.id.activityTextView);
        lottieAnimation.setAnimation(R.raw.waiting);
        activityTrackingEnabled = false;
        database  = Room.databaseBuilder(requireContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();

        activityRecognitionDAO = database.activityDAO();
        buttonSave3 =rootView.findViewById(R.id.buttonSave3);

        // List of activity transitions to track.
        activityTransitionList = new ArrayList<>();

        // TODO: Add activity transitions to track.
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
                .build()); activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());



        // TODO: Initialize PendingIntent that will be triggered when a activity transition occurs.

        Intent intent = new Intent(TRANSITIONS_RECEIVER_ACTION);
        mActivityTransitionsPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0);

        // TODO: Create a BroadcastReceiver to listen for activity transitions.
        // The receiver listens for the PendingIntent above that is triggered by the system when an
        // activity transition occurs.
        mTransitionsReceiver = new TransitionsReceiver();

        printToScreen("App initialized.");

        toggleTrackingButton.setOnClickListener(v -> toggleActivityRecognition());

        saveButton.setOnClickListener(v -> {
            String activityType = textView.getText().toString();
            if (!TextUtils.isEmpty(activityType)) {
                saveActivityToDatabase(); // Appel de la méthode pour sauvegarder l'activité
                printToScreen("Activity saved manually: " + activityType);
            } else {
                printToScreen("No activity type to save.");
            }
        });
        returnButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        printToScreen("Fragment initialized.");

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
        // TODO: Enable/Disable activity tracking and ask for permissions if needed.
        if (activityRecognitionPermissionApproved()) {

            if (activityTrackingEnabled) {
                disableActivityTransitions();

            } else {
                enableActivityTransitions();
            }

        } else {
            // Request permission and start activity for result. If the permission is approved, we
            // want to make sure we start activity recognition tracking.
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
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
        mLogFragment.getLogView().println(message);
        Log.d(TAG, message);
    }
    private void saveActivityToDatabase() {
        String activityType = textView.getText().toString();
        long timestamp = System.currentTimeMillis();
        new Thread(() -> {
            Activity activity = new Activity();
            activity.setActivityType(activityType);
            activity.setDate(new Date(timestamp));
            activity.setUserId(LoginActivity.idUserToConsommations);
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
                    saveActivityToDatabase();
                    try {
                        switch (activityType) {
                            case "WALKING":
                                lottieAnimation.setAnimation(R.raw.walking);
                                lottieAnimation.playAnimation();
                                break;
                            case "STILL":
                                lottieAnimation.setAnimation(R.raw.still);
                                lottieAnimation.playAnimation();
                                break;
                            case "VEHICLE":
                                lottieAnimation.setAnimation(R.raw.vehicule);
                                lottieAnimation.playAnimation();
                                break;
                            default:
                                lottieAnimation.setAnimation(R.raw.waiting);
                                lottieAnimation.playAnimation();
                                break;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error setting Lottie animation: " + e.getMessage());
                        printToScreen("Error setting Lottie animation: " + e.getMessage());
                    }
                }
            } else {
                Log.d(TAG, "No transition result in intent.");
                //printToScreen("Error setting Lottie animation: " + e.getMessage());
            }
        }
    }
}