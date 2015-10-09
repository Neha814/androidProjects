package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class TakeATask extends Activity implements OnClickListener {

	ImageView back;

	LinearLayout back_ll;

	Button all_task, search;

	ListView listview;

	Spinner category_spinner;

	boolean isConnected;

	LinearLayout ll;
	//LinearLayout ll1;

	MyAdapter mAdapter;

	int count = 0;

	TransparentProgressDialog db;

	boolean isSuccess = false;

	LinearLayout search_layout;
	EditText search_editText;

	public static ArrayList<String> cat_list = new ArrayList<String>();

	ArrayList<HashMap<String, String>> taskList = new ArrayList<HashMap<String, String>>();

/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(TakeATask.this);
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
					TakeATask.this).create();

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
					if (isSuccess) {
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.take_a_task);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		back = (ImageView) findViewById(R.id.back);
		all_task = (Button) findViewById(R.id.all_task);
		search = (Button) findViewById(R.id.search);
		
		listview = (ListView) findViewById(R.id.listview);
		search_layout = (LinearLayout) findViewById(R.id.search_layout);
		search_editText = (EditText) findViewById(R.id.search_editText);
		category_spinner = (Spinner) findViewById(R.id.category_spinner);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		ll = (LinearLayout) findViewById(R.id.ll);
	//	ll1 = (LinearLayout) findViewById(R.id.ll1);

		cat_list.clear();
		cat_list.add("All Tasks");

		cat_list.addAll(Constants.GLOBAL_categoryList);

		Log.e("cat list local==>", "" + cat_list);
		Log.e("cat list global==>", "" + Constants.GLOBAL_categoryList);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.simple_spinner_item_new,
				R.id.text, cat_list);
		category_spinner.setAdapter(dataAdapter);

		category_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						String category_text = category_spinner
								.getSelectedItem().toString();
						if (count > 0) {
							if (category_text.equalsIgnoreCase("All Tasks")) {
								mAdapter.filter1("");
							} else {
								mAdapter.filter1(category_text);
							}

						}

						count++;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		back.setOnClickListener(this);
		all_task.setOnClickListener(this);
		search.setOnClickListener(this);
	
		back_ll.setOnClickListener(this);
		ll.setOnClickListener(this);
	//	ll1.setOnClickListener(this);

		search_layout.setVisibility(View.GONE);

		if (isConnected) {

			new getTaskList().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

		search_editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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
					String text = search_editText.getText().toString()
							.toLowerCase(Locale.getDefault());
					mAdapter.filter(text);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v == back || v == back_ll) {
			finish();
		} else if (v == search) {
			search_layout.setVisibility(View.VISIBLE);
		} else if (v == all_task  || v == ll) {
			try {

				Log.e("all task===>>>", "all task");
				search_editText.setText("");
				mAdapter.filter("");
				search_layout.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class getTaskList extends AsyncTask<Void, Void, Void> {
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
				localArrayList.add(new BasicNameValuePair("task_id", ""));
				localArrayList.add(new BasicNameValuePair("is_list", "1"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));

				result = function.taskLIst(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {

					taskList.addAll(result);
					mAdapter = new MyAdapter(getApplicationContext(), taskList);
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
				db = new TransparentProgressDialog(TakeATask.this,
						R.drawable.loadingicon);
				db.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;
		private ArrayList<HashMap<String, String>> mDisplayedValues;

		public MyAdapter(Context context,
				ArrayList<HashMap<String, String>> categoryList) {
			mInflater = LayoutInflater.from(getApplicationContext());

			mDisplayedValues = new ArrayList<HashMap<String, String>>();
			mDisplayedValues.addAll(taskList);
		}

		public void filter1(String category_text) {
			category_text = category_text.toLowerCase(Locale.getDefault());

			taskList.clear();
			if (category_text.length() == 0) {

				taskList.addAll(mDisplayedValues);
			} else {

				for (int i = 0; i < mDisplayedValues.size(); i++) {

					if (mDisplayedValues.get(i).get("category_name")
							.toLowerCase(Locale.getDefault())
							.contains(category_text)) {
						taskList.add(mDisplayedValues.get(i));
					}
				}
			}
			notifyDataSetChanged();

		}

		public void filter(String charText) {
			charText = charText.toLowerCase(Locale.getDefault());

			taskList.clear();
			if (charText.length() == 0) {

				taskList.addAll(mDisplayedValues);
			} else {

				for (int i = 0; i < mDisplayedValues.size(); i++) {

					if (mDisplayedValues.get(i).get("title")
							.toLowerCase(Locale.getDefault())
							.contains(charText)
							|| mDisplayedValues.get(i).get("address")
									.toLowerCase(Locale.getDefault())
									.contains(charText)
							|| mDisplayedValues.get(i).get("city")
									.toLowerCase(Locale.getDefault())
									.contains(charText)
							|| mDisplayedValues.get(i).get("state")
									.toLowerCase(Locale.getDefault())
									.contains(charText)
							|| mDisplayedValues.get(i).get("country")
									.toLowerCase(Locale.getDefault())
									.contains(charText)
							|| mDisplayedValues.get(i).get("zipcode")
									.toLowerCase(Locale.getDefault())
									.contains(charText)
							|| mDisplayedValues.get(i).get("budget")
									.toLowerCase(Locale.getDefault())
									.contains(charText)) {

						taskList.add(mDisplayedValues.get(i));

					}
				}
			}
			notifyDataSetChanged();

		}

		@Override
		public int getCount() {

			return taskList.size();
		}

		@Override
		public Object getItem(int position) {
			return taskList.get(position);
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
				convertView = mInflater.inflate(R.layout.take_task_listitem,
						null);

				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.task_name = (TextView) convertView
						.findViewById(R.id.task_name);
				holder.address = (TextView) convertView
						.findViewById(R.id.address);
				holder.no_of_days = (TextView) convertView
						.findViewById(R.id.no_of_days);
				holder.task_detail = (TextView) convertView
						.findViewById(R.id.task_detail);
				holder.apply = (Button) convertView.findViewById(R.id.apply);
				holder.chat = (TextView) convertView.findViewById(R.id.chat);
				holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.task_detail.setTag(position);
			holder.apply.setTag(position);
			holder.chat.setTag(position);
			holder.ll.setTag(position);

			SpannableString content2 = new SpannableString("Message");
			content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
			holder.chat.setText(content2);

			SpannableString content1 = new SpannableString("Details/Apply");
			content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
			holder.task_detail.setText(content1);

			if (taskList.get(position).get("user_id").equals(Constants.USER_ID)) {
				// holder.apply.setVisibility(View.INVISIBLE);
				holder.chat.setVisibility(View.INVISIBLE);
			} else {
				// holder.apply.setVisibility(View.VISIBLE);
				holder.chat.setVisibility(View.VISIBLE);
			}

			holder.price.setText(taskList.get(position).get("budget"));
			holder.task_name.setText(taskList.get(position).get("title"));
			/*
			 * holder.address.setText(taskList.get(position).get("address")+" "+
			 * taskList
			 * .get(position).get("city")+", "+taskList.get(position).get
			 * ("state")+
			 * ", "+taskList.get(position).get("country")+", "+taskList
			 * .get(position).get("zipcode"));
			 */

			/*holder.address.setText(taskList.get(position).get("city") + ", "
					+ taskList.get(position).get("state"));*/
			
			List<String> addList = new ArrayList<String>();
			
			addList.clear();
			
			String city_local = taskList.get(position).get("city");
			String state_local = taskList.get(position).get("state");
			
			addList.add(city_local);
			addList.add(state_local);
			

			addList.removeAll(Arrays.asList("", null));

			String ADDRESS_TEXT = addList.toString().replace("[", "")
					.replace("]", "").replace(", ", ", ");
			
			holder.address.setText(ADDRESS_TEXT);

			holder.chat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = (Integer) v.getTag();

					Constants.RECEIVER_ID = taskList.get(pos).get("user_id");
					Constants.CHAT_NAME = taskList.get(pos).get("fname") + " "
							+ taskList.get(pos).get("lname");
					Constants.CHAT_IMAGE = taskList.get(pos).get("profile_pic");
					Constants.SENDER_ID = Constants.USER_ID;

					Intent i = new Intent(TakeATask.this, ChatScreen.class);
					startActivity(i);
				}
			});

			holder.task_detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = (Integer) v.getTag();

					Constants.TASK_DETAIL_ADDRESS = taskList.get(pos).get(
							"address");

					Constants.TASK_DETAIL_CITY = taskList.get(pos).get("city");
					Constants.TASK_DETAIL_STATE = taskList.get(pos)
							.get("state");
					Constants.TASK_DETAIL_COUNTRY = taskList.get(pos).get(
							"country");
					Constants.TASK_DETAIL_ZIPCODE = taskList.get(pos).get(
							"zipcode");

					Constants.TASK_DETAIL_DATE = taskList.get(pos).get(
							"due_date");

					Constants.TASK_DETAIL_DESC = taskList.get(pos).get(
							"description");
					Constants.TASK_DETAIL_PRICE = taskList.get(pos).get(
							"budget");
					Constants.TASK_DETAIL_TITLE = taskList.get(pos)
							.get("title");
					Constants.TASK_DETAIL_URL = taskList.get(pos).get("file");
					Constants.TASK_DETAIL_USERID = taskList.get(pos).get(
							"user_id");
					Constants.TASK_DETAIL_ID = taskList.get(pos).get("id");
					Constants.TASK_DETAIL_FNAME = taskList.get(pos)
							.get("fname");
					Constants.TASK_DETAIL_LNAME = taskList.get(pos)
							.get("lname");
					Constants.TASK_DETAIL_CATNAME = taskList.get(pos).get(
							"category_name");
					Constants.TASK_DETAIL_SUBCATNAME = taskList.get(pos).get(
							"subcategory_name");

					Intent i = new Intent(TakeATask.this, TaskDetail.class);
					startActivity(i);
				}
			});

			holder.ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();

					Constants.TASK_DETAIL_ADDRESS = taskList.get(pos).get(
							"address");

					Constants.TASK_DETAIL_CITY = taskList.get(pos).get("city");
					Constants.TASK_DETAIL_STATE = taskList.get(pos)
							.get("state");
					Constants.TASK_DETAIL_COUNTRY = taskList.get(pos).get(
							"country");
					Constants.TASK_DETAIL_ZIPCODE = taskList.get(pos).get(
							"zipcode");

					Constants.TASK_DETAIL_DATE = taskList.get(pos).get(
							"due_date");

					Constants.TASK_DETAIL_DESC = taskList.get(pos).get(
							"description");
					Constants.TASK_DETAIL_PRICE = taskList.get(pos).get(
							"budget");
					Constants.TASK_DETAIL_TITLE = taskList.get(pos)
							.get("title");
					Constants.TASK_DETAIL_URL = taskList.get(pos).get("file");
					Constants.TASK_DETAIL_USERID = taskList.get(pos).get(
							"user_id");
					Constants.TASK_DETAIL_ID = taskList.get(pos).get("id");
					Constants.TASK_DETAIL_FNAME = taskList.get(pos)
							.get("fname");
					Constants.TASK_DETAIL_LNAME = taskList.get(pos)
							.get("lname");
					Constants.TASK_DETAIL_CATNAME = taskList.get(pos).get(
							"category_name");
					Constants.TASK_DETAIL_SUBCATNAME = taskList.get(pos).get(
							"subcategory_name");

					Intent i = new Intent(TakeATask.this, TaskDetail.class);
					startActivity(i);

				}
			});

			holder.apply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();

					String to_id = taskList.get(pos).get("user_id");

					String task_id = taskList.get(pos).get("id");

					String amount = taskList.get(pos).get("budget");

					SendRequest(to_id, task_id, amount);
				}
			});

			return convertView;
		}

		protected void SendRequest(String to_id, String task_id, String price) {
			if (isConnected) {

				new SendRequestCall(to_id, task_id, price).execute(new Void[0]);
			} else {
				showDialog(Constants.No_INTERNET);
			}
		}

		class ViewHolder {
			TextView price, task_name, address, no_of_days, task_detail;

			TextView chat;

			LinearLayout ll;

			Button apply;

		}

	}

	public class SendRequestCall extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String toID, taskID, Price;

		public SendRequestCall(String to_id, String task_id, String price) {

			this.toID = to_id;
			this.taskID = task_id;
			this.Price = price;

		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/hireRequest.php?
			 * authkey
			 * =Auth_TakeATask2015&from_id=11&to_id=18&message=hiii&task_id
			 * =2&price=50
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("from_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("to_id", this.toID));
				localArrayList.add(new BasicNameValuePair("task_id",
						this.taskID));
				localArrayList.add(new BasicNameValuePair("price", this.Price));
				localArrayList.add(new BasicNameValuePair("message", ""));

				result = function.SendRequest(localArrayList);

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
					showDialog(msg);
				} else if (result.get("ResponseCode").equals("false")) {
					String msg = (String) result.get("Message");
					showDialog(msg);
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(TakeATask.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		Intent i = new Intent(TakeATask.this, Home.class);
		startActivity(i);
	}
}
