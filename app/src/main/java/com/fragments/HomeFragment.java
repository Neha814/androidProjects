package com.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imageloader.ImageLoader;
import com.takeatask.R;
import com.takeatask.TakeATask;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class HomeFragment extends Fragment {

	ListView listview;
	private View rootView;
	Boolean isConnected;
	ArrayList<HashMap<String, String>> categoryList = new ArrayList<HashMap<String, String>>();
	TransparentProgressDialog db;
	ImageLoader imageLoader;
	MyAdapter mAdapter;

	/*
	 * protected void showDialog(String msg) { try { final Dialog dialog; dialog
	 * = new Dialog(getActivity()); dialog.setCancelable(false);
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
			AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
					.create();

			alertDialog.setTitle("Alert !");

			alertDialog.setMessage(msg);

			// alertDialog.setIcon(R.drawable.browse);

			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.home_fragment, container, false);

		listview = (ListView) rootView.findViewById(R.id.listview);

		imageLoader = new ImageLoader(getActivity());

		isConnected = NetConnection.checkInternetConnectionn(getActivity());

		if (isConnected) {
			new GetCategoryList().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Constants.HOME_CATEGORY_ID = categoryList.get(position).get(
						"id");
				Constants.HOME_CATEGORY_NAME = categoryList.get(position).get(
						"title");

				Constants.SEARCH_CAT_ID = categoryList.get(position).get(
						"id");
				Constants.SEARCH_CAT_NAME = categoryList.get(position).get(
						"title");

				Intent i = new Intent(getActivity(), TakeATask.class);
				startActivity(i);
			}
		});

		return rootView;

	}

	private void checkForGuestUser() {

		Log.e("user id===>>", "" + Constants.USER_ID);
		if (Constants.USER_ID.equals("")) {
			showDialog("In order to Post task or to perform any functionality , you need to signUp or signIn first.");
		}

	}

	public class GetCategoryList extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			/* http://phphosting.osvin.net/TakeATask/WEB_API/listCategories.php */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				result = function.getCategoryList(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					Constants.GLOBAL_categoryList.clear();
					Constants.GLOBAL_categoryListID.clear();
					categoryList.clear();
					categoryList.addAll(result);
					for (int i = 0; i < categoryList.size(); i++) {
						Constants.GLOBAL_categoryList.add(categoryList.get(i)
								.get("title"));
						Constants.GLOBAL_categoryListID.add(Integer
								.parseInt(categoryList.get(i).get("id")));
					}

					mAdapter = new MyAdapter(getActivity(), categoryList);
					listview.setAdapter(mAdapter);
				} else {
					showDialog("No Data found.");
				}

				/**
				 *************************** Changes **********************************
				 */

				checkForGuestUser();
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(),
					R.drawable.loadingicon);
			db.show();
		}

	}

	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public MyAdapter(FragmentActivity activity,
				ArrayList<HashMap<String, String>> categoryList) {
			mInflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {

			return categoryList.size();
		}

		@Override
		public Object getItem(int position) {
			return categoryList.get(position);
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
				convertView = mInflater.inflate(R.layout.category_listitem,
						null);

				holder.category_iamge = (ImageView) convertView
						.findViewById(R.id.category_iamge);
				holder.icon1 = (ImageView) convertView.findViewById(R.id.icon1);
				holder.icon2 = (ImageView) convertView.findViewById(R.id.icon2);
				holder.category_name = (TextView) convertView
						.findViewById(R.id.category_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String imageURL = categoryList.get(position).get("image");
			String icon1URL = categoryList.get(position).get("icon1");
			String icon2URL = categoryList.get(position).get("icon2");

			/*
			 * Log.i("imageURL ===",""+imageURL);
			 * Log.i("icon1URL ===",""+icon1URL);
			 * Log.i("icon2URL ===",""+icon2URL);
			 */

			holder.category_name.setText(categoryList.get(position)
					.get("title"));
			imageLoader.DisplayImage(imageURL, R.drawable.noimg,
					holder.category_iamge);
			imageLoader.DisplayImage(icon1URL, R.drawable.noimg, holder.icon1);
			imageLoader.DisplayImage(icon2URL, R.drawable.noimg, holder.icon2);
			return convertView;
		}

		class ViewHolder {
			ImageView category_iamge, icon1, icon2;

			TextView category_name;

		}

	}

}
