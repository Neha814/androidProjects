package com.takeatask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SelectTasker extends Activity implements OnClickListener{
	
	Button posted_tasks;
	
	Button taken_tasks;
	
	LinearLayout back_ll;
	
	ImageView back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_tasker);
		
		taken_tasks = (Button) findViewById(R.id.taken_tasks);
		posted_tasks = (Button) findViewById(R.id.posted_tasks);
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back =(ImageView) findViewById(R.id.back);
 		
		taken_tasks.setOnClickListener(this);
		posted_tasks.setOnClickListener(this);
		back.setOnClickListener(this);
		back_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==taken_tasks){
			Intent i = new Intent(SelectTasker.this , CurrentPerformingTasks.class);
			startActivity(i);
			
		} else if(v==posted_tasks){
			Intent i = new Intent(SelectTasker.this , CurrentPostedMyTasks.class);
			startActivity(i);
		} else if(v==back || v==back_ll){
			Intent i = new Intent(SelectTasker.this , Home.class);
			startActivity(i);
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(SelectTasker.this , Home.class);
		startActivity(i);
		super.onBackPressed();
	}

}
