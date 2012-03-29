
package com.redditandroiddevelopers.RedditQuickSubmit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RedditQuickSubmitActivity extends Activity {

    // Create a new instace of AlertDialog called 'alertDialog' to be used
    // later.
    private AlertDialog.Builder alertDialog;

    // This is used for the AlertDialog
    final CharSequence[] items = {
            "Camera", "Gallery"
    };

    // Create variables to be used by the camera intent
    public static final int CAMERA_PIC_REQUEST = 0;
    public static String imageid;
    public static Intent data;
    public static Bitmap photo;
    public static Uri imagePath;
    // Create the camera intent be be called later when the user selects the
    // camera
    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

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

        // Create a new AlertDialog for the pop-up
        alertDialog = new AlertDialog.Builder(this);

        // Set its title
        alertDialog.setTitle("Select Source");

        // Open pop up menu when Camera button is clicked
        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } else if (item == 1) {

                    // Display message when clicking on gallery. Someone is
                    // working on this I believe.
                    Context context = getApplicationContext();
                    CharSequence text = "Gallery functionality will be added soon!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        // Listener for the Image button to open the corresponding activity
        submitImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog myalert = alertDialog.create();
                myalert.show();
            }
        });

        // Listener for the Text button to open the corresponding activity
        submitTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),
                        SubmitTextActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        // Listener for the Link button to open the corresponding activity
        submitLinkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),
                        SubmitLinkActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    // This gets the result from the camera (i.e. the picture) when it is called
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (data != null) {
                photo = (Bitmap) data.getExtras().get("data");
                
                imagePath = (Uri) data.getData();
                
                Intent cameraSubmit = new Intent(
                        RedditQuickSubmitActivity.this,
                        SubmitCameraImageActivity.class);
                startActivity(cameraSubmit);
            }
        }
    }
}
