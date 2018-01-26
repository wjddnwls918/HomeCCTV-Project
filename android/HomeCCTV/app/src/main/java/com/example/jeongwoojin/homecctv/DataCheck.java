package com.example.jeongwoojin.homecctv;


import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class DataCheck extends AppCompatActivity  {

    Button button1;
    Button button2;
    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;



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
                Toast.makeText(this,"로그아웃 메뉴가 선택되었습니다",Toast.LENGTH_LONG).show();
                //finish();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.edit:
                Toast.makeText(this,"외출모드 메뉴가 선택되었습니다",Toast.LENGTH_LONG).show();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_check);



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
                transaction.commit();
                break;
            case 2:

                Fragment2 fragment2 = new Fragment2();
                transaction.replace(R.id.fragment_container,fragment2);
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
