package com.locationTracker.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.locationTracker.backgroundServices.BackgroundServices;

public class MainActivity extends Activity {

	TextView latTextView, longTextView, accuracyTextView, providerTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		latTextView = (TextView) findViewById(R.id.latTextView);
		longTextView = (TextView) findViewById(R.id.longTextView);
		accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);
		providerTextView = (TextView) findViewById(R.id.providerTextView);
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
				accuracyTextView.setText(intent.getExtras().getFloat(
						App.ACCURACY_KEY, 0.0f)
						+ "");
				providerTextView.setText(intent.getExtras().getString(
						App.PROVIDER_KEY));

			}
		}
	};
}
