package com.locationTracker.main;

import android.app.Application;

public class App extends Application {
	public static int FETCH_LOCATION_FREQUENCY = 1000 * 60;
	public static int SYNC_DATA_FREQUENCY = 1000 * 60 * 10;
	public static String LATITUDE_KEY = "LATITUDE";
	public static String LONGITUDE_KEY = "LONGITUDE";
	private static String IP_ADDRESS = "172.26.191.229";
	public static int LOCATION_ACCURACY = 40;
	public static String INSERT_URL = "http://" + IP_ADDRESS
			+ "/LocationTracker/insertLocationData.php";
	public static String CHECK_LAST_UPDATE_URL = "http://" + IP_ADDRESS
			+ "/LocationTracker/getLastUpdatedTimeInMillis.php";
}
