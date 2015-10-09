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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.imageloader.ImageLoader;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class CategoryListing extends Activity {

	ListView listview;
	TextView cat_name;

	boolean isConnected;
	
	ImageView back;
	
	LinearLayout back_ll;

	List<String> addList = new ArrayList<String>();
	
	TransparentProgressDialog db;
	
	ImageLoader imageLoader ;

	MyAdapter mAdapter;
	ArrayList<HashMap<String, String>> cat_listing = new ArrayList<HashMap<String,String>>();
	
/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(CategoryListing.this);
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
					CategoryListing.this).create();

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

		setContentView(R.layout.category_listing);
		
		imageLoader = new ImageLoader(getApplicationContext());
		
		isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());

		cat_name = (TextView) findViewById(R.id.cat_name);
		listview = (ListView) findViewById(R.id.listview);
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		
		back_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		cat_name.setText(Constants.HOME_CATEGORY_NAME);
		
		if (isConnected) {

			new CategoryListAPI().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}
	
	public class CategoryListAPI extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();
		
		

		protected Void doInBackground(Void... paramVarArgs) {
			
			
			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/searchByCategory.php
			 * ?authkey=Auth_TakeATask2015&category_id=1
			 */				
			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("category_id",
						Constants.HOME_CATEGORY_ID));
			
			
				result = function.CategoryListing(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					cat_listing.clear();
					cat_listing.addAll(result);
					mAdapter = new MyAdapter(getApplicationContext(),
							cat_listing);
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
						CategoryListing.this, R.drawable.loadingicon);
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

			return cat_listing.size();
		}

		@Override
		public Object getItem(int position) {
			return cat_listing.get(position);
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
						R.layout.category_listing_listitem, null);

				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.task_image = (ImageView) convertView.findViewById(R.id.task_image);
				
				holder.title = (EditText) convertView.findViewById(R.id.title);
				holder.price = (EditText) convertView.findViewById(R.id.price);
				holder.description = (EditText) convertView.findViewById(R.id.description);
				holder.address = (EditText) convertView.findViewById(R.id.address);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				
				convertView.setTag(holder);
			} else {
				
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.title.setText(cat_listing.get(position).get("title"));
			holder.description.setText(cat_listing.get(position).get("description"));
			holder.price.setText(cat_listing.get(position).get("budget"));
			holder.address.setText(cat_listing.get(position).get("address")+", "+
					cat_listing.get(position).get("city")+", "+cat_listing.get(position).get("state")+", "+
					cat_listing.get(position).get("country"));
			
			addList.clear();
			addList.add(cat_listing.get(position).get("address"));
			addList.add(cat_listing.get(position).get("city"));
			addList.add(cat_listing.get(position).get("state"));
			addList.add(cat_listing.get(position).get("country"));

			addList.removeAll(Arrays.asList("", null));
		
			String ADDRESS_TEXT = addList.toString().replace("[", "")
					.replace("]", "").replace(", ", ", ");
			
			
			if(ADDRESS_TEXT!=null || ADDRESS_TEXT.length()>0){
				holder.address.setText(ADDRESS_TEXT);
			} else {
				holder.address.setVisibility(View.GONE);
			}


			
			holder.date.setText(cat_listing.get(position).get("due_date"));
			
			holder.name.setText(cat_listing.get(position).get("Name"));
			
			String url = cat_listing.get(position).get("file");
			
			imageLoader.DisplayImage(url, R.drawable.default_pic, holder.task_image);
		

			return convertView;
		}

		class ViewHolder {
			
			EditText  title , price,description,address;
			TextView date ,name;
			
			ImageView task_image;

		}

	}

}
