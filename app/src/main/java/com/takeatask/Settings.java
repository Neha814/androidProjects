package com.takeatask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import functions.Constants;

public class Settings extends Activity implements OnClickListener {

	TextView profile, skills, payment, notiifcation, password, location;
	
	ImageView gps_on_off;
	ImageView back;
	View password_view ,profile_view;
	LinearLayout back_ll,password_layout,profile_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting);

		profile = (TextView) findViewById(R.id.profile);
		skills = (TextView) findViewById(R.id.skills);
		payment = (TextView) findViewById(R.id.payment);
		notiifcation = (TextView) findViewById(R.id.notiifcation);
		password = (TextView) findViewById(R.id.password);
		location = (TextView) findViewById(R.id.location);
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		gps_on_off = (ImageView) findViewById(R.id.gps_on_off);
		password_layout = (LinearLayout) findViewById(R.id.password_layout);
		password_view = (View) findViewById(R.id.password_view);
        profile_layout = (LinearLayout) findViewById(R.id.profile_layout);
        profile_view = (View) findViewById(R.id.profile_view);


		profile.setOnClickListener(this);
		skills.setOnClickListener(this);
		payment.setOnClickListener(this);
		notiifcation.setOnClickListener(this);
		password.setOnClickListener(this);
		location.setOnClickListener(this);
		back.setOnClickListener(this);
		gps_on_off.setOnClickListener(this);
		back_ll.setOnClickListener(this);
		
		final Handler localHandler2 = new Handler();
		localHandler2.postDelayed(new Runnable() {
			public void run() {
				if (!((LocationManager) getSystemService("location"))
						.isProviderEnabled("gps")) {
			
					gps_on_off.setImageResource(R.drawable.non_active);
				} else {
				
					gps_on_off.setImageResource(R.drawable.active);
				}
				localHandler2.postDelayed(this, 1000L);
			}
		}, 1000L);

		if(Constants.LOGIN_TYPE.equalsIgnoreCase("fb")){
			password_layout.setVisibility(View.GONE);
			password_view.setVisibility(View.GONE);
            //profile_layout.setVisibility(View.GONE);
            //profile_view.setVisibility(View.GONE);
		}
	
	}

	@Override
	public void onClick(View v) {
		if (v == profile) {
			Intent i = new Intent(Settings.this, Profile.class);
			startActivity(i);
		} else if (v == password) {
			Intent i = new Intent(Settings.this, ChangePassword.class);
			startActivity(i);
		} else if (v == back || v==back_ll) {

			Intent i = new Intent(Settings.this, Home.class);
			startActivity(i);

		} else if(v==gps_on_off){
			CheckSwitchState();
		} 
		else if(v==payment) {
			Intent i = new Intent(Settings.this, Payment.class);
			startActivity(i);
		}  else if(v==notiifcation){
			Intent i = new Intent(Settings.this, AlertNotification.class);
			startActivity(i);
		}
	}

	private void CheckSwitchState() {
		Intent localIntent2 = new Intent(
				"android.settings.LOCATION_SOURCE_SETTINGS");
		startActivity(localIntent2);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent i = new Intent(Settings.this , Home.class);
		startActivity(i);
	}

}
