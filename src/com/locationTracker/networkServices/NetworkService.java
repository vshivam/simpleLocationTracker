package com.locationTracker.networkServices;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.locationTracker.db.DatabaseHandler;
import com.locationTracker.gson.LocationDataForServer;
import com.locationTracker.main.App;
import com.locationTracker.main.MainActivity;
import com.locationTracker.main.R;
import com.locationTracker.main.UserLocation;

public class NetworkService {

	private String uuid = "";
	Context context;
	Gson gson;
	private String LOGTAG = getClass().getSimpleName();

	public NetworkService(Context context) {
		this.uuid = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		this.context = context;
		gson = new Gson();
	}

	public void syncLocations() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String checkURL = App.CHECK_LAST_UPDATE_URL + "?uuid="
							+ uuid;
					Log.d(LOGTAG, "checkURL >>> " + checkURL);
					String checkResponse = makeGETRequest(checkURL);
					if (!checkResponse.equals("")) {
						DatabaseHandler db = new DatabaseHandler(context);
						LocationDataForServer newData = new LocationDataForServer();
						ArrayList<UserLocation> userLocations = db
								.getLocationsSince(checkResponse);
						db.close();
						if (userLocations.size() > 0) {
							newData.setLocations(userLocations);
							newData.setUuid(uuid);
							String locationDataJSON = gson.toJson(newData);
							Log.d(LOGTAG, locationDataJSON);
							locationDataJSON = URLEncoder.encode(
									locationDataJSON, "UTF-8");
							String insertURL = App.INSERT_URL;
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("json",
									locationDataJSON));
							String insertResponse = makePOSTRequest(insertURL,
									nameValuePairs);
							Log.d(LOGTAG, insertResponse);
							ifUninstallShowNotif(insertResponse);
						} else {
							Log.d(LOGTAG,
									"No new locations found to sync with server");
						}
					} else {
						Log.d(LOGTAG,
								"Check for last update datetime >>> No Response");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private String makeGETRequest(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		String response = "";
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.d(LOGTAG, "GET Response >>> " + response);

		return response;
	}

	private String makePOSTRequest(String url,
			List<NameValuePair> nameValuePairs) {
		String response = "";

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(LOGTAG, "POST Response >>> " + response);
		return response;

	}

	private void ifUninstallShowNotif(String response) {
		try {
			JSONObject object = new JSONObject(response);
			if (object.getString("uninstall").trim().equals("true")) {
				showUninstallNotif("Thanks for participating",
						"You may now unistall the app");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showUninstallNotif(String title, String message) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setAutoCancel(true);
		Uri packageURI = Uri.parse("package:com.locationTracker.main");
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		builder.setContentIntent(PendingIntent.getActivity(context, 0,
				uninstallIntent, 0));
		builder.setContentTitle(title);
		builder.setContentText(message);
		builder.setSmallIcon(R.drawable.ic_action_web_site);
		Notification notification = builder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(App.SERVICES_RUNNING_NOTIF_ID);
		manager.notify(App.UNINSTALL_NOTIF_ID, notification);
	}
}
