package com.example.usermanagmentecotracker.cyrinePackage;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetectedActivityIntentService extends IntentService {
    private static final String TAG = "DetectedActivityService";

    public DetectedActivityIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();

            String activityName = getActivityName(mostProbableActivity.getType());
            int confidence = mostProbableActivity.getConfidence();
            String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());

            Log.d(TAG, "Detected activity: " + activityName + " with confidence: " + confidence);
            // Optionally, you can broadcast the result or save it in a database
        }
    }

    private String getActivityName(int activityType) {
        switch (activityType) {
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.IN_VEHICLE:
                return "VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "BICYCLE";
            case DetectedActivity.RUNNING:
                return "RUNNING";
            default:
                return "UNKNOWN";
        }
    }
}