package com.takeatask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imageloader.ImageLoader;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.NetConnection;
import utils.TransparentProgressDialog;

/**
 * Created by sandeep on 12/10/15.
 */
public class TakeATask_Notification extends Activity {

    ImageView senderimage;
    TextView senderName;
    ImageLoader imageLoader;
    TransparentProgressDialog db;
    boolean isConnected;

    MyAdapter mAdapter ;
    ListView listview;
    ArrayList<HashMap<String , String>> Notification_list= new ArrayList<HashMap<String , String>>();


    public void showDialog(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    TakeATask_Notification.this).create();

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

        setContentView(R.layout.take_a_task_notification);

        senderimage = (ImageView) findViewById(R.id.senderimage);
        senderName = (TextView) findViewById(R.id.senderName);
        listview = (ListView) findViewById(R.id.listview);

        imageLoader = new ImageLoader(getApplicationContext());
        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());

        imageLoader.DisplayImage(Constants.CHAT_IMAGE, R.drawable.default_pic,
                senderimage);
        senderName.setText(Constants.CHAT_NAME);

        if (isConnected) {

            new getBiddingNotification().execute(new Void[0]);
        } else {
            showDialog(Constants.No_INTERNET);
        }

    }

    public class getBiddingNotification extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

        ArrayList localArrayList = new ArrayList();



        protected Void doInBackground(Void... paramVarArgs) {


          //  http://phphosting.osvin.net/TakeATask/WEB_API/listAlertNotifications.php?authkey=Auth_TakeATask2015&user_id=38

            try {
                localArrayList.add(new BasicNameValuePair("authkey",
                        "Auth_TakeATask2015"));
                localArrayList.add(new BasicNameValuePair("user_id",
                        Constants.USER_ID));


                result = function.getBiddingNotification(localArrayList);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if (result.size() > 0) {
                    Notification_list.clear();
                    Notification_list.addAll(result);
                    Log.e("list===>>", "" + Notification_list);
                    mAdapter = new MyAdapter(getApplicationContext(),
                            Notification_list);
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
                db = new TransparentProgressDialog(
                        TakeATask_Notification.this, R.drawable.loadingicon);
                db.show();
            } catch (Exception e) {
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

            return Notification_list.size();
        }

        @Override
        public Object getItem(int position) {
            return Notification_list.get(position);
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
                convertView = mInflater.inflate(R.layout.notification_listitem, null);

                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.description = (TextView) convertView.findViewById(R.id.description);
                holder.task_posted_on = (TextView) convertView.findViewById(R.id.task_posted_on);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.message.setText(Notification_list.get(position).get("message"));
            holder.title.setText("Title : "+Notification_list.get(position).get("title"));
            holder.description.setText("Description : "+Notification_list.get(position).get("description"));
            holder.task_posted_on.setText("Task posted on : "+Notification_list.get(position).get("task_posted_on"));




            return convertView;
        }

        class ViewHolder {

            TextView message,title,description,task_posted_on ;
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
