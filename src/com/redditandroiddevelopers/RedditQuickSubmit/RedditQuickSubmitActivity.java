package com.redditandroiddevelopers.RedditQuickSubmit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RedditQuickSubmitActivity extends Activity {

    // Dialog identifiers and data
    public static final int DIALOG_IMAGE = 0;
    public static final CharSequence[] DIALOG_IMAGE_ITEMS = {
            "Camera", "Gallery"
    };

    // Request codes
    public static final int CAMERA_PIC_REQUEST = 0;

    private static final String TAG = null;

    public static Intent data;
    public static Bitmap photo;
    public static Uri imagePath;

    // Start the main activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the title from the window; it doesn't look good.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        Button submitImageButton = (Button) findViewById(R.id.submitImageButton);
        Button submitTextButton = (Button) findViewById(R.id.submitTextButton);
        Button submitLinkButton = (Button) findViewById(R.id.submitLinkButton);

        // Listener for the Image button to open the corresponding activity
        submitImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_IMAGE);
            }
        });

        // Listener for the Text button to open the corresponding activity
        submitTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),
                        SubmitTextActivity.class);
                startActivity(myIntent);
            }
        });

        // Listener for the Link button to open the corresponding activity
        submitLinkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),
                        SubmitLinkActivity.class);

                startActivity(myIntent);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_IMAGE:
                return new AlertDialog.Builder(this)
                        .setTitle("Select source")
                        .setItems(DIALOG_IMAGE_ITEMS, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                                } else if (i == 1) {
                                    // Display message when clicking on gallery. Someone is
                                    // working on this I believe.
                                    CharSequence text = "Gallery functionality will be added soon!";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(RedditQuickSubmitActivity.this, text, duration);
                                    toast.show();
                                }
                            }
                        }).create();
        }
        return super.onCreateDialog(id);
    }

    // This gets the result from the camera (i.e. the picture) when it is called
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Intent cameraSubmit = new Intent(
                        RedditQuickSubmitActivity.this,
                        SubmitCameraImageActivity.class);

                startActivity(cameraSubmit);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Context context = getApplicationContext();
                CharSequence text = "Failed to get the image";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    private Uri getImageUri() {


        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("-mm-ss");

        String redditcapture = "redditquicksubmit" + df.format(date) + ".jpg";
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM", redditcapture);

        Uri imgUri = Uri.fromFile(file);
        imagePath = imgUri;
        return imgUri;
    }
}
