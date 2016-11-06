package com.apres.gerber.loops;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Huimee on 11/5/2016.

 Only shares pictures taken through the camera - no screenshots

 */

public class SharePictureActivity extends AppCompatActivity {

    private Button cameraButton;
    private Button sharePic;
    private ImageView thumbnail;

    public Bitmap pic;
    public Uri picUri;

    private int PHOTO_ID = 101;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_share_picture);

        cameraButton = (Button) findViewById(R.id.share_pic_camera_button);
        sharePic = (Button) findViewById(R.id.share_pic_share_button);
        thumbnail = (ImageView) findViewById(R.id.share_pic_thumbnail);

        setupEvents();
    }

    private void setupEvents() {
        cameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, PHOTO_ID);
            }
        });

        sharePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pic == null) {
                    makeToast("Take a valid picture.");
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_STREAM, picUri);
                    startActivity(Intent.createChooser(intent, "Share picture with:"));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PHOTO_ID) {
            if (resultCode == RESULT_OK) {
                this.showPicture(intent);
            }
        }
    }

    private void showPicture(Intent intent) {
        Bundle intentExtras = intent.getExtras();
        pic = (Bitmap)intentExtras.get("data");
        picUri = intent.getData();

        if (pic != null) {
            thumbnail.setImageBitmap(pic);
            makeToast("Picture set successfully!");
        }
    }

    private void makeToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
