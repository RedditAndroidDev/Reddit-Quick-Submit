package com.redditandroiddevelopers.RedditQuickSubmit;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
	
	// Get the EditTexts from the XML
	final EditText username = (EditText) findViewById(R.id.usernameForm);
	final EditText password = (EditText) findViewById(R.id.passwordForm);
	
	final String url = "https://ssl.reddit.com/api/login/" + username.getText().toString();

	submitLinkButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		
		/*
		
		// Doesn't work.. needs to be fixed
		if (username.getText().toString() == ""){
		    
		    Context context = getApplicationContext();
		    CharSequence text = "Please enter a username!";
		    int duration = Toast.LENGTH_SHORT;
		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
		
		// Doesn't work.. needs to be fixed
		} else if (password.getText().toString() == ""){
		    
		    Context context = getApplicationContext();
		    CharSequence text = "Please enter a password!";
		    int duration = Toast.LENGTH_SHORT;
		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
		    
		} else {
		    
                    try {
                	LoginFunc.login(url, username.getText().toString(), password.getText().toString());
                    } catch (IOException e) {
                	e.printStackTrace();
                    }
		}
	
		*/
		
		
		Intent myIntent = new Intent(v.getContext(),
			RedditQuickSubmitActivity.class);
		startActivityForResult(myIntent, 0);
		
	    }
	});
    }
}

