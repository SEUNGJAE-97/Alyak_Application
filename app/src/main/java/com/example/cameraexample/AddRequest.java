package com.example.cameraexample;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {

    final static private String URL = "http://alyak.dothome.co.kr/Add.php";
    private Map<String, String> map;

    public AddRequest(String UserEmail, String Medicine_ID, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("UserEmail", UserEmail);
        map.put("Medicine_ID", Medicine_ID);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
