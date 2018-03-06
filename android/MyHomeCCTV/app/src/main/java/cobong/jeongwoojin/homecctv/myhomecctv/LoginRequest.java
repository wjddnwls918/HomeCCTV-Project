package cobong.jeongwoojin.homecctv.myhomecctv;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JEONGWOOJIN on 2018-01-17.
 */

public class LoginRequest extends StringRequest {

    //라즈베리 주소할당
    final static private String URL = "http://192.168.219.136/Login.php";
    //final static private String URL = "http://49.161.122.232:8888/Login.php";

    private Map<String,String> parameters;


    public LoginRequest(String userID, String userPW, Response.Listener<String> listner)
    {
        super(Method.POST,URL,listner,null);
        parameters = new HashMap<>();

        //POST
        parameters.put("userID",userID);
        parameters.put("userPW",encryptSHA512(userPW));

    }

    public Map<String,String> getParams()
    {
        return parameters;
    }

    //비밀번호 암호화 함수 (해시)
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
