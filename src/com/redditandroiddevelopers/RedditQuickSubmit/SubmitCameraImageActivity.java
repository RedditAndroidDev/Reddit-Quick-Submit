package com.redditandroiddevelopers.RedditQuickSubmit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SubmitCameraImageActivity extends SubmitImageActivity {
	private static final String TAG = null;
	private ImageView imageView;
	
	private ProgressDialog uploadDialog;
	SharedPreferences settings;
	private String response = "";

	private String imgurl = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		this.imageView = (ImageView) this.findViewById(R.id.cameraPhoto);
		
		imageView.setImageURI(RedditQuickSubmitActivity.imagePath);
		
		uploadDialog = new ProgressDialog(SubmitCameraImageActivity.this);
	
		settings = PreferenceManager.getDefaultSharedPreferences(this);

		Log.v(TAG, "ImagePath in URI = " + RedditQuickSubmitActivity.imagePath);
		Log.v(TAG, "ImagePath in without file = " + RedditQuickSubmitActivity.imagePath.getPath());
		
		 final EditText title = (EditText) findViewById(R.id.titleForm);
		 final EditText subreddit = (EditText) findViewById(R.id.subredditForm);
		
		
		final Button imageButton = (Button) findViewById(R.id.submitTextButton);
		imageButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (title.getText().toString().length() == 0) {
					
					
					Toast.makeText(getApplicationContext(),
						"Please enter a title!", Toast.LENGTH_SHORT).show();

				    } else if (subreddit.getText().toString().length() == 0) {

					
					Toast.makeText(getApplicationContext(),
						"Please enter a subreddit!", Toast.LENGTH_SHORT).show();

				    } else {

				    	new uploadImageTask().execute();
				    }
			}
		});

	}

	//uploading image
	private class uploadImageTask extends AsyncTask<URL, Integer, Long> {
		
		
		
		@Override
		protected void onPreExecute() {
		        
			 	uploadDialog.setMessage("Uploading! Please wait...");
		        uploadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		        uploadDialog.setCancelable(false);
		        uploadDialog.setProgress(0);
		        uploadDialog.show();
		        
		    }
		
		protected Long doInBackground(URL... urls) {
			Bitmap imageToUpload = BitmapFactory.decodeFile(RedditQuickSubmitActivity.imagePath.getPath());
			
		Log.v(TAG, "ImagePath POST in STRING = "
				+ RedditQuickSubmitActivity.imagePath.toString());
	
		List<NameValuePair> postContent = new ArrayList<NameValuePair>(2);
		postContent.add(new BasicNameValuePair("key",
				"14b450817a15fe130a76bbaf156d692f"));
		postContent.add(new BasicNameValuePair("image",
				RedditQuickSubmitActivity.imagePath.toString()));
		 uploadDialog.setProgress(20);
		 
			String url = "http://api.imgur.com/2/upload";
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);

	try {
		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);

		// resize image
	
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		imageToUpload.compress(Bitmap.CompressFormat.PNG, 50, baos);
		uploadDialog.setProgress(30);
		// Creates Byte Array from picture
		byte[] data = baos.toByteArray();
		ByteArrayBody bab = new ByteArrayBody(data, "image.jpg");
		
			
		
		for (int index = 0; index < postContent.size(); index++) {
			if (postContent.get(index).getName()
					.equalsIgnoreCase("image")) {
				// If the key equals to "image", we use FileBody to
				// transfer the data

				entity.addPart(postContent.get(index).getName(), bab);
				Log.i("postcontentdata if image = ",
						postContent.get(index).toString());
			} else {
				// Normal string data
				entity.addPart(postContent.get(index).getName(),
						new StringBody(postContent.get(index)
								.getValue()));
				Log.i("postcontentdata if not image = ", postContent.get(index)
						.toString());
				
			}
			
			
		}

		httpPost.setEntity(entity);
		
		HttpResponse response = httpClient.execute(httpPost,
				localContext);
		Map<String, String> mImgurResponse = parseResponse(response);

		Iterator it = mImgurResponse.entrySet().iterator();
				while (it.hasNext()) {
					HashMap.Entry pairs = (HashMap.Entry) it.next();
		
					Log.i("INFO", pairs.getKey().toString());
					if (pairs.getValue() != null) {
		
						Log.i("INFO", pairs.getValue().toString());
					}
				}

		} catch (IOException e) {
			e.printStackTrace();
		}
				return null;
			}

		protected void onProgressUpdate(Integer... progress) {
			 super.onProgressUpdate(progress);
			
		        
		     
			
			
		}

		protected void onPostExecute(Long result) {

			
			    
			
			uploadDialog.setProgress(50);
			new submitImageToReddit().execute();
		 
			
			
			 
		}
	}

	

	private Map<String, String> parseResponse(HttpResponse response) {
		String xmlResponse = null;

		Log.i("response data =", response.toString());
		try {
			xmlResponse = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (xmlResponse == null)
			return null;

		HashMap<String, String> ret = new HashMap<String, String>();
		ret.put("error", getXMLElementValue(xmlResponse, "error_msg"));
		ret.put("original", getXMLElementValue(xmlResponse, "original"));
		ret.put("imgur", getXMLElementValue(xmlResponse, "imgur_page"));
		ret.put("delete", getXMLElementValue(xmlResponse, "delete_page"));
		ret.put("small_square", getXMLElementValue(xmlResponse, "small_square"));
		ret.put("large_thumbnail",
				getXMLElementValue(xmlResponse, "large_thumbnail"));

		imgurl = getXMLElementValue(xmlResponse, "imgur_page");

		return ret;
	}

	private String getXMLElementValue(String xml, String elementName) {
		if (xml.indexOf(elementName) >= 0)
			return xml.substring(
					xml.indexOf(elementName) + elementName.length() + 1,
					xml.lastIndexOf(elementName) - 2);
		else
			return null;
	}

	public String getRealPathFromURI(String contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		
		Cursor cursor = managedQuery(Uri.parse(contentUri), proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private class submitImageToReddit extends AsyncTask<URL, Integer, Long> {
		
		@Override
		protected Long doInBackground(URL... params) {
			uploadDialog.setProgress(75);
		
			URL url = null;
			try {
				url = new URL("http://www.reddit.com/api/submit");
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//

			final EditText titleText = (EditText) findViewById(R.id.titleForm);

			final EditText subRedditText = (EditText) findViewById(R.id.subredditForm);

			InputStream is;

			String modhash = settings.getString("modhash", "");
			String cookie = settings.getString("cookie", "");

			String data = "?uh=" + modhash + "&url=" + imgurl + "&sr="
					+ subRedditText.getText().toString() + "&kind=" + "link"
					+ "&title=" + titleText.getText().toString();

			Log.v(TAG, "OUR DATA IS " + data);
			uploadDialog.setProgress(95);
			HttpURLConnection ycConnection = null;
			try {
				ycConnection = (HttpURLConnection) url.openConnection();
				ycConnection.setRequestMethod("POST");
				ycConnection.setDoOutput(true);
				ycConnection.setUseCaches(false);
				ycConnection.setRequestProperty("Cookie", "reddit_session="
						+ cookie);
				ycConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded; charset=UTF-8");
				ycConnection.setRequestProperty("Content-Length",
						String.valueOf(data.length()));
				ycConnection.setDoInput(true);
				ycConnection.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(
						ycConnection.getOutputStream());
				wr.writeBytes(data);
				wr.flush();
				wr.close();
				is = ycConnection.getInputStream();
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(is));
				String line;

				while ((line = rd.readLine()) != null) {
					response += line;
					response += '\r';
				}
				for (Entry<String, List<String>> r : ycConnection
						.getHeaderFields().entrySet()) {
					System.out.println(r.getKey() + ": " + r.getValue());
				}
				rd.close();
				System.out.println(response.toString());
				
				
				uploadDialog.setProgress(100);
				uploadDialog.dismiss();
				
				//post submitted, carry on!
				Intent myIntent = new Intent(getApplicationContext(),
						RedditQuickSubmitActivity.class);
					startActivityForResult(myIntent, 0);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		
	}
}
