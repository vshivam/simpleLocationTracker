package com.locationTracker.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FirstRun extends Activity {
	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences(App.PREFS_NAME, 0);
		boolean profileUpdated = settings.getBoolean("profileUpdated", false);
		if (profileUpdated) {
			startMainActivity();
		}
		setContentView(R.layout.first_run);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayList<String> genderList = new ArrayList<String>();
		genderList.add("Male");
		genderList.add("Female");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, genderList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startMainActivity();
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("profileUpdated", true);
				editor.commit();
			}
		});
	}

	private void startMainActivity() {
		Intent mainActivity = new Intent(FirstRun.this, MainActivity.class);
		finish();
		startActivity(mainActivity);
	}

}
