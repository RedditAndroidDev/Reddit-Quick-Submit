
package com.redditandroiddevelopers.RedditQuickSubmit;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;


import android.preference.PreferenceManager;


public class SubmitTextActivity extends Activity {

    private static final String TAG = null;
    private String Cookie;
    
    //
    SharedPreferences settings;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.submittext);
        AsyncHttpClient myClient = new AsyncHttpClient();
    	PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
    	
    	settings = PreferenceManager.getDefaultSharedPreferences(this);
    
    	Cookie = myCookieStore.getCookies().toString();
    
    		
        final Button Btn = (Button) findViewById(R.id.submitTextButton);
        
        Btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {            
            	new submitLink().execute();
            }
        });
       
    }

class submitLink extends AsyncTask<String, Void, SubmitTextActivity> {
    
	    protected SubmitTextActivity doInBackground(String... urls) {

	    	String u;
			URL url = null;
			try {
				url = new URL("http://www.reddit.com/api/submit");
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//
		
		    
			
			  Log.v(TAG, "OUR COOKIE IS " + Cookie);
			final EditText titleText = (EditText) findViewById(R.id.titleForm);
			final EditText bodyText= (EditText) findViewById(R.id.bodyForm);
			final EditText subRedditText= (EditText) findViewById(R.id.subredditForm);
			InputStream is;
			
			String modhash = settings.getString("modhash", "");
			String cookie = settings.getString("cookie", "");
			String cookieValue = settings.getString("cookieValue", "");
			
	        String data = "&title=" + titleText.getText().toString() +"&text=" + titleText.getText().toString() +
	        				"&sr=" + subRedditText.getText().toString() +"&sr=" + "self" +"&uh="+ modhash;
	        
			
			Log.v(TAG, "OUR DATA IS " + data);
	        HttpURLConnection ycConnection = null;
	        try {
					ycConnection = (HttpURLConnection)url.openConnection();
					ycConnection.setRequestMethod("POST");
			        ycConnection.setDoOutput(true);
			        ycConnection.setUseCaches(false);
			        ycConnection.setRequestProperty("Cookie","reddit_session=" + cookie);			        
			        ycConnection.setRequestProperty("Content-Type",
			                "application/x-www-form-urlencoded; charset=UTF-8");
			        ycConnection.setRequestProperty("Content-Length", String.valueOf(data
			                .length()));
			        ycConnection.setDoInput(true);
			        ycConnection.setDoOutput(true);
			        DataOutputStream wr = new DataOutputStream(ycConnection.getOutputStream());
			        wr.writeBytes(data);
			        wr.flush();
			        wr.close();
			       is = ycConnection.getInputStream();
			       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			        String line;
			        String response = "";
			        
			        while ((line = rd.readLine()) != null) {
			            response += line;
			            response += '\r';
			        }
			        for (Entry<String, List<String>> r : ycConnection.getHeaderFields()
			                .entrySet()) {
			            System.out.println(r.getKey() + ": " + r.getValue());
			        }
			        rd.close();
			        System.out.println(response.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       

	       
	       
		    return null;
			
			
	    }


		protected void onPostExecute(SubmitTextActivity feed) {
			
	    }
	 }
	 
	
}
