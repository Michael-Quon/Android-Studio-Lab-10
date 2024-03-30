// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class M4QFragment extends Fragment {

    private int adClickCounter = 0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int MAX_PERMISSION_REQUEST_ATTEMPTS = 2;

    private TextView datetimeTV;
    private Button locationButton;

    private Handler handler = new Handler();
    private Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.time_dateTV), Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());
            datetimeTV.setText(currentDateAndTime);

            // calls the runnable again after 1 second
            handler.postDelayed(this, 1000);
        }
    };

    private SharedPreferences preferences;
    private int permissionRequestCount = 0;

    private static final String CHANNEL_ID = "location_channel";
    private static final int NOTIFICATION_ID = 1001;

    public M4QFragment() {
        // Required empty public constructor
    }

    class LocationNotificationHelper {

        private Context mContext;
        private NotificationManager mNotificationManager;

        public LocationNotificationHelper(Context context) {
            mContext = context;
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        public void showLocationNotification() {
            if (checkLocationPermission()) {
                Location location = getLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    String title = getString(R.string.current_location);
                    String content = getString(R.string.location_snackbar_message, latitude, longitude);

                    Notification notification = createNotification(title, content);
                    if (notification != null) {
                        mNotificationManager.notify(NOTIFICATION_ID, notification);
                    }
                }
            }
        }

        private Notification createNotification(String title, String content) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.location_channel);
                String description = getString(R.string.channel_for_location_notifications);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);

                mNotificationManager.createNotificationChannel(channel);
                builder.setChannelId(CHANNEL_ID);
            }

            return builder.build();
        }

        private boolean checkLocationPermission() {
            return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        private Location getLocation() {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null && checkLocationPermission()) {
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_m4_q, container, false);

        datetimeTV = rootView.findViewById(R.id.Mic_time_date);
        locationButton = rootView.findViewById(R.id.Mic_Locationbutton);

        preferences = requireContext().getSharedPreferences(getString(R.string.saved), Context.MODE_PRIVATE);

        AdView adView = rootView.findViewById(R.id.Mic_adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                adClickCounter++;
                preferences.edit().putInt(getString(R.string.adclickcounter), adClickCounter).apply();
                Toast.makeText(requireContext(), getString(R.string.name_id) + adClickCounter, Toast.LENGTH_LONG).show();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
            }
        });

        // Initialize Mobile Ads SDK
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // SDK initialization completed
            }
        });

        // Load ad into AdView
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler.post(updateTimeRunnable); // updates time
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        } else {
            // Check if the user has denied the permission multiple times
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Request permission
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Permission denied multiple times, open app settings
                openAppSettings();
            }
        }
    }

    private void requestLocationUpdates() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    // Handle location update
                    showLocationSnackbar(location);
                    // Remove location updates after receiving the first update
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {}

                @Override
                public void onProviderDisabled(@NonNull String provider) {}

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
            });
        }
    }

    private void showLocationSnackbar(@Nullable Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // Construct the message with latitude and longitude
            String message = getString(R.string.location_snackbar_message, latitude, longitude);
            Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.dismiss), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismiss Snackbar
                        }
                    }).show();
            new LocationNotificationHelper(requireContext()).showLocationNotification();
        } else {
            // Handle case where location is null
            Snackbar.make(requireView(), getString(R.string.location_not_determined), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.dismiss), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismiss Snackbar
                        }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                // Permission denied, handle it accordingly
                permissionRequestCount++;
                if (permissionRequestCount <= MAX_PERMISSION_REQUEST_ATTEMPTS) {
                    requestLocationPermission();
                } else {
                    openAppSettings();
                }
            }
        }
    }

    private static final String PACKAGE_URI_PREFIX = "package";

    private void openAppSettings() {
        Toast.makeText(requireContext(), getString(R.string.open_app_settings), Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(PACKAGE_URI_PREFIX, requireActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
