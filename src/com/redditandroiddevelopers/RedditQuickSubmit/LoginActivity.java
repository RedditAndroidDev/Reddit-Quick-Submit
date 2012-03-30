package com.redditandroiddevelopers.RedditQuickSubmit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

public class LoginActivity extends Activity {

    private static String errorType;
    private static String errorMessage;
    private static String errorCode;
    private String modhash;
    private String cookie;

    SharedPreferences settings;

    // Start the main activity
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        settings = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        // Check already login or not
        boolean loginStatus = settings.getBoolean("haveAccount", false);

        if (loginStatus) {

            // Login successful, carry on
            Intent myIntent = new Intent(getApplicationContext(),
                    RedditQuickSubmitActivity.class);
            startActivityForResult(myIntent, 0);

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Please login first",
                    Toast.LENGTH_SHORT).show();
        }

    }

    ;

    public void onLoginButtonClick(View v) {

        new LoginRun().execute();
    }

    private class LoginRun extends AsyncTask<Void, Void, Void> {

        private ProgressDialog loginDialog;

        @Override
        protected void onPreExecute() {
            loginDialog = new ProgressDialog(LoginActivity.this);
            loginDialog.setMessage("Loggining in! Please wait.");
            loginDialog.setIndeterminate(true);
            loginDialog.setCancelable(false);
            loginDialog.show();
        }


        protected Void doInBackground(Void... unused) {

            return null;
        }


        protected void onPostExecute(Void unused) {

            final EditText username = (EditText) findViewById(R.id.usernameForm);
            final EditText password = (EditText) findViewById(R.id.passwordForm);

            if (username.getText().toString().length() == 0) {

                loginDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Please enter a username!", Toast.LENGTH_SHORT).show();

            } else if (password.getText().toString().length() == 0) {

                loginDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Please enter a password!", Toast.LENGTH_SHORT).show();

            } else {

                final String url = "https://ssl.reddit.com/api/login/"
                        + username.getText().toString();
                try {


                    // Try logging in with users credentials
                    boolean loginSuccess = login(url, username.getText()
                            .toString(), password.getText().toString());

                    if (loginSuccess) {

                        loginDialog.dismiss();

                        // Login successful.
                        // Check to see if we came from another activity:
                        if(getIntent().getBooleanExtra("needsLogin",false)) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Intent myIntent = new Intent(getApplicationContext(),
                                    RedditQuickSubmitActivity.class);
                            startActivityForResult(myIntent, 0);
                        }
                    } else {

                        loginDialog.dismiss();

                        // Show error for wrong password etc..
                        Toast.makeText(getApplicationContext(),
                                "Error: " + getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (loginDialog.isShowing()) {
                loginDialog.dismiss();
            }
        }
    }

    public boolean login(String u, String user, String pw) throws IOException {

        URL url = new URL(u);

        System.out.println("Username: " + user + ", Password: " + pw);
        String data = "api_type=json&user=" + user + "&passwd=" + pw;

        HttpURLConnection ycConnection = null;
        ycConnection = (HttpURLConnection) url.openConnection();
        ycConnection.setRequestMethod("POST");
        ycConnection.setDoOutput(true);
        ycConnection.setUseCaches(false);
        ycConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        ycConnection.setRequestProperty("Content-Length", String.valueOf(data
                .length()));

        DataOutputStream wr = new DataOutputStream(ycConnection
                .getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();
        InputStream is = ycConnection.getInputStream();
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

        if (response == null || response == "") {
            return false;
        }

        boolean login_success = false;
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(response).getAsJsonObject();
        JsonObject members = object.getAsJsonObject("json");

        JsonArray error = (JsonArray) members.getAsJsonArray("errors");
        if (error.size() > 0) {
            JsonArray errorDetails = (JsonArray) error.get(0);
            errorType = errorDetails.get(0).getAsString();
            errorMessage = errorDetails.get(1).getAsString();
            errorCode = errorDetails.get(2).getAsString();
        }

        JsonObject dataObject = members.getAsJsonObject("data");

        if (dataObject == null)
            return login_success;

        modhash = dataObject.get("modhash").getAsString();
        cookie = dataObject.get("cookie").getAsString();

        if (modhash != "")
            login_success = true;

        System.out.println(cookie);

        AsyncHttpClient myClient = new AsyncHttpClient();

        PersistentCookieStore myCookieStore = new PersistentCookieStore(
                getApplicationContext());
        myClient.setCookieStore(myCookieStore);

        BasicClientCookie newCookie = new BasicClientCookie(
                "RedditQuickSubmit", cookie);

        newCookie.setVersion(1);
        newCookie.setDomain("reddit.com");
        newCookie.setPath("/");

        myCookieStore.addCookie(newCookie);

        // save cookie and modhash in sharedpreferences
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("modhash", modhash);
        editor.putString("cookie", cookie);
        editor.putString("cookieValue", newCookie.toString());
        editor.putBoolean("haveAccount", true);
        editor.commit();

        return login_success;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }
}
