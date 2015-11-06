package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.StringUtils;
import utils.TransparentProgressDialog;

public class ForgotPassword extends Activity implements OnClickListener {
	
	ImageView back;
	EditText email_id;
	Button done;
	TransparentProgressDialog db;
	boolean isSucess = false;
	
	boolean isConnected;
	
	LinearLayout back_ll;
	
	/*protected void showDialog(String msg) {
		final Dialog dialog;
		dialog = new Dialog(ForgotPassword.this);
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
				if(isSucess){
					Intent i = new Intent(ForgotPassword.this, Login.class);
					startActivity(i);
				}
			}
		});
		dialog.show();

	}*/
	
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					ForgotPassword.this).create();

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
					if(isSucess){
						Intent i = new Intent(ForgotPassword.this, Login.class);
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
		setContentView(R.layout.forgot_password);
		
		isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
		
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		email_id = (EditText) findViewById(R.id.email_id);
		done = (Button) findViewById(R.id.done);
		
		done.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		if(v==done){
		String emailID_text = email_id.getText().toString().trim();
		
		if(!(StringUtils.verify(emailID_text))){
			email_id.setError("Please enter valid email address.");
		
		} else {
			CallForgetPasswordAPI(emailID_text);
		}
		} else if(v==back || v==back_ll){
			Intent i = new Intent(ForgotPassword.this, Login.class);
			startActivity(i);
		}
	}

	private void CallForgetPasswordAPI(String emailID) {
		if (isConnected) {
			new ForgotPaasword(emailID).execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}
	
	
	public class ForgotPaasword extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String email;

		double lat, lng;

		public ForgotPaasword(String email) {

			this.email = email;
		}

		protected Void doInBackground(Void... paramVarArgs) {

		/*	http://phphosting.osvin.net/TakeATask/WEB_API/forgetPassword.php?
				authkey=Auth_TakeATask2015&emailId=osvinandroid@gmail.com*/
			
			
			try {
				localArrayList.add(new BasicNameValuePair("emailId",this.email));
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));

				result = function.forgetPass(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {
					
					isSucess = true;
					
					showDialog((String) result.get("MessageWhatHappen"));

					

				} else if (result.get("ResponseCode").equals("false")) {

					showDialog((String) result.get("MessageWhatHappen"));
					Toast.makeText(getApplicationContext(), "status false.",
							Toast.LENGTH_SHORT).show();
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(ForgotPassword.this,
					R.drawable.loadingicon);
			db.show();
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
