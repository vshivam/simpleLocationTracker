package com.locationTracker.backgroundServices;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.locationTracker.main.App;
import com.locationTracker.main.MainActivity;
import com.locationTracker.main.R;

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
		showServicesRunningNotif("Dengue Tracker",
				"Services running in the background !");
	}

	private void showServicesRunningNotif(String title, String message) {

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setAutoCancel(true);
		Intent intent = new Intent(context, MainActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		builder.setContentIntent(PendingIntent.getActivity(context, 0, intent,
				0));
		builder.setContentTitle(title);
		builder.setContentText(message);
		builder.setSmallIcon(R.drawable.ic_action_web_site);
		Notification notification = builder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(App.SERVICES_RUNNING_NOTIF_ID, notification);
	}

}
