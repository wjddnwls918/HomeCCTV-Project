package com.example.jeongwoojin.homecctv;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.VideoView;

public class AfterLogin extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int curId = item.getItemId();
        switch(curId)
        {
            case R.id.logout:
                Toast.makeText(this,"로그아웃 메뉴가 선택되었습니다",Toast.LENGTH_LONG).show();
                finish();

                break;
            case R.id.edit:
                Toast.makeText(this,"설정 메뉴가 선택되었습니다",Toast.LENGTH_LONG).show();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);




        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPW = intent.getStringExtra("userPW");
        String userAccessLevel = intent.getStringExtra("accessLevel");

        String message;
        if( Integer.parseInt(userAccessLevel) == 0) {
            message = "환영합니다, " + userID + " 님! : " + "접근 비허용자";
        }
        else
        {
            message = "환영합니다, " + userID + " 님! : " + "사용자";
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        if( Integer.parseInt(userAccessLevel) == 1)
        {
            String uri = "rtsp://192.168.219.136:8555/unicast";
            VideoView v = (VideoView) findViewById(R.id.videoView);
            v.setVideoURI(Uri.parse(uri) );
            v.requestFocus();
            v.start();
        }

    }
}
