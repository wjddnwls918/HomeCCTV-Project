package com.example.jeongwoojin.homecctv;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by JEONGWOOJIN on 2018-01-25.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
   private static final String TAG = "MyFirebaseIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG,"Refreshed token "+ token);

        sendRegistationToServer(token);


    }

    private void sendRegistationToServer(String token)
    {

    }

}
