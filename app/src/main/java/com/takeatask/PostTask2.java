package com.takeatask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import functions.Constants;

public class PostTask2 extends Activity implements OnClickListener {

	Button continue_btn;
	ImageView back;

	EditText address;

	EditText country;
	
	Button skip;

	LinearLayout back_ll;
	
	EditText city;

	EditText state;

	EditText zipcode;
	
	View post1 ,post2,post3,post4,post5,post6;

	LinearLayout l1 ,l2,l3,l4,l5,l6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_task_2);

		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		continue_btn = (Button) findViewById(R.id.continue_btn);

		address = (EditText) findViewById(R.id.address);
		country = (EditText) findViewById(R.id.country);
		city = (EditText) findViewById(R.id.city);
		state = (EditText) findViewById(R.id.state);
		zipcode = (EditText) findViewById(R.id.zipcode);
		skip = (Button) findViewById(R.id.skip);
		
		post1 = (View) findViewById(R.id.post1);
		post2 = (View) findViewById(R.id.post2);
		post3 = (View) findViewById(R.id.post3);
		post4 = (View) findViewById(R.id.post4);
		post5 = (View) findViewById(R.id.post5);
		post6 = (View) findViewById(R.id.post6);
		
		l1 = (LinearLayout) findViewById(R.id.l1);
		l2 = (LinearLayout) findViewById(R.id.l2);
		l3 = (LinearLayout) findViewById(R.id.l3);
		l4 = (LinearLayout) findViewById(R.id.l4);
		l5 = (LinearLayout) findViewById(R.id.l5);
		l6 = (LinearLayout) findViewById(R.id.l6);

		if (Constants.ADDRESS!=null && Constants.ADDRESS.length() > 1) {
			address.setText(Constants.ADDRESS);
		}
		if (Constants.COUNTRY!=null &&Constants.COUNTRY.length() > 1) {
			country.setText(Constants.COUNTRY);
		}
		if (Constants.CITY!=null &&Constants.CITY.length() > 1) {
			city.setText(Constants.CITY);
		}
		if (Constants.STATE!=null &&Constants.STATE.length() > 1) {
			state.setText(Constants.STATE);
		}
		if (Constants.ZIPCODE!=null &&Constants.ZIPCODE.length() > 1) {
			zipcode.setText(Constants.ZIPCODE);
		}

		back.setOnClickListener(this);
		back_ll.setOnClickListener(this);
		continue_btn.setOnClickListener(this);
		post1.setOnClickListener(this);
		post2.setOnClickListener(this);
		post3.setOnClickListener(this);
		post4.setOnClickListener(this);
		post5.setOnClickListener(this);
		post6.setOnClickListener(this);
		skip.setOnClickListener(this);
		
		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);
		l5.setOnClickListener(this);
		l6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == back || v==back_ll) {
			//finish();
			SaveAndGo();
			Intent i = new Intent(PostTask2.this , PostTask1.class);
			startActivity(i);
		} else if (v == continue_btn) {

			String address_text = address.getText().toString();
			String country_text = country.getText().toString();
			String city_text = city.getText().toString();
			String state_text = state.getText().toString();
			String zipcode_text = zipcode.getText().toString();

			/*
			 * if (address_text.trim().length() < 1) {
			 * address.setError("Please enter address"); }
			 */
			/*if (country_text.trim().length() < 1) {
				country.setError("Please enter country");
			} else if (city_text.trim().length() < 1) {
				city.setError("Please enter city");
			} else if (state_text.trim().length() < 1) {
				state.setError("Please enter state");
			} else if (zipcode_text.trim().length() < 1) {
				zipcode.setError("Please enter zipcode");
			}
			
			else {*/

				Constants.ADDRESS = address_text;
				Constants.COUNTRY = country_text;
				Constants.STATE = state_text;
				Constants.ZIPCODE = zipcode_text;
				Constants.CITY = city_text;
				
			
				Intent i = new Intent(PostTask2.this, PostTask3.class);
				startActivity(i);
			//}
		}else if(v==post1 || v==l1){
			SaveAndGo();
			Intent i = new Intent(PostTask2.this , PostTask1.class);
			startActivity(i);
		}else if(v==post3 || v==l3){
			SaveAndGo();
			Intent i = new Intent(PostTask2.this , PostTask3.class);
			startActivity(i);
		}else if(v==post4 || v==l4){
			SaveAndGo();
			Intent i = new Intent(PostTask2.this , PostTask4.class);
			startActivity(i);
		}else if(v==post5 || v==l5){
			SaveAndGo();
			Intent i = new Intent(PostTask2.this , PostTask5.class);
			startActivity(i);
		}else if(v==post6 || v==l6){
			SaveAndGo();
			Intent i = new Intent(PostTask2.this , PostTask6.class);
			startActivity(i);
		} else if(v==skip){
			Constants.ADDRESS = "";
			Constants.COUNTRY = "";
			Constants.STATE = "";
			Constants.ZIPCODE = "";
			Constants.CITY = "";
			Intent i = new Intent(PostTask2.this, PostTask3.class);
			startActivity(i);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}

	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		SaveAndGo();
		

		finish();
	}*/

	private void SaveAndGo() {
		String address_text = address.getText().toString().trim();
		String country_text = country.getText().toString().trim();
		String city_text = city.getText().toString().trim();
		String state_text = state.getText().toString().trim();
		String zipcode_text = zipcode.getText().toString().trim();

		Constants.ADDRESS = address_text;
		Constants.COUNTRY = country_text;
		Constants.STATE = state_text;
		Constants.ZIPCODE = zipcode_text;
		Constants.CITY = city_text;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		/*clearAllVariables();
		Intent i = new Intent(PostTask2.this , Home.class);
		startActivity(i);*/
		
		SaveAndGo();
		Intent i = new Intent(PostTask2.this , PostTask1.class);
		startActivity(i);
	}
	
	public void clearAllVariables() {
		Constants.TASK_NAME= "";
		Constants.DESCRIBE_TASK = "";
		Constants.ADDRESS = "";
		Constants.COUNTRY = "";
		Constants.STATE = "";
		Constants.ZIPCODE = "";
		Constants.CITY = "";
		Constants.PRICE = "";
		Constants.DATE = "";
		Constants.COMMENTS = "" ;
		
		Constants.IMAGE_TO_UPLOAD1 = new File("");

		Constants.IMAGE_TO_UPLOAD2 = new File("");

		Constants.IMAGE_TO_UPLOAD3 = new File("");

		Constants.IMAGE_TO_UPLOAD4 = new File("");

		Constants.IMAGE_TO_UPLOAD5 = new File("");


	}
}
