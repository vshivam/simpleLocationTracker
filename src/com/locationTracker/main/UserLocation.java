package com.locationTracker.main;

public class UserLocation {

	private double latitude;
	private double longitude;
	private float accuracy;
	private String provider;
	private String datetime;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String timestamp) {
		this.datetime = timestamp;
	}

}
