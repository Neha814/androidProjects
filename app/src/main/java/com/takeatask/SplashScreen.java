package com.takeatask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import functions.Constants;

public class SplashScreen extends Activity {
	
	SharedPreferences sp;
	
	String PROJECT_NUMBER = "503839065447";

	GoogleCloudMessaging gcm;

	String regid;
	
	boolean inHome = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		inHome  = sp.getBoolean("inHome", false);

		Thread t = new Thread() {
			public void run() {
				try {
				//	sleep(3 * 1000);
					
					getRegID();
					
					

					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
	private String getRegID() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					}
					regid = gcm.register(PROJECT_NUMBER);
					msg = regid;

					Log.e("GCM", msg);

				}
				catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Intent i = new Intent(SplashScreen.this, Login.class);
					startActivity(i);
					finish();

				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Constants.REGISTRATIO_ID = msg;

				if(inHome){
					Intent i = new Intent(SplashScreen.this, Home.class);
					startActivity(i);
					finish();
				} else {
					Intent i = new Intent(SplashScreen.this, Login.class);
					startActivity(i);
					finish();
				}

			}
		}.execute(null, null, null);
		return regid;

	}
}
