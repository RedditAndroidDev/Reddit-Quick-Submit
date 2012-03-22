package com.colmmoore.RedditQuickSubmit;

import android.app.Activity;
import android.os.Bundle;
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
        imageView.setImageBitmap(RedditQuickSubmitActivity.photo);
    }
    
}












