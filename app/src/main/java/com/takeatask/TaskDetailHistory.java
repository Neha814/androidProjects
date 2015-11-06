package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imageloader.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import functions.Constants;
import utils.NetConnection;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 15/10/15.
 */
public class TaskDetailHistory extends Activity {

    // ImageView task_image;
    ImageView back;

    LinearLayout back_ll;

    EditText title, price, description;
    TextView date;

    EditText address;

    EditText city, state, country, zipcode;

    LinearLayout price_layout;

    ImageLoader imageLoader;


    boolean isConnected;



    TextView  cat_name;

    TextView file_name,file_name2,file_name3,file_name4,file_name5;

    TransparentProgressDialog db;

    List<String> addList = new ArrayList<String>();



    boolean isSuccessfullyDeleted = false;

    boolean startEditing = false;

    boolean isSuccess = false;

    private Calendar cal;
    private int day;
    private int month;
    private int year;

    TextView tasker_poster_name , tasker_static , comments;

    TextView name;

    boolean isPostSuccess = false;


/*	protected void showDialog(String msg) {
		try {
			final Dialog dialog;
			dialog = new Dialog(MyTaskDetail.this);
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
						Intent i = new Intent(MyTaskDetail.this,
								CurrentPostedMyTasks.class);
						startActivity(i);
					} else if (isPostSuccess) {
						Intent i = new Intent(MyTaskDetail.this,
								CurrentPostedMyTasks.class);
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
                    TaskDetailHistory.this).create();

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
                        Intent i = new Intent(TaskDetailHistory.this,
                                CurrentPostedMyTasks.class);
                        startActivity(i);
                    } else if (isPostSuccess) {
                        Intent i = new Intent(TaskDetailHistory.this,
                                CurrentPostedMyTasks.class);
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

        setContentView(R.layout.history_task_detail);

        isConnected = NetConnection
                .checkInternetConnectionn(getApplicationContext());

        imageLoader = new ImageLoader(getApplicationContext());

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        // task_image = (ImageView) findViewById(R.id.task_image);
        back = (ImageView) findViewById(R.id.back);
        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        description = (EditText) findViewById(R.id.description);
        address = (EditText) findViewById(R.id.address);
        price_layout = (LinearLayout) findViewById(R.id.price_layout);
        tasker_poster_name = (TextView) findViewById(R.id.tasker_poster_name);
        tasker_static = (TextView) findViewById(R.id.tasker_static);
        comments = (TextView) findViewById(R.id.comments);

        file_name = (TextView) findViewById(R.id.file_name);
        file_name2 = (TextView) findViewById(R.id.file_name2);
        file_name3 = (TextView) findViewById(R.id.file_name3);
        file_name4 = (TextView) findViewById(R.id.file_name4);
        file_name5 = (TextView) findViewById(R.id.file_name5);

        cat_name = (TextView) findViewById(R.id.cat_name);
        back_ll = (LinearLayout) findViewById(R.id.back_ll);



        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        zipcode = (EditText) findViewById(R.id.zipcode);

        date = (TextView) findViewById(R.id.date);
        name = (TextView) findViewById(R.id.name);

        price_layout.setVisibility(View.GONE);


        title.setText(Constants.TASK_DETAIL_TITLE);
        price.setText("$ " + Constants.TASK_DETAIL_PRICE);
        description.setText(Constants.TASK_DETAIL_DESC);

        file_name.setClickable(true);
        file_name2.setClickable(true);
        file_name3.setClickable(true);
        file_name4.setClickable(true);
        file_name5.setClickable(true);
		/*
		 * address.setText(Constants.TASK_DETAIL_ADDRESS + " ," +
		 * Constants.TASK_DETAIL_CITY + " ," + Constants.TASK_DETAIL_STATE +
		 * " ," + Constants.TASK_DETAIL_ZIPCODE + " ," +
		 * Constants.TASK_DETAIL_COUNTRY);
		 */

        address.setText(Constants.TASK_DETAIL_ADDRESS);
        city.setText(Constants.TASK_DETAIL_CITY);
        state.setText(Constants.TASK_DETAIL_STATE);
        country.setText(Constants.TASK_DETAIL_COUNTRY);
        zipcode.setText(Constants.TASK_DETAIL_ZIPCODE);

        addList.add(Constants.TASK_DETAIL_ADDRESS);
        addList.add(Constants.TASK_DETAIL_CITY);
        addList.add(Constants.TASK_DETAIL_STATE);
        addList.add(Constants.TASK_DETAIL_ZIPCODE);
        addList.add(Constants.TASK_DETAIL_COUNTRY);

        addList.removeAll(Arrays.asList("", null));

        city.setVisibility(View.GONE);
        state.setVisibility(View.GONE);
        country.setVisibility(View.GONE);
        zipcode.setVisibility(View.GONE);


        String ADDRESS_TEXT = addList.toString().replace("[", "")
                .replace("]", "").replace(", ", ", ");

        address.setText(ADDRESS_TEXT);

        comments.setText(Constants.TASK_DETAIL_COMMENTS);

       /* if (Constants.TASK_DETAIL_ADDRESS == null ||Constants.TASK_DETAIL_ADDRESS.equalsIgnoreCase("")
                || Constants.TASK_DETAIL_ADDRESS.trim().length() < 1) {
            address.setVisibility(View.GONE);
        }

         if (Constants.TASK_DETAIL_CITY == null ||Constants.TASK_DETAIL_CITY.equalsIgnoreCase("")
                || Constants.TASK_DETAIL_CITY.trim().length() < 1) {
            city.setVisibility(View.GONE);
        }

         if (Constants.TASK_DETAIL_STATE == null ||Constants.TASK_DETAIL_STATE.equalsIgnoreCase("")
                || Constants.TASK_DETAIL_STATE.trim().length() < 1) {
            state.setVisibility(View.GONE);
        }

         if (Constants.TASK_DETAIL_COUNTRY == null ||Constants.TASK_DETAIL_COUNTRY.equalsIgnoreCase("")
                || Constants.TASK_DETAIL_COUNTRY.trim().length() < 1) {
            country.setVisibility(View.GONE);
        }

         if (Constants.TASK_DETAIL_ZIPCODE == null ||Constants.TASK_DETAIL_ZIPCODE.equalsIgnoreCase("")
                || Constants.TASK_DETAIL_ZIPCODE.trim().length() < 1) {
            zipcode.setVisibility(View.GONE);
        }*/




        date.setText(Constants.TASK_DETAIL_DATE);

        String UserNAME = Constants.TASK_DETAIL_FNAME + " "
                + Constants.TASK_DETAIL_LNAME;

        SpannableString content2 = new SpannableString(UserNAME);
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        name.setText(content2);

        cat_name.setText(Constants.TASK_DETAIL_CATNAME);

        if(Constants.TASK_DETAIL_TASKER_POSTER_NAME==null ||
                Constants.TASK_DETAIL_TASKER_POSTER_NAME.length()<1){
            tasker_poster_name.setVisibility(View.GONE);
            tasker_static.setVisibility(View.GONE);

        } else {
            tasker_poster_name.setVisibility(View.VISIBLE);
            tasker_static.setVisibility(View.VISIBLE);

            SpannableString content1 = new SpannableString(Constants.TASK_DETAIL_TASKER_POSTER_NAME);
            content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
            tasker_poster_name.setText(content1);
        }

        /*if (Constants.TASK_DETAIL_USERID.equalsIgnoreCase(Constants.USER_ID)) {

            price_layout.setVisibility(View.GONE);


        } else {

            price_layout.setVisibility(View.VISIBLE);

        }*/

        Log.e("******** a 1 ********",""+Constants.TASK_DETAIL_ATTACHMENT_1);

        file_name.setVisibility(View.VISIBLE);
        file_name.setText(Constants.TASK_DETAIL_ATTACHMENT_1);
        file_name2.setText(Constants.TASK_DETAIL_ATTACHMENT_2);
        file_name3.setText(Constants.TASK_DETAIL_ATTACHMENT_3);
        file_name4.setText(Constants.TASK_DETAIL_ATTACHMENT_4);
        file_name5.setText(Constants.TASK_DETAIL_ATTACHMENT_5);

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

		/*
		 * imageLoader.DisplayImage(Constants.TASK_DETAIL_URL, R.drawable.noimg,
		 * task_image);
		 */


        file_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                Uri uri = Uri.parse(Constants.TASK_DETAIL_ATTACHMENT_1); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        file_name2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                Uri uri = Uri.parse(Constants.TASK_DETAIL_ATTACHMENT_2.trim()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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
                startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error occurred.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Constants.VIEW_PROFILE_ID = Constants.TASK_DETAIL_USERID;
                Intent i = new Intent(TaskDetailHistory.this, ViewProfile.class);
                startActivity(i);

            }
        });

        tasker_poster_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.VIEW_PROFILE_ID = Constants.TASK_DETAIL_TASKER_POSTER_ID;
                Intent i = new Intent(TaskDetailHistory.this, ViewProfile.class);
                startActivity(i);
            }
        });



        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back_ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });



    }



    public class DatePickerFragment extends DialogFragment implements
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
        // return new DatePickerDialog(this, datePickerListener, year, month,
        // day);

        DatePickerDialog picker;
        picker = new DatePickerDialog(this, datePickerListener, year, month,
                day);
        // Create a new instance of DatePickerDialog and return it
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return picker;
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




    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        finish();
    }



}

