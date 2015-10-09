package com.takeatask;

import functions.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FAQ_Answers extends Activity {

	TextView question, answer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.faq_answers);

		answer = (TextView) findViewById(R.id.answer);
		question = (TextView) findViewById(R.id.question);
		
	
			question.setText(Constants.GLOBAL_QUES);
			answer.setText(Constants.GLOBAL_ANS);
	
	}

}
