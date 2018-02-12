package cobong.jeongwoojin.homecctv.myhomecctv;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;


/**
 * Created by JEONGWOOJIN on 2018-01-25.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "MyFirebaseIDService";
    private Socket socket;
    public String token ;
    TokenThread tokenThread;

    @Override
    public void onCreate() {
        super.onCreate();
       // Toast.makeText(getApplicationContext(),"FirbaseIDService started",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        tokenThread = new TokenThread();
        tokenThread.start();

    }

    class TokenThread extends Thread {
        public void run() {

            token = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG,"check my token "+ token);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://192.168.219.136/tokeninsert.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    //요청 성공시
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("result", "[" + response + "]");

                            Log.d("insert token","success");

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
                    params.put("Token",token);

                    return params;
                }
            };

            queue.add(request);

        }
    }


}
