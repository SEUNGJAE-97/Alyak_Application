package com.example.cameraexample;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView alyak_img;
    ListView listView;
    String Medicine_ID, Medicine_Name,Medicine_classification,Medicine_Color,corporate_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //이미지뷰에 띄우기 위한 imagefilepath와 데이터베이스에 요청하기위한 msg를 인턴트로 가져온다.
        String path = getIntent().getStringExtra("imagefilepath");
        String key = getIntent().getStringExtra("msg");
        listView = (ListView)findViewById(R.id.list_item);

        ArrayList<String> data = new ArrayList<>(Arrays.asList(Medicine_ID, Medicine_Name, Medicine_classification, Medicine_Color, corporate_name));

        // 경로로부터 파일을 받아 bitmap형식으로 디코딩한다.
        // 이후 ImageView에 해당 bitmap을 뿌려준다.
        bitmap = BitmapFactory.decodeFile(path);
        alyak_img = (ImageView)findViewById(R.id.alyak_image);
        alyak_img.setImageBitmap(bitmap);

        //데이터베이스로부터 해당 이미지의 파일을 받는다.
        request(key);
        //어댑터
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                data);
        listView.setAdapter(adapter);

    }
    public void request(String key){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Medicine_ID = jsonObject.getString( "Medicine_ID" );
                        Medicine_Name = jsonObject.getString( "Medicine_Name" );
                        Medicine_classification = jsonObject.getString( "Medicine_classification" );
                        Medicine_Color = jsonObject.getString("Medicine_Color");
                        corporate_name = jsonObject.getString("corporate_name");
                    }else{
                        Toast.makeText( getApplicationContext(), "cannot read data", Toast.LENGTH_SHORT ).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        DataRequest dataRequest = new DataRequest(key, responseListener);
        RequestQueue queue = Volley.newRequestQueue( ResultActivity.this );
        queue.add( dataRequest );
    }
}





