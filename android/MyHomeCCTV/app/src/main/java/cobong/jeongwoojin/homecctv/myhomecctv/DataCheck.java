package cobong.jeongwoojin.homecctv.myhomecctv;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class DataCheck extends AppCompatActivity {

    Button button1;
    Button button2;
    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;

    String userID;
    String accessLevel;
    String hstate;

    DetectOn detectOn;

    //메뉴 생성
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

            case R.id.goout:
                //외출모드
                hstate = "1";
                detectOn = new DetectOn();
                detectOn.run();
                Toast.makeText(this,"외출모드가 실행되었습니다.",Toast.LENGTH_LONG).show();
                break;

            case R.id.gooutoff:
                //외출모드 끄기
                hstate = "0";
                detectOn = new DetectOn();
                detectOn.run();
                Toast.makeText(this,"외출모드가 꺼졌습니다.",Toast.LENGTH_LONG).show();
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



        //일별 데이터 호출
        button1 = (Button) findViewById(R.id.tab1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(FRAGMENT1);
            }
        });
        //월별 데이터 호출
        button2 = (Button) findViewById(R.id.tab2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(FRAGMENT2);
            }
        });

        callFragment(FRAGMENT1);
    }

    //프레그먼트 호출 함수
    private void callFragment(int fragment_no)
    {
       FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch( fragment_no)
        {
            case 1:
                Fragment1 fragment1 = new Fragment1();
                transaction.replace(R.id.fragment_container,fragment1);
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 2:

                Fragment2 fragment2 = new Fragment2();
                transaction.replace(R.id.fragment_container,fragment2);
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class DetectOn extends Thread
    {
        @Override
        public void run() {
            super.run();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://192.168.219.136/hstateinsert.php";
            //String url = "http://49.161.122.232:8888/hstateinsert.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    //요청 성공시
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("result", "[" + response + "]");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", "[" + error.getMessage() + "]");
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("hstate", hstate);

                    return params;
                }
            };

            queue.add(request);
        }
    }


}

