package com.gexton.cashinvesternew.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gexton.cashinvesternew.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import me.relex.photodraweeview.PhotoDraweeView;

public class RvViewImagesAdapter extends RecyclerView.Adapter<RvViewImagesAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> dataArrayList;

    public RvViewImagesAdapter(Activity activity, ArrayList<String> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUri = Uri.parse(dataArrayList.get(position));
        holder.draweeView.setPhotoUri(imageUri);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PhotoDraweeView draweeView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            draweeView = itemView.findViewById(R.id.draweeView);
        }
    }
}