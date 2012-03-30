
package com.redditandroiddevelopers.RedditQuickSubmit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;

public abstract class SubmitImageActivity extends Activity {
    private static final String TAG = "SubImg";
    private static final int LOGIN_RQ = 1;
    private SharedPreferences settings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        if(!settings.getBoolean("haveAccount",false)){
            Log.i(TAG, "Retrieving Login info");
            Intent loginIntent = new Intent(this,LoginActivity.class);
            loginIntent.putExtra("needsLogin",true);
            startActivityForResult(loginIntent,LOGIN_RQ);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hides the horrible
                                                       // title at the top of
                                                       // the app.
        setContentView(R.layout.submitimage);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i(TAG,"Returning from login");
        if(requestCode == LOGIN_RQ){
            if(resultCode == RESULT_CANCELED){
                Log.i(TAG, "Ignored Login");
                finish();
            }
            settings = PreferenceManager.getDefaultSharedPreferences(this);
        }
    }
}
