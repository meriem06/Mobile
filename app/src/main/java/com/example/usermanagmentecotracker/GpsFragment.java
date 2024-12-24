package com.example.usermanagmentecotracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.usermanagmentecotracker.Database.AppDatabase;
import com.example.usermanagmentecotracker.Entity.Consommation;
import com.example.usermanagmentecotracker.NameDatabaseJihed.DatabaseName;
import com.example.usermanagmentecotracker.jihedFragments.ConsommationPopupFragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;

public class GpsFragment extends Fragment {

    public static float dist, walkTime, carTime;
    public static String placeName;
    private LocationManager locationManager;
    private MapView mapView;
    private AppDatabase db;
    private Location currentLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps, container, false);

        // Initialize database
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, DatabaseName.nameOfDatabase).build();

        // Initialize UI elements
        mapView = view.findViewById(R.id.mapView);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        // Initialize the map
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        // Initialize LocationManager
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        // Request permissions if not granted
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // "View Consommations" button setup
        Button viewConsommationsButton = view.findViewById(R.id.viewConsommationsButton);
        Button viewConsommationsStats = view.findViewById(R.id.showStatisticsButton);

        // View consommation button click listener
        viewConsommationsButton.setOnClickListener(v -> {
            new Thread(() -> {
                List<Consommation> consommations = db.consommationDao().getConsommationsForUser(LoginActivity.idUserToConsommations);

                // Show the popup on the UI thread
                requireActivity().runOnUiThread(() -> {
                    if (consommations != null && !consommations.isEmpty()) {
                        ConsommationPopupFragment popupFragment = new ConsommationPopupFragment(consommations);
                        popupFragment.show(requireActivity().getSupportFragmentManager(), "ConsommationPopup");
                    } else {
                        Toast.makeText(requireContext(), "No consommations found for the user.", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        // View statistics button click listener
        viewConsommationsStats.setOnClickListener(v -> {
            new Thread(() -> {
                // Fetch consommation data for statistics
                List<Consommation> consommations = db.consommationDao().getConsommationsForUser(LoginActivity.idUserToConsommations);

                // Show the popup on the UI thread
                requireActivity().runOnUiThread(() -> {
                    if (consommations != null && !consommations.isEmpty()) {
                        // Create and show StatisticsPopupFragment
                        StatisticsPopupFragment popupFragment = StatisticsPopupFragment.newInstance(consommations);
                        popupFragment.show(requireActivity().getSupportFragmentManager(), "StatisticsPopup");
                    } else {
                        Toast.makeText(requireContext(), "No consommations found for statistics.", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        // Search bar setup
        androidx.appcompat.widget.SearchView searchView = view.findViewById(R.id.searchPlace);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform geocoding when search is submitted
                getPlaceCoordinates(query);
                placeName = query;
                // Show a dialog with the details of the searched place
                PopupDialogFragment popupDialogFragment = new PopupDialogFragment();
                Bundle args = new Bundle();
                args.putString("distance", String.valueOf(dist));
                args.putString("walkingTime", String.valueOf(walkTime));
                args.putString("carTime", String.valueOf(carTime));
                args.putString("placeName", placeName);
                popupDialogFragment.setArguments(args);
                popupDialogFragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "PopupDialog");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        requestLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            currentLocation = location;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            float accuracy = location.getAccuracy();

            // Update the map with the current location
            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
            IMapController mapController = mapView.getController();
            mapController.setZoom(15.0);
            mapController.setCenter(geoPoint);

            // Add a marker for the current location
            Marker marker = new Marker(mapView);
            marker.setPosition(geoPoint);
            marker.setTitle("You are here");
            mapView.getOverlays().clear();
            mapView.getOverlays().add(marker);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {}

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Toast.makeText(requireContext(), "GPS is disabled. Please enable it.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        }
    }

    private void getPlaceCoordinates(String placeName) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(placeName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Calculate distance and time to the searched location
                Location placeLocation = new Location("");
                placeLocation.setLatitude(latitude);
                placeLocation.setLongitude(longitude);

                float distanceInMeters = currentLocation.distanceTo(placeLocation);
                dist = distanceInMeters / 1000;

                // Calculate walking and driving time
                walkTime = calculateWalkingTime(distanceInMeters);
                carTime = calculateCarTime(distanceInMeters);

                // Add a marker for the searched place
                GeoPoint geoPoint = new GeoPoint(latitude, longitude);
                Marker marker = new Marker(mapView);
                marker.setPosition(geoPoint);
                marker.setTitle(placeName);
                mapView.getOverlays().add(marker);
            } else {
                Toast.makeText(requireContext(), "Place not found.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float calculateWalkingTime(float distanceInMeters) {
        // Average walking speed is 5 km/h (5000 meters per hour)
        float walkingSpeed = 5.0f; // meters per hour
        return (distanceInMeters / walkingSpeed) ; // time in minutes
    }

    private float calculateCarTime(float distanceInMeters) {
        // Average driving speed is 50 km/h (50000 meters per hour)
        float drivingSpeed = 50.0f; // meters per hour
        return (distanceInMeters / drivingSpeed) ; // time in minutes
    }
}
