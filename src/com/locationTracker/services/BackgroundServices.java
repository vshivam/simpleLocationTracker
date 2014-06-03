package com.locationTracker.services;

import com.locationTracker.main.App;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class BackgroundServices {

	Context context;

	public BackgroundServices(Context context) {
		this.context = context;
	}

	public void start() {
		Intent locationTrackerIntent = new Intent(context,
				LocationTrackerService.class);
		PendingIntent startServicePendingIntent = PendingIntent.getService(
				context, 0, locationTrackerIntent, 0);

		Intent dataSyncService = new Intent(context, DataSyncService.class);
		PendingIntent dataSyncServicePendingIntent = PendingIntent.getService(
				context, 0, dataSyncService, 0);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), App.FETCH_LOCATION_FREQUENCY,
				startServicePendingIntent);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), App.SYNC_DATA_FREQUENCY,
				dataSyncServicePendingIntent);
	}
}
