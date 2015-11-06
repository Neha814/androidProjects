package com.takeatask;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import functions.Constants;

/**
 * Created by sandeep on 13/10/15.
 */
public class ViewImage extends Activity{

    ImageView view_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_img);

        view_img = (ImageView) findViewById(R.id.view_img);

       if(Constants.image_number==1){
           view_img.setImageBitmap(Constants.TAKENIMAGE1);
       } else if(Constants.image_number==2){
           view_img.setImageBitmap(Constants.TAKENIMAGE2);
       }else if(Constants.image_number==3){
           view_img.setImageBitmap(Constants.TAKENIMAGE3);
       }
       else if(Constants.image_number==4){
           view_img.setImageBitmap(Constants.TAKENIMAGE4);
       }
       else if(Constants.image_number==5){
           view_img.setImageBitmap(Constants.TAKENIMAGE5);
       }
    }
}
