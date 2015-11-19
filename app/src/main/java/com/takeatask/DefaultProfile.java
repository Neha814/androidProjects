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
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.circularImageview.ScalingUtilities;
import com.imageloader.ImageLoader;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class DefaultProfile extends Activity implements OnClickListener {

	ImageView back;
	ImageView profile_pic;
	TextView name, location, memeber_since, rating, about_me, no_of_reviews ,occupation,language,paypal_id;
	ListView listview;
	RatingBar ratingBar;
	boolean isConnected;
	ImageLoader imageLoader;
	ImageView blurr_img;

	ScrollView scrollview;
	
	TextView review_static;
	
	LinearLayout back_ll;

	MyAdapter mAdapter;
	
	List<String> addList = new ArrayList<String>();

	LinearLayout ll_edit;
	ImageView edit;

	TransparentProgressDialog db;

	/*protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(DefaultProfile.this);
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
					DefaultProfile.this).create();

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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.default_profile);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		imageLoader = new ImageLoader(getApplicationContext());

		back = (ImageView) findViewById(R.id.back);
		profile_pic = (ImageView) findViewById(R.id.profile_pic);
		name = (TextView) findViewById(R.id.name);
		location = (TextView) findViewById(R.id.location);
		memeber_since = (TextView) findViewById(R.id.memeber_since);
		rating = (TextView) findViewById(R.id.rating);
		about_me = (TextView) findViewById(R.id.about_me);
		no_of_reviews = (TextView) findViewById(R.id.no_of_reviews);
		listview = (ListView) findViewById(R.id.listview);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		blurr_img = (ImageView) findViewById(R.id.blurr_img);
		review_static = (TextView) findViewById(R.id.review_static);
		occupation = (TextView) findViewById(R.id.occupation);
		language = (TextView) findViewById(R.id.language);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		edit = (ImageView) findViewById(R.id.edit);
		ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		paypal_id  = (TextView) findViewById(R.id.paypal_id);

		paypal_id.setVisibility(View.GONE);
		
		review_static.setVisibility(View.INVISIBLE);

		back.setOnClickListener(this);
		edit.setOnClickListener(this);
		ll_edit.setOnClickListener(this);

		getProfileCall();

		scrollview.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Log.v("PARENT", "PARENT TOUCH");
				findViewById(R.id.listview).getParent()
						.requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});

		listview.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Log.v("CHILD", "CHILD TOUCH");
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}

	private void getProfileCall() {
		if (isConnected) {
			new getProfile(Constants.USER_ID).execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

		/*if(Constants.LOGIN_TYPE.equalsIgnoreCase("fb")){
			ll_edit.setVisibility(View.GONE);
		} else {
			ll_edit.setVisibility(View.VISIBLE);
		}*/
	}

	@Override
	public void onClick(View v) {
		if (v == back || v==back_ll) {
			Intent i = new Intent(DefaultProfile.this, Home.class);
			startActivity(i);
		} else if(v==edit || v==ll_edit){
			Intent i = new Intent(DefaultProfile.this, Profile.class);
			startActivity(i);
		}
	}

	public class getProfile extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String id;

		public getProfile(String ID) {

			this.id = ID;
		}

		protected Void doInBackground(Void... paramVarArgs) {

			// http://phphosting.osvin.net/TakeATask/WEB_API/getUser.php?authkey=Auth_TakeATask2015&id=55

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						Constants.AUTH_KEY));
				localArrayList.add(new BasicNameValuePair("id", this.id));

				result = function.getProfile(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {

					String name_text = (String) result.get("fname");
					String address_text = (String) result.get("address");
					String city_text = (String) result.get("city");
					String state_text = (String) result.get("state");
					String country_text = (String) result.get("country");
					String ratings_text = (String) result.get("ratings");
					final String profile_text = (String) result.get("profile_pic");

					String background = (String) result.get("background");
					String skills = (String) result.get("skills");
					String occupation = (String) result.get("occupation");
					String lang = (String) result.get("language");
					String member_date = (String) result.get("member_from");

                    String paypalId = (String) result.get("paypal_id");

					name.setText(name_text);
					
					/**
					 * Changes
					 */
					
					addList.clear();
					
					addList.add(address_text);
					addList.add(city_text);
					addList.add(state_text);
					

					addList.removeAll(Arrays.asList("", null));

					String ADDRESS_TEXT = addList.toString().replace("[", "")
							.replace("]", "").replace(", ", ", ");
					
					location.setText("Location : " +ADDRESS_TEXT);

				    if(background.length()>1) {
                        about_me.setText("" + background);
                    } else {
                        about_me.setVisibility(View.GONE);
                    }
					rating.setText("Rating : " + ratings_text);

                    paypal_id.setText("PayPal ID : "+paypalId);
					
					memeber_since.setText("Member Since : " + member_date);
					
					DefaultProfile.this.occupation.setText("Occupation : " + occupation);
					DefaultProfile.this.language.setText("Language : " + lang);

					Log.e("profile_text===>>>", "" + profile_text);

					/*imageLoader.DisplayImage(profile_text, R.drawable.noimg,
							profile_pic);
					imageLoader.DisplayImage(profile_text, R.drawable.noimg,
							blurr_img);*/


					Thread t = new Thread(){
						public void run(){
							final Bitmap bitmapToGetFromURL = getBitmapFromURL(profile_text);

							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									croppedBitmap(bitmapToGetFromURL);
								}
							});

						}
					};t.start();

					try {
						ratingBar.setRating(Float.parseFloat(ratings_text));
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (Constants.FollowersList.size() > 0) {
						
						review_static.setVisibility(View.VISIBLE);
						no_of_reviews.setText(""+Constants.FollowersList.size());
						mAdapter = new MyAdapter(getApplicationContext(),
								Constants.FollowersList);
						listview.setAdapter(mAdapter);
					}
				} else if (result.get("ResponseCode").equals("false")) {

					Toast.makeText(getApplicationContext(), "No data found.",
							Toast.LENGTH_SHORT).show();
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
				ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(DefaultProfile.this,
					R.drawable.loadingicon);
			db.show();
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

			return Constants.FollowersList.size();
		}

		@Override
		public Object getItem(int position) {
			return Constants.FollowersList.get(position);
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
				convertView = mInflater.inflate(R.layout.follower_listitem,
						null);

				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.email = (TextView) convertView.findViewById(R.id.email);
				holder.message = (TextView) convertView
						.findViewById(R.id.message);

				holder.profile_pic = (ImageView) convertView
						.findViewById(R.id.profile_pic);

				holder.ratingBar = (RatingBar) convertView
						.findViewById(R.id.ratingBar);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(Constants.FollowersList.get(position).get(
					"fname")
					+ " " + Constants.FollowersList.get(position).get("lname"));
			
			holder.email.setText(Constants.FollowersList.get(position).get(
					"email"));
			holder.message.setText(Constants.FollowersList.get(position).get(
					"reviews"));

            try {
                holder.ratingBar.setRating(Float.parseFloat(Constants.FollowersList.get(position).get("ratings")));

            } catch(Exception e){
                e.printStackTrace();
            }
			
			final String  profile_text = Constants.FollowersList.get(position).get("profile_pic");
			
			imageLoader.DisplayImage(profile_text, R.drawable.noimg,holder.profile_pic);

		/*	Thread t = new Thread(){
				public void run(){

					final Bitmap bitmapToGetFromURL = getBitmapFromURL(profile_text);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//croppedBitmap(bitmapToGetFromURL);




							Bitmap cropedBitmap = null;
							ScalingUtilities mScalingUtilities = new ScalingUtilities();
							Bitmap mBitmap = null;
							if (bitmapToGetFromURL != null)

							{

								int[] size = getImageHeightAndWidthForProFileImageHomsecreen(DefaultProfile.this);
								cropedBitmap = mScalingUtilities
										.createScaledBitmap(bitmapToGetFromURL, size[1],
												size[0], ScalingUtilities.ScalingLogic.CROP);
								//takenImage2.recycle();
								mBitmap = mScalingUtilities.getCircleBitmap(
										cropedBitmap, 1);
								cropedBitmap.recycle();



								holder.profile_pic.setImageBitmap(mBitmap);

							}

						}
					});

				}
			};t.start();*/

			return convertView;
		}

		class ViewHolder {
			TextView name, email, message;

			RatingBar ratingBar;

			ImageView profile_pic;

		}

	}


	private void croppedBitmap(Bitmap takenImage2) {


		Bitmap cropedBitmap = null;
		ScalingUtilities mScalingUtilities = new ScalingUtilities();
		Bitmap mBitmap = null;
		if (takenImage2 != null)

		{

			int[] size = getImageHeightAndWidthForProFileImageHomsecreen(DefaultProfile.this);
			cropedBitmap = mScalingUtilities
					.createScaledBitmap(takenImage2, size[1],
							size[0], ScalingUtilities.ScalingLogic.CROP);
			//takenImage2.recycle();
			mBitmap = mScalingUtilities.getCircleBitmap(
					cropedBitmap, 1);
			//cropedBitmap.recycle();



			profile_pic.setImageBitmap(mBitmap);
			blurr_img.setImageBitmap(mBitmap);
		}
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

		catch(IOException e)

		{
			e.printStackTrace();
			return null;
		} catch(Exception e){
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
