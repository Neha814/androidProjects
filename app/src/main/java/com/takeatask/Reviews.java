package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class Reviews extends Activity {

	TextView rating_text;

	ListView listview;
	
	LinearLayout back_ll;

	boolean isConnected;

	TransparentProgressDialog db;
	
	MyAdapter mAdapter ;
	
	ImageView back;
	
	RelativeLayout rr, rr2;
	
	ArrayList<HashMap<String, String>> review_list = new ArrayList<HashMap<String,String>>();
	
/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(Reviews.this);
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
					Reviews.this).create();

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

		setContentView(R.layout.reviews);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		rating_text = (TextView) findViewById(R.id.rating_text);
		listview = (ListView) findViewById(R.id.listview);
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		rr = (RelativeLayout) findViewById(R.id.rr);
		rr2 = (RelativeLayout) findViewById(R.id.rr2);
		
		rr.setVisibility(View.VISIBLE);
		rr2.setVisibility(View.GONE);
		if (isConnected) {

			new ReviewLIst().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
		
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
	}

	public class ReviewLIst extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			/*http://phphosting.osvin.net/TakeATask/WEB_API/getRatingsReceived.php?
				authkey=Auth_TakeATask2015&id=38*/
			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("id",
						Constants.USER_ID));

				result = function.reviewList(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					review_list.clear();
					review_list.addAll(result);
					
					rating_text.setText(Constants.AVERAGE_RATING_USER);
					
					
					mAdapter = new MyAdapter(getApplicationContext(),
							review_list);
					listview.setAdapter(mAdapter);
				} else {
					//showDialog("No Data found.");
					Toast.makeText(getApplicationContext(), "No data found.", Toast.LENGTH_SHORT).show();
					rr.setVisibility(View.GONE);
					rr2.setVisibility(View.VISIBLE);
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			try {
				db = new TransparentProgressDialog(Reviews.this,
						R.drawable.loadingicon);
				db.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
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

			return review_list.size();
		}

		@Override
		public Object getItem(int position) {
			return review_list.get(position);
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
				convertView = mInflater.inflate(
						R.layout.review_list_item, null);

				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.review = (TextView) convertView.findViewById(R.id.review);
				holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
		
			holder.name.setText(review_list.get(position).get("fromUser"));
			holder.review.setText(review_list.get(position).get("review"));
			
			try {
				float rating = Float.parseFloat(review_list.get(position).get("ratings"));
				holder.ratingBar.setRating(rating);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		
			return convertView;
		}

		class ViewHolder {
			TextView name ,review;
			
			RatingBar  ratingBar;

		}

	}
}
