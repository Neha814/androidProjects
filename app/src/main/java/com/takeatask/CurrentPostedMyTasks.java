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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class CurrentPostedMyTasks extends Activity {

	ListView listview;

	Button task_history, current_task;

	boolean isConnected;

	MyAdapter mAdapter;
	
	LinearLayout back_ll;

	ImageView back;

	//TextView perform_task;
	

	ArrayList<HashMap<String, String>> currentPostedList = new ArrayList<HashMap<String, String>>();

	TransparentProgressDialog db;

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
					CurrentPostedMyTasks.this).create();

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

		setContentView(R.layout.my_posttask);

		listview = (ListView) findViewById(R.id.listview);
		current_task = (Button) findViewById(R.id.current_task);
		task_history = (Button) findViewById(R.id.task_history);
		//perform_task = (TextView) findViewById(R.id.perform_task);

		back = (ImageView) findViewById(R.id.back);
		back_ll  = (LinearLayout) findViewById(R.id.back_ll);

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
				Intent i = new Intent(CurrentPostedMyTasks.this, SelectTasker.class);
				startActivity(i);
			}
		});
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(CurrentPostedMyTasks.this, SelectTasker.class);
				startActivity(i);
			}
		});

		/*perform_task.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(CurrentPostedMyTasks.this,
						CurrentPerformingTasks.class);
				startActivity(i);

			}
		});*/

		task_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(CurrentPostedMyTasks.this,
						History_posted_task.class);
				startActivity(i);
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int pos = position;

			}
		});
	}

	public class GetCurrentPostedTask extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			
			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/listUserTaskPosted.php
			 * ? authkey=Auth_TakeATask2015&user_id=12&is_history=0
			 */

			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("is_history", "0"));

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
					mAdapter = new MyAdapter(getApplicationContext(),
							currentPostedList);
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
				db = new TransparentProgressDialog(CurrentPostedMyTasks.this,
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
				convertView = mInflater.inflate(
						R.layout.my_task_posted_listitem, null);

				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.description = (TextView) convertView
						.findViewById(R.id.description);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				/*holder.view_profile = (TextView) convertView
						.findViewById(R.id.view_profile);*/
				holder.close = (Button) convertView.findViewById(R.id.close);
				holder.view_task_detail = (TextView) convertView.findViewById(R.id.view_task_detail);
				holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
				holder.progress = (TextView) convertView.findViewById(R.id.progress);
				holder.chat = (TextView) convertView.findViewById(R.id.chat);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}


			if(currentPostedList.get(position).get("accepted").equalsIgnoreCase("0")){
				holder.progress.setVisibility(View.GONE);
                holder.chat.setVisibility(View.GONE);
			} else if(currentPostedList.get(position).get("accepted").equalsIgnoreCase("1")){
				holder.progress.setVisibility(View.VISIBLE);
				holder.progress.setText("In progress...");
                holder.chat.setVisibility(View.VISIBLE);
			}
			SpannableString content1 = new SpannableString("View Task Details");
			content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
			holder.view_task_detail.setText(content1);


			holder.close.setTag(position);
			holder.view_task_detail.setTag(position);
			holder.ll.setTag(position);
            holder.chat.setTag(position);

            /*SpannableString content2 = new SpannableString("Message");
            content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
            holder.chat.setText(content2);*/

            holder.chat.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();

                    Constants.RECEIVER_ID = currentPostedList.get(pos).get("accepted_by");
                    Constants.CHAT_NAME = currentPostedList.get(pos).get("accepted_fname") + " "
                            + currentPostedList.get(pos).get("accepted_lname");
                    Constants.CHAT_IMAGE = currentPostedList.get(pos).get("accepted_pic");
                    Constants.SENDER_ID = Constants.USER_ID;

                    Intent i = new Intent(CurrentPostedMyTasks.this, ChatScreen.class);
                    startActivity(i);
                }
            });
			holder.ll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();

                    goToDetailPage(pos);
				}
			});
			
			holder.view_task_detail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();

                    goToDetailPage(pos);
					
				}
			});
			
			holder.close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();
					
					String task_id = currentPostedList.get(pos).get("task_id");
					String to_id = currentPostedList.get(pos).get("accepted_by");

					Constants.REVIEW_TASK_ID = task_id;
					
					showConfirmationDialog("Are you sure you want to close this Task ?" , task_id , to_id);
				}
			});

			/*holder.view_profile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();
					Constants.VIEW_PROFILE_ID = currentPostedList.get(pos).get(
							"user_id");
				
						Intent i = new Intent(CurrentPostedMyTasks.this , ViewProfile.class);
						startActivity(i);
					
				}
			});*/

			holder.title.setText(currentPostedList.get(position).get("title"));
			holder.description.setText(currentPostedList.get(position).get(
					"description"));
			holder.price.setText("$ "
					+ currentPostedList.get(position).get("price"));

			return convertView;
		}

        private void goToDetailPage(int pos) {
            Constants.TASK_DETAIL_ADDRESS = currentPostedList.get(pos).get("address");

            Constants.TASK_DETAIL_CITY =currentPostedList.get(pos).get("city") ;
            Constants.TASK_DETAIL_STATE = currentPostedList.get(pos).get("state");
            Constants.TASK_DETAIL_COUNTRY =currentPostedList.get(pos).get("country") ;
            Constants.TASK_DETAIL_ZIPCODE = currentPostedList.get(pos).get("zipcode");


            Constants.TASK_DETAIL_DATE = currentPostedList.get(pos).get("due_date");

            Constants.TASK_DETAIL_DESC = currentPostedList.get(pos).get("description");
            Constants.TASK_DETAIL_PRICE = currentPostedList.get(pos).get("price");
            Constants.TASK_DETAIL_TITLE = currentPostedList.get(pos).get("title");
          //  Constants.TASK_DETAIL_URL = currentPostedList.get(pos).get("file");
            Constants.TASK_DETAIL_USERID = currentPostedList.get(pos).get("user_id");
            Constants.TASK_DETAIL_ID = currentPostedList.get(pos).get("task_id");
            Constants.TASK_DETAIL_FNAME = currentPostedList.get(pos).get("fname");
            Constants.TASK_DETAIL_LNAME = currentPostedList.get(pos).get("lname");
            Constants.TASK_DETAIL_CATNAME = currentPostedList.get(pos).get("category_name");
            Constants.TASK_DETAIL_SUBCATNAME = currentPostedList.get(pos).get("subcategory_name");
            Constants.TASK_DETAIL_ACCEPTED = currentPostedList.get(pos).get("accepted");
            Constants.TASK_DETAIL_TASKER_POSTER_NAME = currentPostedList.get(pos).get("accepted_fname")+" "
                    +currentPostedList.get(pos).get("accepted_lname");
            Constants.TASK_DETAIL_TASKER_POSTER_ID = currentPostedList.get(pos).get("accepted_by");
            String attachmentListString  = currentPostedList.get(pos).get("attachments");
			Constants.TASK_DETAIL_COMMENTS = currentPostedList.get(pos).get("comments");

            String str = attachmentListString;
            List<String> attachmentList = Arrays.asList(str.split(","));



            Constants.TASK_DETAIL_ATTACHMENT_1 = attachmentList.get(0);
            Constants.TASK_DETAIL_ATTACHMENT_2 = attachmentList.get(1);
            Constants.TASK_DETAIL_ATTACHMENT_3 = attachmentList.get(2);
            Constants.TASK_DETAIL_ATTACHMENT_4 = attachmentList.get(3);
            Constants.TASK_DETAIL_ATTACHMENT_5 = attachmentList.get(4);

			Log.e("**** attch 1 ****", "" + Constants.TASK_DETAIL_ATTACHMENT_1);
            Intent i = new Intent(CurrentPostedMyTasks.this , MyTaskDetail.class);
            startActivity(i);

        }

  /*      protected void showConfirmationDialog(String message , final String task_id , final String to_id) {
		final Dialog dialog;
		dialog = new Dialog(CurrentPostedMyTasks.this);
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

		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

				CloseTask(task_id , to_id);
			}
		});

		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}*/

		protected void showConfirmationDialog(final String message , final String task_id , final String to_id) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(CurrentPostedMyTasks.this);

			// Setting Dialog Title
			alertDialog.setTitle("Exit...");

			// Setting Dialog Message
			alertDialog.setMessage(message);

			// Setting Icon to Dialog
			//alertDialog.setIcon(R.drawable.alarm);

			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					dialog.cancel();
					CloseTask(task_id , to_id);

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
		
		protected void CloseTask(String task_id , String to_id) {
			if (isConnected) {

				new CloseTask( task_id ,  to_id).execute(new Void[0]);
			} else {
				showDialog(Constants.No_INTERNET);
			}
		}

		class ViewHolder {

			TextView title, description, price;
			
			Button close;
			
			TextView view_task_detail;
			
			TextView  progress ,chat;
			
			LinearLayout ll;

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(CurrentPostedMyTasks.this, SelectTasker.class);
		startActivity(i);
	}
	
	//******************************** close task ***************************************
	
	
		public class CloseTask extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();

			HashMap result = new HashMap();

			ArrayList localArrayList = new ArrayList();

			String taskID, toID;

			public CloseTask(String task_id, String to_id) {
				this.taskID = task_id;
				this.toID = to_id ;
			}

		
			protected Void doInBackground(Void... paramVarArgs) {

				
				
				/*http://phphosting.osvin.net/TakeATask/WEB_API/setTaskCompleted.php?
					authkey=Auth_TakeATask2015&from_id=19&to_id=18&task_id=2&is_completed=0*/

				try {
					localArrayList.add(new BasicNameValuePair("authkey",
							"Auth_TakeATask2015"));
					localArrayList.add(new BasicNameValuePair("task_id",
							this.taskID));
					localArrayList.add(new BasicNameValuePair("to_id",
							this.toID));
					localArrayList.add(new BasicNameValuePair("from_id",
							Constants.USER_ID));
					localArrayList.add(new BasicNameValuePair("is_completed","0"));

					result = function.closeTask(localArrayList);

				} catch (Exception localException) {
					localException.printStackTrace();
				}

				return null;
			}

			protected void onPostExecute(Void paramVoid) {
				db.dismiss();

				try {
					if (result.get("ResponseCode").equals("true")) {

					Toast.makeText(getApplicationContext(), "Task closed successfully.", Toast.LENGTH_SHORT).show();
						Constants.TASK_DETAIL_USERID= this.toID;
						Intent i = new Intent(CurrentPostedMyTasks.this, AddRating.class);
						startActivity(i);
					} else if (result.get("ResponseCode").equals("false")) {
						
						String msg = "Something went wrong.Please try again.";
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
						//showDialog(msg);
					}
				}

				catch (Exception ae) {
					showDialog(Constants.ERROR_MSG);
				}

			}

			protected void onPreExecute() {
				super.onPreExecute();
				db = new TransparentProgressDialog(CurrentPostedMyTasks.this,
						R.drawable.loadingicon);
				db.show();
			}

		}
}
