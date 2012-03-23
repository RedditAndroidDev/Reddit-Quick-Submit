package com.redditandroiddevelopers.RedditQuickSubmit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.redditandroiddevelopers.RedditQuickSubmit.R;

public class SubmitTextActivity extends Activity { 
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.submittext);
    }
    
}