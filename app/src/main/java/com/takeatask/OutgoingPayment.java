package com.takeatask;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 6/10/15.
 */
public class OutgoingPayment extends Activity implements View.OnClickListener {

    LinearLayout back_ll;
    ImageView back;
    Button incoming_payment;
    ListView listview;
    boolean isConnected;
    TransparentProgressDialog db;
    MyAdapter mAdapter;

    ArrayList<HashMap<String, String>> outgoingPaymentList = new ArrayList<HashMap<String, String>>();

    public void showDialog(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    OutgoingPayment.this).create();

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

        setContentView(R.layout.outgoing_payment);

        isConnected = NetConnection
                .checkInternetConnectionn(getApplicationContext());

        back = (ImageView) findViewById(R.id.back);
        back_ll = (LinearLayout) findViewById(R.id.back_ll);
        incoming_payment = (Button) findViewById(R.id.incoming_payment);
        listview = (ListView) findViewById(R.id.listview);

        back.setOnClickListener(this);
        back_ll.setOnClickListener(this);
        incoming_payment.setOnClickListener(this);

        if (isConnected) {

            new GetOutgoingPayments().execute(new Void[0]);
        } else {
            showDialog(Constants.No_INTERNET);
        }




    }

    @Override
    public void onClick (View view){
        if (view == back_ll || view == back) {
            Intent i = new Intent(OutgoingPayment.this, Payment.class);
            startActivity(i);
        } else if (view == incoming_payment) {
            Intent i = new Intent(OutgoingPayment.this, IncomingPayment.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        Intent i = new Intent(OutgoingPayment.this, Payment.class);
        startActivity(i);
    }


    public class GetOutgoingPayments extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

        ArrayList localArrayList = new ArrayList();

        protected Void doInBackground(Void... paramVarArgs) {

            // http://phphosting.osvin.net/TakeATask/WEB_API/listTransactions.php?authkey=Auth_TakeATask2015&user_id=39&type=2
            // type 1 = incoming , type 0 = outgoing

            try {
                localArrayList.add(new BasicNameValuePair("authkey","Auth_TakeATask2015"));
                localArrayList.add(new BasicNameValuePair("user_id",Constants.USER_ID));
                localArrayList.add(new BasicNameValuePair("type","0"));

                result = function.IncomingPayments(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if (result.size() > 0) {

                    outgoingPaymentList.addAll(result);
                    mAdapter = new MyAdapter(getApplicationContext(), outgoingPaymentList);
                    listview.setAdapter(mAdapter);
                } else {
                    showDialog("No Data found.");
                }
            }

            catch (Exception ae) {
                showDialog(Constants.ERROR_MSG);
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            try {
                db = new TransparentProgressDialog(OutgoingPayment.this,
                        R.drawable.loadingicon);
                db.show();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater mInflater = null;

        public MyAdapter(Context context,
                         ArrayList<HashMap<String, String>> categoryList) {
            mInflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {

            return outgoingPaymentList.size();
        }

        @Override
        public Object getItem(int position) {
            return outgoingPaymentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.incoming_payment_listitem,
                        null);

                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.description = (TextView) convertView.findViewById(R.id.description);
                holder.amount = (TextView) convertView.findViewById(R.id.amount);
                holder.payment_to = (TextView) convertView.findViewById(R.id.payment_to);
                holder.payment_date = (TextView) convertView.findViewById(R.id.payment_date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText("Title: "+outgoingPaymentList.get(position).get("title"));
            holder.description.setText("Description: "+outgoingPaymentList.get(position).get("description"));
            holder.amount.setText("$ "+outgoingPaymentList.get(position).get("amount"));
            holder.payment_to.setText("Payment To: "+outgoingPaymentList.get(position).get("name"));
            holder.payment_date.setText("Payment Date: " + outgoingPaymentList.get(position).get("date"));


            return convertView;
        }

        class ViewHolder {
            TextView amount ,title, description,payment_to ,payment_date;

        }

    }
}

