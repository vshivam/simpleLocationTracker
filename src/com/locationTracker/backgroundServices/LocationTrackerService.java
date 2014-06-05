package com.locationTracker.backgroundServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.locationTracker.db.DatabaseHandler;
import com.locationTracker.main.App;

public class LocationTrackerService extends Service implements LocationListener {

	private LocationManager locationManager;
	Boolean isNetworkEnabled = false;
	Boolean isGPSEnabled = false;
	private String LOGTAG = getClass().getSimpleName();
	private static Boolean isServiceRunning = false;
	DatabaseHandler dbHandler;
	Handler networkHandler;
	Runnable updateLocationProvider;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOGTAG, "onCreate >>> " + LOGTAG);
		dbHandler = new DatabaseHandler(this);
		networkHandler = new Handler();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(LOGTAG, "onDestroy >>> " + LOGTAG);
		isServiceRunning = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!isServiceRunning) {
			isServiceRunning = true;
			Log.d(LOGTAG, "onStartCommand >>> " + LOGTAG);

			updateLocationProvider = new Runnable() {
				@Override
				public void run() {
					if (locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, 0, 0,
								LocationTrackerService.this);
						Log.d(LOGTAG, "Requesting location updates from GPS");
					} else {
						Log.d(LOGTAG, "GPS location providers not enabled");
					}
				}
			};

			networkHandler.postDelayed(updateLocationProvider,
					App.ADD_GPS_LISTENER_DELAY);

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			if (locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0, this);
			} else {
				Log.d(LOGTAG, "Network location providers not enabled");
			}
		}

		return START_STICKY;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(LOGTAG, "Latitude >>> " + location.getLatitude());
		Log.d(LOGTAG, "Longitude >>> " + location.getLongitude());
		Log.d(LOGTAG, "Accuracy >>> " + location.getAccuracy());
		Log.d(LOGTAG, "Provider >>> " + location.getProvider());
		Intent locationBroadcastIntent = new Intent("LOCATION_UPDATED");
		locationBroadcastIntent.putExtra(App.LATITUDE_KEY,
				location.getLatitude());
		locationBroadcastIntent.putExtra(App.LONGITUDE_KEY,
				location.getLongitude());
		locationBroadcastIntent.putExtra(App.ACCURACY_KEY,
				location.getAccuracy());
		locationBroadcastIntent.putExtra(App.PROVIDER_KEY,
				location.getProvider());
		sendBroadcast(locationBroadcastIntent);

		if (location.getAccuracy() < App.LOCATION_ACCURACY) {
			locationManager.removeUpdates(this);
			dbHandler.addLocation(location);
			networkHandler.removeCallbacks(updateLocationProvider);
			stopSelf();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(LOGTAG, "Provider Disabled >>> " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(LOGTAG, "Provider Enabled >>> " + provider);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(LOGTAG, "Status changed >>> " + provider);
	}

}
