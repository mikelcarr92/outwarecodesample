package com.mcarr.officialsmooviesapp.helpers;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

/**********************************
 * Created by Mikel on 23-Jun-16.
 *********************************/
public class IntentHelper {

    public final static int FILE_PICK = 1001;
    public final static int TAKE_PHOTO = 1002;

    public static void chooseFileIntent(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, FILE_PICK);
    }

    public static void takePhotoIntent(Activity activity) {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(imageIntent, TAKE_PHOTO);
    }
}
