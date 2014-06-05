package com.locationTracker.main;

import android.app.Application;
import android.util.Log;

public class App extends Application {
	public static final int SERVICES_RUNNING_NOTIF_ID = 0;
	public static final int UNINSTALL_NOTIF_ID = 1;
	private String LOGTAG = getClass().getSimpleName();
	public static int ADD_GPS_LISTENER_DELAY = 60 * 1000;
	public static int FETCH_LOCATION_FREQUENCY = 1000 * 60;
	public static int SYNC_DATA_FREQUENCY = 1000 * 60 * 10;
	public static String LATITUDE_KEY = "LATITUDE";
	public static String LONGITUDE_KEY = "LONGITUDE";
	private static String IP_ADDRESS = "vshivam.spectralsun.com";
	public static String ACCURACY_KEY = "ACCURACY";
	public static String PROVIDER_KEY = "PROVIDER";
	public static int LOCATION_ACCURACY = 50;
	public static String INSERT_URL = "http://" + IP_ADDRESS
			+ "/LocationTracker/insertLocationData.php";
	public static String CHECK_LAST_UPDATE_URL = "http://" + IP_ADDRESS
			+ "/LocationTracker/getLastUpdatedDatetime.php";

	public App() {
		Log.d(LOGTAG, "Initializing Application");
	}
}
