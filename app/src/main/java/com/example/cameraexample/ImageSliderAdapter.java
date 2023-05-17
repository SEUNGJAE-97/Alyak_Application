package com.example.cameraexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ViewHolder> {
    private Context context;
    private List<Bitmap> imageBitmaps;
    private List<String> imageUrls;

    public ImageSliderAdapter(Context context, List<Bitmap> imageBitmaps, List<String> imageUrls) {
        this.context = context;
        this.imageBitmaps = imageBitmaps;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (imageBitmaps != null) {
            Bitmap bitmap = imageBitmaps.get(position);
            holder.imageView.setImageBitmap(bitmap);
        } else if (imageUrls != null) {
            String url = imageUrls.get(position);
            Glide.with(context).load(url).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (imageBitmaps != null) {
            return imageBitmaps.size();
        } else if (imageUrls != null) {
            return imageUrls.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.alyak_image);
        }
    }
}