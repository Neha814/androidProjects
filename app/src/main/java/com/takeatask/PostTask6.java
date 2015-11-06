package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import functions.Constants;
import utils.GPSTracker;
import utils.HttpClientUpload;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class PostTask6 extends Activity implements OnClickListener {

	Button continue_btn;
	ImageView back;
	boolean isConnected;
	boolean isSuccess = false;
	
	LinearLayout back_ll;

	GPSTracker gps;

	PostTask postTaskObj;

	TransparentProgressDialog db;

	TextView price, description, date, address, title, attachment, comment ,cat_name;
	
	TextView cityTV ,stateTV,countryTV,zipcodeTV;
	
	View post1 ,post2,post3,post4,post5,post6;
	
	LinearLayout l1 ,l2,l3,l4,l5,l6;
	
	List<String> addList = new ArrayList<String>();

	List<String> attachmentList = new ArrayList<String>();
	
	
	String addreess , city , state , country , zipcode;

	ImageView img1,img2,img3,img4,img5;

/*	protected void showDialog(String msg) {
		final Dialog dialog;
		dialog = new Dialog(PostTask6.this);
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
					Intent i = new Intent(PostTask6.this, Home.class);
					startActivity(i);
				}
			}
		});
		dialog.show();

	}*/
	
	public void showDialog(String msg) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(
					PostTask6.this).create();

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
						Intent i = new Intent(PostTask6.this, Home.class);
						startActivity(i);
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

		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_task_6);

		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		continue_btn = (Button) findViewById(R.id.continue_btn);
		price = (TextView) findViewById(R.id.price);
		address = (TextView) findViewById(R.id.address);
		date = (TextView) findViewById(R.id.date);
		title = (TextView) findViewById(R.id.title);
		description = (TextView) findViewById(R.id.description);
		attachment = (TextView) findViewById(R.id.attachment);
		comment = (TextView) findViewById(R.id.comment);
		cat_name = (TextView) findViewById(R.id.cat_name);
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
		img4 = (ImageView) findViewById(R.id.img4);
		img5 = (ImageView) findViewById(R.id.img5);

		
		cityTV = (TextView) findViewById(R.id.city);
		stateTV = (TextView) findViewById(R.id.state);
		countryTV = (TextView) findViewById(R.id.country);
		zipcodeTV = (TextView) findViewById(R.id.zipcode);
		
		post1 = (View) findViewById(R.id.post1);
		post2 = (View) findViewById(R.id.post2);
		post3 = (View) findViewById(R.id.post3);
		post4 = (View) findViewById(R.id.post4);
		post5 = (View) findViewById(R.id.post5);
		post6 = (View) findViewById(R.id.post6);
		
		l1 = (LinearLayout) findViewById(R.id.l1);
		l2 = (LinearLayout) findViewById(R.id.l2);
		l3 = (LinearLayout) findViewById(R.id.l3);
		l4 = (LinearLayout) findViewById(R.id.l4);
		l5 = (LinearLayout) findViewById(R.id.l5);
		l6 = (LinearLayout) findViewById(R.id.l6);
		
		Log.e("state===>>>>>",""+Constants.STATE);

		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());

		gps = new GPSTracker(PostTask6.this);
		if (gps.canGetLocation()) 
		{
			Constants.LATITUDE = gps.getLatitude();
			Constants.LONGITUDE = gps.getLongitude();

		}
		
		if(Constants.PRICE!=null && Constants.PRICE.length()>0) {
		price.setText(Constants.PRICE);
		
		} else {
			price.setText("Not Mentioned.");
		}
		
		
		if(Constants.GLOBAL_CATEGORY_NAME==null || Constants.GLOBAL_CATEGORY_NAME.equals("")){
			cat_name.setVisibility(View.GONE);
		} else {
			cat_name.setVisibility(View.VISIBLE);
			cat_name.setText(Constants.GLOBAL_CATEGORY_NAME) ;
		}
	
		
		//if(Constants.ADDRESS!=null && Constants.ADDRESS.length()>0){
		
		/*} else {
			address.setText("Not Mentioned.");
		}*/
		
	
		
		if(Constants.ADDRESS!=null && Constants.ADDRESS.length()>0){
			addreess =Constants.ADDRESS;
			address.setText(addreess);
			address.setVisibility(View.VISIBLE);
			} else {
				addreess="";
				address.setVisibility(View.GONE);
			}
		
		if(Constants.CITY!=null && Constants.CITY.length()>0){
			city =Constants.CITY;
			cityTV.setText(city);
			cityTV.setVisibility(View.VISIBLE);
		} else {
			city="";
			cityTV.setVisibility(View.GONE);
		}
		
		if(Constants.STATE!=null && Constants.STATE.length()>0){
			state = Constants.STATE;
			stateTV.setText(state);
			stateTV.setVisibility(View.VISIBLE);
			} else {
				state = "";
				stateTV.setVisibility(View.GONE);
			}
		
		if(Constants.COUNTRY!=null && Constants.COUNTRY.length()>0){
			country = Constants.COUNTRY;
			countryTV.setText(country);
			countryTV.setVisibility(View.VISIBLE);
			} else {
				country="";
				countryTV.setVisibility(View.GONE);
			}
		
		if(Constants.ZIPCODE!=null && Constants.ZIPCODE.length()>0){
			zipcode = Constants.ZIPCODE;
			zipcodeTV.setText(zipcode);
			zipcodeTV.setVisibility(View.VISIBLE);
			} else {
				zipcode="";
				zipcodeTV.setVisibility(View.GONE);
			}
		addList.add(addreess);
		addList.add(city);
		addList.add(state);
		addList.add(zipcode);
		
		addList.removeAll(Arrays.asList("", null));
		
		String ADDRESS_TEXT = addList.toString().replace("[", "")
				.replace("]", "").replace(", ", ", ");
		
		//address.setText(ADDRESS_TEXT);
		
		if(Constants.DATE!=null && Constants.DATE.length()>0){
		date.setText(Constants.DATE_TO_SHOW);
		} else {
			date.setText("Not Mentioned.");
		}
		
		if(Constants.DESCRIBE_TASK!=null && Constants.DESCRIBE_TASK.length()>0){
		description.setText(Constants.DESCRIBE_TASK);
		} else {
			description.setText("Not Mentioned.");
		}

		if(Constants.TASK_NAME!=null && Constants.TASK_NAME.length()>0){
		title.setText(Constants.TASK_NAME);
		} else {
			
		}

		attachmentList.clear();

		attachmentList.add(Constants.IMAGE_TO_UPLOAD1.getName());
		attachmentList.add(Constants.IMAGE_TO_UPLOAD2.getName());
		attachmentList.add(Constants.IMAGE_TO_UPLOAD3.getName());
		attachmentList.add(Constants.IMAGE_TO_UPLOAD4.getName());
		attachmentList.add(Constants.IMAGE_TO_UPLOAD5.getName());

		attachmentList.removeAll(Arrays.asList("", null));

		/*String ATTACHMENT_TEXT = attachmentList.toString().replace("[", "")
				.replace("]", "").replace(", ", ", ");*/

        String ATTACHMENT_TEXT = attachmentList.toString().replace("[", "")
                .replace("]", "").replace(", ", "\n");

		/*attachment.setText(Constants.IMAGE_TO_UPLOAD1.getName() + "\n"
				+ Constants.IMAGE_TO_UPLOAD2.getName() + "\n"
				+ Constants.IMAGE_TO_UPLOAD3.getName() + "\n"
				+ Constants.IMAGE_TO_UPLOAD4.getName() + "\n"
				+ Constants.IMAGE_TO_UPLOAD5.getName());*/

		attachment.setText(ATTACHMENT_TEXT);
		
		if(Constants.COMMENTS!=null && Constants.COMMENTS.length()>0){
		comment.setText(Constants.COMMENTS);
		} else {
			comment.setVisibility(View.GONE);
		}

		back.setOnClickListener(this);
		back_ll.setOnClickListener(this);
		continue_btn.setOnClickListener(this);
		post1.setOnClickListener(this);
		post2.setOnClickListener(this);
		post3.setOnClickListener(this);
		post4.setOnClickListener(this);
		post5.setOnClickListener(this);
		post6.setOnClickListener(this);
		
		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);
		l5.setOnClickListener(this);
		l6.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);

        // ************  set thumbnail ************************//
        try {
        if(img1!=null) {

                img1.setImageBitmap(Constants.TAKENIMAGE1);

        } else {
            img1.setVisibility(View.GONE);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(img2!=null) {

                img2.setImageBitmap(Constants.TAKENIMAGE2);

            } else {
                img2.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(img3!=null) {

                img3.setImageBitmap(Constants.TAKENIMAGE3);

            } else {
                img3.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(img4!=null) {

                img4.setImageBitmap(Constants.TAKENIMAGE4);

            } else {
                img4.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(img5!=null) {

                img5.setImageBitmap(Constants.TAKENIMAGE5);

            } else {
                img5.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void onClick(View v) {
		if (v == back || v==back_ll) {
			/*Intent i = new Intent(PostTask6.this , Home.class);
			startActivity(i);*/
			
			Intent i = new Intent(PostTask6.this , PostTask5.class);
			startActivity(i);
		}
        else if(v==img1){
            Constants.image_number = 1;
            Intent i = new Intent(PostTask6.this , ViewImage.class);
            startActivity(i);
        }else if(v==img2){
            Constants.image_number = 2;
            Intent i = new Intent(PostTask6.this , ViewImage.class);
            startActivity(i);
        }else if(v==img3){
            Constants.image_number = 3;
            Intent i = new Intent(PostTask6.this , ViewImage.class);
            startActivity(i);
        }else if(v==img4){
            Constants.image_number = 4;
            Intent i = new Intent(PostTask6.this , ViewImage.class);
            startActivity(i);
        }else if(v==img5){
            Constants.image_number = 5;
            Intent i = new Intent(PostTask6.this , ViewImage.class);
            startActivity(i);
        }

        else if (v == continue_btn) {
			

			if (isConnected) {

				postTaskObj = new PostTask(Constants.AUTH_KEY,
						Constants.USER_ID, Constants.TASK_NAME,
						Constants.DESCRIBE_TASK, Constants.ADDRESS,
						Constants.CITY, Constants.STATE, Constants.COUNTRY,
						Constants.ZIPCODE, Constants.COMMENTS,
						String.valueOf(Constants.LATITUDE),
						String.valueOf(Constants.LONGITUDE), Constants.DATE,
						Constants.PRICE, Constants.CATEGORY_ID,
						Constants.CATEGORY_NAME);
				postTaskObj.execute();

			} else {
				showDialog(Constants.No_INTERNET);
			}
		}else if(v==post1 || v ==l1){
		
			Intent i = new Intent(PostTask6.this , PostTask1.class);
			startActivity(i);
		}else if(v==post2 || v==l2){

			Intent i = new Intent(PostTask6.this , PostTask2.class);
			startActivity(i);
		}else if(v==post3 || v==l3){
	
			Intent i = new Intent(PostTask6.this , PostTask3.class);
			startActivity(i);
		}else if(v==post4 || v==l4){
			
			Intent i = new Intent(PostTask6.this , PostTask4.class);
			startActivity(i);
		}else if(v==post5 || v==l5){
			
			Intent i = new Intent(PostTask6.this , PostTask5.class);
			startActivity(i);
		}
	}

	public class PostTask extends AsyncTask<String, Void, String> {
		ByteArrayOutputStream baos;

		String authkey, user_id, ttitle, description, address, city, state,
				country, zipcode, comments, lat, lng, date, price, cat_id,
				cat_name;


		int maxBufferSize = 5 * 1024 * 1024;

		public PostTask(String aUTH_KEY, String uSER_ID, String tASK_NAME,
				String dESCRIBE_TASK, String aDDRESS, String cITY,
				String sTATE, String cOUNTRY, String zIPCODE, String cOMMENTS,
				String valueOf, String valueOf2, String dATE, String pRICE,
				String cATEGORY_ID, String cATEGORY_NAME) {

			this.authkey = aUTH_KEY;
			this.user_id = uSER_ID;
			this.ttitle = tASK_NAME;
			this.description = dESCRIBE_TASK;
			this.address = aDDRESS;
			this.city = cITY;
			this.state = sTATE;
			this.country = cOUNTRY;
			this.zipcode = zIPCODE;
			this.comments = cOMMENTS;
			this.lat = valueOf;
			this.lng = valueOf2;
			this.date = dATE;
			this.price = pRICE;
			this.cat_id = cATEGORY_ID;
			this.cat_name = cATEGORY_NAME;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(PostTask6.this,
					R.drawable.loadingicon);
			db.show();

		}

		@Override
		protected String doInBackground(String... Params) {
			try {
				baos = new ByteArrayOutputStream();
				Constants.TAKENIMAGE.compress(CompressFormat.PNG, 100, baos);


            }

			catch (Exception e) {
				Log.e("excptn==", "" + e);
			}

			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpClientUpload client = new HttpClientUpload(
						"https://takeataskservices.com/WEB_API/addTask.php?");
				client.connectForMultipart();

				 /* http://phphosting.osvin.net/TakeATask/WEB_API/addTask.php?
				  authkey
				  =Auth_TakeATask2015&user_id=38&title=want%20a%20boy%20to
				  %20clean%20a%20room
				  &description=clean%20a%20room&address=new%
				  20janta%20nagar&city=chandigarh
				  &state=chandigarh&country=india&zipcode=141003&comments=good
				  &
				  latitude=30.900965&longitude=75.857276&due_date=2015-07-15&budget
				  =50&category_id=1&category_name=*/

				Log.e("user_id", "" + user_id);
				Log.e("title", "" + ttitle);
				Log.e("description", "" + description);
				Log.e("address", "" + address);
				Log.e("city", "" + city);
				Log.e("state", "" + state);
				Log.e("country", "" + country);
				Log.e("zipcode", "" + zipcode);
				Log.e("comments", "" + comments);
				Log.e("latitude", "" + lat);
				Log.e("longitude", "" + lng);
				Log.e("due_date", "" + date);
				Log.e("budget", "" + price);
				Log.e("category_id", "" + cat_id);
				Log.e("category_name", "" + cat_name);

				client.addFormPart("authkey", this.authkey);
				client.addFormPart("user_id", this.user_id);
				client.addFormPart("title", this.ttitle);
				client.addFormPart("description", this.description);
				client.addFormPart("address", this.address);
				client.addFormPart("city", this.city);
				client.addFormPart("state", this.state);
				client.addFormPart("country", this.country);
				client.addFormPart("zipcode", this.zipcode);
				client.addFormPart("comments", this.comments);
				client.addFormPart("latitude", this.lat);
				client.addFormPart("longitude", this.lng);
				client.addFormPart("due_date", this.date);
				client.addFormPart("budget", this.price);
				client.addFormPart("category_id", this.cat_id);

				client.addFormPart("category_name", this.cat_name);

				if (!(Constants.IMAGE_TO_UPLOAD1.getName().equals("") || Constants.IMAGE_TO_UPLOAD1
						.getName() == null)) {

					/*
					 * attachment1 attachment2 attachment3 attachment4
					 * attachment5
					 */


                    /*********************** attachment 1 ***********************/

					Log.e("111", "111");
                    Log.e("name111", ""+Constants.IMAGE_TO_UPLOAD1.getName());

                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;

                    FileInputStream fileInputStream = new FileInputStream(Constants.IMAGE_TO_UPLOAD1);
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);



					client.addFilePart("attachment1",
                            Constants.IMAGE_TO_UPLOAD1.getName(), buffer);
				}

                /*********************** attachment 2 ***********************/


				if(!(Constants.IMAGE_TO_UPLOAD2.getName().equals("") || Constants.IMAGE_TO_UPLOAD2
						.getName() == null)){

                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;

                    FileInputStream fileInputStream = new FileInputStream(Constants.IMAGE_TO_UPLOAD2);
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

					Log.e("222", "222");
                    Log.e("name222", "" + Constants.IMAGE_TO_UPLOAD2.getName());



					client.addFilePart("attachment2",
							Constants.IMAGE_TO_UPLOAD2.getName(), buffer);
				}

                /*********************** attachment 3 ***********************/

				if(!(Constants.IMAGE_TO_UPLOAD3.getName().equals("") || Constants.IMAGE_TO_UPLOAD3
						.getName() == null)){

                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;

                    FileInputStream fileInputStream = new FileInputStream(Constants.IMAGE_TO_UPLOAD3);
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

					Log.e("333", "333");
                    Log.e("name333", "" + Constants.IMAGE_TO_UPLOAD3.getName());



					client.addFilePart("attachment3",
							Constants.IMAGE_TO_UPLOAD3.getName(),
							buffer);
				}

                /*********************** attachment 4 ***********************/

				if(!(Constants.IMAGE_TO_UPLOAD4.getName().equals("") || Constants.IMAGE_TO_UPLOAD4
						.getName() == null)){
					Log.e("444", "444");
                    Log.e("name444", ""+Constants.IMAGE_TO_UPLOAD4.getName());

                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;

                    FileInputStream fileInputStream = new FileInputStream(Constants.IMAGE_TO_UPLOAD4);
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);


					client.addFilePart("attachment4",
							Constants.IMAGE_TO_UPLOAD4.getName(),
							buffer);
				}

                /*********************** attachment 5 ***********************/

				if(!(Constants.IMAGE_TO_UPLOAD5.getName().equals("") || Constants.IMAGE_TO_UPLOAD5
						.getName() == null)){
					Log.e("555", "555");
                    Log.e("name555", ""+Constants.IMAGE_TO_UPLOAD5.getName());

                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;

                    FileInputStream fileInputStream = new FileInputStream(Constants.IMAGE_TO_UPLOAD5);
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);


					client.addFilePart("attachment5",
							Constants.IMAGE_TO_UPLOAD5.getName(),
							buffer);
				}
				

				client.finishMultipart();

				String data = client.getResponse();

				Log.e("data==", "" + data);

				return data;
			} catch (Throwable t) {
				t.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			db.dismiss();

			try {

				// "ResponseCode":true,"Message":"Profile update Successfully"
				


				JSONObject localJSONObject = new JSONObject(result);
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				String status = localJSONObject.getString("ResponseCode");
				if (status.equalsIgnoreCase("true")) {

					clearAllVariables();

					isSuccess = true;
					showDialog("Task is posted successfully.");

				} else {

					Toast.makeText(PostTask6.this, "Error occured...",
							Toast.LENGTH_SHORT).show();

				}
			} catch (Exception e) {
				Log.e("Exception===", "" + e);
				showDialog("Something went wrong while processing your request.Please try again.");
			}
		}

	}

	public void clearAllVariables() {
		Constants.TASK_NAME= "";
		Constants.DESCRIBE_TASK = "";
		Constants.ADDRESS = "";
		Constants.COUNTRY = "";
		Constants.STATE = "";
		Constants.ZIPCODE = "";
		Constants.CITY = "";
		Constants.PRICE = "";
		Constants.DATE = "";
		Constants.COMMENTS = "" ;

		Constants.CAT_POS=0;
		
		Constants.IMAGE_TO_UPLOAD1 = new File("");

		Constants.IMAGE_TO_UPLOAD2 = new File("");

		Constants.IMAGE_TO_UPLOAD3 = new File("");

		Constants.IMAGE_TO_UPLOAD4 = new File("");

		Constants.IMAGE_TO_UPLOAD5 = new File("");

        Constants.ATTACHMENTCOUNT = 0;

		Constants.TAKENIMAGE1 = null;
		Constants.TAKENIMAGE2 = null;
		Constants.TAKENIMAGE3 = null;
		Constants.TAKENIMAGE4 = null;
		Constants.TAKENIMAGE5 = null;



	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		
		/*clearAllVariables();
		Intent i = new Intent(PostTask6.this , Home.class);
		startActivity(i);*/
		
		Intent i = new Intent(PostTask6.this , PostTask5.class);
		startActivity(i);
	}
}
