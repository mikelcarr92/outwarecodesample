package com.mcarr.officialsmooviesapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.helpers.DocumentHelper;
import com.mcarr.officialsmooviesapp.helpers.IntentHelper;
import com.mcarr.officialsmooviesapp.services.UploadService;
import com.mcarr.officialsmooviesapp.util.Constants;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**********************************
 * Created by Mikel on 20-Jun-16.
 *********************************/
public class MyProfileActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 24;

    /** Views **/
    @BindView(R.id.activity_my_profile_image) ImageView profileImage;

    /** Instance **/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        //String profileImageUrl = prefs.getString(Constants.PROFILE_URL, null);

//        if (profileImageUrl != null) {
//            Picasso.with(getBaseContext())
//                    .load(profileImageUrl)
//                    .into(profileImage);
//        }
    }

    @OnClick(R.id.activity_my_profile_upload_button) void fileUploadClicked() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            IntentHelper.chooseFileIntent(this);
        }

    }

    @OnClick(R.id.activity_my_profile_camera_button) void cameraUploadClicked() {
        IntentHelper.takePhotoIntent(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    IntentHelper.chooseFileIntent(this);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { return; }
        switch (requestCode) {
            case IntentHelper.FILE_PICK:
                Uri returnUri = data.getData();
                String filePath = DocumentHelper.getPath(this, returnUri);
                if (filePath == null || filePath.isEmpty()) return;
                File chosenFile = new File(filePath);
                Picasso.with(getBaseContext())
                        .load(chosenFile)
                        .fit()
                        .into(profileImage);
                Intent intent = new Intent(this, UploadService.class);
                intent.putExtra(UploadService.FILE_URL, chosenFile.getPath());
                intent.putExtra(UploadService.TOKEN, Util.getToken(this));
                startService(intent);
                break;

            case IntentHelper.TAKE_PHOTO:

                break;
        }
    }
}