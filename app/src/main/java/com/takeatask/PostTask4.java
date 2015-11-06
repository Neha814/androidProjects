package com.takeatask;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;

import functions.Constants;

public class PostTask4 extends FragmentActivity implements OnClickListener {

	Button continue_btn;
	ImageView back;
	TextView task;
	
	LinearLayout back_ll;

	TextView date;

	EditText comment;

	private Calendar cal;
	private int day;
	private int month;
	private int year;
	
	View post1 ,post2,post3,post4,post5,post6;
	
	LinearLayout l1 ,l2,l3,l4,l5,l6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_task_4);

		back = (ImageView) findViewById(R.id.back);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		continue_btn = (Button) findViewById(R.id.continue_btn);
		task = (TextView) findViewById(R.id.task);

		date = (TextView) findViewById(R.id.date);
		comment = (EditText) findViewById(R.id.comment);
		
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
		
		if(Constants.DATE!=null && Constants.DATE.length()>0){
			date.setText(Constants.DATE_TO_SHOW);
		}
		
		if(Constants.COMMENTS!=null && Constants.COMMENTS.length()>0){
			comment.setText(Constants.COMMENTS);
		}

		back.setOnClickListener(this);
		back_ll.setOnClickListener(this);
		continue_btn.setOnClickListener(this);
		date.setOnClickListener(this);
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

		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);

		SpannableString content1 = new SpannableString(
				"Task to be completed by");
		content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
		task.setText(content1);
	}

	@Override
	public void onClick(View v) {
		if (v == back || v==back_ll) {
			//finish();
			/*Intent i = new Intent(PostTask4.this , Home.class);
			startActivity(i);*/

			SaveAndGo();
			Intent i = new Intent(PostTask4.this , PostTask3.class);
			startActivity(i);
			
		} else if (v == continue_btn) {

			String date_Text = date.getText().toString();
			String comment_text = comment.getText().toString();

			if (date_Text.trim().length() < 1 || date_Text.equals("Date")) {
				date.setError("Please select date");
			}
			/*else if (comment_text.trim().length() < 1) {
				comment.setError("Please enter comment");
			} */
			else {

				//Constants.DATE = date_Text;
				Constants.DATE = Constants.POST4_DATE ;
				Constants.DATE_TO_SHOW = date_Text;
				Constants.COMMENTS = comment_text;

				Intent i = new Intent(PostTask4.this, PostTask5.class);
				startActivity(i);
			}
		} else if (v == date) {
			showDialog(0);

		}else if(v==post1 || v==l1){

			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask4.this, PostTask1.class);
				startActivity(i);
			}
		}else if(v==post2 || v==l2){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask4.this, PostTask2.class);
				startActivity(i);
			}
		}else if(v==post3 || v==l3){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask4.this, PostTask3.class);
				startActivity(i);
			}
		}else if(v==post5 || v==l5){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask4.this, PostTask5.class);
				startActivity(i);
			}
		}else if(v==post6 || v==l6){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask4.this, PostTask6.class);
				startActivity(i);
			}
		}
	}

	private boolean ValidateFields() {

		String date_Text = date.getText().toString();
		String comment_text = comment.getText().toString();

		if (date_Text.trim().length() < 1 || date_Text.equals("Date")) {
			date.setError("Please select date");
			return false;
		}


		return  true;
	}

/*	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		
		 DatePickerDialog picker;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
		

			// Create a new instance of DatePickerDialog and return it
		//	return new DatePickerDialog(getActivity(), this, year, month, day);
			
			
			
				 picker = new DatePickerDialog(getActivity(),
						 this, year, month,day);
				 // Create a new instance of DatePickerDialog and return it
				 picker.getDatePicker().setMinDate(c.getTime().getTime());
				  return picker;
			
			
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
	}*/

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		//return new DatePickerDialog(this, datePickerListener, year, month, day);
		DatePickerDialog picker;
		 picker = new DatePickerDialog(this,
				 datePickerListener, year, month,day);
		 // Create a new instance of DatePickerDialog and return it
		 picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
		  return picker;
	}

	private OnDateSetListener datePickerListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			/*date.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
					+ selectedYear);*/
			
			date.setText((selectedMonth + 1) + " / " + selectedDay + " / "
					+ selectedYear);
			
			Constants.POST4_DATE = selectedYear + "-" +(selectedMonth + 1) + "-" + selectedDay;
		}
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}
	
	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		SaveAndGo();
		
		

		finish();
	}*/

	public void SaveAndGo() {
		String date_text = date.getText().toString().trim();
		String comment_text = comment.getText().toString().trim();
		
		Constants.DATE = Constants.POST4_DATE;
		Constants.DATE_TO_SHOW = date_text;
		Constants.COMMENTS = comment_text ;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		/*clearAllVariables();
		
		Intent i = new Intent(PostTask4.this , Home.class);
		startActivity(i);*/
		
		SaveAndGo();
		Intent i = new Intent(PostTask4.this , PostTask3.class);
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