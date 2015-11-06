package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

/*import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;*/
/*import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;*/



public class Bidding extends Activity implements OnClickListener{

	ImageView back;

	ListView listview;

	boolean isConnected;
	boolean  isSuccess = false ,nodataFound = false;

	MyAdapter mAdapter;
	
	LinearLayout back_ll;

	TransparentProgressDialog db;

	ArrayList<HashMap<String, String>> biddingList = new ArrayList<HashMap<String, String>>();

	String ACCEPT_PRICE;
	String ACCEPT_MESSAGE;
    String ACCEPT_TO_ID;
    String ACCEPT_FROM_ID;
    String ACCEPT_TASK_ID;
    String ACCEPT_REQ_ID;


	private AsyncHttpClient client = new AsyncHttpClient();


    /**
     * BrainTree
     */

    private static final String SERVER_BASE = "http://tim.ngrok.com";
    private static final int REQUEST_CODE = Menu.FIRST;

    private String clientToken;
   // private String clientToken = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJjMTBmNmFkYjUyYWExMjY2YWZmNjMyOWQwY2RjZDRhYWQ3MGZhZTBmYzA4ZTNiZGY1YTllNTU4ZTJiMzZlZDFmfGNyZWF0ZWRfYXQ9MjAxNS0xMC0wN1QwNzo0Mzo0MS40Nzc2MDc3MjMrMDAwMFx1MDAyNm1lcmNoYW50X2lkPWIycTNwbnpmYnd5c2I1czVcdTAwMjZwdWJsaWNfa2V5PTdybmYzd3h5N3E2OWpodG0iLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvYjJxM3BuemZid3lzYjVzNS9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL2IycTNwbnpmYnd5c2I1czUvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIn0sInRocmVlRFNlY3VyZUVuYWJsZWQiOmZhbHNlLCJwYXlwYWxFbmFibGVkIjp0cnVlLCJwYXlwYWwiOnsiZGlzcGxheU5hbWUiOiJPc3ZpbiIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjpudWxsLCJtZXJjaGFudEFjY291bnRJZCI6Im9zdmluIiwiY3VycmVuY3lJc29Db2RlIjoiVVNEIn0sImNvaW5iYXNlRW5hYmxlZCI6ZmFsc2UsIm1lcmNoYW50SWQiOiJiMnEzcG56ZmJ3eXNiNXM1IiwidmVubW8iOiJvZmYifQ==";
	/**
	 * PayPal variables
	 * 
	 * @param msg
	 */

	/*PayPalPayment thingToBuy;

	private static final String TAG = "paymentExample";

	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "AUcUg5oQHvPZyc6sYU4yZj9FDYYxSzQC0zy2dkC1NsUpbUJgujKvzHy9MrtEirI3XjBdWTct4iC_z4Yv";

	private static final int REQUEST_CODE_PAYMENT = 1;

	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Hipster Store")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));*/

/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(Bidding.this);
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
					} else if (nodataFound) {
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
					Bidding.this).create();

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
					/*if (isSuccess) {
						 
						finish();
					}*/ 
					
					 if (nodataFound) {
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

		super.onCreate(savedInstanceState);

		setContentView(R.layout.bidding);




		back = (ImageView) findViewById(R.id.back);
		listview = (ListView) findViewById(R.id.listview);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		/*Intent intent = new Intent(Bidding.this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);*/

        getClientToken();

		/*if (isConnected) {

			new getBiddingList().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}*/

		back.setOnClickListener(this);
        back_ll.setOnClickListener(this);
	}



    @Override
	public void onClick(View v) {
		if (v == back || v==back_ll) {
			finish();
		}

	}

	public class getBiddingList extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/getTask.php?
			 * authkey=Auth_TakeATask2015&task_id=12&is_list=0
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("task_id",
						Constants.TASK_ID_TO_GET_BIDDING_LIST));
				localArrayList.add(new BasicNameValuePair("is_list", "0"));

				result = function.biddingTask(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {

					biddingList.addAll(result);
					mAdapter = new MyAdapter(getApplicationContext(),
							biddingList);
					listview.setAdapter(mAdapter);



				} else {
					nodataFound = true;
					showDialog("No Data found.");
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			try {
				db = new TransparentProgressDialog(Bidding.this,
						R.drawable.loadingicon);
				db.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	//http://phphosting.osvin.net/TakeATask/WEB_API/genrateBraintreeClientToken.php

	private void getClientToken() {

        try {

            client.get("https://takeataskservices.com/WEB_API/genrateBraintreeClientToken.php", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    clientToken = content;
				/*Toast.makeText(getApplicationContext(),
						clientToken, Toast.LENGTH_SHORT)
						.show();*/
                    Log.e("client id=", "" + clientToken);

                    new getBiddingList().execute(new Void[0]);

                }
            });
        } catch(Exception e){
            e.printStackTrace();
            new getBiddingList().execute(new Void[0]);
        }
	}


	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public MyAdapter(Context context,
				ArrayList<HashMap<String, String>> categoryList) {
			mInflater = LayoutInflater.from(getApplicationContext());
		}

		@Override
		public int getCount() {

			return biddingList.size();
		}

		@Override
		public Object getItem(int position) {
			return biddingList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater
						.inflate(R.layout.bidding_listitem, null);

				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.accept = (Button) convertView.findViewById(R.id.accept);
				holder.decline = (Button) convertView.findViewById(R.id.decline);
				holder.message = (TextView) convertView.findViewById(R.id.message);
				holder.chat = (Button) convertView.findViewById(R.id.chat);
				holder.name1 = (TextView) convertView.findViewById(R.id.name1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.accept.setTag(position);
			holder.decline.setTag(position);
			holder.name.setTag(position);
			holder.chat.setTag(position);

			holder.price
					.setText(" applied for the task with price $ " + biddingList.get(position).get("price"));
			
			
			SpannableString content1 = new SpannableString(biddingList.get(position).get("fname"));
			content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
			holder.name.setText(content1);

            holder.name1.setText(biddingList.get(position).get("fname"));

//			holder.name.setText(biddingList.get(position).get("fname"));

			holder.message.setText("Message : "
					+ biddingList.get(position).get("message"));
			
			holder.name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = (Integer) v.getTag();
					Constants.VIEW_PROFILE_ID = biddingList.get(pos).get(
							"user_id");

					Intent i = new Intent(Bidding.this, ViewProfile.class);
					startActivity(i);
				}
			});

			holder.chat.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					int pos = (Integer) view.getTag();

					Constants.RECEIVER_ID = biddingList.get(pos).get("user_id");
					Constants.CHAT_NAME = biddingList.get(pos).get("fname") + " "
							+ biddingList.get(pos).get("lname");
					Constants.CHAT_IMAGE = biddingList.get(pos).get("profile_pic");
					Constants.SENDER_ID = Constants.USER_ID;

					Intent i = new Intent(Bidding.this, ChatScreen.class);
					startActivity(i);
				}
			});

			holder.accept.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Integer pos = (Integer) v.getTag();

					ACCEPT_PRICE = biddingList.get(pos).get("price");
					ACCEPT_MESSAGE = biddingList.get(pos).get("message");
                    ACCEPT_TO_ID = biddingList.get(pos).get("user_id");
                    ACCEPT_FROM_ID = Constants.USER_ID;
                    ACCEPT_TASK_ID = biddingList.get(pos).get("task_id");
					ACCEPT_REQ_ID = biddingList.get(pos).get("request_id");
					AcceptBid();
				}
			});

			holder.decline.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Integer pos = (Integer) v.getTag();

					String re_id = biddingList.get(pos).get("request_id");
					DeclineBid(re_id);
				}
			});

			return convertView;
		}

		protected void AcceptBid() {

            goToPayment();

		}

		public void DeclineBid(final String reqID) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(Bidding.this);

			// Setting Dialog Title
			alertDialog.setTitle("Exit...");

			// Setting Dialog Message
			alertDialog.setMessage("Are you sure you want to Decline this Task ?");

			// Setting Icon to Dialog
			//alertDialog.setIcon(R.drawable.alarm);

			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {

					dialog.cancel();

                    String re_id = reqID;

                    if (isConnected) {

                        new DeclineBid(re_id).execute(new Void[0]);
                    } else {
                        showDialog(Constants.No_INTERNET);
                    }


				}
			});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to invoke NO event

					dialog.cancel();
				}
			});

			// Showing Alert Message
			alertDialog.show();
		}



		class ViewHolder {
			TextView name, name1 ,price, message;

			Button accept, decline;

			Button chat;

		}

	}



    private void goToPayment() {

        Customization customization = new Customization.CustomizationBuilder()
                .primaryDescription("Awesome payment")
                .secondaryDescription("Using the Client SDK")
                .amount("$1.00")
                .submitButtonText("Pay")
                .build();

        Intent intent = new Intent(this, BraintreePaymentActivity.class);
     // intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, clientToken);
        startActivityForResult(intent, REQUEST_CODE);
    }





    public class AcceptBid extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String ReqID;

		public AcceptBid(String req_id) {

			this.ReqID = req_id;

		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/acceptRequest.php?
			 * authkey=Auth_TakeATask2015&request_id=6&is_accepted=1
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("request_id",
						this.ReqID));
				localArrayList.add(new BasicNameValuePair("is_accepted", "1"));

				result = function.AcceptBid(localArrayList);

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
					Toast.makeText(getApplicationContext(),
							"Task Accepted Successfully..", Toast.LENGTH_SHORT)
							.show();

                    Intent i = new Intent(Bidding.this , Home.class);
                    startActivity(i);
				} else if (result.get("ResponseCode").equals("false")) {
					isSuccess = false;

					Toast.makeText(getApplicationContext(),
							"Something went wrong.", Toast.LENGTH_SHORT).show();

				}

				
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Bidding.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	public class DeclineBid extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String ReqID;

		public DeclineBid(String req_id) {

			this.ReqID = req_id;

		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/acceptRequest.php?
			 * authkey=Auth_TakeATask2015&request_id=6&is_accepted=1
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("request_id",
						this.ReqID));
				localArrayList.add(new BasicNameValuePair("is_accepted", "0"));

				result = function.AcceptBid(localArrayList);

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

					String msg = (String) result.get("Message");
					Toast.makeText(getApplicationContext(),"Task Declined successfully.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Bidding.this , Home.class);
                    startActivity(i);
				} else if (result.get("ResponseCode").equals("false")) {
					/*String msg = (String) result.get("Message");
					showDialog(msg);*/
					Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_SHORT).show();
					Intent i = new Intent(Bidding.this , Home.class);
					startActivity(i);
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Bidding.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	/*public void goToPayPal() {
		thingToBuy = new PayPalPayment(new BigDecimal(ACCEPT_PRICE), "USD",
				ACCEPT_MESSAGE, PayPalPayment.PAYMENT_INTENT_SALE);
		Intent intent = new Intent(Bidding.this, PaymentActivity.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		startActivityForResult(intent, REQUEST_CODE_PAYMENT);

	}*/

	/*public void onFuturePaymentPressed(View pressed) {
		Intent intent = new Intent(Bidding.this,
				PayPalFuturePaymentActivity.class);

		startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
	}*/

	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				Log.e("result ok", "result ok");

				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				Log.e("confirm==", "" + confirm);
				if (confirm != null) {
					try {
						Log.e(TAG, confirm.toJSONObject().toString(4));
						Log.e(TAG, confirm.getPayment().toJSONObject()
								.toString(4));

						Log.i("JSON==", "" + confirm.toJSONObject().toString());

						Toast.makeText(
								Bidding.this,
								"Payment Confirmation info received from PayPal",
								Toast.LENGTH_LONG).show();
						JSONObject jobj = new JSONObject();
						jobj = confirm.toJSONObject();

						JSONObject respose = jobj.getJSONObject("response");
						String transaction_id = respose.getString("id");
						String response_type = jobj.getString("response_type");

					} catch (JSONException e) {
						Log.e(TAG, "an extremely unlikely failure occurred: ",
								e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
		} else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PayPalAuthorization auth = data
						.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.i("FuturePaymentExample", auth.toJSONObject()
								.toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("FuturePaymentExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(Bidding.this,
								"Future Payment code received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e("FuturePaymentExample",
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("FuturePaymentExample", "The user canceled.");
			} else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("FuturePaymentExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
		}
	}*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            Log.e("requestCode==>>", "" + requestCode);
            Log.e("resultCode==>>", "" + resultCode);
            Log.e("data==>>", "" + data);
            Log.e("nonce==>>", "" + data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE));

            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);

          /*  RequestParams requestParams = new RequestParams();
            requestParams.put("payment_method_nonce", paymentMethodNonce);
            requestParams.put("amount", "10.00");*/

                //Toast.makeText(getApplicationContext(), paymentMethodNonce, Toast.LENGTH_LONG).show();
                Log.e("data==>>", "" + data);

           /* client.post(SERVER_BASE + "/payment", requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    Toast.makeText(Bidding.this, content, Toast.LENGTH_LONG).show();
                }
            });*/

                if (isConnected) {

                    new SendNonceToBackend(paymentMethodNonce, ACCEPT_FROM_ID, ACCEPT_TO_ID,
                            ACCEPT_PRICE, ACCEPT_TASK_ID).execute(new Void[0]);
                } else {
                    showDialog(Constants.No_INTERNET);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            Toast.makeText(Bidding.this,"Something went wrong.Please try again." , Toast.LENGTH_LONG).show();
        }
    }



 //******************************** sendNonceToBAckend ****************************************//

    public class SendNonceToBackend extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();

        String NONCE, TO_ID, FROM_ID, PRICE, TASK_ID;



        public SendNonceToBackend(String paymentMethodNonce, String accept_from_id, String accept_to_id, String accept_price, String accept_task_id) {

       this.NONCE = paymentMethodNonce;
            this.TO_ID = accept_to_id;
            this.FROM_ID = accept_from_id;
            this.PRICE = accept_price;
            this.TASK_ID = accept_task_id;
        }

        protected Void doInBackground(Void... paramVarArgs) {

            /*http://phphosting.osvin.net/TakeATask/WEB_API/addTransaction.php?
            authkey=Auth_TakeATask2015&task_id=6&amount=100&from_id=38&to_id=39&
            payment_method_nonce=fake-valid-mastercard-nonce*/

            try {
                localArrayList.add(new BasicNameValuePair("authkey",
                        "Auth_TakeATask2015"));
                localArrayList.add(new BasicNameValuePair("payment_method_nonce",
                        this.NONCE));
                localArrayList.add(new BasicNameValuePair("task_id", this.TASK_ID));
                localArrayList.add(new BasicNameValuePair("from_id", this.FROM_ID));
                localArrayList.add(new BasicNameValuePair("to_id", this.TO_ID));
                localArrayList.add(new BasicNameValuePair("amount", this.PRICE));


                result = function.sendNOnce(localArrayList);

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
                    Toast.makeText(getApplicationContext(),
                            "Payment Successful.", Toast.LENGTH_SHORT)
                            .show();

				new AcceptBid(ACCEPT_REQ_ID).execute(new Void[0]);


                } else if (result.get("ResponseCode").equals("false")) {
                    isSuccess = false;

                    Toast.makeText(getApplicationContext(),
                            "Payment Unsuccessful.Please try again.", Toast.LENGTH_SHORT).show();

                }


            }

            catch (Exception ae) {
                showDialog(Constants.ERROR_MSG);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(Bidding.this,
                    R.drawable.loadingicon);
            db.show();
        }

    }



   /* private void sendAuthorizationToServer(PayPalAuthorization authorization) {

	}

	public void onFuturePaymentPurchasePressed(View pressed) {
		// Get the Application Correlation ID from the SDK
		String correlationId = PayPalConfiguration
				.getApplicationCorrelationId(Bidding.this);

		Log.i("FuturePaymentExample", "Application Correlation ID: "
				+ correlationId);

		// TODO: Send correlationId and transaction details to your server for
		// processing with
		// PayPal...
		Toast.makeText(Bidding.this, "App Correlation ID received from SDK",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDestroy() {
		// Stop service when done
		stopService(new Intent(Bidding.this, PayPalService.class));
		super.onDestroy();
	}*/
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(Bidding.this,BiddingList.class);
		startActivity(i);
	}


}
