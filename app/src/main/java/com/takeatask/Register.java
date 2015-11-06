package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.GPSTracker;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

public class Register extends Activity implements OnClickListener {

	TextView terms;
	CheckBox agree_checkbox;
	//Spinner help_spinner;
	LinearLayout sign_up;
	Button register;
	Boolean isConnected;
	EditText firstname, lastname, email, password, confirm_password, zipcode, state, city, country;

	
	// EditText location ;

	SharedPreferences sp;
	GPSTracker gps;

	TransparentProgressDialog db;
	
	boolean isCheckboxChecked = false ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.register);

		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		terms = (TextView) findViewById(R.id.terms);
		agree_checkbox = (CheckBox) findViewById(R.id.agree_checkbox);
		//help_spinner = (Spinner) findViewById(R.id.help_spinner);
		firstname = (EditText) findViewById(R.id.firstname);
		lastname = (EditText) findViewById(R.id.lastname);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);
		zipcode = (EditText) findViewById(R.id.zipcode);
		sign_up = (LinearLayout) findViewById(R.id.sign_up);
		register = (Button) findViewById(R.id.register);
		// location = (EditText) findViewById(R.id.location);
		state = (EditText) findViewById(R.id.state);
		city = (EditText) findViewById(R.id.city);
		country = (EditText) findViewById(R.id.country);
		terms = (TextView) findViewById(R.id.terms);

		register.setOnClickListener(this);
		sign_up.setOnClickListener(this);
		terms.setOnClickListener(this);

		SpannableString content1 = new SpannableString("Terms & conditions");
		content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
		terms.setText(content1);



		/*String[] list = new String[] { "I want to post a task", "Post task",
				"Run task" };
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.simple_spinner_item,
				R.id.text, list);
		help_spinner.setAdapter(dataAdapter);*/

		agree_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {
							isCheckboxChecked = true;
							agree_checkbox
									.setButtonDrawable(R.drawable.checkbox);
						} else {
							isCheckboxChecked = false;
							agree_checkbox
									.setButtonDrawable(R.drawable.uncheck);
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		if (v == register || v == sign_up) {
			if(isCheckboxChecked){
			VAlidateAndCallRegisterAPI();
			} else {
				showDialog("Please accept terms and condition to proceed further.");
			}
		} else if(v==terms){
			Uri uri = Uri.parse(Constants.TERMS_AND_CONDITIONS); // missing 'http://' will cause crashed
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	private void VAlidateAndCallRegisterAPI() {


		String firstname_text = firstname.getText().toString();
		String lastname_text = lastname.getText().toString();
		String email_text = email.getText().toString().trim();
		String password_text = password.getText().toString();
		String confirm_password_text = confirm_password.getText().toString();
		String zipcode_text = zipcode.getText().toString();

	//	String location_text = location.getText().toString();
		String country_text = country.getText().toString();
		String city_text = city.getText().toString();
		String state_text = state.getText().toString();

		if (firstname_text.trim().length() < 1) {

			firstname.setError("Please enter firstname.");

		} else if (lastname_text.trim().length() < 1) {

			lastname.setError("Please enter lastname.");

		} else if (email_text.trim().length() < 1) {

			email.setError("Please enter email address.");

		} else if (!(StringUtils.verify(email_text))) {

			email.setError("Please enter valid email address.");

		} else if (password_text.trim().length() < 1) {

			password.setError("Please enter password");

		} else if (password_text.trim().length() < 6) {

			password.setError("Please enter password having minimum length of 6.");

		}

		else if(isAlphaNumeric(password_text)){
			password.setError("Password must contain atleast one special character and number.");
		}
		
		
		
		else if (confirm_password_text.trim().length() < 1) {

			confirm_password.setError("Please enter confirm password field.");

		} else if (!confirm_password_text.equals(password_text)) {

			confirm_password
					.setError("Password did not match with confirm password.");

		} 
		
		/*else if (location_text.trim().length() < 1) {
			location.setError("Please enter location.");
		} */
		
		 else if (city_text.trim().length() < 1) {
			city.setError("Please enter city.");
		} else if (state_text.trim().length() < 1) {
			state.setError("Please enter state.");
		}

		else if (zipcode_text.trim().length() < 1) {

			zipcode.setError("Please enter zipcode.");
		}
		else if (country_text.trim().length() < 1) {
			country.setError("Please enter country.");
		}else {
			if (isConnected) {

				new RegisterTask(firstname_text, lastname_text, email_text,
						password_text, confirm_password_text,
						country_text, city_text, state_text, zipcode_text).execute(new Void[0]);
			} else {
				showDialog(Constants.No_INTERNET);
			}
		}

	}

	public boolean isAlphaNumeric(String s) {
		    String pattern= "^[a-zA-Z0-9]*$";
	        if(s.matches(pattern)) {
	            return true;
	        }
	        return false;   
	}
		

	public class RegisterTask extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String firstname, lastname, email, password, location, country, city,
				state, zipcode , token_id;
		
	//	String  ispost, isrun; 

		double lat, lng;

		public RegisterTask(String firstname_text, String lastname_text,
				String email_text, String password_text,
				String confirm_password_text,
				String country_text, String city_text, String state_text,
				String zipcode_text) {

			this.firstname = firstname_text;
			this.lastname = lastname_text;
			this.email = email_text;
			this.password = password_text;
			this.country = country_text;
			this.city = city_text;
			this.state = state_text;
			this.zipcode = zipcode_text;

			
			this.token_id = Constants.REGISTRATIO_ID;
			gps = new GPSTracker(Register.this);
			if (gps.canGetLocation()) {
				this.lat = gps.getLatitude();
				this.lng = gps.getLongitude();

			}
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/signup.php?
			 * emailId=osvinandroid@gmail.com
			 * &fname=robin&lname=singh&password=password&city=ludhiana
			 * &state=punjab&country=india&zipcode=141003&phone=988989899
			 * &user_type =1&longitude=75.857276&latitude=30.900965&is_post_task
			 * =0&is_run_task=1
			 */
			try {
				localArrayList.add(new BasicNameValuePair("fname",
						this.firstname));
				localArrayList.add(new BasicNameValuePair("lname",
						this.lastname));
				localArrayList
						.add(new BasicNameValuePair("emailId", this.email));
				localArrayList.add(new BasicNameValuePair("password",
						this.password));

				localArrayList.add(new BasicNameValuePair("city", this.city));
				localArrayList.add(new BasicNameValuePair("state", this.state));
				localArrayList.add(new BasicNameValuePair("country",
						this.country));
				localArrayList.add(new BasicNameValuePair("zipcode",
						this.zipcode));
				localArrayList.add(new BasicNameValuePair("phone", ""));
				localArrayList.add(new BasicNameValuePair("longitude", String
						.valueOf(this.lng)));
				localArrayList.add(new BasicNameValuePair("latitude", String
						.valueOf(this.lat)));

				localArrayList
						.add(new BasicNameValuePair("is_post_task", ""));
				localArrayList
						.add(new BasicNameValuePair("is_run_task", ""));

				localArrayList
						.add(new BasicNameValuePair("signup_via", "email"));

				localArrayList
						.add(new BasicNameValuePair("fb_pic", ""));

				localArrayList.add(new BasicNameValuePair("device_id", "0"));
				localArrayList.add(new BasicNameValuePair("token_id",
						this.token_id));
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));

				result = function.register(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {

					Constants.EMAIL = (String) result.get("email");
					Constants.USER_ID = (String) result.get("user_id");
                    Constants.LOGIN_TYPE = (String) result.get("signup_via");
					
					Constants.TYPE = "mannual";

					Editor e = sp.edit();
					e.putString("email", Constants.EMAIL);
					e.putString("user_id", Constants.USER_ID);
					e.putString("type", Constants.TYPE);
					e.putString("login_type",Constants.LOGIN_TYPE);
					e.commit();

                    Toast.makeText(getApplicationContext(),"Successfully Registered.",Toast.LENGTH_SHORT).show();

					Intent i = new Intent(Register.this, Home.class);
					startActivity(i);

				} else if (result.get("ResponseCode").equals("false")) {
					showDialog(Constants.ERROR_MSG);
					showDialog("User Already Exist.");
					/*Toast.makeText(getApplicationContext(), "Not registered.",
							Toast.LENGTH_SHORT).show();*/
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Register.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	/*protected void showDialog(String msg) {
		final Dialog dialog;
		dialog = new Dialog(Register.this);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(0);
		dialog.getWindow().setBackgroundDrawable(d);

		Button ok;
		TextView message;

		dialog.setContentView(R.layout.dialog);

		ok = (Button) dialog.findViewById(R.id.ok);
		message = (TextView) dialog.findViewById(R.id.message);

		message.setText(msg);

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();

	}*/
	
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					Register.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("Alert !");

			// Setting Dialog Message
			alertDialog.setMessage(msg);

			// Setting Icon to Dialog
		//	alertDialog.setIcon(R.drawable.browse);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					dialog.cancel();
				}
			});

			// Showing Alert Message
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                                                        INPUT_METHOD_SERVICE);
       imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
       return true;
    }
}
