package com.example.jeongwoojin.homecctv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText id;
    EditText pw;
    Button logIn;
    Button signUp;
    CheckBox remember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);
        remember = (CheckBox)findViewById(R.id.remember);


        //저장된 아이디 불러오기
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    SharedPreferences preferences = getSharedPreferences("PrefName",MODE_PRIVATE);
                    id.setText(preferences.getString("id",""));
                }
            }
        });



        //로그인
        logIn = (Button)findViewById(R.id.login);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(getApplicationContext(),TcpService.class);
                intent.putExtra("id",id.getText().toString().trim());
                intent.putExtra("pw",pw.getText().toString().trim());
                startService(intent);*/

                String userID = id.getText().toString();
                String userPW = pw.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success)
                            {
                                String userID = jsonResponse.getString("userID");
                                String userPW = jsonResponse.getString("userPW");
                                String accessLevel = jsonResponse.getString("accessLevel");

                                //로그인 정보 남김
                                SharedPreferences preferences = getSharedPreferences("PrefName",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("id",userID);
                                editor.commit();


                                Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userPW",userPW);
                                intent.putExtra("accessLevel",accessLevel);
                                startActivity(intent);

                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }


                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                };


                LoginRequest loginRequest = new LoginRequest(userID,userPW,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);




            }
        });





        //회원가입
        signUp = (Button)findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(),RegisterActivity.class);
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            }
        });





    }


    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"LoginActivity Pause 호출",Toast.LENGTH_LONG).show();

    }

}
