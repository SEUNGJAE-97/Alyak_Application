package com.example.cameraexample;

import static com.example.cameraexample.ResultActivity.requestQueue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaSession2Service;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;



public class ListActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener{
    static private String URL = "http://alyak.dothome.co.kr/UserAlyaklist.php?UserEmail=";
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    ArrayList<Item>items = new ArrayList<Item>();
    RequestQueue requestQueue;
    String UserEmail;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_list);
        recyclerView = findViewById(R.id.recycle_view);

        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.setOnItemClickListener(this);

        Intent intent = getIntent();
        UserEmail = intent.getStringExtra("UserEmail");

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                System.out.println(items);
                switch(direction){
                    case ItemTouchHelper.LEFT:
                        Item deleteItem = items.get(position);
                        items.remove(position);
                        itemAdapter.removeItem(position);
                        itemAdapter.notifyItemRemoved(position);
                        itemAdapter.notifyDataSetChanged();
                        // volley 통신을 통해 DB상에 존재하는 position 데이터를 삭제한다.
                        // **구현**
                        delete_Item_From_DB(deleteItem, position);
                        break;
                }
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder,
                        dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(Color.RED)
                        .addSwipeLeftLabel("remove")
                        .addSwipeLeftActionIcon(R.mipmap.ic_delete)
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView,viewHolder,dX,dY,actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);
        //*** Medicine_ID를 통해 Medicine_Name을 가져와서 뿌려주도록 한다.***
        itemAdapter.removeAllItem();
        // **구현**
        requestQueue = Volley.newRequestQueue(this);
        get_Medicine_ID();

        itemAdapter.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();
    }
    private void delete_Item_From_DB(Item item, int position){
        String url = "http://alyak.dothome.co.kr/Delete.php?UserEmail="+UserEmail+"&Medicine_ID="+item.getTitle();
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Delete Error", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(request);
    }
    @Override
    public void onItemClick(Item item){
        Toast.makeText(this, "clicked : "+item.getTitle(), Toast.LENGTH_SHORT).show();
        //클릭시 activity_result layout으로 이동한다.
        //**구현**
        Intent intent = new Intent(this, list_to_info.class);
        intent.putExtra("msg", item.getTitle());
        intent.putExtra("UserEmail", UserEmail);
        startActivity(intent);
    }
    private void get_Medicine_ID(){
        String url = URL+UserEmail;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.replace("<br>", "");
                        String[] Medicine_IDs = response.split(",");
                        /*
                        for(String medicine_ID : Medicine_IDs){
                            get_Data(medicine_ID);
                            System.out.println(medicine_ID);
                        }
                         */
                        for (int i = 0; i < Medicine_IDs.length; i++) {
                            String medicine_ID = Medicine_IDs[i].trim(); // 공백 제거
                            get_Data(medicine_ID);
                            System.out.println(medicine_ID);
                        }
                        //itemAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);
    }
    private void get_Data(String Medicine_ID){
        String url = "http://alyak.dothome.co.kr/NameRequest.php?Medicine_ID=" + Medicine_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if(success){
                                String medicineName = response.getString("Medicine_Name");
                                Item item = new Item();
                                item.setTitle(Medicine_ID);
                                item.setDescription(medicineName);

                                System.out.println(url);

                                // Arraylist에 추가해야함...(삽질햇네)
                                items.add(item);
                                itemAdapter.addItem(item);
                                itemAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(getApplicationContext(),"occurred error", Toast.LENGTH_SHORT).show();
                                System.out.println(url);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"occurred error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);
    }
}

