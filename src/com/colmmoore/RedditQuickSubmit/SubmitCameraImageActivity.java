package com.colmmoore.RedditQuickSubmit;

import android.os.Bundle;
import android.widget.ImageView;

/**
 * User: Christopher Kruse <ckruse@ballpointcarrot.net>
 * Date: 3/22/12
 * Time: 1:14 AM
 */
public class SubmitCameraImageActivity extends SubmitImageActivity {
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.imageView = (ImageView)this.findViewById(R.id.cameraPhoto);
        imageView.setImageBitmap(RedditQuickSubmitActivity.photo);
    }
}
