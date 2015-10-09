package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.imageloader.ImageLoader;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class ViewProfile extends Activity implements OnClickListener {

	ImageView back;
	ImageView profile_pic;
	TextView name, location, memeber_since, rating, about_me, no_of_reviews,
			occupation, language;
	ListView listview;
	RatingBar ratingBar;
	boolean isConnected;
	ImageLoader imageLoader;
	ImageView blurr_img;

	LinearLayout back_ll;

	TextView review_static;

	MyAdapter mAdapter;

	TextView add_rating;

	TransparentProgressDialog db;

	/*
	 * protected void showDialog(String msg) { try { final Dialog dialog; dialog
	 * = new Dialog(ViewProfile.this); dialog.setCancelable(false);
	 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
	 * 
	 * Drawable d = new ColorDrawable(Color.BLACK); d.setAlpha(0);
	 * dialog.getWindow().setBackgroundDrawable(d);
	 * 
	 * Button ok; TextView message;
	 * 
	 * dialog.setContentView(R.layout.dialog);
	 * 
	 * ok = (Button) dialog.findViewById(R.id.ok); message = (TextView)
	 * dialog.findViewById(R.id.message);
	 * 
	 * message.setText(msg);
	 * 
	 * ok.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { dialog.dismiss();
	 * 
	 * } }); dialog.show(); } catch (Exception e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); }
	 * 
	 * }
	 */

	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(ViewProfile.this)
					.create();

			// Setting Dialog Title
			alertDialog.setTitle("Alert !");

			// Setting Dialog Message
			alertDialog.setMessage(msg);

			// Setting Icon to Dialog
			// alertDialog.setIcon(R.drawable.browse);

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

		setContentView(R.layout.view_profile);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		imageLoader = new ImageLoader(getApplicationContext());

		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
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
		add_rating = (TextView) findViewById(R.id.add_rating);

		review_static.setVisibility(View.INVISIBLE);

		back.setOnClickListener(this);
		add_rating.setOnClickListener(this);
		back_ll.setOnClickListener(this);

		getProfileCall();
	}

	private void getProfileCall() {
		if (isConnected) {
			Constants.FollowersList.clear();
			new getProfile(Constants.VIEW_PROFILE_ID).execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == back || v == back_ll) {
			finish();
		} else if (v == add_rating) {
			Intent i = new Intent(ViewProfile.this, AddRating.class);
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

				Log.e("result===", "" + result);
				if (result.get("ResponseCode").equals("true")) {

					String name_text = (String) result.get("fname");
					String address_text = (String) result.get("address");
					String city_text = (String) result.get("city");
					String state_text = (String) result.get("state");
					String country_text = (String) result.get("country");
					String ratings_text = (String) result.get("ratings");
					String profile_text = (String) result.get("profile_pic");

					String background = (String) result.get("background");
					String skills = (String) result.get("skills");
					String occupation = (String) result.get("occupation");
					String lang = (String) result.get("language");
					String member_date = (String) result.get("member_from");

					name.setText(name_text);
					location.setText("Location : " + address_text + " ,"
							+ city_text + " ," + state_text);
					// about_me.setText("Background : "+background+" .Skills : "+skills+" .occupation : "+occupation);
					about_me.setText("" + background);
					rating.setText("Rating : " + ratings_text);

					/*
					 * String dateConvert =member_date; SimpleDateFormat
					 * dateFormat = new SimpleDateFormat( "MM-dd-yyyy"); Date
					 * myDate = new Date(); try { myDate =
					 * dateFormat.parse(dateConvert);
					 * 
					 * SimpleDateFormat formatter = new SimpleDateFormat(
					 * "MMM-d-yyyy"); String DateConverted =
					 * formatter.format(myDate);
					 * 
					 * 
					 * } catch (ParseException e) { e.printStackTrace(); }
					 */

					memeber_since.setText("Member Since : " + member_date);

					ViewProfile.this.occupation.setText("Occupation : "
							+ occupation);
					ViewProfile.this.language.setText("Language : " + lang);

					Log.e("profile_text===>>>", "" + profile_text);

					imageLoader.DisplayImage(profile_text, R.drawable.noimg,
							profile_pic);
					imageLoader.DisplayImage(profile_text, R.drawable.noimg,
							blurr_img);

					try {
						ratingBar.setRating(Float.parseFloat(ratings_text));
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (Constants.FollowersList.size() > 0) {

						review_static.setVisibility(View.VISIBLE);
						no_of_reviews.setText(""
								+ Constants.FollowersList.size());
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
			db = new TransparentProgressDialog(ViewProfile.this,
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
			ViewHolder holder;

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

			holder.ratingBar.setRating(Float.parseFloat(Constants.FollowersList
					.get(position).get("ratings")));

			String profile_text = Constants.FollowersList.get(position).get(
					"profile_pic");

			imageLoader.DisplayImage(profile_text, R.drawable.noimg,
					holder.profile_pic);

			return convertView;
		}

		class ViewHolder {
			TextView name, email, message;

			RatingBar ratingBar;

			ImageView profile_pic;

		}

	}
}
