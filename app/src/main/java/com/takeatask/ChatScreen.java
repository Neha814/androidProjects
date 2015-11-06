package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

public class ChatScreen extends Activity {

	boolean isConnected;

	ImageView senderimage;

	ImageLoader imageLoader;

	TextView senderName;

	ListView listview;

	MyAdapter mAdapter;

	TransparentProgressDialog db;

	// private SwipeRefreshLayout swipeNewRefreshLayoutSL;

	int page = 1;

	int totalPageCount = 0;

	EditText chat_editText;

	Button send_chat_message_button;

	int global_msg_id = 0;

	boolean inchatscreeFirstTime = true;

	TextView load_more;

	ImageButton delete;

	ArrayList<HashMap<String, String>> chat_list = new ArrayList<HashMap<String, String>>();

	ArrayList<Integer> temp_list = new ArrayList<Integer>();

/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(ChatScreen.this);
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

			e.printStackTrace();
		}

	}*/
	
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					ChatScreen.this).create();

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

		setContentView(R.layout.chat_screen_new);
		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());
		imageLoader = new ImageLoader(getApplicationContext());

		senderimage = (ImageView) findViewById(R.id.senderimage);
		senderName = (TextView) findViewById(R.id.senderName);
		listview = (ListView) findViewById(R.id.listview);
		delete = (ImageButton) findViewById(R.id.delete);

		// swipeNewRefreshLayoutSL = (SwipeRefreshLayout)
		// findViewById(R.id.swipeNewRefreshLayoutSL);
		chat_editText = (EditText) findViewById(R.id.chat_editText);
		send_chat_message_button = (Button) findViewById(R.id.send_chat_message_button);

		/*
		 * swipeNewRefreshLayoutSL.setColorSchemeColors(Color.RED,
		 * Color.parseColor("#009654"), Color.parseColor("#1469EB"),
		 * Color.parseColor("#D7422C"), Color.parseColor("#FFB701"));
		 */

		imageLoader.DisplayImage(Constants.CHAT_IMAGE, R.drawable.default_pic,
				senderimage);
		senderName.setText(Constants.CHAT_NAME);

		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(
				R.layout.load_more_header, listview, false);
		load_more = (TextView) header.findViewById(R.id.load_more);
		listview.addHeaderView(header, null, false);

		hideSoftKeyboard();

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDeleteConfirmationDialog("Are you sure you want to delete the whole conversation?");

			}
		});

		if (isConnected) {

			new LoadPreviousMessageFirstTime().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

		/**
		 ************* to receive chat from another person
		 * *************************************
		 */

		final Handler localHandler2 = new Handler();
		localHandler2.postDelayed(new Runnable() {
			public void run() {

				if (inchatscreeFirstTime == false) {

					ReceiveChat();
				}
				localHandler2.postDelayed(this, 6000L);
			}
		}, 1000L);

		/**
		 ************************* Refresh Functionality *******************************************
		 */

		/*
		 * swipeNewRefreshLayoutSL.setOnRefreshListener(new OnRefreshListener()
		 * {
		 * 
		 * @Override public void onRefresh() {
		 * 
		 * // swipeNewRefreshLayoutSL.setRefreshing(false);
		 * 
		 * loadPreviousChat();
		 * 
		 * }
		 * 
		 * });
		 */

		load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadPreviousChat();
			}
		});

		send_chat_message_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String messageToSend = chat_editText.getText().toString();

				int size = chat_list.size();

				if (messageToSend.trim().length() > 0) {

					HashMap localHashMap2 = new HashMap();
					localHashMap2.put("message", messageToSend);
					localHashMap2.put("sender_id", Constants.USER_ID);

					if (size <= 0) {
						chat_list.add(0, localHashMap2);
						chat_editText.setText("");
						chat_editText.requestFocus();

						temp_list.add(0);

						Log.e("total page count===>>", ""
								+ Constants.TOTAL_PAGE_COUNT);
						Log.e("page===>>", "" + page);

						if (page < Constants.TOTAL_PAGE_COUNT) {
							load_more.setVisibility(View.VISIBLE);
						} else {
							load_more.setVisibility(View.GONE);
						}

						mAdapter = new MyAdapter(getApplicationContext(),
								chat_list);
						listview.setAdapter(mAdapter);
					} else {
						chat_list.add(size, localHashMap2);

						temp_list.add(size);

						mAdapter.notifyDataSetChanged();
						scrollChatListToBottom();
					}

					chat_editText.setText("");
					chat_editText.requestFocus();
					SendMessage(messageToSend);
				}

			}
		});
	}

	public void hideSoftKeyboard() {
		if (getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}

	}

	protected void ReceiveChat() {

		if (isConnected) {

			new ReceiveChat().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

	}

	protected void SendMessage(String messageToSend) {
		if (isConnected) {

			new SendMessage(messageToSend).execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

	}

	protected void loadPreviousChat() {
		if (isConnected) {

			new LoadPreviousChat().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}

	}

	/**
	 ****************** load chat for the first time ******************************
	 */

	public class LoadPreviousMessageFirstTime extends
			AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/chatting.php?
			 * authkey
			 * =Auth_TakeATask2015&from_id=43&to_id=44&message=testing2&is_list
			 * =0&chat_type=1&page=2
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("from_id",
						Constants.SENDER_ID));
				localArrayList.add(new BasicNameValuePair("to_id",
						Constants.RECEIVER_ID));
				localArrayList.add(new BasicNameValuePair("is_list", "1"));
				localArrayList.add(new BasicNameValuePair("message", ""));
				localArrayList.add(new BasicNameValuePair("chat_type", "1"));
				localArrayList.add(new BasicNameValuePair("page", String
						.valueOf(page)));

				result = function.getPreviousMsgList(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {

				Log.e("total page count===>>", "" + Constants.TOTAL_PAGE_COUNT);
				Log.e("page===>>", "" + page);

				if (page < Constants.TOTAL_PAGE_COUNT) {
					load_more.setVisibility(View.VISIBLE);
				} else {
					load_more.setVisibility(View.GONE);
				}
				inchatscreeFirstTime = false;
				if (result.size() > 0) {

					global_msg_id = Integer.parseInt((String) result.get(
							result.size() - 1).get("message_id"));

					page++;

					chat_list.addAll(result);
					mAdapter = new MyAdapter(getApplicationContext(), chat_list);
					listview.setAdapter(mAdapter);

					scrollChatListToBottom();
				} else {
					// showDialog("No Data found.");
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			try {
				db = new TransparentProgressDialog(ChatScreen.this,
						R.drawable.loadingicon);
				db.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ************************** Load previous chat (pagination)
	 * ***********************************
	 */

	public class LoadPreviousChat extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/chatting.php?
			 * authkey
			 * =Auth_TakeATask2015&from_id=43&to_id=44&message=testing2&is_list
			 * =0&chat_type=1&page=2
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("from_id",
						Constants.SENDER_ID));
				localArrayList.add(new BasicNameValuePair("to_id",
						Constants.RECEIVER_ID));
				localArrayList.add(new BasicNameValuePair("is_list", "1"));
				localArrayList.add(new BasicNameValuePair("message", ""));
				localArrayList.add(new BasicNameValuePair("chat_type", "1"));
				localArrayList.add(new BasicNameValuePair("page", String
						.valueOf(page)));

				result = function.getPreviousMsgList(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {

			try {

				if (page < Constants.TOTAL_PAGE_COUNT) {
					load_more.setVisibility(View.VISIBLE);
				} else {
					load_more.setVisibility(View.GONE);
				}
				// swipeNewRefreshLayoutSL.setRefreshing(false);
				if (result.size() > 0) {
					page++;

					chat_list.addAll(0, result);
					mAdapter.notifyDataSetChanged();

				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ******************************* Send Message
	 * ********************************************
	 */

	public class SendMessage extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap<String, String> result = new HashMap<String, String>();

		ArrayList localArrayList = new ArrayList();

		String msg;

		public SendMessage(String messageToSend) {
			this.msg = messageToSend;
		}

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/chatting.php?
			 * authkey
			 * =Auth_TakeATask2015&from_id=43&to_id=44&message=testing2&is_list
			 * =0&chat_type=1&page=2
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("from_id",
						Constants.SENDER_ID));
				localArrayList.add(new BasicNameValuePair("to_id",
						Constants.RECEIVER_ID));
				localArrayList.add(new BasicNameValuePair("is_list", "0"));
				localArrayList.add(new BasicNameValuePair("message", this.msg));
				localArrayList.add(new BasicNameValuePair("chat_type", "1"));
				localArrayList.add(new BasicNameValuePair("page", ""));

				result = function.sendMessage(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {

			try {
				if (result.get("ResponseCode").equalsIgnoreCase("true")) {
					global_msg_id = Integer.parseInt((String) result
							.get("Message_id"));

					temp_list.clear();

					mAdapter.notifyDataSetChanged();

				} else {

				}
			}

			catch (Exception ae) {
				// showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ************************** Receive Chat
	 * ***********************************
	 */

	public class ReceiveChat extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/receiveCurrentChat.php
			 * ? authkey=Auth_TakeATask2015&from_id=43&to_id=44&msg_id=10
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("from_id",
						Constants.SENDER_ID));
				localArrayList.add(new BasicNameValuePair("to_id",
						Constants.RECEIVER_ID));
				localArrayList.add(new BasicNameValuePair("msg_id", String
						.valueOf(global_msg_id)));

				Log.e("****** msg_id *******", "******" + global_msg_id
						+ "******");
				result = function.receiveChat(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {

			if (page < Constants.TOTAL_PAGE_COUNT) {
				load_more.setVisibility(View.VISIBLE);
			} else {
				load_more.setVisibility(View.GONE);
			}

			try {
				if (result.size() > 0) {

					if (chat_list.size() <= 0) {

						chat_list.addAll(0, result);
						global_msg_id = Integer.parseInt((String) result.get(
								result.size() - 1).get("message_id"));

					} else {
						int size = chat_list.size();
						chat_list.addAll(size, result);
						global_msg_id = Integer.parseInt((String) result.get(
								result.size() - 1).get("message_id"));

						// mAdapter.notifyDataSetChanged();
						mAdapter = new MyAdapter(getApplicationContext(),
								chat_list);
						listview.setAdapter(mAdapter);
					}

					scrollChatListToBottom();
				}
			}

			catch (Exception ae) {
				/* showDialog(Constants.ERROR_MSG); */
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			try {

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

			return chat_list.size();
		}

		@Override
		public Object getItem(int position) {
			return chat_list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			holder = new ViewHolder();
			if (chat_list.get(position).get("sender_id")
					.equals(Constants.USER_ID)) {
				convertView = mInflater.inflate(R.layout.my_chat, null);
			} else {
				convertView = mInflater.inflate(R.layout.other_chat, null);
			}

			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);

			convertView.setTag(holder);

			holder.message.setText(chat_list.get(position).get("message"));
			if (chat_list.get(position).get("sender_id")
					.equals(Constants.USER_ID)) {
				holder.name.setText("Me");
			} else {
				holder.name.setText(Constants.CHAT_NAME);
			}

			if (temp_list.size() > 0) {
				if (temp_list.contains(position)) {

					if (chat_list.get(position).get("sender_id")
							.equals(Constants.USER_ID)) {
						holder.message
								.setBackgroundResource(R.drawable.chatboxright_light);
					} else {
						holder.message
								.setBackgroundResource(R.drawable.chatboxleft);
					}
				} else {
					if (chat_list.get(position).get("sender_id")
							.equals(Constants.USER_ID)) {
						holder.message
								.setBackgroundResource(R.drawable.chatboxright);
					} else {
						holder.message
								.setBackgroundResource(R.drawable.chatboxleft);
					}
				}
			} else {
				if (chat_list.get(position).get("sender_id")
						.equals(Constants.USER_ID)) {
					holder.message
							.setBackgroundResource(R.drawable.chatboxright);
				} else {
					holder.message
							.setBackgroundResource(R.drawable.chatboxleft);
				}
			}

			return convertView;
		}

		class ViewHolder {
			TextView message, name;

		}

	}

	public void scrollChatListToBottom() {
		
			listview.post(new Runnable() {

				@Override
				public void run() {
					// Select the last row so it will scroll into view...
					try {
						listview.setSelection(mAdapter.getCount() - 1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Constants.TOTAL_PAGE_COUNT = 1;
		
		new ReceiveChat().cancel(isConnected);
		
		/*Intent i = new Intent(ChatScreen.this , MessageList.class);
		startActivity(i);*/
		finish();
	}

	/*protected void showDeleteConfirmationDialog(String message) {
		final Dialog dialog;
		dialog = new Dialog(ChatScreen.this);
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
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatScreen.this);
		 
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
					Intent i = new Intent(ChatScreen.this , MessageList.class);
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
			db = new TransparentProgressDialog(ChatScreen.this,
					R.drawable.loadingicon);
			db.show();
		}

	}
	

	
}
