package com.takeatask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fragments.HomeFragment;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class Home extends FragmentActivity implements OnClickListener {

	ActionBarDrawerToggle actionBarDrawerToggle;
	DrawerLayout drawerLayout;
	ListView left_drawer;
	String[] lvMenuItems1 = new String[10];
	Button menu_button;
	
	TransparentProgressDialog db;

	Boolean isConnected;
	Button logout;

	SharedPreferences sp;
	
	ImageButton close_menu;

	MyListAdapter mAdapter;

	public String[] lvMenuItems2;
	public Integer[] images1;
	
	FragmentManager fm;
	FragmentTransaction ft;
	
	int UnreadMsgCount = 0;

	Integer[] images = { R.drawable.post_task, R.drawable.browse,
			R.drawable.my_task,R.drawable.my_task, R.drawable.message, R.drawable.profile,
			R.drawable.reviews, R.drawable.setting,
			R.drawable.faq, R.drawable.logout_menu };

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.home);

		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		Constants.EMAIL = sp.getString("email", "");
		Constants.USER_ID = sp.getString("user_id", "");
		
		Editor e = sp.edit();
		e.putBoolean("inHome", true);
		e.commit();
		
		
		

		menu_button = (Button) findViewById(R.id.menu_button);
		drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
		left_drawer = (ListView) findViewById(R.id.left_drawer);
		logout = (Button) findViewById(R.id.logout);

		logout.setOnClickListener(this);
		menu_button.setOnClickListener(this);

		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.string.app_name, R.string.app_name) {
			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {

			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				
				callMessageCountAPI();
			}
		};

		lvMenuItems1[0] = "Post A Task";
		lvMenuItems1[1] = "Take A Task";
		lvMenuItems1[2] = "My Tasks";
		lvMenuItems1[3] = "Bidding List";
		lvMenuItems1[4] = "Messages";
		lvMenuItems1[5] = "Profile";

		lvMenuItems1[6] = "Reviews";
	//	lvMenuItems1[7] = "Notifications";
		lvMenuItems1[7] = "Settings";
		lvMenuItems1[8] = "FAQ";
		lvMenuItems1[9] = "Logout";

		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header,
				left_drawer, false);
		left_drawer.addHeaderView(header, null, false);

		close_menu = (ImageButton) header.findViewById(R.id.close_menu);
		mAdapter = new MyListAdapter(lvMenuItems1, images, Home.this);
		left_drawer.setAdapter(mAdapter);
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		
		close_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawer(left_drawer);
			}
		});
		
		left_drawer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("positon===",""+position);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						view.getApplicationWindowToken(), 0);
				 fm = Home.this
						.getSupportFragmentManager();
				 ft = fm.beginTransaction();
				Fragment fragment = null;
				
				lvMenuItems1[0] = "Post A Task";
				lvMenuItems1[1] = "Take A Task";
				lvMenuItems1[2] = "My Tasks";
				lvMenuItems1[3] = "Bidding List";
				lvMenuItems1[4] = "Messages";
				lvMenuItems1[5] = "Profile";

				lvMenuItems1[6] = "Reviews";
			//	lvMenuItems1[7] = "Notifications";
				lvMenuItems1[7] = "Settings";
				lvMenuItems1[8] = "FAQ";
				lvMenuItems1[9] = "Logout";

				
				if(position==10){
					CallLogoutAPi();
				}else if(position==8){
					/*fragment = new Settings();
					showFragment(fragment);*/
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , Settings.class);
					
					startActivity(i);
				} else if(position==6){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , DefaultProfile.class);
				
					startActivity(i);
				}
				else if(position==4){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , BiddingList.class);
				
					startActivity(i);
				}
				else if(position==1){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , PostTask1.class);
					startActivity(i);
				}
				
				else if(position==3){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , SelectTasker.class);
					startActivity(i);
				}
				else if(position==5){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , MessageList.class);
					startActivity(i);
				} else if(position==7){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , Reviews.class);
					startActivity(i);
				}
				/*else if(position==8){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , AlertNotification.class);
					startActivity(i);
				}*/
				else if(position==2){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , TakeATask.class);
					startActivity(i);
				}else if(position==9){
					drawerLayout.closeDrawer(left_drawer);
					Intent i = new Intent(Home.this , FAQ.class);
					startActivity(i);
				}
				
			}  
		});
		
		
		
		/**
		 * add fragment as initial fragment
		 */
		
		FragmentManager fm  = Home.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		Fragment fragment = null;
		
		fragment = new  HomeFragment();
		
		if(fragment!=null){
			 ft.replace(R.id.frame_layout, fragment);
		} else {
			ft.add(R.id.frame_layout, fragment);
		}
		
		//ft.addToBackStack(null);
		ft.commit();
	}


	


	protected void callMessageCountAPI() {
		if (isConnected) {

			new GetUnreadMessageCount().execute(new Void[0]);
		} else {
			showDialog(Constants.No_INTERNET);
		}
	}





	protected void showFragment(Fragment fragment) {
		try {
			
				try {
					fm.popBackStack(
							null,
							FragmentManager.POP_BACK_STACK_INCLUSIVE);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"stack problem", Toast.LENGTH_SHORT)
							.show();
				}
				drawerLayout.closeDrawer(left_drawer);

				// addInitialFragmnet();
				if (fragment != null) {
					ft.replace(R.id.frame_layout, fragment);
				} else {
					ft.add(R.id.frame_layout, fragment);
				}
				ft.addToBackStack(null);
				ft.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}


	class MyListAdapter extends BaseAdapter {
		Resources res = getResources();

		LayoutInflater mInflater = null;

		public MyListAdapter(String[] lvMenuItems1, Integer[] images,
				Home context) {

			lvMenuItems2 = lvMenuItems1;
			images1 = images;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {

			return images1.length;
		}

		@Override
		public Object getItem(int position) {

			return images1[position];
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder;
			//if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.menu_list_item, null);

				holder.text2 = (TextView) convertView.findViewById(R.id.text2);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				holder.msgCount = (TextView) convertView.findViewById(R.id.msgCount);
				holder.view = (View) convertView.findViewById(R.id.view);
				
				

			try {
				holder.text2.setText(lvMenuItems2[position]);
				holder.image.setImageResource(images1[position]);
				
				if(holder.text2.getText().equals("Messages")){
					
					
					if(UnreadMsgCount>0){
						holder.msgCount.setVisibility(View.VISIBLE);
						holder.msgCount.setText(UnreadMsgCount+"");
					} else {
						holder.msgCount.setVisibility(View.GONE);
					}
				} else {
					holder.msgCount.setVisibility(View.GONE);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}

	}

	class ViewHolder {
		TextView text2 ,msgCount;
		ImageView image;
		View view;

	}

	@Override
	public void onClick(View v) {
		if (v == logout) {
			CallLogoutAPi();
		} else if(v==menu_button){
			drawerLayout.openDrawer(left_drawer);
		}
	}

	private void CallLogoutAPi() {
		showLogoutDialog();
	}

	/*protected void showLogoutDialog() {
		final Dialog dialog;
		dialog = new Dialog(Home.this);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(0);
		dialog.getWindow().setBackgroundDrawable(d);

		Button yes, no;

		dialog.setContentView(R.layout.logout);
		yes = (Button) dialog.findViewById(R.id.yes);
		no = (Button) dialog.findViewById(R.id.no);

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Editor e = sp.edit();
				e.putBoolean("inHome", false);
				e.commit();
				
				*//**
				 * to clear shared Prefrences
				 *//*
				
				try {
					Constants.mPrefs = getPreferences(MODE_PRIVATE);
					Constants.mPrefs.edit().clear().commit();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				try {
					sp.edit().clear().commit();
				}catch(Exception e2){
					e2.printStackTrace();
				}
				
				Intent i = new Intent(Home.this, Login.class);
				Constants.USER_ID = "";
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				
				

				dialog.dismiss();
			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();
	}*/
	
	public void showLogoutDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
		 
        // Setting Dialog Title
        alertDialog.setTitle("Exit...");
 
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to Logout?");
 
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);
 
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
 
            	 dialog.cancel();

				new Logout().execute(new Void[0]);


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



	public class Logout extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();




		protected Void doInBackground(Void... paramVarArgs) {

			/*http://phphosting.osvin.net/TakeATask/WEB_API/logout.php?
			authkey=Auth_TakeATask2015&emailId=singhrobin44@hotmail.com&
			device_id=0&token_id=APA91bFKJR50YVp8stswiZKpsVp4K6z_lcycDG3ZqeeACEad*/
			try {
				localArrayList.add(new BasicNameValuePair("emailId",
						Constants.EMAIL));
				localArrayList.add(new BasicNameValuePair("token_id",
					Constants.REGISTRATIO_ID));
				localArrayList.add(new BasicNameValuePair("device_id",
						"0"));
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));

				result = function.logout(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get("ResponseCode").equals("true")) {
					Editor e = sp.edit();
					e.putBoolean("inHome", false);
					e.commit();


					//to clear shared Prefrences


					try {
						Constants.mPrefs = getPreferences(MODE_PRIVATE);
						Constants.mPrefs.edit().clear().commit();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					try {
						sp.edit().clear().commit();
					}catch(Exception e2){
						e2.printStackTrace();
					}

					Intent i = new Intent(Home.this, Login.class);
					Constants.USER_ID = "";
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();

				} else if (result.get("ResponseCode").equals("false")) {
						Toast.makeText(getApplicationContext(),"Problem Occurred.",Toast.LENGTH_SHORT).show();
				}
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Home.this,
					R.drawable.loadingicon);
			db.show();
		}

	}

/*	protected void showDialog(String msg) {
		final Dialog dialog;
		dialog = new Dialog(Home.this);
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

	}*/
	
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					Home.this).create();

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		
		CallLogoutAPi();
	}
	
	
	
	//**************************** get unread message Count *********************************//
	
	public class GetUnreadMessageCount extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {

		//http://phphosting.osvin.net/TakeATask/WEB_API/countMessages.php?authkey=Auth_TakeATask2015&user_id=38
			try {
	
				localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));
				localArrayList.add(new BasicNameValuePair("user_id",Constants.USER_ID));

				result = function.GetUnreadMessageCount(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			

			try {
				if (result.get("ResponseCode").equals("true")) {
					UnreadMsgCount = Integer.parseInt((String)result.get("TotalMessage"));
					
					Log.e("unreadMsg==>>>",""+UnreadMsgCount);
					
					mAdapter.notifyDataSetChanged();
				}
				
			}

			catch (Exception ae) {
				showDialog(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			
		}

	}
}
