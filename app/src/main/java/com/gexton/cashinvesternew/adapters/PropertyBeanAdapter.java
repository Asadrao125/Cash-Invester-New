package com.gexton.cashinvesternew.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.activities.SinglePropertyActivity;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.PropertyBean;
import com.gexton.cashinvesternew.models.ResponseMessageModel;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyBeanAdapter extends RecyclerView.Adapter<PropertyBeanAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<PropertyBean> dataArrayList;
    ApiService apiService;

    public PropertyBeanAdapter(Activity activity, ArrayList<PropertyBean> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
        apiService = APIClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PropertyBean propertyBean = dataArrayList.get(position);
        Picasso.get().load(propertyBean.thumbnail.thumbnail_url).placeholder(R.drawable.profile_bg).into(holder.img_thumbnail);
        holder.tvViewCount.setText(propertyBean.views_count);
        holder.tvDescription.setText(propertyBean.title);
        holder.tvTitle.setText(propertyBean.type);

        String commentDate = propertyBean.updated_at;
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).parse(commentDate);//"2020-2-31 11:30:19"
            commentDate = new SimpleDateFormat("dd, MMM yyyy @ hh:mm a").format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvUpdatedAt.setText("Uploaded:  " + commentDate);
        holder.tvBed.setText(propertyBean.rooms + " Bed");
        holder.tvBath.setText(propertyBean.bathrooms + " Bath");
        holder.tvArea.setText(propertyBean.area + " Sq.Ft");
        holder.tvPrice.setText("$ " + propertyBean.price);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SinglePropertyActivity.class);
                intent.putExtra("p_id", propertyBean.hashid);
                activity.startActivity(intent);
            }
        });

        if (propertyBean.is_bookmarked.equals("true")) {
            holder.imgStar.setImageResource(R.drawable.bookmark_enable);
        } else {
            holder.imgStar.setImageResource(R.drawable.bookmark_disable);
        }

        SharedPref.init(activity);
        holder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.imgStar.getDrawable().getConstantState() == activity.getResources().getDrawable(R.drawable.bookmark_disable).getConstantState()) {
                    bookmarkProperty(propertyBean.hashid, SharedPref.read("token", ""));
                    holder.imgStar.setImageResource(R.drawable.bookmark_enable);
                } else {
                    bookmarkProperty(propertyBean.hashid, SharedPref.read("token", ""));
                    holder.imgStar.setImageResource(R.drawable.bookmark_disable);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvViewCount, tvTitle, tvDescription, tvUpdatedAt, tvBed, tvBath, tvArea, tvPrice;
        ImageView img_thumbnail, imgStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvViewCount = itemView.findViewById(R.id.tvViewsCount);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUpdatedAt = itemView.findViewById(R.id.tvUpdatedAt);
            tvBed = itemView.findViewById(R.id.tvBed);
            tvBath = itemView.findViewById(R.id.tvBath);
            tvArea = itemView.findViewById(R.id.tvArea);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
            imgStar = itemView.findViewById(R.id.imgStar);
        }
    }

    private void bookmarkProperty(String property_id, String token) {
        Call<ResponseMessageModel> call1 = apiService.bookmarkProperty(property_id, "Bearer" + token);
        call1.enqueue(new Callback<ResponseMessageModel>() {
            @Override
            public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseMessageModel responseMessageModel = response.body();
                    Toast.makeText(activity, "" + responseMessageModel.msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessageModel> call, Throwable t) {
                Toast.makeText(activity, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

}