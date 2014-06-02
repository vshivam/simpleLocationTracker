package com.locationTracker.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.fci.locationTracker.R;
import com.locationTracker.services.BackgroundServices;

public class MainActivity extends Activity {

	TextView latTextView;
	TextView longTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		latTextView = (TextView) findViewById(R.id.latTextView);
		longTextView = (TextView) findViewById(R.id.longTextView);
		this.registerReceiver(updateLocationReceiver, new IntentFilter(
				"LOCATION_UPDATED"));

		BackgroundServices backgroundServices = new BackgroundServices(this);
		backgroundServices.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(updateLocationReceiver);
	}

	private BroadcastReceiver updateLocationReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (longTextView != null && latTextView != null) {
				longTextView.setText(intent.getExtras().getDouble(
						App.LONGITUDE_KEY, 0.0)
						+ "");
				latTextView.setText(intent.getExtras().getDouble(
						App.LATITUDE_KEY, 0.0)
						+ "");
			}

		}
	};
}
