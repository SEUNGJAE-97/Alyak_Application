package com.example.cameraexample;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DataRequest extends StringRequest {
    final static private String URL = "http://alyak.dothome.co.kr/DataRequest.php";
    private Map<String, String> map;

    public DataRequest(String key, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        if(key != null){
            map.put("key",key);
        }
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
