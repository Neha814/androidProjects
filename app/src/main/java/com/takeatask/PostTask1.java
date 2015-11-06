package com.takeatask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import functions.Constants;

public class PostTask1 extends Activity implements OnClickListener {
	
	ImageView back;
	
	EditText task_name;
	
	Button continue_btn;
	
	LinearLayout back_ll;
	
	EditText describe_task;
	
	SharedPreferences sp;
	
	View post1 ,post2,post3,post4,post5,post6;
	
	TextView step1;
	
	LinearLayout l1 ,l2,l3,l4,l5,l6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.post_task_1);
		
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		back = (ImageView) findViewById(R.id.back);
		continue_btn = (Button) findViewById(R.id.continue_btn);
		task_name = (EditText) findViewById(R.id.task_name);
		describe_task = (EditText) findViewById(R.id.describe_task);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		
		post1 = (View) findViewById(R.id.post1);
		post2 = (View) findViewById(R.id.post2);
		post3 = (View) findViewById(R.id.post3);
		post4 = (View) findViewById(R.id.post4);
		post5 = (View) findViewById(R.id.post5);
		post6 = (View) findViewById(R.id.post6);
		
		step1 = (TextView) findViewById(R.id.step1);
		
		l1 = (LinearLayout) findViewById(R.id.l1);
		l2 = (LinearLayout) findViewById(R.id.l2);
		l3 = (LinearLayout) findViewById(R.id.l3);
		l4 = (LinearLayout) findViewById(R.id.l4);
		l5 = (LinearLayout) findViewById(R.id.l5);
		l6 = (LinearLayout) findViewById(R.id.l6);
		
		
		if(Constants.TASK_NAME!=null && Constants.TASK_NAME.length()>0){
			task_name.setText(Constants.TASK_NAME);
		}
		if(Constants.DESCRIBE_TASK!=null && Constants.DESCRIBE_TASK.length()>0){
			describe_task.setText(Constants.DESCRIBE_TASK);
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
		
		
	}

	@Override
	public void onClick(View v) {
		if(v==back || v==back_ll){
			clearAllVariables();
			/*Intent i = new Intent(PostTask1.this , Home.class);
			startActivity(i);*/
		} else if(v==continue_btn){
			
			String task_name_text = task_name.getText().toString();
			String describe_task_text = describe_task.getText().toString();
			
			SaveInSharedPreferences(task_name_text , describe_task_text);
			if(task_name_text.trim().length()<1){
				task_name.setError("Please enter task name.");
			} else if(describe_task_text.trim().length()<1){
				describe_task.setError("Please describe something about task.");
			} else {
				
				Constants.TASK_NAME = task_name_text;
				Constants.DESCRIBE_TASK = describe_task_text;
				
			Intent i = new Intent(PostTask1.this , PostTask2.class);
			startActivity(i);
			}
		} else if(v==post2 || v==l2){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask1.this, PostTask2.class);
				startActivity(i);
			}
		}else if(v==post3 || v==l3){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask1.this, PostTask3.class);
				startActivity(i);
			}
		}else if(v==post4 || v==l4){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask1.this, PostTask4.class);
				startActivity(i);
			}
		}else if(v==post5 || v==l5){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask1.this, PostTask5.class);
				startActivity(i);
			}
		}else if(v==post6 || v==l6){
			boolean isValid = ValidateFields();

			if(isValid) {
				SaveAndGo();
				Intent i = new Intent(PostTask1.this, PostTask6.class);
				startActivity(i);
			}
		}
	}

	private boolean ValidateFields() {

		String task_name_text = task_name.getText().toString();
		String describe_task_text = describe_task.getText().toString();

		SaveInSharedPreferences(task_name_text , describe_task_text);
		if(task_name_text.trim().length()<1){
			task_name.setError("Please enter task name.");
			return false;
		} else if(describe_task_text.trim().length()<1){
			describe_task.setError("Please describe something about task.");
			return false;
		}

		return  true;
	}

	private void SaveInSharedPreferences(String task_name_text,
			String describe_task_text) {
		Editor e = sp.edit();
		e.putString("title", task_name_text);
		e.putString("description", describe_task_text);
		e.commit();
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                                                        INPUT_METHOD_SERVICE);
       imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
       return true;
    }
	
	private void SaveAndGo() {
		
		Constants.TASK_NAME= task_name.getText().toString().trim();
		Constants.DESCRIBE_TASK = describe_task.getText().toString().trim();
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		
		clearAllVariables();
		
		
		super.onBackPressed();
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

		Constants.ATTACHMENTCOUNT = 0;
		
		Constants.IMAGE_TO_UPLOAD1 = new File("");

		Constants.IMAGE_TO_UPLOAD2 = new File("");

		Constants.IMAGE_TO_UPLOAD3 = new File("");

		Constants.IMAGE_TO_UPLOAD4 = new File("");

		Constants.IMAGE_TO_UPLOAD5 = new File("");

		Constants.TAKENIMAGE1 = null;
		Constants.TAKENIMAGE2 = null;
		Constants.TAKENIMAGE3 = null;
		Constants.TAKENIMAGE4 = null;
		Constants.TAKENIMAGE5 = null;

		Intent i = new Intent(PostTask1.this , Home.class);
		startActivity(i);
	}
	
}
