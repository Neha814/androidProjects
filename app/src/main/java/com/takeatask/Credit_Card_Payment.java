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
import utils.TransparentProgressDialog;

public class Credit_Card_Payment extends Activity implements OnClickListener {

	ImageView back;

	Button save;
	
	LinearLayout back_ll;

	EditText firstname, lastname, card_no, expiry_date, cvv_no;

	static Boolean isValidDate, isConnected ,isSuccess = false;

	String firstname_text;
	String lastname_text;
	String card_no_text;
	String expiry_date_text;
	String cvv_no_text;
	
	TransparentProgressDialog db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.credit_card_payment);

		back = (ImageView) findViewById(R.id.back);
		save = (Button) findViewById(R.id.save);
		firstname = (EditText) findViewById(R.id.firstname);
		lastname = (EditText) findViewById(R.id.lastname);
		card_no = (EditText) findViewById(R.id.card_no);
		expiry_date = (EditText) findViewById(R.id.expiry_date);
		cvv_no = (EditText) findViewById(R.id.cvv_no);
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
			ValidateAndSaveCardDetails();
		}
	}

	private void ValidateAndSaveCardDetails() {

		firstname_text = firstname.getText().toString().trim();
		lastname_text = lastname.getText().toString().trim();
		card_no_text = card_no.getText().toString().trim();
		expiry_date_text = expiry_date.getText().toString().trim();
		cvv_no_text = cvv_no.getText().toString().trim();

		isValidDate = expiry_date_text.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");

		if (firstname_text.length() < 1) {
			firstname.setError("Please enter firstname.");
		} else if (lastname_text.length() < 1) {
			lastname.setError("Please enter lastname.");
		} else if (card_no_text.length() < 16) {
			card_no.setError("Please enter 16 digit card number.");
		} else if (expiry_date_text.length() < 1) {
			expiry_date.setError("Please enter expiry date.");
		} else if (!isValidDate) {
			expiry_date.setError("Please enter valid date.");
		} else if (cvv_no_text.length() < 3) {
			cvv_no.setError("Please enter valid CVV number.");
		} else {
			CallApiToSaveData();
		}

	}

	private void CallApiToSaveData() {
		if (isConnected) {
			new CreditCardDetails(firstname_text, lastname_text, card_no_text,
					expiry_date_text, cvv_no_text).execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}

/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(Credit_Card_Payment.this);
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
					Credit_Card_Payment.this).create();

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
	
	public class CreditCardDetails extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String firstName, lastName, cardNo, expiryDate, cVV;

		public CreditCardDetails(String firstname_text, String lastname_text,
				String card_no_text, String expiry_date_text, String cvv_no_text) {
		
			this.firstName = firstname_text;
			this.lastName = lastname_text;
			this.cardNo = card_no_text;
			this.expiryDate = expiry_date_text;
			this.cVV = cvv_no_text;
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*http://phphosting.osvin.net/TakeATask/WEB_API/addUserPaymentAccount.php?
				authkey=Auth_TakeATask2015&user_id=38&account_type=0&fname=robin
				&lname=singh&cardNum=123456&expiryDate=2025-10-12&cvvNumber=5252&paypal_id=robin@osvin.biz*/

			try {
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("user_id",Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("account_type","0"));
				localArrayList.add(new BasicNameValuePair("fname",this.firstName));
				localArrayList.add(new BasicNameValuePair("lname",this.lastName));
				localArrayList.add(new BasicNameValuePair("cardNum",this.cardNo));
				localArrayList.add(new BasicNameValuePair("expiryDate",this.expiryDate));
				localArrayList.add(new BasicNameValuePair("cvvNumber",this.cVV));
				localArrayList.add(new BasicNameValuePair("paypal_id",""));
			
				
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
					showDialog("Your Account details is added successfully.");
			
				} else if (result.get("ResponseCode").equals("false")) {
					showDialog("Something went wrong while saving your bank details. Please try again after some time.");
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Credit_Card_Payment.this,
					R.drawable.loadingicon);
			db.show();
		}

	}
}
