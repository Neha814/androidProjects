package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class ChangePassword extends Activity implements OnClickListener {

	EditText current_password;
	EditText new_password;
	EditText confirm_password;

	Button save;
	
	LinearLayout back_ll;

	ImageView back;

	boolean isConnected;

	boolean isSuccess = false;

	String current_passsword_text;
	String new_password_text;
	String confirm_password_text;

	TransparentProgressDialog db;

/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(ChangePassword.this);
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
					if (isSuccess) {
						finish();
					}

				}
			});
			dialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}*/
	
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					ChangePassword.this).create();

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
					if (isSuccess) {
						finish();
					}

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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.change_password);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		current_password = (EditText) findViewById(R.id.current_password);
		new_password = (EditText) findViewById(R.id.new_password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);
		back = (ImageView) findViewById(R.id.back);
		save = (Button) findViewById(R.id.save);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);

		back.setOnClickListener(this);
		save.setOnClickListener(this);
		back_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == back || v==back_ll) {

			Intent i = new Intent(ChangePassword.this, Settings.class);
			startActivity(i);
		}

		else if (v == save) {
			Validate();
		}
	}

	private void Validate() {
		current_passsword_text = current_password.getText().toString().trim();
		new_password_text = new_password.getText().toString().trim();
		confirm_password_text = confirm_password.getText().toString().trim();

		if (current_passsword_text.length() < 1) {
			current_password.setError("Please enter current password");
		} else if (new_password_text.length() < 1) {
			new_password.setError("Please enter new  password.");
		} else if (confirm_password_text.length() < 1) {
			confirm_password.setError("Please enter confirm password.");
		} else {
			if (confirm_password_text.equals(new_password_text)) {
				CallAPI();
			} else {
				confirm_password
						.setError("Password did not match with new password.");
			}
		}

	}

	private void CallAPI() {
		if (isConnected) {
			new ChangePasswordAPI(current_passsword_text, new_password_text,
					confirm_password_text).execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}

	public class ChangePasswordAPI extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap<String, String> result = new HashMap<String, String>();

		ArrayList localArrayList = new ArrayList();

		String currentPASS, newPASS, confirmPASS;
		Double lat, lng;

		public ChangePasswordAPI(String current_passsword_text,
				String new_password_text, String confirm_password_text) {

			this.currentPASS = current_passsword_text;
			this.newPASS = new_password_text;
			this.confirmPASS = confirm_password_text;
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/changePassword.php?
			 * authkey=Auth_TakeATask2015&emailId=osvinandroid@gmail.com
			 * &oldPassword=password&newPassword=password123
			 */

			try {

				localArrayList.add(new BasicNameValuePair("authkey",
						Constants.AUTH_KEY));
				localArrayList.add(new BasicNameValuePair("emailId",
						Constants.EMAIL));
				localArrayList.add(new BasicNameValuePair("oldPassword",
						this.currentPASS));
				localArrayList.add(new BasicNameValuePair("newPassword",
						this.newPASS));

				result = function.changePassword(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {

					isSuccess = true;
					showDialog("Password changed successfully.");

				} else if (result.get("ResponseCode").equals("false")) {

					String msg = (String) result.get("Message");
					Log.e("msg===>>", "" + msg);
					/*showDialog(msg);*/
					showDialog("Your current password is not correct.");
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(ChangePassword.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}

}
