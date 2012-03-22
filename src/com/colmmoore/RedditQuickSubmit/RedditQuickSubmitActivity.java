package com.colmmoore.RedditQuickSubmit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RedditQuickSubmitActivity extends Activity {
	
	
	final CharSequence[] items = {"Camera", "Gallery"}; 	// This is used for the AlertDialog
	public static final int CAMERA_PIC_REQUEST = 0; 		// This is the activity request code necessary for startActivityForResult(Intent);
	public static String imageid;
	public static Intent data;
	
	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  	// Hides the horrible title at the top of the app. 
		setContentView(R.layout.main);
		
		
		//Bind to the initialized buttons
		Button submitImageButton = (Button) findViewById(R.id.submitImageButton); 	// Creates buttons to be used using the 
		Button submitTextButton = (Button) findViewById(R.id.submitTextButton);		// properties specified in the XML file
		Button submitLinkButton = (Button) findViewById(R.id.submitLinkButton);
		
		//And assign their respective click listeners
		submitImageButton.setOnClickListener(new SubmitImageOnClickListener());
		submitTextButton.setOnClickListener(new SubmitTextOnClickListener());
		submitLinkButton.setOnClickListener(new SubmitLinkOnClickListener());
		
	}
	
	// A lot of this code is experimental, can someone improve upon it maybe? I'm trying to get the image the camera took
	// and open a new Activity displaying that image. Once we get that working, we can add a text box for title, and then a 
	// submit button at the top. 
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			if (data != null) {
				
				Bitmap photo = (Bitmap) data.getExtras().get("data"); 
			
				Intent cameraSubmit = new Intent(RedditQuickSubmitActivity.this, SubmitImageActivity.class);
				cameraSubmit.putExtra("data", photo);
				startActivity(cameraSubmit);
				
			}
		}
	}
	
	private class SubmitImageOnClickListener implements OnClickListener {
		// Listens for a button click on the
		@Override															// Submit Image button. 
		public void onClick(View v) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());					// Creates a new AlertDialog for the popup
			alertDialog.setTitle("Select Source");							// Sets its title
			
			
			alertDialog.setItems(items, new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface dialog, int item) {
					if(item == 0) {
						startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST); 
					}
				}
			});
			AlertDialog myalert = alertDialog.create();						// Opens the Alert Dialog above if click on. 
			myalert.show();
		}
	}
	
	private class SubmitTextOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(v.getContext(), SubmitTextActivity.class);
			startActivityForResult(myIntent, 0);
		}
	}
	
	private class SubmitLinkOnClickListener implements OnClickListener {	
		@Override
		public void onClick(View v) {						
			Intent myIntent = new Intent(v.getContext(), SubmitLinkActivity.class);
			startActivityForResult(myIntent, 0);
		}
	}
}