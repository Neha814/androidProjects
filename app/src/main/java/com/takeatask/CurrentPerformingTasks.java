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

public class CurrentPerformingTasks extends Activity implements OnClickListener{
	
	ListView listview;

	Button task_history;
	
	LinearLayout back_ll;

	boolean isConnected;

	MyAdapter mAdapter;
	
	//TextView post_task;
	
	ImageView back;

	ArrayList<HashMap<String, String>> currentPostedList = new ArrayList<HashMap<String, String>>();

	TransparentProgressDialog db;

/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(CurrentPerformingTasks.this);
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
					CurrentPerformingTasks.this).create();

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
		
		setContentView(R.layout.current_performing_tasks);
		
	
		listview = (ListView) findViewById(R.id.listview);
		task_history = (Button) findViewById(R.id.task_history);
		back = (ImageView) findViewById(R.id.back);
		back_ll =(LinearLayout) findViewById(R.id.back_ll);
		
		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		if (isConnected) {

			new GetCurrentPerformingTask().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
		
		back_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(CurrentPerformingTasks.this,
						SelectTasker.class);
				startActivity(i);
			}
		});
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(CurrentPerformingTasks.this,
						SelectTasker.class);
				startActivity(i);
			}
		});
		
		
		task_history.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		 if(v==task_history){
			Intent i = new Intent(CurrentPerformingTasks.this , History_Performing_Task.class);
			startActivity(i);
		}
	}
	
	
	public class GetCurrentPerformingTask extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

			
			/*http://phphosting.osvin.net/TakeATask/WEB_API/listUserTaskPerformed.php?
				authkey=Auth_TakeATask2015&user_id=38&is_history=1*/


			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("is_history", "0"));

				result = function.GetCurrentPerformingTask(localArrayList);

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
				db = new TransparentProgressDialog(CurrentPerformingTasks.this,
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
				convertView = mInflater.inflate(R.layout.current_listitem, null);

				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.description = (TextView) convertView
						.findViewById(R.id.description);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				
				holder.task_details = (TextView) convertView.findViewById(R.id.task_details);

                holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
				
				holder.complete = (Button) convertView.findViewById(R.id.complete);

                holder.chat = (TextView) convertView.findViewById(R.id.chat);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.task_details.setTag(position);
            holder.ll.setTag(position);
			holder.chat.setTag(position);
			holder.complete.setTag(position);
			
			SpannableString content1 = new SpannableString("View Task Details");
			content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
			holder.task_details.setText(content1);
			
			holder.title.setText(currentPostedList.get(position).get("title"));
			holder.description.setText(currentPostedList.get(position).get(
					"description"));
			holder.price.setText("$ " + currentPostedList.get(position).get("price"));
			
			holder.ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					int pos = (Integer) view.getTag();

					goToTaskDetailHistory(pos);


				}
			});
			holder.task_details.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();
					//Constants.VIEW_PROFILE_ID = currentPostedList.get(pos).get("user_id");
				    //ViewProfile();
                    goToTaskDetailHistory(pos);


				}
			});
            holder.chat.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();

                    Constants.RECEIVER_ID = currentPostedList.get(pos).get("user_id");
                    Constants.CHAT_NAME = currentPostedList.get(pos).get("fname") + " "
                            + currentPostedList.get(pos).get("lname");
                    Constants.CHAT_IMAGE = currentPostedList.get(pos).get("profile_pic");
                    Constants.SENDER_ID = Constants.USER_ID;

                    Intent i = new Intent(CurrentPerformingTasks.this, ChatScreen.class);
                    startActivity(i);
                }
            });


			
			holder.complete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();

					String paymentAdded = currentPostedList.get(pos).get("paymentAdded");
					String task_id = currentPostedList.get(pos).get("task_id");
					String to_id = currentPostedList.get(pos).get("user_id");

					if(paymentAdded.equals("1")) {
						Constants.REVIEW_TASK_ID = task_id;

						showConfirmationDialog("Are you sure you want to complete this Task ?", task_id, to_id);
					} else if(paymentAdded.equals("0")) {
						showDialog("Please add your PayPal ID in settings screen to complete your task.");
					}
					
				}
			});

			return convertView;
		}

		private void goToTaskDetailHistory(int pos) {
            Constants.TASK_DETAIL_ADDRESS = currentPostedList.get(pos).get("address");

            Constants.TASK_DETAIL_CITY =currentPostedList.get(pos).get("city") ;
            Constants.TASK_DETAIL_STATE = currentPostedList.get(pos).get("state");
            Constants.TASK_DETAIL_COUNTRY =currentPostedList.get(pos).get("country") ;
            Constants.TASK_DETAIL_ZIPCODE = currentPostedList.get(pos).get("zipcode");


            Constants.TASK_DETAIL_DATE = currentPostedList.get(pos).get("due_date");

            Constants.TASK_DETAIL_DESC = currentPostedList.get(pos).get("description");
            Constants.TASK_DETAIL_PRICE = currentPostedList.get(pos).get("price");
            Constants.TASK_DETAIL_TITLE = currentPostedList.get(pos).get("title");
           // Constants.TASK_DETAIL_URL = currentPostedList.get(pos).get("file");
            Constants.TASK_DETAIL_USERID = currentPostedList.get(pos).get("user_id");
            Constants.TASK_DETAIL_ID = currentPostedList.get(pos).get("task_id");
            Constants.TASK_DETAIL_FNAME = currentPostedList.get(pos).get("fname");
            Constants.TASK_DETAIL_LNAME = currentPostedList.get(pos).get("lname");
            Constants.TASK_DETAIL_CATNAME = currentPostedList.get(pos).get("category_name");
            Constants.TASK_DETAIL_SUBCATNAME = currentPostedList.get(pos).get("subcategory_name");
			Constants.TASK_DETAIL_COMMENTS = currentPostedList.get(pos).get("comments");
            Constants.TASK_DETAIL_ACCEPTED = "";
            Constants.TASK_DETAIL_TASKER_POSTER_NAME = "";
            Constants.TASK_DETAIL_TASKER_POSTER_ID = "";

            String attachmentListString  = currentPostedList.get(pos).get("attachments");

            String str = attachmentListString;
            List<String> attachmentList = Arrays.asList(str.split(","));



            Constants.TASK_DETAIL_ATTACHMENT_1 = attachmentList.get(0);
            Constants.TASK_DETAIL_ATTACHMENT_2 = attachmentList.get(1);
            Constants.TASK_DETAIL_ATTACHMENT_3 = attachmentList.get(2);
            Constants.TASK_DETAIL_ATTACHMENT_4 = attachmentList.get(3);
            Constants.TASK_DETAIL_ATTACHMENT_5 = attachmentList.get(4);

			Log.e("**** attch 1 ****", "" + Constants.TASK_DETAIL_ATTACHMENT_1);
            Intent i = new Intent(CurrentPerformingTasks.this , TaskDetailHistory.class);
            startActivity(i);
		}

		protected void ViewProfile() {
			
			Intent i = new Intent(CurrentPerformingTasks.this , ViewProfile.class);
			startActivity(i);
		}

		class ViewHolder {
			
			TextView title, description, price ;
			
			Button complete;
			
			TextView task_details;

			LinearLayout ll;

			TextView chat;

		}

	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();		
		Intent i = new Intent(CurrentPerformingTasks.this, SelectTasker.class);
		startActivity(i);
	}
	


	protected void showConfirmationDialog(final String message , final String task_id , final String to_id) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(CurrentPerformingTasks.this);

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
                CompleteTask(task_id , to_id);

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

	protected void CompleteTask(String task_id , String to_id) {
		if (isConnected) {

			new CompleteTask( task_id ,  to_id).execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}

	
	//******************************** complete task ***************************************
	
	
	public class CompleteTask extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String taskID, toID;

		public CompleteTask(String task_id , String to_id) {

			this.taskID = task_id;
			this.toID = to_id ;
		}

		protected Void doInBackground(Void... paramVarArgs) {

			
			
			/*http://phphosting.osvin.net/TakeATask/WEB_API/setTaskCompleted.php?
				authkey=Auth_TakeATask2015&from_id=19&to_id=18&task_id=2&is_completed=1*/



			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("task_id",
						this.taskID));
				localArrayList.add(new BasicNameValuePair("to_id",
						this.toID));
				localArrayList.add(new BasicNameValuePair("from_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("is_completed","1"));

				result = function.completeTask(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {


				Toast.makeText(getApplicationContext(), "Task completed successfully.", Toast.LENGTH_SHORT).show();

					Constants.TASK_DETAIL_USERID= this.toID;
				Intent i = new Intent(CurrentPerformingTasks.this, AddRating.class);
				startActivity(i);
				} else if (result.get("ResponseCode").equals("false")) {
					
					String msg = "Something went wrong while completing your request.Please try again.";
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
			db = new TransparentProgressDialog(CurrentPerformingTasks.this,
					R.drawable.loadingicon);
			db.show();
		}

	}
	
	
	
}