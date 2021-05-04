package com.gexton.cashinvesternew.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.activities.SavedSearchesActivity;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.ResponseMessageModel;
import com.gexton.cashinvesternew.models.SavedSearchesModel;
import com.gexton.cashinvesternew.utils.SharedPref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedSearchesAdapter extends RecyclerView.Adapter<SavedSearchesAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<SavedSearchesModel> dataArrayList;
    ApiService apiService;
    String token;

    public SavedSearchesAdapter(Activity activity, ArrayList<SavedSearchesModel> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
        apiService = APIClient.getClient().create(ApiService.class);
        SharedPref.init(activity);
        token = SharedPref.read("token", "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_searches, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedSearchesModel savedSearchesModel = dataArrayList.get(position);
        holder.tvTitle.setText(savedSearchesModel.title);

        //yyyy-MM-dd HH:mm:ss
        String commentDate = savedSearchesModel.created_at;//2021-03-04T09:16:36.000000Z      2020-02-26T16:01:34.000Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).parse(commentDate);//"2020-2-31 11:30:19"
            commentDate = new SimpleDateFormat("dd, MMM yyyy @ hh:mm a").format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvCreatedAt.setText(commentDate);

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Edit Saved Search", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDefaultDialog(savedSearchesModel.hashid);
            }
        });

        holder.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Search Saved Search", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCreatedAt;
        ImageView imgSearch, imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            imgSearch = itemView.findViewById(R.id.imgSearch);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    public void showDefaultDialog(String s_id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteSavedSearch(s_id);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteSavedSearch(String searchId) {
        Call<ResponseMessageModel> call1 = apiService.deleteSavedSearch(searchId, "Bearer" + token);
        call1.enqueue(new Callback<ResponseMessageModel>() {
            @Override
            public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseMessageModel responseMessageModel = response.body();
                    Toast.makeText(activity, "" + responseMessageModel.msg, Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, SavedSearchesActivity.class));
                    activity.finish();
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