package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

public class FAQ extends Activity {

	ArrayList<String> question = new ArrayList<String>();

	ListView listview;
	
	ImageView back;
	
	LinearLayout back_ll;
	
	Boolean isConnected;
	
	MyAdapter mAdapter ;
	
	TransparentProgressDialog db;
	
	ArrayList<HashMap<String, String>> faqList = new ArrayList<HashMap<String,String>>();
	
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					FAQ.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("Alert !");

			// Setting Dialog Message
			alertDialog.setMessage(msg);

			// Setting Icon to Dialog
		//	alertDialog.setIcon(R.drawable.browse);

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

		setContentView(R.layout.faq);
		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		listview = (ListView) findViewById(R.id.listview);
		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);


		
		if (isConnected) {

			new GetFAQ().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Constants.GLOBAL_QUES = faqList.get(position).get("question") ;
				
				Constants.GLOBAL_ANS = faqList.get(position).get("ans") ;
				
				Intent i = new Intent(FAQ.this , FAQ_Answers.class);
				startActivity(i);
				
				
			}
			
		});
		
		back_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(FAQ.this , Home.class);
				startActivity(i);
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(FAQ.this , Home.class);
				startActivity(i);
			}
		});

	}

	
	public class GetFAQ extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

		//	http://phphosting.osvin.net/TakeATask/WEB_API/listFAQ.php?authkey=Auth_TakeATask2015
			try {
	
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));

				result = function.FAQ(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if(result.size()>0){
					faqList.addAll(result);
					mAdapter = new MyAdapter(getApplicationContext(),
							faqList);
					listview.setAdapter(mAdapter);
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(FAQ.this,
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

			return faqList.size();
		}

		@Override
		public Object getItem(int position) {
			return faqList.get(position);
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
						R.layout.faq_listitem, null);

				holder.question = (TextView) convertView.findViewById(R.id.question);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.question.setText(faqList.get(position).get("question"));
			
			return convertView;
		}

		class ViewHolder {
			TextView question;


		}

	}
}
