package com.redditandroiddevelopers.RedditQuickSubmit;

import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
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
	
	final Button submitLoginButton = (Button) findViewById(R.id.submitLoginButton);
	final EditText username = (EditText) findViewById(R.id.usernameForm);
	final EditText password = (EditText) findViewById(R.id.passwordForm);
	
	
	
	submitLoginButton.setOnClickListener(new OnClickListener() { 				     
	    @Override
	    public void onClick(View v) { 
		if (username.getText().toString().length() == 0){
		    
		    Context context = getApplicationContext();
		    CharSequence text = "Please enter a username!";
		    int duration = Toast.LENGTH_SHORT;
		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
		
		} else if (password.getText().toString().length() == 0){
		    
		    Context context = getApplicationContext();
		    CharSequence text = "Please enter a password!";
		    int duration = Toast.LENGTH_SHORT;
		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
		    
		} else {
			
			final String url = "https://ssl.reddit.com/api/login/" + username.getText().toString();
		    try {
		    	LoginFunc func = new LoginFunc();
		    	boolean loginSuccess = func.login(url, username.getText().toString(), password.getText().toString());
		    	if(loginSuccess){
		    		//Login successful, carry on
		    		Intent myIntent = new Intent(v.getContext(),
		    				RedditQuickSubmitActivity.class);
		    		startActivityForResult(myIntent, 0);    		
		    	}
		    	else {
		    		Context context = getApplicationContext();
				    CharSequence text = "Error: " + func.getErrorMessage(); 
				    int duration = Toast.LENGTH_SHORT;
				    Toast toast = Toast.makeText(context, text, duration);
				    toast.show();
		    	}
		    	
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }
	});    		   		
    }
}

