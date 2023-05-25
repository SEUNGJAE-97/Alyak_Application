package com.example.cameraexample;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ListActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener{
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    ArrayList<Item>items = new ArrayList<Item>();
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_list);
        recyclerView = findViewById(R.id.recycle_view);


        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.setOnItemClickListener(this);




        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch(direction){
                    case ItemTouchHelper.LEFT:
                        Item deleteItem = items.get(position);

                        items.remove(position);
                        itemAdapter.removeItem(position);
                        itemAdapter.notifyItemRemoved(position);
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

        itemAdapter.removeAllItem();
        for (int i =0; i<10; i++){
            Item item = new Item();
            item.setTitle("title"+i);
            item.setDescription("description"+i);

            items.add(item);
            itemAdapter.addItem(item);
        }
        itemAdapter.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();
    }
    @Override
    public void onItemClick(Item item){
        Toast.makeText(this, "clicked : "+item.getTitle(), Toast.LENGTH_SHORT).show();

    }
}
