package com.locationTracker.gson;

import java.util.ArrayList;

import com.locationTracker.main.UserLocation;

public class LocationDataForServer {
	private String uuid;
	private ArrayList<UserLocation> locations;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public ArrayList<UserLocation> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<UserLocation> locations) {
		this.locations = locations;
	}
}
