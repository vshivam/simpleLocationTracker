package com.locationTracker.receivers;

import com.locationTracker.backgroundServices.BackgroundServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		BackgroundServices backgroundServices = new BackgroundServices(context);
		backgroundServices.start();
	}
}
