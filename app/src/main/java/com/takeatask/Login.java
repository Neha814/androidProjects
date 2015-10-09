package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.GPSTracker;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

public class Login extends Activity implements OnClickListener {

	EditText usernameET;
	EditText passwordET;
	TextView forgot_password, register_here, take_task;
	LinearLayout sign_in;
	Button fb_login;

	Button login;
	GPSTracker gps;

	TransparentProgressDialog db;
	SharedPreferences sp;
	Boolean isConnected;

	// Instance of Facebook Class
	String APP_ID = "1443617225937981";
	// String APP_ID = "1451811558461995";
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		mAsyncRunner = new AsyncFacebookRunner(facebook);

		usernameET = (EditText) findViewById(R.id.username);
		passwordET = (EditText) findViewById(R.id.password);
		forgot_password = (TextView) findViewById(R.id.forgot_password);
		login = (Button) findViewById(R.id.login);
		register_here = (TextView) findViewById(R.id.register_here);
		take_task = (TextView) findViewById(R.id.take_task);
		fb_login = (Button) findViewById(R.id.fb_login);
		sign_in = (LinearLayout) findViewById(R.id.sign_in);

		SpannableString content1 = new SpannableString("Register Here");
		content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
		register_here.setText(content1);

		SpannableString content2 = new SpannableString("Take A Look");
		content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
		take_task.setText(content2);

		/*
		 * if (!((LocationManager) getSystemService("location"))
		 * .isProviderEnabled("gps")) { showGPSDisabledAlertToUser(); }
		 */

		login.setOnClickListener(this);
		sign_in.setOnClickListener(this);
		fb_login.setOnClickListener(this);
		register_here.setOnClickListener(this);
		forgot_password.setOnClickListener(this);
		take_task.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == login || v == sign_in) {
			callLoginAPI();
		} else if (v == register_here) {
			Intent i = new Intent(Login.this, Register.class);
			startActivity(i);
		} else if (v == fb_login) {
			FacebookLogin();
		} else if (v == forgot_password) {
			Intent i = new Intent(Login.this, ForgotPassword.class);
			startActivity(i);
		} else if (v == take_task) {
			// Intent i = new Intent(Login.this , TakeALook.class);
			Intent i = new Intent(Login.this, Home.class);
			startActivity(i);
		}

	}

	private void FacebookLogin() {
		Log.i("fb lgin", "fb login");
		Constants.mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = Constants.mPrefs.getString("access_token", null);
		long expires = Constants.mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);

			Log.d("FB Sessions", "" + facebook.isSessionValid());
			Log.d("1111111", "1111111");

			getProfileInformation();
		}

		if (expires != 0) {
			Log.d("222222", "222222");
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			Log.d("3333333", "3333333");
			facebook.authorize(this, new String[] { "read_stream",
					"user_photos", "email", "user_location", "public_profile",
					"basic_info", "publish_actions" }, new DialogListener() {

				@Override
				public void onCancel() {
					// Function to handle cancel event
				}

				@Override
				public void onComplete(Bundle values) {
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					Editor editor = Constants.mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
					getProfileInformation();

				}

				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub

				}

			});
		}

	}

	/**
	 * to get facebook profile info
	 */

	public void getProfileInformation() {
		Log.e("get profile info===", "get profile info==");
		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					// Facebook Profile JSON data
					JSONObject profile = new JSONObject(json);

					Log.d("profile response", profile + "");
					// getting name of the user
					final String name = profile.getString("name");
					final String id = profile.getString("id");

					final String firstname = profile.getString("first_name");
					final String lastname = profile.getString("last_name");

					// getting email of the user
					final String email = profile.getString("email");

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							/*
							 * Log.e("name==", "" + name); Editor e = sp.edit();
							 * e.putString("name", name);
							 * e.putString("email",name+".facebook.com" );
							 * 
							 * String trimNAME = name.replace(" ", ""); String
							 * email = trimNAME+".facebook.com";
							 * 
							 * e.commit();
							 */
							Toast.makeText(getApplicationContext(),
									"Login successfull.", Toast.LENGTH_LONG)
									.show();

							new LoginTaskFB(name, email, id)
									.execute(new Void[0]);
						}

					});

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}

			@Override
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void callLoginAPI() {
		String username = usernameET.getText().toString().trim();
		String passwrod = passwordET.getText().toString();

		if (username.trim().length() < 1) {
			usernameET.setError("Please enter username.");
		} else if (passwrod.trim().length() < 1) {
			passwordET.setError("Please enter password.");
		} else if (!(StringUtils.verify(username))) {

			usernameET.setError("Please enter valid email address.");

		}

		else {
			if (isConnected) {
				new LoginTask(username, passwrod).execute(new Void[0]);
			} else {
				showDialog(Constants.No_INTERNET);
			}
		}
	}

	public class LoginTask extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String username, pass, email, token_id;

		Double lat, lng;

		public LoginTask(String username_text, String password_text) {
			this.username = username_text;
			this.pass = password_text;
			/*
			 * this.token_id = Secure.getString(getApplicationContext()
			 * .getContentResolver(), Secure.ANDROID_ID);
			 */

			this.token_id = Constants.REGISTRATIO_ID;

			gps = new GPSTracker(Login.this);
			if (gps.canGetLocation()) {
				this.lat = gps.getLatitude();
				this.lng = gps.getLongitude();
			}
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/login.php?
			 * emailId=osvinandroid
			 * 
			 * @gmail.com&password=password123&login_via=email&
			 * latitude=30.900965&longitude=75.857276&device_id=1&token_id=12
			 */

			try {
				localArrayList.add(new BasicNameValuePair("emailId",
						this.username));
				localArrayList
						.add(new BasicNameValuePair("password", this.pass));
				localArrayList.add(new BasicNameValuePair("longitude", String
						.valueOf(this.lng)));
				localArrayList.add(new BasicNameValuePair("latitude", String
						.valueOf(this.lat)));

				localArrayList
						.add(new BasicNameValuePair("login_via", "email"));
				localArrayList.add(new BasicNameValuePair("device_id", "0"));
				localArrayList.add(new BasicNameValuePair("token_id",
						this.token_id));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));

				result = function.login(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {

					Constants.USER_ID = (String) result.get("user_id");
					Constants.EMAIL = (String) result.get("email");
					Constants.TYPE = "mannual";

					Editor e = sp.edit();
					e.putString("email", Constants.EMAIL);
					e.putString("user_id", Constants.USER_ID);
					e.putString("type", Constants.TYPE);
					e.commit();

					Intent i = new Intent(Login.this, Home.class);
					startActivity(i);
				} else if (result.get("ResponseCode").equals("false")) {
					showDialog("Invalid email or password.");
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Login.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	public class LoginTaskFB extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String username, email, token_id, fb_id;

		Double lat, lng;

		public LoginTaskFB(String name, String email2, String id) {
			this.username = name;
			this.email = email2;
			this.fb_id = id;
			/*this.token_id = Secure.getString(getApplicationContext()
					.getContentResolver(), Secure.ANDROID_ID);*/
			
			this.token_id = Constants.REGISTRATIO_ID;
			gps = new GPSTracker(Login.this);
			if (gps.canGetLocation()) {
				this.lat = gps.getLatitude();
				this.lng = gps.getLongitude();

			}
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/login .php?
			 * emailId=osvinandroid@gmail.com&login_via=fb &
			 * fb_id=12345&latitude=30.900965&longitude=75.857276
			 * &device_id=1&token_id=15222
			 */

			try {
				localArrayList.add(new BasicNameValuePair("username",
						this.username));
				localArrayList
						.add(new BasicNameValuePair("emailId", this.email));
				localArrayList.add(new BasicNameValuePair("login_via", "fb"));
				localArrayList.add(new BasicNameValuePair("longitude", String
						.valueOf(this.lng)));
				localArrayList.add(new BasicNameValuePair("latitude", String
						.valueOf(this.lat)));

				localArrayList.add(new BasicNameValuePair("fb_id", this.fb_id));
				localArrayList.add(new BasicNameValuePair("device_id", "0"));
				localArrayList.add(new BasicNameValuePair("token_id",
						this.token_id));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));

				result = function.login(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {

					Constants.USER_ID = (String) result.get("user_id");
					Constants.EMAIL = (String) result.get("email");
					Constants.TYPE = "facebook";

					Editor e = sp.edit();
					e.putString("email", Constants.EMAIL);
					e.putString("user_id", Constants.USER_ID);
					e.putString("type", Constants.TYPE);
					e.commit();

					Intent i = new Intent(Login.this, Home.class);
					startActivity(i);

				} else if (result.get("ResponseCode").equals("false")) {
					new RegisterTaskFB(this.username, this.email,
							this.token_id, this.fb_id).execute(new Void[0]);
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Login.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	public class RegisterTaskFB extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String username, email, type, token_id, fb_id;

		double lat, lng;

		public RegisterTaskFB(String username2, String email2, String token_id,
				String fb_id) {
			this.username = username2;
			this.email = email2;

			this.fb_id = fb_id;

			/*this.token_id = Secure.getString(getApplicationContext()
					.getContentResolver(), Secure.ANDROID_ID);*/
			
			this.token_id = Constants.REGISTRATIO_ID;
			gps = new GPSTracker(Login.this);
			if (gps.canGetLocation()) {
				this.lat = gps.getLatitude();
				this.lng = gps.getLongitude();

			}
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/signup.php?
			 * emailId=robin.singhkhalsa1988@gmail.com
			 * &fname=robin&lname=singh&password=&city=&state=&country=
			 * &zipcode=&phone=&user_type=&longitude=&latitude=&
			 * is_post_task=&is_run_task=&fb_id=123456&tw_id=
			 */
			try {
				localArrayList.add(new BasicNameValuePair("fname",
						this.username));
				localArrayList.add(new BasicNameValuePair("lname", ""));
				localArrayList
						.add(new BasicNameValuePair("emailId", this.email));
				localArrayList.add(new BasicNameValuePair("password", ""));
				localArrayList.add(new BasicNameValuePair("fb_id", this.fb_id));
				localArrayList.add(new BasicNameValuePair("city", ""));
				localArrayList.add(new BasicNameValuePair("state", ""));
				localArrayList.add(new BasicNameValuePair("country", ""));
				localArrayList.add(new BasicNameValuePair("zipcode", ""));
				localArrayList.add(new BasicNameValuePair("phone", ""));
				localArrayList.add(new BasicNameValuePair("longitude", String
						.valueOf(this.lng)));
				localArrayList.add(new BasicNameValuePair("latitude", String
						.valueOf(this.lat)));

				localArrayList.add(new BasicNameValuePair("s_post_task", "0"));
				localArrayList.add(new BasicNameValuePair("is_run_task", "0"));

				localArrayList.add(new BasicNameValuePair("signup_via", "fb"));

				localArrayList.add(new BasicNameValuePair("device_id", "0"));
				localArrayList.add(new BasicNameValuePair("token_id",
						this.token_id));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));

				result = function.register(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {
					Constants.USER_ID = (String) result.get("user_id");
					Constants.EMAIL = (String) result.get("email");
					Constants.TYPE = "facebook";

					Editor e = sp.edit();
					e.putString("email", Constants.EMAIL);
					e.putString("user_id", Constants.USER_ID);
					e.putString("type", Constants.TYPE);

					e.commit();

					Intent i = new Intent(Login.this, Home.class);
					startActivity(i);

				} else if (result.get("ResponseCode").equals("false")) {
					Toast.makeText(getApplicationContext(),
							"Registration error.", Toast.LENGTH_SHORT).show();
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Login.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	/*protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(Login.this);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					Login.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("Alert !");

			// Setting Dialog Message
			alertDialog.setMessage(msg);

			// Setting Icon to Dialog
			//alertDialog.setIcon(R.drawable.browse);

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

	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface paramAnonymousDialogInterface,
									int paramAnonymousInt) {

								Intent localIntent2 = new Intent(
										"android.settings.LOCATION_SOURCE_SETTINGS");
								startActivity(localIntent2);

							}
						});
		localBuilder.create().show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}
}
