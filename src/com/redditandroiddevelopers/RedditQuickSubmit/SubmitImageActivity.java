package com.redditandroiddevelopers.RedditQuickSubmit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import com.redditandroiddevelopers.RedditQuickSubmit.R;

public abstract class SubmitImageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  	// Hides the horrible title at the top of the app.
        setContentView(R.layout.submitimage);
    }
}

