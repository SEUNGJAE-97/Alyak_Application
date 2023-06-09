package com.example.cameraexample;

import static com.android.volley.VolleyLog.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class list_to_info extends AppCompatActivity {
    TextView Medicine_ID, Medicine_Name, Medicine_classification, Medicine_Color, corporate_name;
    static private String URL = "http://alyak.dothome.co.kr/DataRequest.php?Medicine_ID=";
    static RequestQueue requestQueue;
    JSONObject jsonObj;
    ImageView alyak_img;
    private boolean isOtherButtonsVisible = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        alyak_img = (ImageView)findViewById(R.id.alyak_image);
        Medicine_ID = (TextView) findViewById(R.id.Medicine_ID);
        Medicine_Name = (TextView) findViewById(R.id.Medicine_Name);
        Medicine_classification = (TextView) findViewById(R.id.classification);
        Medicine_Color = (TextView) findViewById(R.id.Medicine_Color);
        corporate_name = (TextView) findViewById(R.id.corporate_name);

        FloatingActionButton fab = findViewById(R.id.fab);//아래 리스트를 보여주는 버튼
        FloatingActionButton fabCamera = findViewById(R.id.fabCamera);// MainActivity로 이동
        FloatingActionButton fabList = findViewById(R.id.fablist);//My_Alyak Activity로 이동
        FloatingActionButton fabEdit = findViewById(R.id.fabEdit);//USER DB에 Medicine_ID 추가버튼

        String key = getIntent().getStringExtra("msg");
        String UserEmail = getIntent().getStringExtra("UserEmail");
        URL = String.valueOf(URL + key);

        request(URL, new RequestCallback(){
            @Override
            public void onImageUriReceived(final String imageUri) {
                // 이미지 비트맵 리스트 생성
                Glide.with(getApplicationContext()).asBitmap().load(imageUri).into(alyak_img);
            }
        });
        fab.setVisibility(View.GONE);
    }
    public void request(String URL, list_to_info.RequestCallback callback) {
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "응답 : " + response, Toast.LENGTH_SHORT).show();
                //유니코드 -> 한글
                jsonObj = decode(response);
                try {
                    //데이터베이스로부터 해당 이미지의 파일을 받는다.
                    Medicine_ID.setText(jsonObj.getString("Medicine_ID"));
                    Medicine_Name.setText(jsonObj.getString("Medicine_Name"));
                    corporate_name.setText(jsonObj.getString("corporate_name"));
                    Medicine_Color.setText(jsonObj.getString("Medicine_Color"));
                    Medicine_classification.setText(jsonObj.getString("Medicine_classification"));
                    String imageUri = jsonObj.getString("image_uri");
                    callback.onImageUriReceived(imageUri);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>param = new HashMap<String, String>();
                return param;
            }
        };
        request.setShouldCache(false);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
    // json 파싱
    public static JSONObject decode(String unicodeString){
        JSONObject jsonObject = null;
        try{
            JSONArray jsonArray = parseUnicodeToJson(unicodeString);
            jsonObject = jsonArray.getJSONObject(0);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONArray parseUnicodeToJson(String unicodeString) throws JSONException {
        String decodedString = decodeUnicode(unicodeString);
        return new JSONArray(decodedString);
    }

    public static String decodeUnicode(String unicodeString) {
        StringBuilder sb = new StringBuilder();
        int length = unicodeString.length();
        for (int i = 0; i < length; i++) {
            if (unicodeString.charAt(i) == '\\' && i + 1 < length && unicodeString.charAt(i + 1) == 'u') {
                String hexCode = unicodeString.substring(i + 2, i + 6);
                int charCode = Integer.parseInt(hexCode, 16);
                sb.append((char) charCode);
                i += 5;
            } else {
                sb.append(unicodeString.charAt(i));
            }
        }
        return sb.toString();
    }
    public interface RequestCallback {
        void onImageUriReceived(String imageUri);
    }
}
