package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imageloader.ImageLoader;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import functions.Constants;
import functions.Functions;
import utils.HttpClientUpload;
import utils.NetConnection;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 14/10/15.
 */
public class TaskDetailCopy extends Activity {

    // ImageView task_image;
    ImageView back;
    EditText title, price, description;
    EditText message;
    TextView date;

    LinearLayout back_ll;

    EditText address;

    EditText cityET, stateET, countryET, zipcodeET;

    LinearLayout price_layout;

    List<String> addList = new ArrayList<String>();

    ImageLoader imageLoader;

    ImageView edit, delete, del;

    boolean isConnected;

    LinearLayout ll_edit, ll_delete, ll_del;

    TextView Message_static, file_name, cat_name, file_name2,file_name3,file_name4,file_name5;

    String addreess, city, state, country, zipcode;

    TransparentProgressDialog db;

    Button Done, apply;

    EditText bid_price;

    boolean isSuccessfullyDeleted = false;

    boolean startEditing = false;

    boolean isSuccess = false;

    private Calendar cal;
    private int day;
    private int month;
    private int year;

    TextView name,comments;

    boolean isPostSuccess = false;

    updateProfileTask updateProfileObj;

    Bitmap takenImage;
    File imgFileGallery;
    public static ContentResolver appContext;

/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(TaskDetail.this);
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

					if (isSuccessfullyDeleted) {
						Intent i = new Intent(TaskDetail.this, TakeATask.class);
						startActivity(i);
					} else if (isPostSuccess) {
						Intent i = new Intent(TaskDetail.this, TakeATask.class);
						startActivity(i);
					} else if (isSuccess) {

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
                    TaskDetailCopy.this).create();

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
                    if (isSuccessfullyDeleted) {
                        Intent i = new Intent(TaskDetailCopy.this, TakeATask.class);
                        startActivity(i);
                    } else if (isPostSuccess) {
                        Intent i = new Intent(TaskDetailCopy.this, TakeATask.class);
                        startActivity(i);
                    } else if (isSuccess) {

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

        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_detail);

        isConnected = NetConnection
                .checkInternetConnectionn(getApplicationContext());

        imageLoader = new ImageLoader(getApplicationContext());

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        appContext = getContentResolver();

        imgFileGallery = new File("");

        // task_image = (ImageView) findViewById(R.id.task_image);
        back = (ImageView) findViewById(R.id.back);
        back_ll = (LinearLayout) findViewById(R.id.back_ll);
        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        description = (EditText) findViewById(R.id.description);
        address = (EditText) findViewById(R.id.address);
        price_layout = (LinearLayout) findViewById(R.id.price_layout);
        message = (EditText) findViewById(R.id.message);
        Message_static = (TextView) findViewById(R.id.Message_static);
        bid_price = (EditText) findViewById(R.id.bid_price);
        file_name = (TextView) findViewById(R.id.file_name);
        cat_name = (TextView) findViewById(R.id.cat_name);

        comments = (TextView) findViewById(R.id.comments);

        cityET = (EditText) findViewById(R.id.city);
        stateET = (EditText) findViewById(R.id.state);
        countryET = (EditText) findViewById(R.id.country);
        zipcodeET = (EditText) findViewById(R.id.zipcode);

        date = (TextView) findViewById(R.id.date);
        edit = (ImageView) findViewById(R.id.edit);
        delete = (ImageView) findViewById(R.id.delete);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
        Done = (Button) findViewById(R.id.Done);
        del = (ImageView) findViewById(R.id.del);
        ll_del = (LinearLayout) findViewById(R.id.ll_del);
        name = (TextView) findViewById(R.id.name);
        apply = (Button) findViewById(R.id.apply);

        file_name2 = (TextView) findViewById(R.id.file_name2);
        file_name3 = (TextView) findViewById(R.id.file_name3);
        file_name4 = (TextView) findViewById(R.id.file_name4);
        file_name5 = (TextView) findViewById(R.id.file_name5);

        title.setText(Constants.TASK_DETAIL_TITLE);
        price.setText("$ "+Constants.TASK_DETAIL_PRICE);
        description.setText(Constants.TASK_DETAIL_DESC);

        cityET.setText(Constants.TASK_DETAIL_CITY);
        stateET.setText(Constants.TASK_DETAIL_STATE);
        countryET.setText(Constants.TASK_DETAIL_COUNTRY);
        zipcodeET.setText(Constants.TASK_DETAIL_ZIPCODE);

        if (Constants.TASK_DETAIL_ADDRESS != null
                && Constants.TASK_DETAIL_ADDRESS.length() > 0) {
            addreess = Constants.TASK_DETAIL_ADDRESS;
            address.setText(addreess);
            address.setVisibility(View.VISIBLE);
        } else {
            addreess = "";
            address.setVisibility(View.GONE);
        }

        if (Constants.TASK_DETAIL_CITY != null
                && Constants.TASK_DETAIL_CITY.length() > 0) {
            city = Constants.TASK_DETAIL_CITY;
            cityET.setText(city);
            cityET.setVisibility(View.VISIBLE);
        } else {
            city = "";
            cityET.setVisibility(View.GONE);
        }

        if (Constants.TASK_DETAIL_STATE != null
                && Constants.TASK_DETAIL_STATE.length() > 0) {
            state = Constants.TASK_DETAIL_STATE;
            stateET.setText(state);
            stateET.setVisibility(View.VISIBLE);
        } else {
            state = "";
            stateET.setVisibility(View.GONE);
        }

        if (Constants.TASK_DETAIL_COUNTRY != null
                && Constants.TASK_DETAIL_COUNTRY.length() > 0) {
            country = Constants.TASK_DETAIL_COUNTRY;
            countryET.setText(country);
            countryET.setVisibility(View.VISIBLE);
        } else {
            country = "";
            countryET.setVisibility(View.GONE);
        }

        if (Constants.TASK_DETAIL_ZIPCODE != null
                && Constants.TASK_DETAIL_ZIPCODE.length() > 0) {
            zipcode = Constants.TASK_DETAIL_ZIPCODE;

            zipcodeET.setText(zipcode);
            zipcodeET.setVisibility(View.VISIBLE);
        } else {
            zipcode = "";
            zipcodeET.setVisibility(View.GONE);
        }
        addList.add(addreess);
        addList.add(city);
        addList.add(state);
        addList.add(zipcode);
        addList.add(country);

        addList.removeAll(Arrays.asList("", null));

        String ADDRESS_TEXT = addList.toString().replace("[", "")
                .replace("]", "").replace(", ", ", ");

        comments.setText(Constants.TASK_DETAIL_COMMENTS);

        //address.setText(ADDRESS_TEXT);

		/*
		 * address.setText(Constants.TASK_DETAIL_ADDRESS+" ,"+Constants.
		 * TASK_DETAIL_CITY
		 * +" ,"+Constants.TASK_DETAIL_STATE+" ,"+Constants.TASK_DETAIL_ZIPCODE
		 * +" ,"+Constants.TASK_DETAIL_COUNTRY);
		 */

		/*
		 * DateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
		 *
		 * MyDate d1; try { d1 = (MyDate) df.parse(Constants.TASK_DETAIL_DATE);
		 * Log.e("**** date ****",""+d1); System.out.println(d1); } catch
		 * (ParseException e) { e.printStackTrace(); }
		 */

        date.setText(Constants.TASK_DETAIL_DATE);

        String UserNAME = Constants.TASK_DETAIL_FNAME + " "
                + Constants.TASK_DETAIL_LNAME;

        SpannableString content2 = new SpannableString(UserNAME);
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        name.setText(content2);

        cat_name.setText(Constants.TASK_DETAIL_CATNAME);


            ll_delete.setVisibility(View.GONE);
            ll_edit.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
            price_layout.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            Message_static.setVisibility(View.GONE);
            ll_edit.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
            price_layout.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            Message_static.setVisibility(View.GONE);



        if (Constants.TASK_DETAIL_ATTACHMENT_1 != null
                || Constants.TASK_DETAIL_ATTACHMENT_1.length() > 1) {
            file_name.setText(Constants.TASK_DETAIL_ATTACHMENT_1);
        } else {
            file_name.setVisibility(View.GONE);
        }

        if(Constants.TASK_DETAIL_ATTACHMENT_2.contains("defaultTask.png")){
            file_name2.setVisibility(View.GONE);
        } else {
            file_name2.setText(Constants.TASK_DETAIL_ATTACHMENT_2);
        }

        if(Constants.TASK_DETAIL_ATTACHMENT_3.contains("defaultTask.png")){
            file_name3.setVisibility(View.GONE);
        } else {
            file_name3.setText(Constants.TASK_DETAIL_ATTACHMENT_3);
        }

        if(Constants.TASK_DETAIL_ATTACHMENT_4.contains("defaultTask.png")){
            file_name4.setVisibility(View.GONE);
        } else {
            file_name4.setText(Constants.TASK_DETAIL_ATTACHMENT_4);
        }

        if(Constants.TASK_DETAIL_ATTACHMENT_5.contains("defaultTask.png")){
            file_name5.setVisibility(View.GONE);
        } else {
            file_name5.setText(Constants.TASK_DETAIL_ATTACHMENT_5);
        }

        // file_name.setText(Constants.TASK_DETAIL_URL);

        file_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                Uri uri = Uri.parse(Constants.TASK_DETAIL_ATTACHMENT_1); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error occurred.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        file_name2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    Log.e("size1===>",""+Constants.TASK_DETAIL_ATTACHMENT_1.trim().length());
                    Log.e("size2===>",""+Constants.TASK_DETAIL_ATTACHMENT_2.trim().length());

                        Uri uri = Uri.parse(Constants.TASK_DETAIL_ATTACHMENT_2.trim()); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error occurred.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        file_name3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                Uri uri = Uri.parse(Constants.TASK_DETAIL_ATTACHMENT_3.trim()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error occurred.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        file_name4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                Uri uri = Uri.parse(Constants.TASK_DETAIL_ATTACHMENT_4.trim()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error occurred.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        file_name5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                Uri uri = Uri.parse(Constants.TASK_DETAIL_ATTACHMENT_5.trim()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error occurred.",Toast.LENGTH_SHORT).show();
                }
            }
        });

       /* name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Constants.VIEW_PROFILE_ID = Constants.TASK_DETAIL_USERID;
                Intent i = new Intent(TaskDetailCopy.this, ViewProfile.class);
                startActivity(i);

            }
        });*/

        Done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ValidateAndUpdate();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDeleteConfirmationDialog("Are you sure you want to delete this post ?");

            }
        });

        ll_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog("Are you sure you want to delete this post ?");

            }
        });

        back_ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StartEditing();
                // CallEditAPI();

            }
        });

        ll_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StartEditing();
                // CallEditAPI();

            }
        });

        del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stopEditingAndBAckToDefault();

            }
        });

        ll_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stopEditingAndBAckToDefault();

            }
        });

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (startEditing) {

                    showDialog(0);
                }
            }
        });

		/*
		 * task_image.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { if (startEditing) { Intent
		 * GaleryIntent = new Intent( Intent.ACTION_PICK,
		 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * startActivityForResult(GaleryIntent, 1); }
		 *
		 * } });
		 */

        apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String bidPRice = bid_price.getText().toString().trim();
                String messageToSend = message.getText().toString().trim();

                if(messageToSend.length()<1){
                    messageToSend="";
                }
                if (bidPRice.length() > 0) {
                    String to_id = Constants.TASK_DETAIL_USERID;

                    String task_id = Constants.TASK_DETAIL_ID;

                    // String amount = Constants.TASK_DETAIL_PRICE;

                    String amount = bidPRice;

                    SendRequest(to_id, task_id, amount,messageToSend);
                } else {
                    bid_price.setError("Please enter your price.");
                }
            }
        });

    }

    protected void SendRequest(String to_id, String task_id, String price , String mSG) {
        if (isConnected) {

            new SendRequestCall(to_id, task_id, price,mSG).execute(new Void[0]);
        } else {
            showDialog(Constants.No_INTERNET);
        }
    }

    protected void ValidateAndUpdate() {
        String title_text = title.getText().toString().trim();
        String description_text = description.getText().toString().trim();
        String address_text = address.getText().toString().trim();

		/*
		 * String city_text = city.getText().toString().trim(); String
		 * state_text = state.getText().toString().trim(); String country_text =
		 * country.getText().toString().trim(); String zipcode_text =
		 * zipcode.getText().toString().trim();
		 */

        String date_text = date.getText().toString().trim();
        String price_text = price.getText().toString().trim();

        if (title_text.length() < 1) {
            title.setError("Please enter title.");
        } else if (price_text.length() < 1) {
            price.setError("Please enter price.");
        } else if (description_text.length() < 1) {
            description.setError("Please enter description.");
        } else if (address_text.length() < 1) {
            address.setError("Please enter address.");
        }

		/*
		 * else if (city_text.length() < 1) {
		 * city.setError("Please enter city."); } else if (state_text.length() <
		 * 1) { state.setError("Please enter state."); } else if
		 * (country_text.length() < 1) {
		 * country.setError("Please enter country."); } else if
		 * (zipcode_text.length() < 1) {
		 * zipcode.setError("Please enter zipcode."); }
		 */

        else if (date_text.length() < 1) {
            date.setError("Please select date.");
        } else {
            updateProfileObj = new updateProfileTask(title_text, price_text,
                    description_text, address_text, date_text);
            updateProfileObj.execute();
        }

    }

    protected void stopEditingAndBAckToDefault() {

        ll_edit.setVisibility(View.VISIBLE);
        ll_del.setVisibility(View.INVISIBLE);
        Done.setVisibility(View.INVISIBLE);

        title.setEnabled(false);
        price.setEnabled(false);
        description.setEnabled(false);
        address.setEnabled(false);
		/*
		 * city.setEnabled(false); state.setEnabled(false);
		 * country.setEnabled(false); zipcode.setEnabled(false);
		 */
        // task_image.setEnabled(false);
        date.setEnabled(false);

        title.setBackgroundResource(0);
        price.setBackgroundResource(0);
        description.setBackgroundResource(0);
        address.setBackgroundResource(0);
		/*
		 * city.setBackgroundResource(0); state.setBackgroundResource(0);
		 * country.setBackgroundResource(0); zipcode.setBackgroundResource(0);
		 */
        date.setBackgroundResource(0);
        // task_image.setBackgroundResource(0);

        startEditing = false;

        title.setText(Constants.TASK_DETAIL_TITLE);
        price.setText("$ "+Constants.TASK_DETAIL_PRICE);
        description.setText(Constants.TASK_DETAIL_DESC);
        address.setText(Constants.TASK_DETAIL_ADDRESS);

		/*
		 * city.setText(Constants.TASK_DETAIL_CITY);
		 * country.setText(Constants.TASK_DETAIL_COUNTRY);
		 * state.setText(Constants.TASK_DETAIL_STATE);
		 * zipcode.setText(Constants.TASK_DETAIL_ZIPCODE);
		 */

        date.setText(Constants.TASK_DETAIL_DATE);

		/*
		 * imageLoader.DisplayImage(Constants.TASK_DETAIL_URL, R.drawable.noimg,
		 * task_image);
		 */

    }

    protected void StartEditing() {

        Done.setVisibility(View.VISIBLE);
        ll_del.setVisibility(View.VISIBLE);

        title.setEnabled(true);
        price.setEnabled(true);
        description.setEnabled(true);
        address.setEnabled(true);
		/*
		 * city.setEnabled(true); state.setEnabled(true);
		 * country.setEnabled(true); zipcode.setEnabled(true);
		 */

        date.setEnabled(true);
        // task_image.setEnabled(true);

        startEditing = true;

        title.setBackgroundResource(R.drawable.cellule);
        price.setBackgroundResource(R.drawable.cellule);
        description.setBackgroundResource(R.drawable.cellule);
        address.setBackgroundResource(R.drawable.cellule);
		/*
		 * city.setBackgroundResource(R.drawable.cellule);
		 * state.setBackgroundResource(R.drawable.cellule);
		 * country.setBackgroundResource(R.drawable.cellule);
		 * zipcode.setBackgroundResource(R.drawable.cellule);
		 */
        date.setBackgroundResource(R.drawable.cellule);
        // task_image.setBackgroundResource(R.drawable.cellule);

    }

    public  class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            String formattedDate = sdf.format(c.getTime());

            Log.e("date====", "" + formattedDate);
            date.setText(formattedDate);
        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
			/*
			 * date.setText(selectedDay + " / " + (selectedMonth + 1) + " / " +
			 * selectedYear);
			 */

            date.setText((selectedMonth + 1) + " / " + selectedDay + " / "
                    + selectedYear);

            Constants.TASK_DETAIL_DATE_TO_SEND = selectedYear + "-"
                    + (selectedMonth + 1) + "-" + selectedDay;
        }
    };

    protected void showDeleteConfirmationDialog(String message) {
        final Dialog dialog;
        dialog = new Dialog(TaskDetailCopy.this);
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

                DeletePost();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    protected void DeletePost() {

        if (isConnected) {

            new DeletePost(Constants.TASK_DETAIL_ID).execute(new Void[0]);
        } else {
            showDialog(Constants.No_INTERNET);
        }

    }

    public class DeletePost extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();

        String taskID;

        public DeletePost(String task_id) {

            this.taskID = task_id;

        }

        protected Void doInBackground(Void... paramVarArgs) {

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/deleteTask.php?
			 * authkey=Auth_TakeATask2015&task_id=16
			 */

            try {
                localArrayList.add(new BasicNameValuePair("authkey",
                        "Auth_TakeATask2015"));
                localArrayList.add(new BasicNameValuePair("task_id",
                        this.taskID));

                result = function.deletePost(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if (result.get("ResponseCode").equals("true")) {

                    isSuccessfullyDeleted = true;
                    String msg = "Post is deleted successfully.";
                    showDialog(msg);
                } else if (result.get("ResponseCode").equals("false")) {
                    isSuccessfullyDeleted = false;
                    String msg = "Something went wrong while deleting your post.Please try again after some time .";
                    showDialog(msg);
                }
            }

            catch (Exception ae) {
                showDialog(Constants.ERROR_MSG);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(TaskDetailCopy.this,
                    R.drawable.loadingicon);
            db.show();
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

                // task_image.setImageBitmap(takenImage);

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

                Log.e("picturePath==", "" + picturePath);

                imgFileGallery = new File(picturePath);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * *************** update post ************************************
     */

    public class updateProfileTask extends AsyncTask<String, Void, String> {
        ByteArrayOutputStream baos;

        String TITLE, PRICE, DESCRIPTION, ADDRESS, DATE;

        public updateProfileTask(String title_text, String price_text,
                                 String description_text, String address_text, String date_text) {
            this.TITLE = title_text;
            this.PRICE = price_text;
            this.DESCRIPTION = description_text;
            this.ADDRESS = address_text;
            this.DATE = date_text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(TaskDetailCopy.this,
                    R.drawable.loadingicon);
            db.show();

        }

        @Override
        protected String doInBackground(String... Params) {
            try {
                baos = new ByteArrayOutputStream();
                takenImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            }

            catch (Exception e) {
                Log.e("excptn==", "" + e);
            }

			/*
			 * http://phphosting.osvin.net/TakeATask/WEB_API/updateTask.php?
			 * task_id
			 * =42&title=test&description=test&address=test&due_date=2015-
			 * 08-15&budget=800&attachment1=
			 */
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpClientUpload client = new HttpClientUpload(
                        "https://takeataskservices.com/WEB_API/updateTask.php?");
                client.connectForMultipart();

                Log.i("task_id", "" + Constants.TASK_DETAIL_ID);
                Log.i("title", "" + this.TITLE);
                Log.i("description", "" + this.DESCRIPTION);
                Log.i("address", "" + this.ADDRESS);
                Log.i("due_date", "" + this.DATE);
                Log.i("budget", "" + this.PRICE);

                client.addFormPart("task_id", Constants.TASK_DETAIL_ID);
                client.addFormPart("title", this.TITLE);
                client.addFormPart("description", this.DESCRIPTION);
                client.addFormPart("address", this.ADDRESS);
                client.addFormPart("due_date",
                        Constants.TASK_DETAIL_DATE_TO_SEND);
                client.addFormPart("budget", this.PRICE);

                client.addFormPart("city", Constants.TASK_DETAIL_CITY);
                client.addFormPart("state", Constants.TASK_DETAIL_STATE);
                client.addFormPart("country", Constants.TASK_DETAIL_COUNTRY);
                client.addFormPart("zipcode", Constants.ZIPCODE);

                client.addFormPart("authkey", Constants.AUTH_KEY);

                if (!(imgFileGallery.getName().equals("") || imgFileGallery
                        .getName() == null)) {

                    client.addFilePart("attachment1", imgFileGallery.getName(),
                            baos.toByteArray());
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

                    isPostSuccess = true;
                    showDialog("post updated successfully.");

                } else {
					/*
					 * Intent i = new Intent(PicAddORgRadius.this , Home.class);
					 * startActivity(i);
					 */

                    Toast.makeText(TaskDetailCopy.this, "Error occured...",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                Log.e("Exception===", "" + e);
                showDialog("Something went wrong while processing your request.Please try again.");
            }
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

      finish();
    }

    // ******************** apply for a task
    // ****************************************

    public class SendRequestCall extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();

        String toID, taskID, Price , msg;

        public SendRequestCall(String to_id, String task_id, String price, String MSG) {

            this.toID = to_id;
            this.taskID = task_id;
            this.Price = price;
            this.msg = MSG;

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
                localArrayList.add(new BasicNameValuePair("message",this.msg));

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
            db = new TransparentProgressDialog(TaskDetailCopy.this,
                    R.drawable.loadingicon);
            db.show();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
