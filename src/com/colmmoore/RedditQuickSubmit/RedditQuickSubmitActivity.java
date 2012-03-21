package com.colmmoore.RedditQuickSubmit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RedditQuickSubmitActivity extends Activity { 
    
    private AlertDialog.Builder alertDialog;
    
    
    final CharSequence[] items = {"Camera", "Gallery"}; 	// This is used for the AlertDialog
    private static final int CAMERA_PIC_REQUEST = 1337; 	// This is where the image captured from the camera is stored. Still
    								// gotta figure this but out. 
    
    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.main);
        
        Button submitImageButton = (Button) findViewById(R.id.submitImageButton); 
 
	alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Select Source");
        
        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
        	startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);  
            }
        });
        
        submitImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog myalert = alertDialog.create();
                myalert.show();
            }
                
        });
       
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == CAMERA_PIC_REQUEST) {
	    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");  
	}
    }

   
}
    
    
    