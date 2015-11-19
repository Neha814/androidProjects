package com.takeatask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.circularImageview.ScalingUtilities;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import functions.Constants;
import functions.Functions;
import utils.HttpClientUpload;
import utils.NetConnection;
import utils.TransparentProgressDialog;

public class Profile extends Activity implements OnClickListener {

    EditText firstname, lastname, location, languages, occupation, skills,
            background;

    ImageView profile_pic;

    LinearLayout back_ll;
    private int mobile_width = 300;
    private Bitmap photoBitmap;
    Button save;
    ImageView back;
    boolean isConnected;
    Bitmap takenImage;
    File imgFileGallery;
    public static ContentResolver appContext;
    ImageView blurr_img;
    boolean isSuccess = false;

   // ImageLoader imageLoader;

    updateProfileTask updateProfileObj;

    TransparentProgressDialog db;

/*	protected void showDialog(String msg) {
        final Dialog dialog;
		dialog = new Dialog(Profile.this);
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
				if (isSuccess) {
					Intent i = new Intent(Profile.this, Home.class);
					startActivity(i);
				}
			}
		});
		dialog.show();

	}*/

    public void showDialog(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    Profile.this).create();

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

                    if (isSuccess) {
                        Intent i = new Intent(Profile.this, Home.class);
                        startActivity(i);
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile);

        isConnected = NetConnection
                .checkInternetConnectionn(getApplicationContext());

       // imageLoader = new ImageLoader(getApplicationContext());

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);

        location = (EditText) findViewById(R.id.location);
        languages = (EditText) findViewById(R.id.languages);
        occupation = (EditText) findViewById(R.id.occupation);
        skills = (EditText) findViewById(R.id.skills);
        background = (EditText) findViewById(R.id.background);
        save = (Button) findViewById(R.id.save);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        blurr_img = (ImageView) findViewById(R.id.blurr_img);
        back = (ImageView) findViewById(R.id.back);
        back_ll = (LinearLayout) findViewById(R.id.back_ll);

        appContext = getContentResolver();

        imgFileGallery = new File("");

        save.setOnClickListener(this);

        back.setOnClickListener(this);
        back_ll.setOnClickListener(this);

        blurr_img.setDrawingCacheEnabled(false);
        profile_pic.setDrawingCacheEnabled(false);

        //	profile_pic.setAdjustViewBounds(true);
	/*	profile_pic.setScaleType(ScaleType.FIT_CENTER);*/

        getProfileCall();

        if(Constants.LOGIN_TYPE.equalsIgnoreCase("fb")){
            profile_pic.setOnClickListener(this);
            firstname.setEnabled(false);
            lastname.setEnabled(false);
        } else {
            profile_pic.setOnClickListener(this);
            firstname.setEnabled(true);
            lastname.setEnabled(true);
        }
    }

    private void getProfileCall() {
        if (isConnected) {
            new getProfile(Constants.USER_ID).execute(new Void[0]);
        } else {
            showDialog(Constants.No_INTERNET);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == save) {
            CallUpdateProfileAPI();
        } else if (v == profile_pic) {
            getImage();
        } else if (v == back || v == back_ll) {
           /* Intent i = new Intent(Profile.this, Settings.class);
            startActivity(i);*/

            finish();
        }
    }

    private void CallUpdateProfileAPI() {

        String firstname_text = firstname.getText().toString();
        String lastname_text = lastname.getText().toString();

        String location_text = location.getText().toString();
        String occupation_text = occupation.getText().toString();
        String skills_text = skills.getText().toString();
        String background_text = background.getText().toString();
        String language_text = languages.getText().toString();

        if (isConnected) {
            updateProfileObj = new updateProfileTask(firstname_text,
                    lastname_text, location_text, occupation_text, skills_text,
                    background_text, language_text);
            updateProfileObj.execute();

        } else {
            showDialog(Constants.No_INTERNET);
        }
    }

    private void getImage() {
        Intent GaleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(GaleryIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String path = getRealPathFromURI(selectedImage);

                Log.e("**** path ****", "" + path);
                InputStream imageStream = null;
                try {
                    imageStream = appContext.openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("Exception==", "" + e);
                }
                takenImage = BitmapFactory.decodeStream(imageStream);

                takenImage = decodeFile(path);

               // takenImage = resizeBitmap(path);

               profile_pic.setImageBitmap(takenImage);
                blurr_img.setImageBitmap(takenImage);


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

                Log.e("picturePath==", "" + picturePath);

                imgFileGallery = new File(picturePath);

               /*Bitmap newCroppedBitmap = takenImage;
                croppedBitmap(newCroppedBitmap);*/

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public class updateProfileTask extends AsyncTask<String, Void, String> {
        ByteArrayOutputStream baos;

        String firstNAME, lastNAME, emailID, Location, Occupation, Skills,
                Backgorund, language;

        public updateProfileTask(String firstname_text, String lastname_text,
                                 String location_text, String occupation_text,
                                 String skills_text, String background_text, String language_text) {

            this.firstNAME = firstname_text;
            this.lastNAME = lastname_text;
            this.Location = location_text;
            this.Occupation = occupation_text;
            this.Skills = skills_text;
            this.Backgorund = background_text;
            this.language = language_text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(Profile.this,
                    R.drawable.loadingicon);
            db.show();

        }

        @Override
        protected String doInBackground(String... Params) {
            try {
               baos = new ByteArrayOutputStream();
                takenImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            } catch (Exception e) {
                Log.e("excptn==", "" + e);
            }

			/*
			 * http://phphosting.osvin.net/divineDistrict/api/userOptions.php?
			 * user_id
			 * =31&authKey=divineDistrict@31&radius=30&organisations=7,8&image=
			 */

            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpClientUpload client = new HttpClientUpload(
                        "https://takeataskservices.com/WEB_API/updateProfile.php?");
                client.connectForMultipart();

				/*
				 * http://phphosting.osvin.net/TakeATask/WEB_API/updateProfile.php
				 */
				/*
				 * 1.fname 2.lname 3.address 4.zip_code 5.city 6.state 7.country
				 * 8.phone 9.dob 10.latitude 11.longitude 12.profile_pic
				 * 13.authkey - Auth_TakeATask2015
				 */

                client.addFormPart("fname", this.firstNAME);
                client.addFormPart("lname", this.lastNAME);
                client.addFormPart("address", this.Location);
                client.addFormPart("language", this.language);
                client.addFormPart("occupation", this.Occupation);
                client.addFormPart("skills", this.Skills);
                client.addFormPart("background", this.Backgorund);

                client.addFormPart("emailId", Constants.EMAIL);
                client.addFormPart("authkey", Constants.AUTH_KEY);

                client.addFormPart("login_via", Constants.LOGIN_TYPE);

                if (!(imgFileGallery.getName().equals("") || imgFileGallery
                        .getName() == null)) {


                    client.addFilePart("profile_pic", imgFileGallery.getName(),
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

                //"ResponseCode":true,"Message":"Profile update Successfully"


                JSONObject localJSONObject = new JSONObject(result);
                HashMap<String, String> localHashMap = new HashMap<String, String>();
                String status = localJSONObject.getString("ResponseCode");
                if (status.equalsIgnoreCase("true")) {

                    isSuccess = true;
                    showDialog("Profile updated successfully.");

                } else {
					/*
					 * Intent i = new Intent(PicAddORgRadius.this , Home.class);
					 * startActivity(i);
					 */

                    Toast.makeText(Profile.this, "Error occured...",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                Log.e("Exception===", "" + e);
                showDialog("Something went wrong while processing your request.Please try again.");
            }
        }

    }


    public class getProfile extends AsyncTask<Void, Void, Void> {
        Functions function = new Functions();

        HashMap result = new HashMap();

        ArrayList localArrayList = new ArrayList();

        String id;

        public getProfile(String ID) {

            this.id = ID;
        }

        protected Void doInBackground(Void... paramVarArgs) {

            //http://phphosting.osvin.net/TakeATask/WEB_API/getUser.php?authkey=Auth_TakeATask2015&id=55


            try {
                localArrayList.add(new BasicNameValuePair("authkey", Constants.AUTH_KEY));
                localArrayList.add(new BasicNameValuePair("id", this.id));


                result = function.getProfile(localArrayList);

            } catch (Exception localException) {

            }

            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            db.dismiss();

            try {
                if (result.get("ResponseCode").equals("true")) {

                    String name_text = (String) result.get("fname");
                    String last_name = (String) result.get("lname");
                    String address_text = (String) result.get("address");
                    String city_text = (String) result.get("city");
                    String state_text = (String) result.get("state");
                    String country_text = (String) result.get("country");
                    String ratings_text = (String) result.get("ratings");
                    final String profile_text = (String) result.get("profile_pic");

                    String background = (String) result.get("background");
                    String skills = (String) result.get("skills");
                    String occupation = (String) result.get("occupation");

                    String laguage_text = (String) result.get("language");


                    firstname.setText(name_text);
                    lastname.setText(last_name);
                    location.setText(address_text);
                    languages.setText(laguage_text);
                    Profile.this.occupation.setText(occupation);
                    Profile.this.skills.setText(skills);
                    Profile.this.background.setText(background);

                    Thread t = new Thread(){
                        public void run(){
                            final Bitmap bitmapToGetFromURL = getBitmapFromURL(profile_text);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    croppedBitmap(bitmapToGetFromURL);
                                }
                            });

                        }
                    };t.start();



                   /* imageLoader.DisplayImage(profile_text, R.drawable.noimg, profile_pic);
                    imageLoader.DisplayImage(profile_text, R.drawable.noimg, blurr_img);*/
                }
            } catch (Exception ae) {
                //showDialog(Constants.ERROR_MSG);
                ae.printStackTrace();
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = new TransparentProgressDialog(Profile.this,
                    R.drawable.loadingicon);
            db.show();
        }

    }


    //****************************** ROTATE BITMAP *********************************************//

    public Bitmap decodeFile(String path) {//you can provide file path here
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 0;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);

            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Log.e("ExifInteface .........", "rotation =" + orientation);

            //exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

            Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
                //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                return bitmap;
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    //******************************************** crop Image *************************************//

    private Bitmap resizeBitmap(String path) {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int outWidth, outHeight;
        if (path != null) {
            BitmapFactory.decodeFile(path, options);
            outWidth = options.outWidth;
            outHeight = options.outHeight;
        } else {
            if (photoBitmap != null) {
                outWidth = photoBitmap.getWidth();
                outHeight = photoBitmap.getHeight();
            } else {
                return null;
            }
        }

        int ratio = (int) ((((float) outWidth) / mobile_width) + 0.5f);

        if (ratio == 0) {
            ratio = 1;
        }

        if (path != null) {
            options.inJustDecodeBounds = false;
            options.inSampleSize = ratio;
            // Applog.Log(TAG, "from gallery  " + ratio);
            photoBitmap = BitmapFactory.decodeFile(path, options);
            // ivItemPhoto.setImageBitmap(photoBitmap);
            return photoBitmap;
        } else {
            outWidth = outWidth / ratio;
            outHeight = outHeight / ratio;
            photoBitmap = Bitmap.createScaledBitmap(photoBitmap, outWidth,
                    outHeight, true);
            return photoBitmap;
        }
    }

    private void croppedBitmap(Bitmap takenImage2) {


        Bitmap cropedBitmap = null;
        ScalingUtilities mScalingUtilities = new ScalingUtilities();
        Bitmap mBitmap = null;
        if (takenImage2 != null)

        {

            int[] size = getImageHeightAndWidthForProFileImageHomsecreen(Profile.this);
            cropedBitmap = mScalingUtilities
                    .createScaledBitmap(takenImage2, size[1],
                            size[0], ScalingUtilities.ScalingLogic.CROP);
            //takenImage2.recycle();
            mBitmap = mScalingUtilities.getCircleBitmap(
                    cropedBitmap, 1);
            //cropedBitmap.recycle();



            profile_pic.setImageBitmap(mBitmap);
            blurr_img.setImageBitmap(mBitmap);
        }
    }


    public static Bitmap getBitmapFromURL(final String src) {


            try

            {
                URL url = null;
                try {
                    url = new URL(src);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }

            catch(
            IOException e
            )

            {
                e.printStackTrace();
                return null;
            }

        catch(Exception e){
            e.printStackTrace();
            return  null;
        }

    }


    //imageLayoutHeightandWidth
    public int[] getImageHeightAndWidthForProFileImageHomsecreen(
            Activity activity) {
        // //Log.i(TAG, "getImageHeightAndWidth");

        int imageHeightAndWidth[] = new int[2];
        int screenHeight = getHeight(activity);
        int screenWidth = getWidth(activity);
        // //Log.i(TAG, "getImageHeightAndWidth  screenHeight "+screenHeight);
        // //Log.i(TAG, "getImageHeightAndWidth  screenWidth  "+screenWidth);
        int imagehiegth;
        int imagewidth;
        if ((screenHeight <= 500 && screenHeight >= 480)
                && (screenWidth <= 340 && screenWidth >= 300)) {
            // //Log.i(TAG, "getImageHeightAndWidth mdpi");
            imagehiegth = 100;
            imagewidth = 100;
            imageHeightAndWidth[0] = imagehiegth;
            imageHeightAndWidth[1] = imagewidth;

        }

        else if ((screenHeight <= 400 && screenHeight >= 300)
                && (screenWidth <= 240 && screenWidth >= 220))

        {

            // //Log.i(TAG, "getImageHeightAndWidth ldpi");
            imagehiegth = 120;
            imagewidth = 120;
            imageHeightAndWidth[0] = imagehiegth;
            imageHeightAndWidth[1] = imagewidth;
        }

        else if ((screenHeight <= 840 && screenHeight >= 780)
                && (screenWidth <= 500 && screenWidth >= 440)) {

            // //Log.i(TAG, "getImageHeightAndWidth hdpi");
            imagehiegth = 150;
            imagewidth = 150;
            imageHeightAndWidth[0] = imagehiegth;
            imageHeightAndWidth[1] = imagewidth;
        } else if ((screenHeight <= 1280 && screenHeight >= 840)
                && (screenWidth <= 720 && screenWidth >= 500)) {

            // //Log.i(TAG, "getImageHeightAndWidth xdpi");
            imagehiegth = 200;
            imagewidth = 200;
            imageHeightAndWidth[0] = imagehiegth;
            imageHeightAndWidth[1] = imagewidth;
        } else {
            imagehiegth = 200;
            imagewidth = 200;
            imageHeightAndWidth[0] = imagehiegth;
            imageHeightAndWidth[1] = imagewidth;
        }

        return imageHeightAndWidth;
    }

    @SuppressLint("NewApi")
    public static int getWidth(Context mContext) {
        int width = 0;
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth(); // deprecated
        }
        return width;
    }

    @SuppressLint("NewApi")
    public static int getHeight(Context mContext) {
        int height = 0;
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight(); // deprecated
        }
        return height;
    }
}
