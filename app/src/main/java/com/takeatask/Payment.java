package com.takeatask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Payment extends Activity implements OnClickListener {

	TextView credit_card_textview, paypal_textview;

	RelativeLayout paypal_layout, credit_card_layout ,history_layout;
	
	ImageView back;
	
	LinearLayout back_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.payment);

		credit_card_textview = (TextView) findViewById(R.id.credit_card_textview);

		paypal_textview = (TextView) findViewById(R.id.paypal_textview);

		paypal_layout = (RelativeLayout) findViewById(R.id.paypal_layout);

		credit_card_layout = (RelativeLayout) findViewById(R.id.credit_card_layout);

		history_layout = (RelativeLayout) findViewById(R.id.history_layout);
		
		back = (ImageView) findViewById(R.id.back) ;
		
		back_ll = (LinearLayout) findViewById(R.id.back_ll);

		credit_card_textview.setOnClickListener(this);
		paypal_textview.setOnClickListener(this);
		paypal_layout.setOnClickListener(this);
		credit_card_layout.setOnClickListener(this);
		back.setOnClickListener(this);
		back_ll.setOnClickListener(this);
		history_layout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if(v==credit_card_textview || v == credit_card_layout){
			
			Intent i = new Intent(Payment.this , Credit_Card_Payment.class);
			startActivity(i);
		} else if(v==paypal_layout || v== paypal_textview){
			Intent i = new Intent(Payment.this , PayPal_Payment.class);
			startActivity(i);
		} else if(v==back || v==back_ll){
			Intent i = new Intent(Payment.this , Settings.class);
			startActivity(i);
		} else if(v==history_layout){
			Intent i = new Intent(Payment.this , IncomingPayment.class);
			startActivity(i);
		}
		
	}
}
