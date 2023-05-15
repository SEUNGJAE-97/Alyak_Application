package com.example.cameraexample;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView alyak_img;
    TextView Medicine_ID, Medicine_Name, Medicine_classification, Medicine_Color, corporate_name;
    static RequestQueue requestQueue;
    static private String URL = "http://alyak.dothome.co.kr/DataRequest.php?Medicine_ID=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        //이미지뷰에 띄우기 위한 imagefilepath와 데이터베이스에 요청하기위한 msg를 인턴트로 가져온다.
        String path = getIntent().getStringExtra("imagefilepath");
        String key = getIntent().getStringExtra("msg");

        Medicine_ID = (TextView) findViewById(R.id.Medicine_ID);
        Medicine_Name = (TextView) findViewById(R.id.Medicine_Name);
        Medicine_classification = (TextView) findViewById(R.id.classification);
        Medicine_Color = (TextView) findViewById(R.id.Medicine_Color);
        corporate_name = (TextView) findViewById(R.id.corporate_name);
        String image_uri;

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        URL = String.valueOf(URL + key);
        //데이터베이스로부터 해당 이미지의 파일을 받는다.
        request(URL);
        System.out.println(URL);

        // 경로로부터 파일을 받아 bitmap형식으로 디코딩한다.
        // 이후 ImageView에 해당 bitmap을 뿌려준다.
        bitmap = BitmapFactory.decodeFile(path);
        alyak_img = (ImageView) findViewById(R.id.alyak_image);
        alyak_img.setImageBitmap(bitmap);
    }

    public void request(String URL) {
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "응답 : " + response, Toast.LENGTH_SHORT).show();
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "에러 : "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Response<String>parseNetworkResponse(NetworkResponse response){
                try{
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                }catch (UnsupportedEncodingException e){
                    return Response.error(new ParseError(e));
                }catch (Exception e){
                    return Response.error(new ParseError(e));
                }
            }
            @Override
            protected Map<String, String>getParams() throws AuthFailureError{
                Map<String, String>param = new HashMap<String, String>();
                return param;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
}





