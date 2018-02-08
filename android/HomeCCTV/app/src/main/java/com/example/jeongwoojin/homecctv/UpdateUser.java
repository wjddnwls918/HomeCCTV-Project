package com.example.jeongwoojin.homecctv;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class UpdateUser extends AppCompatActivity {

    TextView userID;
    EditText userPW;

    String receivedId;
    String receivedPW;

    Button update;
    Button exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        userID = (TextView)findViewById(R.id.id);
        userPW = (EditText) findViewById(R.id.pw);

        Intent intent = getIntent();
        receivedId = intent.getStringExtra("userID");


        userID.setText("ID: "+ receivedId);

        update = (Button)findViewById(R.id.update);
        exit = (Button) findViewById(R.id.exit);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                receivedPW = userPW.getText().toString().trim();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.219.136/Update.php";

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        //요청 성공시
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUser.this);
                                        builder.setMessage("정보 수정에 성공했습니다.")
                                                .setPositiveButton("확인", null)
                                                .create()
                                                .show();

                                        //finish();
                                        Intent intent1 = new Intent(getApplicationContext(),LoginActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent1);

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUser.this);
                                        builder.setMessage("정보 수정에 실패했습니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();

                                /*Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);*/
                                    }
                                }catch ( JSONException e)
                                {
                                    e.printStackTrace();
                                }
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
                        params.put("receivedId",receivedId);
                        params.put("receivedPW",encryptSHA512(receivedPW));

                        return params;
                    }
                };

                queue.add(request);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }

    public String encryptSHA512(String target)
    {
        try
        {
            MessageDigest sh = MessageDigest.getInstance("SHA-512");
            sh.update(target.getBytes());

            StringBuffer sb = new StringBuffer();

            for(byte b : sh.digest())
                sb.append(Integer.toHexString(0xff & b));

            return sb.toString();


        }catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }


    }

}
