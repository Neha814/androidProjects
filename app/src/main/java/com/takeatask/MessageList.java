package com.takeatask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.imageloader.ImageLoader;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import functions.Constants;
import functions.Functions;
import utils.GPSTracker;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class MessageList extends Activity implements OnClickListener {
	
	ListView listview;
	
	ImageView back;
	
	ImageView search;
	
	ImageView cancel;
	
	LinearLayout back_ll;
	
	boolean isConnected ;
	
	TransparentProgressDialog db;
	
	MyAdapter mAdapter ;
	
	TextView static_textview;
	
	EditText search_edittext;
	
	ImageLoader imageLoader ;
	
	GPSTracker gps;
	
	double lat , lng ;
	
	ArrayList<HashMap<String, String>> message_list = new ArrayList<HashMap<String,String>>();
	
/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(MessageList.this);
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
					MessageList.this).create();

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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.message_list);
		
		imageLoader = new ImageLoader(getApplicationContext());
		isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
		
		listview = (ListView) findViewById(R.id.listview);
		back = (ImageView) findViewById(R.id.back);
		search = (ImageView) findViewById(R.id.search);
		static_textview = (TextView) findViewById(R.id.static_textview);
		search_edittext = (EditText) findViewById(R.id.search_edittext);
		cancel = (ImageView) findViewById(R.id.cancel);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		cancel.setOnClickListener(this);
		back_ll.setOnClickListener(this);
		
		/*if (!((LocationManager) getSystemService("location"))
				.isProviderEnabled("gps")) {
			showGPSDisabledAlertToUser();
		}*/
		
		gps = new GPSTracker(MessageList.this);
		if (gps.canGetLocation()) {
			lat = gps.getLatitude();
			lng = gps.getLongitude();
		}
		
		if (isConnected) {

			new MessageListAPI().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if(position==0){
                    Constants.CHAT_NAME = message_list.get(position).get("fname")
                            + " " + message_list.get(position).get("lname");
                    Constants.CHAT_IMAGE = message_list.get(position).get("profile_pic");
					Intent i = new Intent(MessageList.this, TakeATask_Notification.class);
					startActivity(i);

				} else {

					Constants.RECEIVER_ID = message_list.get(position).get("user_id");
					Constants.CHAT_NAME = message_list.get(position).get("fname")
							+ " " + message_list.get(position).get("lname");
					Constants.CHAT_IMAGE = message_list.get(position).get("profile_pic");
					Constants.SENDER_ID = Constants.USER_ID;

					Intent i = new Intent(MessageList.this, ChatScreen.class);
					startActivity(i);
				}
			}
		});
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Constants.RECEIVER_ID = message_list.get(position).get("user_id");
				Constants.SENDER_ID = Constants.USER_ID;
				
				showDeleteConfirmationDialog("Are you sure you want to delete the whole conversation?");
				return true;
			}
		});
		
		
		search_edittext.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				try {
					String text = search_edittext.getText().toString()
							.toLowerCase(Locale.getDefault());
					mAdapter.filter(text);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*protected void showDeleteConfirmationDialog(String message) {
		final Dialog dialog;
		dialog = new Dialog(MessageList.this);
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
		msg = (TextView) dialog.findViewById(R.id.msg);

		msg.setText(message);

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

				DeleteMessage();
			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}*/
	
	
	
	public void showDeleteConfirmationDialog(String message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MessageList.this);
		 
        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");
 
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete the conversation?");
 
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);
 
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
 
            	 dialog.cancel();

				DeleteMessage();
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

	protected void DeleteMessage() {

		if (isConnected) {

			new DeleteChat().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

	}

	@Override
	public void onClick(View v) {
	if(v==back || v==back_ll){
		Intent i = new Intent(MessageList.this , Home.class);
		startActivity(i);
	}else if(v==search){
		static_textview.setVisibility(View.GONE);
		search_edittext.setVisibility(View.VISIBLE);
		search.setVisibility(View.GONE);
		cancel.setVisibility(View.VISIBLE);
	} else if(v==cancel){
		static_textview.setVisibility(View.VISIBLE);
		search_edittext.setVisibility(View.GONE);
		search.setVisibility(View.VISIBLE);
		cancel.setVisibility(View.GONE);
		
		search_edittext.setText("");
		String text = "";
		mAdapter.filter(text);
	}
		
	}
	
	public class MessageListAPI extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();
		
		

		protected Void doInBackground(Void... paramVarArgs) {
			
			
		/*	http://phphosting.osvin.net/TakeATask/WEB_API/listUsers.php?
				authkey=Auth_TakeATask2015&user_id=62&latitude=30.7237234&longitude=76.84731*/
				
			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("latitude",
						String.valueOf(lat)));		
				localArrayList.add(new BasicNameValuePair("longitude",
						String.valueOf(lng)));
			
				result = function.MessageList(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					message_list.clear();
					message_list.addAll(result);
					mAdapter = new MyAdapter(getApplicationContext(),
							message_list);
					listview.setAdapter(mAdapter);
				} else {
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
				db = new TransparentProgressDialog(
						MessageList.this, R.drawable.loadingicon);
				db.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
		/**
		 * ************** delete user **************
		 */
		
		public class DeleteChat extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();

			HashMap result = new HashMap();

			ArrayList localArrayList = new ArrayList();

			public DeleteChat() {

			}

			protected Void doInBackground(Void... paramVarArgs) {

				// http://phphosting.osvin.net/TakeATask/WEB_API/deleteMessage.php?authkey=Auth_TakeATask2015&from_id=43&to_id=44

				try {
					localArrayList.add(new BasicNameValuePair("authkey",
							"Auth_TakeATask2015"));
					localArrayList.add(new BasicNameValuePair("from_id",
							Constants.SENDER_ID));
					localArrayList.add(new BasicNameValuePair("to_id",
							Constants.RECEIVER_ID));

					result = function.deleteMessage(localArrayList);

				} catch (Exception localException) {
					localException.printStackTrace();
				}

				return null;
			}

			protected void onPostExecute(Void paramVoid) {
				db.dismiss();

				try {
					if (result.get("ResponseCode").equals("true")) {

						Toast.makeText(getApplicationContext(), "Message deleted successfully.", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(MessageList.this , MessageList.class);
						startActivity(i);
						finish();
					} else if (result.get("ResponseCode").equals("false")) {

						Toast.makeText(getApplicationContext(), "Something went wrong.Please try again after some time .", Toast.LENGTH_SHORT).show();

					}
				}

				catch (Exception ae) {
					showDialog(Constants.ERROR_MSG);
				}

			}

			protected void onPreExecute() {
				super.onPreExecute();
				db = new TransparentProgressDialog(MessageList.this,
						R.drawable.loadingicon);
				db.show();
			}

		}

	
	
	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;
		
		private ArrayList<HashMap<String, String>> mDisplayedValues;

		public MyAdapter(Context context,
				ArrayList<HashMap<String, String>> categoryList) {
			mInflater = LayoutInflater.from(getApplicationContext());
			
			mDisplayedValues = new ArrayList<HashMap<String, String>>();
			mDisplayedValues.addAll(message_list);
		}

		public void filter(String charText) {
			charText = charText.toLowerCase(Locale.getDefault());

			message_list.clear();
			if (charText.length() == 0) {

				message_list.addAll(mDisplayedValues);
			} else {

				for (int i = 0 ;i<mDisplayedValues.size();i++) {

					if (mDisplayedValues.get(i).get("fname").toLowerCase(Locale.getDefault())
							.startsWith(charText)) {

					
						message_list.add(mDisplayedValues.get(i));

					}
				}
			}
			notifyDataSetChanged();
			
		}

		@Override
		public int getCount() {

			return message_list.size();
		}

		@Override
		public Object getItem(int position) {
			return message_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.message_list_item, null);

				holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
				holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
				holder.message_count = (TextView) convertView.findViewById(R.id.message_count);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final String url = message_list.get(position).get("profile_pic");
			holder.user_name.setText(message_list.get(position).get("fname")+" "+message_list.get(position).get("lname"));
			imageLoader.DisplayImage(url, R.drawable.default_pic, holder.user_image);

			/*Thread t = new Thread(){
				public void run(){
					final Bitmap bitmapToGetFromURL = getBitmapFromURL(url);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							*//*croppedBitmap(bitmapToGetFromURL);*//*




								Bitmap cropedBitmap = null;
								ScalingUtilities mScalingUtilities = new ScalingUtilities();
								Bitmap mBitmap = null;
								if (bitmapToGetFromURL != null)

								{

									int[] size = getImageHeightAndWidthForProFileImageHomsecreen(MessageList.this);
									cropedBitmap = mScalingUtilities
											.createScaledBitmap(bitmapToGetFromURL, size[1],
													size[0], ScalingUtilities.ScalingLogic.CROP);
									//takenImage2.recycle();
									mBitmap = mScalingUtilities.getCircleBitmap(
											cropedBitmap, 1);
									//cropedBitmap.recycle();



									holder.user_image.setImageBitmap(mBitmap);

								}

						}
					});

				}
			};t.start();*/
		
			String messageCount = message_list.get(position).get("messageCount");
			int msgCOUNT = Integer.parseInt(messageCount);
			
			if(msgCOUNT>0){
				holder.message_count.setVisibility(View.VISIBLE);
				holder.message_count.setText(""+msgCOUNT);
			} else {
				holder.message_count.setVisibility(View.GONE);
				holder.message_count.setText(""+msgCOUNT);
			}
			return convertView;
		}

		class ViewHolder {
			TextView user_name ,message_count;
			
			ImageView user_image;

		}

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		Intent i = new Intent(MessageList.this , Home.class);
		startActivity(i);
		finish();
}



	public static Bitmap getBitmapFromURL(final String src) {


		try

		{
			URL url = null;
			try {
				url = new URL(src);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		}

		catch(
				IOException e
				)

		{
			e.printStackTrace();
			return null;
		}

	}


	//imageLayoutHeightandWidth
	public int[] getImageHeightAndWidthForProFileImageHomsecreen(
			Activity activity) {
		// //Log.i(TAG, "getImageHeightAndWidth");

		int imageHeightAndWidth[] = new int[2];
		int screenHeight = getHeight(activity);
		int screenWidth = getWidth(activity);
		// //Log.i(TAG, "getImageHeightAndWidth  screenHeight "+screenHeight);
		// //Log.i(TAG, "getImageHeightAndWidth  screenWidth  "+screenWidth);
		int imagehiegth;
		int imagewidth;
		if ((screenHeight <= 500 && screenHeight >= 480)
				&& (screenWidth <= 340 && screenWidth >= 300)) {
			// //Log.i(TAG, "getImageHeightAndWidth mdpi");
			imagehiegth = 100;
			imagewidth = 100;
			imageHeightAndWidth[0] = imagehiegth;
			imageHeightAndWidth[1] = imagewidth;

		}

		else if ((screenHeight <= 400 && screenHeight >= 300)
				&& (screenWidth <= 240 && screenWidth >= 220))

		{

			// //Log.i(TAG, "getImageHeightAndWidth ldpi");
			imagehiegth = 120;
			imagewidth = 120;
			imageHeightAndWidth[0] = imagehiegth;
			imageHeightAndWidth[1] = imagewidth;
		}

		else if ((screenHeight <= 840 && screenHeight >= 780)
				&& (screenWidth <= 500 && screenWidth >= 440)) {

			// //Log.i(TAG, "getImageHeightAndWidth hdpi");
			imagehiegth = 150;
			imagewidth = 150;
			imageHeightAndWidth[0] = imagehiegth;
			imageHeightAndWidth[1] = imagewidth;
		} else if ((screenHeight <= 1280 && screenHeight >= 840)
				&& (screenWidth <= 720 && screenWidth >= 500)) {

			// //Log.i(TAG, "getImageHeightAndWidth xdpi");
			imagehiegth = 200;
			imagewidth = 200;
			imageHeightAndWidth[0] = imagehiegth;
			imageHeightAndWidth[1] = imagewidth;
		} else {
			imagehiegth = 200;
			imagewidth = 200;
			imageHeightAndWidth[0] = imagehiegth;
			imageHeightAndWidth[1] = imagewidth;
		}

		return imageHeightAndWidth;
	}

	@SuppressLint("NewApi")
	public static int getWidth(Context mContext) {
		int width = 0;
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT > 12) {
			Point size = new Point();
			display.getSize(size);
			width = size.x;
		} else {
			width = display.getWidth(); // deprecated
		}
		return width;
	}

	@SuppressLint("NewApi")
	public static int getHeight(Context mContext) {
		int height = 0;
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT > 12) {
			Point size = new Point();
			display.getSize(size);
			height = size.y;
		} else {
			height = display.getHeight(); // deprecated
		}
		return height;
	}
}
