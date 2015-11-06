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

public class PostTask3 extends Activity implements OnClickListener {

	Button continue_btn;
	ImageView back;

	EditText price;
	
	LinearLayout back_ll;
	
	View post1 ,post2,post3,post4,post5,post6;
	
	LinearLayout l1 ,l2,l3,l4,l5,l6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_task_3);

		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		continue_btn = (Button) findViewById(R.id.continue_btn);
		price = (EditText) findViewById(R.id.price);
		
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

		if (Constants.PRICE != null && Constants.PRICE.length() > 0) {
			price.setText(Constants.PRICE);
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
		
		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);
		l5.setOnClickListener(this);
		l6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == back || v== back_ll) {
			//finish();
			
			/*Intent i = new Intent(PostTask3.this , Home.class);
			startActivity(i);*/
			
			SaveAndGo();
			Intent i = new Intent(PostTask3.this , PostTask2.class);
			startActivity(i);
			
		} else if (v == continue_btn) {

			String price_text = price.getText().toString();

			if (price_text.trim().length() < 1) {
				price.setError("Please enter price");
			} else {

				Constants.PRICE = price_text;
				Intent i = new Intent(PostTask3.this, PostTask4.class);
				startActivity(i);
			}
		}else if(v==post1 || v==l1){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask3.this, PostTask1.class);
				startActivity(i);
			}
		}else if(v==post2 || v==l2){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask3.this, PostTask2.class);
				startActivity(i);
			}
		}else if(v==post4 || v==l4){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask3.this, PostTask4.class);
				startActivity(i);
			}
		}else if(v==post5 || v==l5){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();

				Intent i = new Intent(PostTask3.this, PostTask5.class);
				startActivity(i);
			}
		}else if(v==post6 || v==l6){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask3.this, PostTask6.class);
				startActivity(i);
			}
		}
	}

	private boolean ValidateFields() {

		String price_text = price.getText().toString();

		if (price_text.trim().length() < 1) {
			price.setError("Please enter price");
			return false;
		}

		return  true;
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
		String price_text = price.getText().toString().trim();

		Constants.PRICE = price_text;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		/*clearAllVariables();
		
		Intent i = new Intent(PostTask3.this , Home.class);
		startActivity(i);*/
		
		SaveAndGo();
		Intent i = new Intent(PostTask3.this , PostTask2.class);
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