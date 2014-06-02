package com.locationTracker.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.locationTracker.main.UserLocation;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "locationDb";
	private static final String TABLE_NAME = "locationTable";
	private static final String KEY_ID = "id";
	private static final String KEY_LATITUDE = "lat";
	private static final String KEY_LONGITUDE = "long";
	private static final String KEY_ACCURACY = "accuracy";
	private static final String KEY_PROVIDER = "source";
	private static final String DATETIME = "datetime";
	private final String LOGTAG = getClass().getSimpleName();

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " TEXT,"
				+ KEY_LONGITUDE + " TEXT," + KEY_ACCURACY + " TEXT,"
				+ KEY_PROVIDER + " TEXT," + DATETIME + " INTEGER" + ")";
		db.execSQL(CREATE_LOCATION_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	private String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public void addLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LATITUDE, Double.toString(location.getLatitude()));
		values.put(KEY_LONGITUDE, Double.toString(location.getLongitude()));
		values.put(KEY_ACCURACY, Double.toString(location.getAccuracy()));
		values.put(KEY_PROVIDER, location.getProvider());
		values.put(DATETIME, getCurrentDateTime());
		long id = db.insert(TABLE_NAME, null, values);
		Log.d(LOGTAG, "New Location with ID : " + id + " added");
		db.close();
	}

	public ArrayList<UserLocation> getLocationsSince(String datetime) {
		String selectQuery = "SELECT * FROM " + TABLE_NAME
				+ " WHERE DATETIME(datetime) > '" + datetime + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		ArrayList<UserLocation> trackedLocations = new ArrayList<>();
		if (cursor.moveToFirst()) {
			do {
				UserLocation location = new UserLocation();
				location.setLatitude(Double.parseDouble(cursor.getString(1)));
				location.setLongitude(Double.parseDouble(cursor.getString(2)));
				location.setAccuracy(Float.parseFloat(cursor.getString(3)));
				location.setProvider(cursor.getString(4));
				location.setDatetime(cursor.getString(5));
				trackedLocations.add(location);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		Log.d(LOGTAG, trackedLocations.size() + " locations tracked since "
				+ datetime);
		return trackedLocations;
	}
}
