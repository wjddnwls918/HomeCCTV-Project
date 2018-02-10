package cobong.jeongwoojin.homecctv.myhomecctv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    Button button;
    EditText id;
    EditText pw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);


        button = (Button)findViewById(R.id.Register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = id.getText().toString();
                String userPW = pw.getText().toString();

                //정규표현식
                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", userPW))
                {
                    Toast.makeText(getApplicationContext(),"비밀번호는 영어, 숫자, 특수문자 조합의 8~20자의 문자입니다.",Toast.LENGTH_LONG).show();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {


                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 등록에 성공했습니다.")
                                            .setPositiveButton("확인", null)
                                            .create()
                                            .show();

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 등록에 실패했습니다.")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();

                                /*Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);*/
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };


                    RegisterRequest registerRequest = new RegisterRequest(userID, userPW, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }

            }
        });

        Button button2 = (Button)findViewById(R.id.login);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"RegisterActivity Pause 호출",Toast.LENGTH_LONG).show();

    }
}
