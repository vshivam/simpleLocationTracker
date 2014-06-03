package com.locationTracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOGTAG, "onCreate >>> " + LOGTAG);
		dbHandler = new DatabaseHandler(this);
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
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			/*
			 * Criteria criteria = new Criteria();
			 * criteria.setAccuracy(Criteria.ACCURACY_COARSE); String
			 * bestProvider = locationManager.getBestProvider(criteria, true);
			 * 
			 * if (bestProvider != null) { Log.d(LOGTAG,
			 * "Selected Provider >>> " + bestProvider);
			 * locationManager.requestLocationUpdates(bestProvider, 1000, 1,
			 * this); } else { Log.d(LOGTAG, "All Providers Disabled"); }
			 */

			if (locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER,
						App.FETCH_LOCATION_FREQUENCY, 0, this);
			} else {
				Log.d(LOGTAG, "No location providers enabled");
			}
		}

		return START_STICKY;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(LOGTAG, "Latitude >>> " + location.getLatitude());
		Log.d(LOGTAG, "Longitude >>> " + location.getLongitude());
		Log.d(LOGTAG, "Accuracy >>> " + location.getAccuracy());
		Intent locationBroadcastIntent = new Intent("LOCATION_UPDATED");
		locationBroadcastIntent.putExtra(App.LATITUDE_KEY,
				location.getLatitude());
		locationBroadcastIntent.putExtra(App.LONGITUDE_KEY,
				location.getLongitude());
		sendBroadcast(locationBroadcastIntent);

		if (location.getAccuracy() < App.LOCATION_ACCURACY) {
			locationManager.removeUpdates(this);
			dbHandler.addLocation(location);
			stopSelf();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(LOGTAG, "Provider Disabled >>> " + provider);
		switch (provider) {
		case LocationManager.GPS_PROVIDER:
			isGPSEnabled = false;
			break;
		case LocationManager.NETWORK_PROVIDER:
			isNetworkEnabled = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(LOGTAG, "Provider Enabled >>> " + provider);
		switch (provider) {
		case LocationManager.GPS_PROVIDER:
			isGPSEnabled = true;
			break;
		case LocationManager.NETWORK_PROVIDER:
			isNetworkEnabled = true;
			break;
		default:
			break;
		}
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
