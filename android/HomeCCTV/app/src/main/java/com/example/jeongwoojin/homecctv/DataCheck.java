package com.example.jeongwoojin.homecctv;


import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;



public class DataCheck extends AppCompatActivity  {

    Button button1;
    Button button2;
    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;

    String userID;
    String accessLevel;

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
                //Toast.makeText(this,"로그아웃 메뉴가 선택되었습니다",Toast.LENGTH_LONG).show();
                //finish();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.infoupdate:
                Intent infoupdate = new Intent(getApplicationContext(),UpdateUser.class);
                infoupdate.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                infoupdate.putExtra("userID",userID);
                startActivity(infoupdate);
                break;

            case R.id.edit:
                //Toast.makeText(this,"외출모드 메뉴가 선택되었습니다",Toast.LENGTH_LONG).show();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_check);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        accessLevel = intent.getStringExtra("accessLevel");




        button1 = (Button) findViewById(R.id.tab1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(FRAGMENT1);
            }
        });
        button2 = (Button) findViewById(R.id.tab2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(FRAGMENT2);
            }
        });




        callFragment(FRAGMENT1);
    }

    private void callFragment(int fragment_no)
    {
       FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch( fragment_no)
        {
            case 1:
                Fragment1 fragment1 = new Fragment1();
                transaction.replace(R.id.fragment_container,fragment1);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 2:

                Fragment2 fragment2 = new Fragment2();
                transaction.replace(R.id.fragment_container,fragment2);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"DataCheck Pause 호출",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {

        /*BusProvider.getInstance().unregister(this);*/
        super.onDestroy();

    }




}

