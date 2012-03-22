package com.colmmoore.RedditQuickSubmit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.ImageView;

public class SubmitImageActivity extends Activity { 
    
    private ImageView imageView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  	// Hides the horrible title at the top of the app. 
        setContentView(R.layout.submitimage);
  
        this.imageView = (ImageView)this.findViewById(R.id.cameraPhoto);
        
        Bitmap photo = (Bitmap) getIntent().getExtras().get("data");
        if (photo != null) 
        	imageView.setImageBitmap(photo);
    }
    
}












