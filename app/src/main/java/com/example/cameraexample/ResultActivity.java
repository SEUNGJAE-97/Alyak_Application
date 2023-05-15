package com.example.cameraexample;


import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView alyak_img;
    ListView listView;
    String Medicine_ID, Medicine_Name,Medicine_classification,Medicine_Color,corporate_name;
    public ArrayList<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //이미지뷰에 띄우기 위한 imagefilepath와 데이터베이스에 요청하기위한 msg를 인턴트로 가져온다.
        String path = getIntent().getStringExtra("imagefilepath");
        String key = getIntent().getStringExtra("msg");
        // 수정되어야함 listView-> TableRow
        // listView = (ListView)findViewById(R.id.list_item);

        // 경로로부터 파일을 받아 bitmap형식으로 디코딩한다.
        // 이후 ImageView에 해당 bitmap을 뿌려준다.
        bitmap = BitmapFactory.decodeFile(path);
        alyak_img = (ImageView)findViewById(R.id.alyak_image);
        alyak_img.setImageBitmap(bitmap);


        //데이터베이스로부터 해당 이미지의 파일을 받는다.
        request(key);

    }
    public void request(String key){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    if(success){
                        List<String> dataList = new ArrayList<>();
                        for (int i = 0; i<dataArray.length(); i++){
                            String data = dataArray.getString(i);
                            dataList.add(data);
                        }
                        adapter.clear();
                        adapter.addAll(dataList);
                    }else{
                        Toast.makeText( getApplicationContext(), "cannot read data", Toast.LENGTH_SHORT ).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley error : "+ error.getMessage());
            }
        };
        DataRequest dataRequest = new DataRequest(key, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue( ResultActivity.this );
        queue.add( dataRequest );
    }
}





