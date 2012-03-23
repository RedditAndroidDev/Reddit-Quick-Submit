package com.redditandroiddevelopers.RedditQuickSubmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.redditandroiddevelopers.RedditQuickSubmit.R;

public class LoginActivity extends Activity {

    // Start the main activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	// Remove the title from the window; it doesn't look good. 
	requestWindowFeature(Window.FEATURE_NO_TITLE); 
	setContentView(R.layout.login);
	
	Button submitLinkButton = (Button) findViewById(R.id.submitLoginButton);

	// Listener for the Link button to open the corresponding activity
	submitLinkButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent myIntent = new Intent(v.getContext(),
			RedditQuickSubmitActivity.class);
		startActivityForResult(myIntent, 0);
	    }
	});
    }
}

