package com.gexton.cashinvesternew.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.utils.Amenities;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> dataArrayList;
    ArrayList<Integer> list = new ArrayList<>();

    public AmenitiesAdapter(Activity activity, ArrayList<String> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amenities, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(dataArrayList.get(position));
        for (int i = 0; i < dataArrayList.size(); i++) {
            //list.add(Amenities.getAmmeityDrawableRes(dataArrayList.get(i)));
            holder.imgAmenities.setImageResource(Amenities.getAmmeityDrawableRes(dataArrayList.get(i)));
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imgAmenities;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            imgAmenities = itemView.findViewById(R.id.imgAmenities);
        }
    }
}
