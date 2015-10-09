package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class AlertNotification extends Activity implements OnClickListener {

	ImageView notification_on_off;

	ImageView back;
	
	LinearLayout back_ll;

	boolean isConnected;

	TransparentProgressDialog db;

	String isTurnedOn = "";
	
	String notificationvALUEtOsET = "";

	/*protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(AlertNotification.this);
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
					AlertNotification.this).create();

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.alert_notification);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		notification_on_off = (ImageView) findViewById(R.id.notification_on_off);
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		
		back_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		notification_on_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showConfirmationDialog();

				

				if (isConnected) {

					if (isTurnedOn.equals("1")) {
						notificationvALUEtOsET = "0";
					} else if (isTurnedOn.equals("0")) {
						notificationvALUEtOsET = "1";
					}
					new setNotifications(notificationvALUEtOsET)
							.execute(new Void[0]);
				} else {
					showDialog(Constants.No_INTERNET);
				}
			}
		});

		if (isConnected) {
			new IsNotificationEnabled().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == back) {
			finish();
		}
	}

	public class IsNotificationEnabled extends AsyncTask<Void, Void, Void> {

		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/setNotificationOnOff
			 * .php? status=1&is_set=0&user_id=39&authkey=Auth_TakeATask2015
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						Constants.AUTH_KEY));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("status", ""));
				localArrayList.add(new BasicNameValuePair("is_set", "0"));

				result = function.isNotificationEnable(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {

				String status = (String) result.get("status");

				isTurnedOn = status;
				if (status.equals("1")) {
					notification_on_off.setImageResource(R.drawable.active);

				} else if (status.equals("0")) {
					notification_on_off.setImageResource(R.drawable.non_active);

				}
				

				
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
				ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(AlertNotification.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	public class setNotifications extends AsyncTask<Void, Void, Void> {

		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();
		
		String valueToSet ;

		public setNotifications(String notificationvALUEtOsET) {
			this.valueToSet = notificationvALUEtOsET ;
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/setNotificationOnOff
			 * .php? status=1&is_set=0&user_id=39&authkey=Auth_TakeATask2015
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						Constants.AUTH_KEY));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("status", this.valueToSet));
				localArrayList.add(new BasicNameValuePair("is_set", "1"));

				result = function.setNotifications(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {

				String status = (String) result.get("ResponseCode");

				if (status.equals("true")) {
					
					if(notificationvALUEtOsET.equals("1")){
					notification_on_off.setImageResource(R.drawable.active);
					} else if(notificationvALUEtOsET.equals("0")){
						notification_on_off.setImageResource(R.drawable.non_active);
					}

				} else if (status.equals("false")) {
					

				}
				
				if (isConnected) {
					new IsNotificationEnabled().execute(new Void[0]);
				} else {
					showDialog(Constants.No_INTERNET);
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
				ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(AlertNotification.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	protected void showConfirmationDialog() {
		final Dialog dialog;
		dialog = new Dialog(AlertNotification.this);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(0);
		dialog.getWindow().setBackgroundDrawable(d);

		Button yes, no;

		TextView msg;

		dialog.setContentView(R.layout.logout);
		yes = (Button) dialog.findViewById(R.id.yes);
		no = (Button) dialog.findViewById(R.id.no);
		msg = (Button) dialog.findViewById(R.id.msg);

		if (isTurnedOn.equals("1")) {
			msg.setText("Are you sure wait to turn off notifications ?");
		} else if (isTurnedOn.equals("0")) {
			msg.setText("Are you sure wait to turn on notifications ?");
		}

		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();
	}
}
