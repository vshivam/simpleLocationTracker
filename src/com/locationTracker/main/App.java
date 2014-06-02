package com.locationTracker.main;

import android.app.Application;

public class App extends Application {
	public static String LATITUDE_KEY = "LATITUDE";
	public static String LONGITUDE_KEY = "LONGITUDE";
	public static int LOCATION_ACCURACY = 40;
	public static String INSERT_URL = "http://172.26.191.180/LocationTracker/insertLocationData.php";
	public static String CHECK_LAST_UPDATE_URL = "http://172.26.191.180/LocationTracker/getLastUpdatedTimeInMillis.php";
}
