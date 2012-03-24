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
    
    public static void login(String u, String user, String pw) throws IOException {

	URL url = new URL(u);
	
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
    }
}



