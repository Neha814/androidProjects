package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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

    Spinner category_spinner;

    EditText attachment_name1, attachment_name2, attachment_name3,
            attachment_name4, attachment_name5;
    EditText cat_name;

    public static ContentResolver appContext;
    Bitmap takenImage;
    File imgFileGallery;

    int cat_pos;

    int attachmentCount = Constants.ATTACHMENTCOUNT;

    ImageView c_1, c_2, c_3, c_4, c_5, c_1_1;

    LinearLayout c_l_1, c_l_2, c_l_3, c_l_4, c_l_5  ,l_1;

    LinearLayout ll2, ll3, ll4, ll5;

    View post1, post2, post3, post4, post5, post6;

    int count = 0;

    public static ArrayList<String> catList = new ArrayList<String>();

    LinearLayout l1, l2, l3, l4, l5, l6;

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


        attachment_name1 = (EditText) findViewById(R.id.attachment_name1);
        attachment_name2 = (EditText) findViewById(R.id.attachment_name2);
        attachment_name3 = (EditText) findViewById(R.id.attachment_name3);
        attachment_name4 = (EditText) findViewById(R.id.attachment_name4);
        attachment_name5 = (EditText) findViewById(R.id.attachment_name5);
        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        cat_name = (EditText) findViewById(R.id.cat_name);
        c_1 = (ImageView) findViewById(R.id.c_1);
        c_2 = (ImageView) findViewById(R.id.c_2);
        c_3 = (ImageView) findViewById(R.id.c_3);
        c_4 = (ImageView) findViewById(R.id.c_4);
        c_5 = (ImageView) findViewById(R.id.c_5);
        c_l_1 = (LinearLayout) findViewById(R.id.c_l_1);
        c_l_2 = (LinearLayout) findViewById(R.id.c_l_2);
        c_l_3 = (LinearLayout) findViewById(R.id.c_l_3);
        c_l_4 = (LinearLayout) findViewById(R.id.c_l_4);
        c_l_5 = (LinearLayout) findViewById(R.id.c_l_5);
        c_1_1 = (ImageView) findViewById(R.id.c_1_1);
        l_1 = (LinearLayout) findViewById(R.id.l_1);


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

                        try {
                        String category_text = category_spinner
                                .getSelectedItem().toString();

                        if (count == 0) {

                        } else {
                            Constants.CAT_POS = position;
                        }
                        count++;
                        int pos = position;
                        cat_pos = pos;

                        if (cat_pos == 0) {

                        } else {
                            cat_pos = cat_pos - 1;
                        }
                        if (category_text.equalsIgnoreCase("Other")) {
                            cat_name.setVisibility(View.VISIBLE);
                        } else {
                            cat_name.setVisibility(View.GONE);
                        }

                        Log.e("cat_pos==", "" + cat_pos);
                        Log.e("size==", "" + Constants.GLOBAL_categoryListID.size());
                        Log.e("Const.GLOBAL_catID==", "" + Constants.GLOBAL_categoryListID);

                        Constants.CATEGORY_ID = String
                                .valueOf(Constants.GLOBAL_categoryListID
                                        .get(cat_pos));
                        Constants.CATEGORY_NAME = category_text;

                        Constants.GLOBAL_CATEGORY_NAME = category_text;

                        Log.e("cat_id==", "" + Constants.CATEGORY_ID);
                        Log.e("cat_name==", "" + Constants.CATEGORY_NAME);
                    } catch(Exception e){
                            e.printStackTrace();
                        }
                }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

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

        attachment_name1.setOnClickListener(this);
        attachment_name2.setOnClickListener(this);
        attachment_name3.setOnClickListener(this);
        attachment_name4.setOnClickListener(this);
        attachment_name5.setOnClickListener(this);

        c_1.setOnClickListener(this);
        c_2.setOnClickListener(this);
        c_3.setOnClickListener(this);
        c_4.setOnClickListener(this);
        c_5.setOnClickListener(this);
        c_l_1.setOnClickListener(this);
        c_l_2.setOnClickListener(this);
        c_l_3.setOnClickListener(this);
        c_l_4.setOnClickListener(this);
        c_l_5.setOnClickListener(this);

        c_1_1.setOnClickListener(this);
        l_1.setOnClickListener(this);


        Log.e("IMAGE_TO_UPLOAD1==", "" + Constants.IMAGE_TO_UPLOAD1);
        Log.e("IMAGE_TO_UPLOAD2==", "" + Constants.IMAGE_TO_UPLOAD2);
        Log.e("IMAGE_TO_UPLOAD3==", "" + Constants.IMAGE_TO_UPLOAD3);
        Log.e("IMAGE_TO_UPLOAD4==", "" + Constants.IMAGE_TO_UPLOAD4);
        Log.e("IMAGE_TO_UPLOAD5==", "" + Constants.IMAGE_TO_UPLOAD5);




        if(Constants.IMAGE_TO_UPLOAD1.getName().length()>4){
            Log.e("111", "" );
            attachment_name1.setText(Constants.IMAGE_TO_UPLOAD1.getName());
            ll2.setVisibility(View.VISIBLE);
        }

         if(Constants.IMAGE_TO_UPLOAD2.getName().length()>4){
            Log.e("222", "" );
            ll2.setVisibility(View.VISIBLE);
             ll3.setVisibility(View.VISIBLE);
            attachment_name2.setText(Constants.IMAGE_TO_UPLOAD2.getName());
        }

         if(Constants.IMAGE_TO_UPLOAD3.getName().length()>4){
            Log.e("333", "" );
            ll3.setVisibility(View.VISIBLE);
             ll4.setVisibility(View.VISIBLE);
            attachment_name3.setText(Constants.IMAGE_TO_UPLOAD3.getName());
        }


         if(Constants.IMAGE_TO_UPLOAD4.getName().length()>4){
            Log.e("444", "" );
            ll4.setVisibility(View.VISIBLE);
             ll5.setVisibility(View.VISIBLE);
            attachment_name4.setText(Constants.IMAGE_TO_UPLOAD4.getName());
        }


         if(Constants.IMAGE_TO_UPLOAD5.getName().length()>4){
            Log.e("555", "" );
            ll5.setVisibility(View.VISIBLE);
            attachment_name5.setText(Constants.IMAGE_TO_UPLOAD5.getName());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == back || v == back_ll) {
            //finish();
			/*Intent i = new Intent(PostTask5.this , Home.class);
			startActivity(i);*/

            //SaveAndGo();
            Intent i = new Intent(PostTask5.this, PostTask4.class);
            startActivity(i);
        } else if (v == continue_btn) {
            String category_text = category_spinner.getSelectedItem()
                    .toString();
            String cat_name_text = cat_name.getText().toString();

            if (category_text.equalsIgnoreCase("Other")
                    && cat_name_text.trim().length() < 1) {
                cat_name.setError("Please enter sub-category name");
            } else if (category_text.equalsIgnoreCase("Select Category")) {
                Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_SHORT).show();
            } else {

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
        } else if (v == attachment_name1 || v == attachment_name2 ||
                v == attachment_name3 || v == attachment_name4 || v == attachment_name5) {

            attachmentCount++;
            Intent GaleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(GaleryIntent, 1);
        } else if (v == post1 || v == l1) {
            boolean isValid = ValidateFields();

            if (isValid) {

                Intent i = new Intent(PostTask5.this, PostTask1.class);
                startActivity(i);
            }
        } else if (v == post2 || v == l2) {
            boolean isValid = ValidateFields();

            if (isValid) {
                Intent i = new Intent(PostTask5.this, PostTask2.class);
                startActivity(i);
            }
        } else if (v == post3 || v == l3) {
            boolean isValid = ValidateFields();

            if (isValid) {
                Intent i = new Intent(PostTask5.this, PostTask3.class);
                startActivity(i);
            }
        } else if (v == post4 || v == l4) {
            boolean isValid = ValidateFields();

            if (isValid) {
                Intent i = new Intent(PostTask5.this, PostTask4.class);
                startActivity(i);
            }
        } else if (v == post6 || v == l6) {
            boolean isValid = ValidateFields();

            if (isValid) {
                Intent i = new Intent(PostTask5.this, PostTask6.class);
                startActivity(i);
            }
        }

        else if(v==c_1 || v==c_l_1){
            attachment_name1.setText("");
            attachment_name1.setHint("Attachment");
            Constants.IMAGE_TO_UPLOAD1 = new File("");
            Constants.TAKENIMAGE1 = null;
        }
        else if(v==c_2 || v==c_l_2){
            attachment_name2.setText("");
            attachment_name2.setHint("Attachment");
            Constants.IMAGE_TO_UPLOAD2 = new File("");
            Constants.TAKENIMAGE2 = null;
        }
        else if(v==c_3 || v==c_l_3){
            attachment_name3.setText("");
            attachment_name3.setHint("Attachment");
            Constants.IMAGE_TO_UPLOAD3 = new File("");
            Constants.TAKENIMAGE3 = null;
        }
        else if(v==c_4 || v==c_l_4){
            attachment_name4.setText("");
            attachment_name4.setHint("Attachment");
            Constants.IMAGE_TO_UPLOAD4 = new File("");
            Constants.TAKENIMAGE4 = null;
        }
        else if(v==c_5 || v==c_l_5){
            attachment_name5.setText("");
            attachment_name5.setHint("Attachment");
            Constants.IMAGE_TO_UPLOAD5 = new File("");
            Constants.TAKENIMAGE5 = null;
        } else if(v==l_1 || v== c_1_1){


            Constants.ATTACHMENTCOUNT = 0;
            attachment_name1.setText("");
            attachment_name1.setHint("Attachment");

            attachment_name2.setText("");
            attachment_name2.setHint("Attachment");

            attachment_name3.setText("");
            attachment_name3.setHint("Attachment");

            attachment_name4.setText("");
            attachment_name4.setHint("Attachment");

            attachment_name5.setText("");
            attachment_name5.setHint("Attachment");
            attachmentCount = 0;
            Constants.IMAGE_TO_UPLOAD1 = new File("");

            Constants.IMAGE_TO_UPLOAD2 = new File("");

            Constants.IMAGE_TO_UPLOAD3 = new File("");

            Constants.IMAGE_TO_UPLOAD4 = new File("");

            Constants.IMAGE_TO_UPLOAD5 = new File("");



            Constants.TAKENIMAGE = null;
            Constants.TAKENIMAGE1 = null;
            Constants.TAKENIMAGE2 = null;
            Constants.TAKENIMAGE3 = null;
            Constants.TAKENIMAGE4 = null;
            Constants.TAKENIMAGE5 = null;

            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);

        }
    }

    private boolean ValidateFields() {

        String category_text = category_spinner.getSelectedItem()
                .toString();
        String cat_name_text = cat_name.getText().toString();

        if (category_text.equalsIgnoreCase("Other")
                && cat_name_text.trim().length() < 1) {
            cat_name.setError("Please enter sub-category name");
            return false;
        } else if (category_text.equalsIgnoreCase("Select Category")) {
            Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_SHORT).show();
            return false;
        } else {

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
            return true;
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
                String[] FilePathColumn = {MediaStore.Images.Media.DATA};

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

                long fileSize = imgFileGallery.length();

                Log.e("fileSize==>>", "" + fileSize);

                if (fileSize>=5000000) {
                    showDialog("File size should not be greater than 5MB.");

                } else {
                if (attachmentCount == 1) {
                    Constants.TAKENIMAGE1 = takenImage;
                    attachment_name1.setText("" + imgFileGallery.getName());
                    Constants.IMAGE_TO_UPLOAD1 = imgFileGallery;
                    ll2.setVisibility(View.VISIBLE);
                } else if (attachmentCount == 2) {
                    Constants.TAKENIMAGE2 = takenImage;
                    ll2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    attachment_name2.setText("" + imgFileGallery.getName());
                    Constants.IMAGE_TO_UPLOAD2 = imgFileGallery;
                } else if (attachmentCount == 3) {
                    Constants.TAKENIMAGE3 = takenImage;
                    ll3.setVisibility(View.VISIBLE);
                    ll4.setVisibility(View.VISIBLE);
                    attachment_name3.setText("" + imgFileGallery.getName());
                    Constants.IMAGE_TO_UPLOAD3 = imgFileGallery;
                } else if (attachmentCount == 4) {
                    Constants.TAKENIMAGE4 = takenImage;
                    ll4.setVisibility(View.VISIBLE);
                    ll5.setVisibility(View.VISIBLE);
                    attachment_name4.setText("" + imgFileGallery.getName());
                    Constants.IMAGE_TO_UPLOAD4 = imgFileGallery;
                } else if (attachmentCount == 5) {
                    Constants.TAKENIMAGE5 = takenImage;
                    ll5.setVisibility(View.VISIBLE);
                    attachment_name5.setText("" + imgFileGallery.getName());
                    Constants.IMAGE_TO_UPLOAD5 = imgFileGallery;
                }
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

        Intent i = new Intent(PostTask5.this, PostTask4.class);
        startActivity(i);
    }

    public void clearAllVariables() {
        Constants.TASK_NAME = "";
        Constants.DESCRIBE_TASK = "";
        Constants.ADDRESS = "";
        Constants.COUNTRY = "";
        Constants.STATE = "";
        Constants.ZIPCODE = "";
        Constants.CITY = "";
        Constants.PRICE = "";
        Constants.DATE = "";
        Constants.COMMENTS = "";

        Constants.IMAGE_TO_UPLOAD1 = new File("");

        Constants.IMAGE_TO_UPLOAD2 = new File("");

        Constants.IMAGE_TO_UPLOAD3 = new File("");

        Constants.IMAGE_TO_UPLOAD4 = new File("");

        Constants.IMAGE_TO_UPLOAD5 = new File("");


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return true;
        }
    }
}
