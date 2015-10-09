package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import utils.StringUtils;
import utils.TransparentProgressDialog;

public class PayPal_Payment extends Activity implements OnClickListener {

	ImageView back;

	EditText paypal_id, password;

	Button save;
	
	LinearLayout back_ll;

	String payPal_ID = "";
	
	boolean isConnected  ,isSuccess= false;
	
	TransparentProgressDialog db ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.paypal_payment);

		back = (ImageView) findViewById(R.id.back);
		save = (Button) findViewById(R.id.save);
		password = (EditText) findViewById(R.id.password);
		paypal_id = (EditText) findViewById(R.id.paypal_id);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		
		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		back.setOnClickListener(this);
		save.setOnClickListener(this);
		back_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == back || v==back_ll) {
			finish();
		} else if (v == save) {
			validateAndCallAPI();
		}
	}

	private void validateAndCallAPI() {
		payPal_ID = paypal_id.getText().toString().trim();
		
		if(payPal_ID.length()<1){
			paypal_id.setError("Please enter your PayPal id.");
		} else if(!(StringUtils.verify(payPal_ID))){
			paypal_id.setError("Please enter valid PayPal id.");
		} else {
			CallAPiToSaveData();
		}
	}

	private void CallAPiToSaveData() {
		if (isConnected) {
			new PayPalDetails(payPal_ID).execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}
	
/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(PayPal_Payment.this);
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
					if(isSuccess){
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
					PayPal_Payment.this).create();

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
	public class PayPalDetails extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String payPAlID;

		public PayPalDetails(String pay_pal_id) {
		
			this.payPAlID = pay_pal_id;
		
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*http://phphosting.osvin.net/TakeATask/WEB_API/addUserPaymentAccount.php?
				authkey=Auth_TakeATask2015&user_id=38&account_type=1&fname=robin
				&lname=singh&cardNum=123456&expiryDate=2025-10-12&cvvNumber=5252&paypal_id=robin@osvin.biz*/

			try {
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("user_id",Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("account_type","1"));
				localArrayList.add(new BasicNameValuePair("fname",""));
				localArrayList.add(new BasicNameValuePair("lname",""));
				localArrayList.add(new BasicNameValuePair("cardNum",""));
				localArrayList.add(new BasicNameValuePair("expiryDate",""));
				localArrayList.add(new BasicNameValuePair("cvvNumber",""));
				localArrayList.add(new BasicNameValuePair("paypal_id",this.payPAlID));
			
				
				result = function.CreditCard(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {
					
					isSuccess = true ;
					showDialog("Your PayPal Account details is added successfully.");
			
				} else if (result.get("ResponseCode").equals("false")) {
					showDialog("Something went wrong while saving your payPal details. Please try again after some time.");
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(PayPal_Payment.this,
					R.drawable.loadingicon);
			db.show();
		}

	}
}
