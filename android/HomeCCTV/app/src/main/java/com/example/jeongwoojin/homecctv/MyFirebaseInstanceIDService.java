package com.example.jeongwoojin.homecctv;

import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by JEONGWOOJIN on 2018-01-25.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "MyFirebaseIDService";
    private Socket socket;
    public String token ;
    TokenThread tokenThread;

    @Override
    public void onCreate() {
        super.onCreate();

       // Toast.makeText(getApplicationContext(),"FirbaseIDService started",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();


        tokenThread = new TokenThread();
        tokenThread.start();


    }



    class TokenThread extends Thread {
        public void run() {

            token = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG,"Refreshed token "+ token);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://192.168.219.136/tokeninsert.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    //요청 성공시
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("result", "[" + response + "]");

                            Log.d("insert token","success");

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", "[" + error.getMessage() + "]");
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<>();
                    params.put("Token",token);

                    return params;
                }
            };

            queue.add(request);



        }
    }


}
