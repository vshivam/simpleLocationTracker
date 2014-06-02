package com.locationTracker.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.locationTracker.networkServices.NetworkService;

public class DataSyncService extends Service {

	private final String LOGTAG = getClass().getSimpleName();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOGTAG, "onCreate >>> " + LOGTAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(LOGTAG, "onDestroy >>> " + LOGTAG);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOGTAG, "onStartCommand >>> " + LOGTAG);
		NetworkService networkServices = new NetworkService(this);
		networkServices.syncLocations();
		return START_STICKY;
	}
}
