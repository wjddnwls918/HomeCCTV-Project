package com.example.jeongwoojin.homecctv;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TcpService extends Service {

    public static final String TAG ="TcpService";

    public TcpService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate() 호출됨.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onCreate() 호출됨.");

        if(intent == null)
            return Service.START_STICKY;
        else{
            processCommand(intent);
        }


        return super.onStartCommand(intent, flags, startId);
    }

    public void processCommand(Intent intent)
    {
        String id = intent.getStringExtra("id");
        String pw = intent.getStringExtra("pw");

        Toast.makeText(getApplicationContext(),"id : "+id+" pw : "+pw,Toast.LENGTH_LONG).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

class URLConnector extends Thread
{
    private String result;
    private String url;

    public URLConnector(String url)
    {
        this.url = url;
    }

    @Override
    public void run() {
        super.run();
    }

    public String getResult()
    {
        return result;
    }

}
