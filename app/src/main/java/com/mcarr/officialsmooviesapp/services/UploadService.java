package com.mcarr.officialsmooviesapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mcarr.officialsmooviesapp.object.request.SetProfileImageRequest;
import com.mcarr.officialsmooviesapp.object.response.BaseResponse;
import com.mcarr.officialsmooviesapp.retrofit.ApiClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**********************************
 * Created by Mikel on 23-Jun-16.
 *********************************/
public class UploadService extends IntentService {

    public static final String FILE_URL = "file_url";
    public static final String TOKEN = "token";
    private ApiClient mApiClient;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadService(String name) {
        super(name);
    }

    public UploadService() {
        super("UploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Map config = new HashMap();
        mApiClient = ApiClient.getClient();

        if (intent == null) {
            return;
        }

        config.put("cloud_name", "dfh2vwxrt");
        config.put("api_key", "676799334571527");
        config.put("api_secret", "enPfUSM0OQ1rFWYna-7imFLNqlQ");
        Cloudinary cloudinary = new Cloudinary(config);

        InputStream is;

        String token = intent.getStringExtra(TOKEN);
        boolean success = false;
        String uploadUrl = "";

        try {
            is = new FileInputStream(intent.getStringExtra(FILE_URL));

            Map upload = cloudinary.uploader().upload(is, ObjectUtils.emptyMap());
            Log.i("DEV9", "url: " + upload.get("url"));
            uploadUrl = (String) upload.get("url");
            is.close();
            success = true;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (success) {
            mApiClient.getSmooviesApiService().setProfileImage(new SetProfileImageRequest(token, uploadUrl)).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });
        }
    }
}
