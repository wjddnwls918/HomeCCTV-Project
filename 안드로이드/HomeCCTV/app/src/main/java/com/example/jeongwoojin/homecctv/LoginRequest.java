package com.example.jeongwoojin.homecctv;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JEONGWOOJIN on 2018-01-17.
 */

public class LoginRequest extends StringRequest {

    final static private String URL = "http://192.168.219.136/Login.php";
    private Map<String,String> parameters;


    public LoginRequest(String userID, String userPW, Response.Listener<String> listner)
    {
        super(Method.POST,URL,listner,null);
        parameters = new HashMap<>();

        parameters.put("userID",userID);
        parameters.put("userPW",userPW);

    }

    public Map<String,String> getParams()
    {
        return parameters;
    }

}
