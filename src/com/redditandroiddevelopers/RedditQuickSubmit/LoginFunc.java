package com.redditandroiddevelopers.RedditQuickSubmit;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class LoginFunc {
    
    private static String errorType;
	private static String errorMessage;
	private static String errorCode;
	private String modhash;
	private String cookie;

	public LoginFunc(){
		this.errorCode = "";
		this.errorType = "";
		this.errorMessage = "";
	}
	
	public boolean login(String u, String user, String pw) throws IOException {

		URL url = new URL(u);
	
		System.out.println("Username: " + user + ", Password: " + pw);
		String data = "api_type=json&user=" + user + "&passwd=" + pw;
        HttpURLConnection ycConnection = null;
        ycConnection = ( HttpURLConnection ) url.openConnection();
        ycConnection.setRequestMethod( "POST" );
        ycConnection.setDoOutput( true );
        ycConnection.setUseCaches( false );
        ycConnection.setRequestProperty( "Content-Type",
            "application/x-www-form-urlencoded; charset=UTF-8" );
        ycConnection.setRequestProperty( "Content-Length", String.valueOf( data.length() ) );

        DataOutputStream wr = new DataOutputStream(
            ycConnection.getOutputStream() );
        wr.writeBytes( data );
        wr.flush();
        wr.close();
        InputStream is = ycConnection.getInputStream();
        BufferedReader rd = new BufferedReader( new InputStreamReader( is ) );
        String line;
        String response = "";
        while ( ( line = rd.readLine() ) != null ) {
            response += line;
            response += '\r';
        }
        for ( Entry< String, List< String >> r : ycConnection.getHeaderFields().entrySet() ) {
            System.out.println( r.getKey() + ": " + r.getValue());
        }
        rd.close();
        System.out.println( response.toString());
        
        //TODO: Check this case on real hardware. 
        //Sometimes the connection returns an empty string on the emulator.
        if(response == null || response == ""){
        	return false;
        }
        
        boolean login_success = false; 
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(response).getAsJsonObject();
        JsonObject members = object.getAsJsonObject("json");
        
        JsonArray error = (JsonArray) members.getAsJsonArray("errors");
        if(error.size() > 0){
        	JsonArray errorDetails = (JsonArray) error.get(0);
        	errorType = errorDetails.get(0).getAsString();
        	errorMessage = errorDetails.get(1).getAsString();
        	errorCode = errorDetails.get(2).getAsString();
        }
    	
    	JsonObject dataObject = members.getAsJsonObject("data");
        if(dataObject == null)
        	return login_success;
        
        modhash = dataObject.get("modhash").getAsString();
    	cookie = dataObject.get("cookie").getAsString();
    	
    	if(modhash != "")
    		login_success = true;
    	
        return login_success;
    }
    
    public static String getErrorType() {
		return errorType;
	}

	public static void setErrorType(String errorType) {
		LoginFunc.errorType = errorType;
	}

	public static String getErrorMessage() {
		return errorMessage;
	}

	public static void setErrorMessage(String errorMessage) {
		LoginFunc.errorMessage = errorMessage;
	}

	public static String getErrorCode() {
		return errorCode;
	}

	public static void setErrorCode(String errorCode) {
		LoginFunc.errorCode = errorCode;
	}

	public String getModhash() {
		return modhash;
	}

	public void setModhash(String modhash) {
		this.modhash = modhash;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public class LoginData {
    	
    	
    	public LoginData(){
    	
    	}
    }
}



