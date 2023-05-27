package com.example.cameraexample;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    ArrayList<Item> items = new ArrayList<Item>();
    Context context;
    int lastPosition = -1;
    private OnItemClickListener listener;

    //수정...중..
    public interface OnItemClickListener{
        void onItemClick(Item item);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.activity_item, viewGroup,false);
        context =viewGroup.getContext();

        return new ViewHolder(itemView, listener, items);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if (viewHolder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            ((ViewHolder)viewHolder).itemView.startAnimation(animation);
            Item item = items.get(position);
            viewHolder.setItem(item);

        }
        //Item item = items.get(position);
        //viewHolder.setItem(item);
    }

    public void addItem(Item item){
        items.add(item);
    }
    @Override
    public int getItemCount(){
        return items.size();
    }
    public void addItem(int position, Item item){
        items.add(position, item);
    }
    public void removeItem(int position){
        items.remove(position);
    }
    public void removeAllItem(){
        items.clear();
    }
    public Item getItem(int position){
        return items.get(position);
    }




    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_view;
        TextView description;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener,final ArrayList<Item> items) {
            super(itemView);
            title_view = itemView.findViewById(R.id.title_text);
            description = itemView.findViewById(R.id.desc_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(items.get(position));
                        Intent intent = new Intent(itemView.getContext(), ResultActivity.class );
                        intent.putExtra("Medicine_ID", items.get(position).getTitle());

                        itemView.getContext().startActivity(intent);
                    }
                }
            });

        }
        public void setItem(Item item){
            title_view.setText(item.getTitle());
            description.setText(item.getDescription());
        }
    }
}
