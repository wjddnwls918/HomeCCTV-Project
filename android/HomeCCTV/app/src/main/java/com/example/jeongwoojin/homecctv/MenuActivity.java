package com.example.jeongwoojin.homecctv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    Button cctv;
    Button data;
    String userID;
    String userPW;
    String userAccessLevel;
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
                //Toast.makeText(this,"로그아웃 메뉴가 선택되었습니다",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();

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
        setContentView(R.layout.activity_menu);




        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userPW = intent.getStringExtra("userPW");
        userAccessLevel = intent.getStringExtra("accessLevel");

        String message;
        if( Integer.parseInt(userAccessLevel) == 0) {
            message = userID + "님!, 사용자 권한을 얻으세요!";
        }
        else
        {
            message = "환영합니다, " + userID + " 님! : " + "사용자";
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        cctv = (Button)findViewById(R.id.cctv);
        data = (Button)findViewById(R.id.data);

        cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AfterLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("userID",userID);
                intent.putExtra("userPW",userPW);
                intent.putExtra("accessLevel",userAccessLevel);
                startActivity(intent);
            }
        });
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),DataCheck.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"MenuActivity Pause 호출",Toast.LENGTH_LONG).show();

    }
}
