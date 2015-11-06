package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

public class TermsAndConditions extends Activity implements OnClickListener{
	
	LinearLayout back_ll;
	ImageView back;
	Boolean isConnected;
	TextView terms_text;
	TransparentProgressDialog db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.terms_conditions);
		
		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());
		
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		terms_text = (TextView) findViewById(R.id.terms_text);

		back.setOnClickListener(this);
		back_ll.setOnClickListener(this);
		
		if (isConnected) {

			new GetTerms().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}

	@Override
	public void onClick(View v) {
		if(v==back || v==back_ll){
			finish();
		}
	}
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					TermsAndConditions.this).create();

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
	
	public class GetTerms extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

	
		
	//	String  ispost, isrun; 

		double lat, lng;

	

		protected Void doInBackground(Void... paramVarArgs) {

		//	http://phphosting.osvin.net/TakeATask/WEB_API/listTermsandconditions.php?authkey=Auth_TakeATask2015
			try {
	
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));

				result = function.termsAndConditions(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {
					
					String msg = (String)result.get("Message");
					
					terms_text.setText(msg);

				} else if (result.get("ResponseCode").equals("false")) {
					String msg = (String)result.get("Message");
					
					terms_text.setText(msg);
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
                ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(TermsAndConditions.this,
					R.drawable.loadingicon);
			db.show();
		}

	}
}
