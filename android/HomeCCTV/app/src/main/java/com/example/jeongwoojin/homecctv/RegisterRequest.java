package com.example.jeongwoojin.homecctv;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JEONGWOOJIN on 2018-01-17.
 */

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://192.168.219.136/Register.php";
    private Map<String,String> parameters;


    public RegisterRequest(String userID, String userPW,Response.Listener<String> listner)
    {
        super(Method.POST,URL,listner,null);
        parameters = new HashMap<>();

        parameters.put("userID",userID);
        parameters.put("userPW",encryptSHA512(userPW));

    }

    public Map<String,String> getParams()
    {
        return parameters;
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

