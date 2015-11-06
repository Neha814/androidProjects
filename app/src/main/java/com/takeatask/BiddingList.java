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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class BiddingList extends Activity {

	ListView listview;

	boolean isConnected;

	MyAdapter mAdapter;
	
	LinearLayout back_ll;

	ArrayList<HashMap<String, String>> currentPostedList = new ArrayList<HashMap<String, String>>();

	TransparentProgressDialog db;
	
	ImageView back;

	/*protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(getApplicationContext());
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
					BiddingList.this).create();

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

		setContentView(R.layout.bidding_list);

		listview = (ListView) findViewById(R.id.listview);
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		

		/*listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Constants.TASK_ID_TO_GET_BIDDING_LIST = currentPostedList.get(position).get("task_id");
				Intent i = new Intent(BiddingList.this , Bidding.class);
				startActivity(i);
				
			}
		});*/

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		if (isConnected) {

			new GetCurrentPostedTask().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
		
		back_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(BiddingList.this , Home.class);
				startActivity(i);
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent i = new Intent(BiddingList.this , Home.class);
			startActivity(i);

			}
		});
	}
	
	public class GetCurrentPostedTask extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {
			
			/*http://phphosting.osvin.net/TakeATask/WEB_API/listUserTaskPosted.php?
			authkey=Auth_TakeATask2015&user_id=12&is_history=0*/
		

			try {
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("user_id",Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("is_history","2"));
				
				result = function.GetCurrentPostedTask(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					
					currentPostedList.addAll(result);
					mAdapter = new MyAdapter(getApplicationContext(), currentPostedList);
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
			db = new TransparentProgressDialog(BiddingList.this,
					R.drawable.loadingicon);
			db.show();
			} catch(Exception e){
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

			return currentPostedList.size();
		}

		@Override
		public Object getItem(int position) {
			return currentPostedList.get(position);
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
				convertView = mInflater.inflate(R.layout.bid_list,
						null);

				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.description = (TextView) convertView.findViewById(R.id.description);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.view_bid = (TextView) convertView.findViewById(R.id.view_bid);
				holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.view_bid.setTag(position);
			holder.ll.setTag(position);
			
			holder.view_bid.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int position = (Integer) v.getTag();
					Constants.TASK_ID_TO_GET_BIDDING_LIST = currentPostedList.get(position).get("task_id");
					Intent i = new Intent(BiddingList.this, Bidding.class);
					startActivity(i);
				}
			});

			holder.ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int position = (Integer)v.getTag();
					Constants.TASK_ID_TO_GET_BIDDING_LIST = currentPostedList.get(position).get("task_id");
					Intent i = new Intent(BiddingList.this , Bidding.class);
					startActivity(i);
				}
			});
			
			SpannableString content1 = new SpannableString("View Bid");
			content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
			holder.view_bid.setText(content1);

			holder.title.setText(currentPostedList.get(position).get("title"));
			holder.description.setText(currentPostedList.get(position).get("description"));
			holder.price.setText("$ "+currentPostedList.get(position).get("price"));
			
			return convertView;
		}

		class ViewHolder {
			TextView title, description,price ,view_bid;
			LinearLayout ll;
		}

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(BiddingList.this,Home.class);
		startActivity(i);
	}
}
