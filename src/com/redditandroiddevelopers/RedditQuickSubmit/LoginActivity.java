package com.redditandroiddevelopers.RedditQuickSubmit;

import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.redditandroiddevelopers.RedditQuickSubmit.R;

public class LoginActivity extends Activity {
    
    private static LoginActivity instance;
    
    // Start the main activity
    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	// Remove the title from the window; it doesn't look good.
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.login);
	
	instance = this;

    };
    
    public void onLoginClick(View v) {
	
	final ProgressDialog loginDialog;
	
	loginDialog = new ProgressDialog(LoginActivity.this);
	loginDialog.setMessage("Loading, please wait!");
	
	final EditText username = (EditText) findViewById(R.id.usernameForm);
	final EditText password = (EditText) findViewById(R.id.passwordForm);

	new Thread(new Runnable() {

	    public void run() {

		if (username.getText().toString().length() == 0) {
		    
		    instance.runOnUiThread(new Runnable() {
			public void run() {
			    Toast.makeText(instance, "Please enter a username!", Toast.LENGTH_SHORT).show();
			}
		    });

		} else if (password.getText().toString().length() == 0) {

		    instance.runOnUiThread(new Runnable() {
			public void run() {
			    Toast.makeText(instance, "Please enter a password!", Toast.LENGTH_SHORT).show();
			}
		    });


		} else {
			
		    loginDialog.show();

		    final String url = "https://ssl.reddit.com/api/login/"
			    + username.getText().toString();
		    try {
			LoginFunc func = new LoginFunc();
			boolean loginSuccess = func.login(url, username
				.getText().toString(), password
				.getText().toString());
			if (loginSuccess) {
			    loginDialog.dismiss();
			    // Login successful, carry on
			    Intent myIntent = new Intent(instance,
	    				RedditQuickSubmitActivity.class);
			    startActivityForResult(myIntent, 0);    
			} else {
			    loginDialog.dismiss();
			    instance.runOnUiThread(new Runnable() {
				public void run() {				    
				    Toast.makeText(instance, 
					    "Error: " + LoginFunc.getErrorMessage(), 
					    Toast.LENGTH_SHORT
					    ).show();
				}
			    });			    
			}

		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }
	}).start();
    }
    
    public static Context getContext() {
        return instance;
    }

}
