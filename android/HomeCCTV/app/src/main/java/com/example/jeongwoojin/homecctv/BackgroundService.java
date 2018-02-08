package com.example.jeongwoojin.homecctv;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class BackgroundService extends Service {

    JSONObject json = null;

    ServiceRealdata serviceRealdata;

    public BackgroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("Service Test","service onStartCommand");

        Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_LONG).show();

     /*  serviceRealdata = new ServiceRealdata();
        serviceRealdata.start();
*/
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class ServiceRealdata extends Thread{



        @Override
        public void run() {
            super.run();

            while(true) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.219.136/Realtime.php";

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        //요청 성공시
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Log.d("result", "[" + response + "]");

                                Bundle bundle = new Bundle();


                                try {
                                    json = new JSONObject(response);

                                    bundle.putString("temperature", json.getString("temperature"));
                                    bundle.putString("humidity", json.getString("humidity"));
                                    bundle.putString("ppm", json.getString("ppm"));
                                    bundle.putString("flameState", json.getString("flameState"));
                                    bundle.putString("humState", json.getString("humState"));
                                    bundle.putString("inputtime", json.getString("inputtime"));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                alarm(bundle);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("error", "[" + error.getMessage() + "]");
                            }
                        }
                );

                queue.add(request);

                try {
                    this.sleep(5000);
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

    public void alarm(Bundle bundle)
    {
        String tem = bundle.getString("temperature");
        String hum = bundle.getString("humidity");
        String ppm = bundle.getString("ppm");
        String flameState = bundle.getString("flameState");
        String humState = bundle.getString("humState");
        String inputtime = bundle.getString("inputtime");


        if( flameState.equals("1") )
        {
           /*Intent alarmintent = new Intent(getApplicationContext(),StreamActivity.class);
            alarmintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            alarmintent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(alarmintent);*/

        }

    }

}



