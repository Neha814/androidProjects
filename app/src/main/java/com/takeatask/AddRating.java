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
import android.widget.RatingBar;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class AddRating extends Activity {

	RatingBar ratingBar1;
	EditText message;
	Button rate;
	ImageView back;
	boolean isConnected;

	LinearLayout back_ll;

	TransparentProgressDialog db;

	boolean isSuccess = false ;

/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(AddRating.this);
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

			e.printStackTrace();
		}

	}*/
	
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					AddRating.this).create();

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
                    if(isSuccess){
                        Intent i = new Intent(AddRating.this , Home.class);
                        startActivity(i);
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

		setContentView(R.layout.add_rating);
		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
		message = (EditText) findViewById(R.id.message);
		rate = (Button) findViewById(R.id.rate);
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		back_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		rate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float rateValue = ratingBar1.getRating();
				String messageText = message.getText().toString();

				if (isConnected) {
					new GiveRating(String.valueOf(rateValue), messageText)
							.execute(new Void[0]);
				} else {
					showDialog(Constants.No_INTERNET);
				}
			}
		});
	}

	public class GiveRating extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap<String, String> result = new HashMap<String, String>();

		ArrayList localArrayList = new ArrayList();

		String rateVALUE, REVIEW;

		public GiveRating(String valueOf, String messageText) {
			this.rateVALUE = valueOf;
			this.REVIEW = messageText;
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/addRating.php?
			 * authkey
			 * =Auth_TakeATask2015&from_id=19&to_id=12&ratings=4&review=test
			 * %20ratings
			 */

			// TASK_DETAIL_USERID

			try {

				localArrayList.add(new BasicNameValuePair("authkey",
						Constants.AUTH_KEY));
				localArrayList.add(new BasicNameValuePair("from_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("to_id",
						Constants.TASK_DETAIL_USERID));
				localArrayList.add(new BasicNameValuePair("ratings",
						this.rateVALUE));
				localArrayList.add(new BasicNameValuePair("task_id",
                        Constants.REVIEW_TASK_ID));
				localArrayList
						.add(new BasicNameValuePair("review", this.REVIEW));

				result = function.giveRate(localArrayList);

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

					showDialog("Rating added successfully.");

				} else if (result.get("ResponseCode").equals("false")) {

					String msg = (String) result.get("Message");
					Log.e("msg===>>", "" + msg);
					showDialog(msg);
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(AddRating.this,
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
