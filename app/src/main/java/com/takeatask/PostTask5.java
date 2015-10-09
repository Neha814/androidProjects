package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import functions.Constants;

public class PostTask5 extends Activity implements OnClickListener {

	Button continue_btn;
	ImageView back;
	
	LinearLayout back_ll;

	ImageView attachment;
	Spinner category_spinner;

	EditText attachment_name1, attachment_name2, attachment_name3,
			attachment_name4, attachment_name5;
	EditText cat_name;

	public static ContentResolver appContext;
	Bitmap takenImage;
	File imgFileGallery;

	int cat_pos;
	
	int attachmentCount = 0;
	
	LinearLayout attachment_layout;

	LinearLayout ll2, ll3, ll4, ll5;
	
	View post1 ,post2,post3,post4,post5,post6;
	
	int count = 0;
	
	public static ArrayList<String> catList = new ArrayList<String>();
	
	LinearLayout l1 ,l2,l3,l4,l5,l6;

	/*protected void showDialog(String msg) {
		final Dialog dialog;
		dialog = new Dialog(PostTask5.this);
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
					PostTask5.this).create();

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

		setContentView(R.layout.post_task_5);

		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		continue_btn = (Button) findViewById(R.id.continue_btn);
		attachment = (ImageView) findViewById(R.id.attachment);

		attachment = (ImageView) findViewById(R.id.attachment);

		attachment_name1 = (EditText) findViewById(R.id.attachment_name1);
		attachment_name2 = (EditText) findViewById(R.id.attachment_name2);
		attachment_name3 = (EditText) findViewById(R.id.attachment_name3);
		attachment_name4 = (EditText) findViewById(R.id.attachment_name4);
		attachment_name5 = (EditText) findViewById(R.id.attachment_name5);
		attachment_layout = (LinearLayout) findViewById(R.id.attachment_layout);
		category_spinner = (Spinner) findViewById(R.id.category_spinner);

		cat_name = (EditText) findViewById(R.id.cat_name);
		
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

		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		ll4 = (LinearLayout) findViewById(R.id.ll4);
		ll5 = (LinearLayout) findViewById(R.id.ll5);

		ll2.setVisibility(View.GONE);
		ll3.setVisibility(View.GONE);
		ll4.setVisibility(View.GONE);
		ll5.setVisibility(View.GONE);

		appContext = getContentResolver();

		imgFileGallery = new File("");
		
		catList.clear();
		
		catList.add("Select Category");
		catList.addAll(Constants.GLOBAL_categoryList);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.simple_spinner_item,
				R.id.text, catList);
		category_spinner.setAdapter(dataAdapter);
		
		
		category_spinner.setSelection(Constants.CAT_POS);
		

		category_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						String category_text = category_spinner
								.getSelectedItem().toString();
						
						if(count==0){
							
						} else {
						Constants.CAT_POS = position ;
						}
						count++;
						int pos = position;
						cat_pos = pos;
						
						if(cat_pos==0){
							
						} else {
							cat_pos = cat_pos-1;
						}
						if (category_text.equalsIgnoreCase("Other")) {
							cat_name.setVisibility(View.VISIBLE);
						} else {
							cat_name.setVisibility(View.GONE);
						}
						
						Log.e("cat_pos==",""+cat_pos);
						Log.e("size==",""+Constants.GLOBAL_categoryListID.size());
						Log.e("Const.GLOBAL_catID==",""+Constants.GLOBAL_categoryListID);
						
						Constants.CATEGORY_ID = String
								.valueOf(Constants.GLOBAL_categoryListID
										.get(cat_pos));
						Constants.CATEGORY_NAME = category_text;
						
						Constants.GLOBAL_CATEGORY_NAME = category_text;
						
						Log.e("cat_id==",""+Constants.CATEGORY_ID);
						Log.e("cat_name==",""+Constants.CATEGORY_NAME);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		back.setOnClickListener(this);
		back_ll.setOnClickListener(this);
		continue_btn.setOnClickListener(this);
		attachment.setOnClickListener(this);
		attachment_layout.setOnClickListener(this);
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
		
		attachment_name1.setOnClickListener(this);
		attachment_name2.setOnClickListener(this);
		attachment_name3.setOnClickListener(this);
		attachment_name4.setOnClickListener(this);
		attachment_name5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == back || v==back_ll) {
			//finish();
			/*Intent i = new Intent(PostTask5.this , Home.class);
			startActivity(i);*/
			
			//SaveAndGo();
			Intent i = new Intent(PostTask5.this , PostTask4.class);
			startActivity(i);
		} else if (v == continue_btn) {
			String category_text = category_spinner.getSelectedItem()
					.toString();
			String cat_name_text = cat_name.getText().toString();

			if (category_text.equalsIgnoreCase("Other")
					&& cat_name_text.trim().length() < 1) {
				cat_name.setError("Please enter sub-category name");
			}
			
			else if(category_text.equalsIgnoreCase("Select Category")){
				Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_SHORT).show();
			}
			/*
			 * else if(!(imgFileGallery.getName().length()>3)){
			 * showDialog("Please select Attachment."); }
			 */
			else {

				try {
					if (!category_text.equalsIgnoreCase("Other")) {
						Constants.CATEGORY_ID = String
								.valueOf(Constants.GLOBAL_categoryListID
										.get(cat_pos));
						// Constants.CATEGORY_NAME = "";
						Constants.CATEGORY_NAME = category_spinner.getSelectedItem().toString();
					} else {
						Constants.CATEGORY_NAME = cat_name_text;
						//Constants.CATEGORY_ID = "";
						Constants.CATEGORY_ID = String
								.valueOf(Constants.GLOBAL_categoryListID
										.get(cat_pos));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent i = new Intent(PostTask5.this, PostTask6.class);
				startActivity(i);
			}
		} else if (v == attachment || v==attachment_layout
				|| v== attachment_name1 || v== attachment_name2 ||
				v== attachment_name3 || v==attachment_name4 || v== attachment_name5) {
			
			attachmentCount++;
			Intent GaleryIntent = new Intent(
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(GaleryIntent, 1);
		} 
		
		else if(v==post1 || v==l1){
		
			Intent i = new Intent(PostTask5.this , PostTask1.class);
			startActivity(i);
		}else if(v==post2 || v== l2){

			Intent i = new Intent(PostTask5.this , PostTask2.class);
			startActivity(i);
		}else if(v==post3 || v==l3){
	
			Intent i = new Intent(PostTask5.this , PostTask3.class);
			startActivity(i);
		}else if(v==post4 || v==l4){
			
			Intent i = new Intent(PostTask5.this , PostTask4.class);
			startActivity(i);
		}else if(v==post6 || v==l6){
			
			Intent i = new Intent(PostTask5.this , PostTask6.class);
			startActivity(i);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {

			if (requestCode == 1) {
				Uri selectedImage = data.getData();
				InputStream imageStream = null;
				try {
					imageStream = appContext.openInputStream(selectedImage);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("Exception==", "" + e);
				}
				takenImage = BitmapFactory.decodeStream(imageStream);

				Constants.TAKENIMAGE = takenImage;

				/**
				 * saving to file
				 */

				Uri SelectedImage = data.getData();
				String[] FilePathColumn = { MediaStore.Images.Media.DATA };

				Cursor SelectedCursor = appContext.query(SelectedImage,
						FilePathColumn, null, null, null);
				SelectedCursor.moveToFirst();

				int columnIndex = SelectedCursor
						.getColumnIndex(FilePathColumn[0]);
				String picturePath = SelectedCursor.getString(columnIndex);
				SelectedCursor.close();

				/*Log.e("picturePath==", "" + picturePath); */
				Log.e("File name===", "" + imgFileGallery.getName());

				imgFileGallery = new File(picturePath);

				Log.e("attachmentCount==>>", "" + attachmentCount);

			
				
				if(attachmentCount==1){
				attachment_name1.setText("" + imgFileGallery.getName());
				Constants.IMAGE_TO_UPLOAD1 = imgFileGallery;
				ll2.setVisibility(View.VISIBLE);
				} 
				else if(attachmentCount==2){
					ll2.setVisibility(View.VISIBLE);
					ll3.setVisibility(View.VISIBLE);
					attachment_name2.setText("" + imgFileGallery.getName());
					Constants.IMAGE_TO_UPLOAD2 = imgFileGallery;
				}
				else if(attachmentCount==3){
					ll3.setVisibility(View.VISIBLE);
					ll4.setVisibility(View.VISIBLE);
					attachment_name3.setText("" + imgFileGallery.getName());
					Constants.IMAGE_TO_UPLOAD3 = imgFileGallery;
				}
				else if(attachmentCount==4){
					ll4.setVisibility(View.VISIBLE);
					ll5.setVisibility(View.VISIBLE);
					attachment_name4.setText("" + imgFileGallery.getName());
					Constants.IMAGE_TO_UPLOAD4 = imgFileGallery;
				}
				else if(attachmentCount==5){
					ll5.setVisibility(View.VISIBLE);
					attachment_name5.setText("" + imgFileGallery.getName());
					Constants.IMAGE_TO_UPLOAD5 = imgFileGallery;
				}

			}
		} catch (Exception e) {
			attachmentCount--;
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
	/*	clearAllVariables();
		Intent i = new Intent(PostTask5.this , Home.class);
		startActivity(i);*/
		
		Intent i = new Intent(PostTask5.this , PostTask4.class);
		startActivity(i);
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
		
		Constants.IMAGE_TO_UPLOAD1 = new File("");

		Constants.IMAGE_TO_UPLOAD2 = new File("");

		Constants.IMAGE_TO_UPLOAD3 = new File("");

		Constants.IMAGE_TO_UPLOAD4 = new File("");

		Constants.IMAGE_TO_UPLOAD5 = new File("");


	}
}
