package com.example.jeongwoojin.homecctv;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.net.URISyntaxException;

import io.socket.client.IO;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class StreamActivity extends AppCompatActivity {


    ImageView left;
    ImageView right;

    private Socket socket;

    ConnectThread connectThread;
    VideoThread videoThread;
    LeftThread leftThread;
    RightThread rightThread;
    Realdata realdata;
    RealTimehandler handler;
    JSONObject json = null;
    TextView tem ;
    TextView hum ;
    TextView p ;

    VideoView v;
    ProgressDialog progDialog;

    String userID;
    String userPW;
    String userAccessLevel;

    ImageView firedetector;
    boolean fireflag = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int curId = item.getItemId();
        switch(curId)
        {
            case R.id.logout:
                //finish();
                Intent logout = new Intent(getApplicationContext(),LoginActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;

            case R.id.infoupdate:
                Intent infoupdate = new Intent(getApplicationContext(),UpdateUser.class);
                infoupdate.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                infoupdate.putExtra("userID",userID);
                startActivity(infoupdate);
                break;

            case R.id.edit:
                //Toast.makeText(this,"외출모드가 선택되었습니다",Toast.LENGTH_LONG).show();
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);


        left = (ImageView)findViewById(R.id.left);
        right = (ImageView)findViewById(R.id.right);

        tem = (TextView)findViewById(R.id.temperature);
        hum = (TextView)findViewById(R.id.humidity);
        p = (TextView)findViewById(R.id.ppm);

        handler = new RealTimehandler();

        firedetector = (ImageView)findViewById(R.id.firedetector);
        //사용자 출력
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userPW = intent.getStringExtra("userPW");
        userAccessLevel = intent.getStringExtra("accessLevel");


        //연결
        connectThread = new ConnectThread();
        connectThread.start();

        //권한 확인후 스트리밍

        if( Integer.parseInt(userAccessLevel) == 1) {

            progDialog = ProgressDialog.show(this, "잠시만 기다려주세요.", "동영상을 로딩중입니다......", true);
            v = (VideoView) findViewById(R.id.videoView);
            v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.d("VIDEO", "DONE!");
                    progDialog.dismiss();
                }
            });
            videoThread = new VideoThread();
            videoThread.start();

            realdata = new Realdata();
            realdata.start();

            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(),"왼쪽 누름",Toast.LENGTH_LONG).show();

                    leftThread = new LeftThread();
                    leftThread.start();


                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(),"오른쪽 누름",Toast.LENGTH_LONG).show();

                    rightThread = new RightThread();
                    rightThread.start();


                }
            });


        }
    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    public void connect()
    {
        try{
            socket = IO.socket("http://192.168.219.136:3000");

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                   // Log.d("연결 완료", "연결은 되는데");

                }
            }).on("response", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                        JSONObject receivedData = (JSONObject) args[0];


                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socket.disconnect();
                }
            });
            socket.connect();


        }catch (URISyntaxException e)
        {
            Log.d("connect failed","연결실패");
            e.printStackTrace();
        }
    }

    class VideoThread extends Thread{

        public void run()
        {

            String uri = "rtsp://192.168.219.136:8555/unicast";
            v.setVideoURI( Uri.parse(uri) );
            //v.setMediaController(new MediaController(getApplicationContext()));
            v.requestFocus();
            v.start();
        }
    }

    class ConnectThread extends Thread
    {
        public void run()
        {
            //연결
            connect();
            //Toast.makeText(getApplicationContext(),"연결 성공",Toast.LENGTH_LONG).show();
          /*  handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"연결 성공",Toast.LENGTH_LONG).show();
                }
            });*/
        }
    }

    class LeftThread extends Thread
    {
        public void run()
        {
            JSONObject data = new JSONObject();
            try{
                data.put("command","left");
                socket.emit("control",data);
            }catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
    class RightThread extends Thread
    {
        public void run()
        {
            JSONObject data = new JSONObject();
            try{
                data.put("command","right");
                socket.emit("control",data);
            }catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }

    class Realdata extends Thread{



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
                                Message msg = handler.obtainMessage();
                                Bundle bundle = new Bundle();


                                try {
                                    json = new JSONObject(response);

                                    bundle.putString("temperature", json.getString("temperature"));
                                    bundle.putString("humidity", json.getString("humidity"));
                                    bundle.putString("ppm", json.getString("ppm"));
                                    bundle.putString("flameState", json.getString("flameState"));
                                    bundle.putString("humState", json.getString("humState"));
                                    bundle.putString("inputtime", json.getString("inputtime"));
                                    msg.setData(bundle);



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                handler.sendMessage(msg);

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

    class RealTimehandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String temperature = bundle.getString("temperature");
            String humidity = bundle.getString("humidity");
            String ppm = bundle.getString("ppm");
            String flameState = bundle.getString("flameState");
            String humState = bundle.getString("humState");
            String inputtime = bundle.getString( "inputtime");

            tem.setText(temperature+"℃");
            hum.setText(humidity+"%");
            p.setText(ppm+"ppm");



            if(flameState.equals("0") &&  !fireflag )
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(StreamActivity.this);
                dialog.setTitle("경고!");
                dialog.setMessage("불꽃이 감지되었습니다.");
                firedetector.setImageResource(R.drawable.fire);
                fireflag = true;

                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"확인 눌렀땅!",Toast.LENGTH_LONG).show();
                    }
                });

                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
            }
            else if(flameState.equals("1"))
            {
                firedetector.setImageResource(R.drawable.on);
                fireflag = false;
            }

        }
    }



}


