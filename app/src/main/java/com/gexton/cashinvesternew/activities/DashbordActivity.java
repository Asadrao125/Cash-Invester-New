package com.gexton.cashinvesternew.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.HomeModel;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashbordActivity extends BaseActivity {
    TextView tvName, tvTotalViewed, tvViewAll, tvBookmarked, tvSavedSearches;
    CircleImageView profileImage;
    String fName, lName, imageUrl, userRole;
    ApiService apiService;
    String token;
    Dialog_CustomProgress customProgressDialog;
    CardView cvAllProperties, cvTotalViewed, cvSavedSearches, cvBookmarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_dashbord, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        profileImage = findViewById(R.id.profileImage);
        tvName = findViewById(R.id.tvName);
        tvTotalViewed = findViewById(R.id.tvTotalViewed);
        tvViewAll = findViewById(R.id.tvViewAll);
        cvAllProperties = findViewById(R.id.cvAllProperties);
        cvTotalViewed = findViewById(R.id.cvTotalViewed);
        cvSavedSearches = findViewById(R.id.cvSavedSearches);
        cvBookmarked = findViewById(R.id.cvBookmarked);
        apiService = APIClient.getClient().create(ApiService.class);
        tvBookmarked = findViewById(R.id.tvBookmarked);
        tvSavedSearches = findViewById(R.id.tvSavedSearches);
        customProgressDialog = new Dialog_CustomProgress((Activity) DashbordActivity.this);

        settingData();

        getHomeApiData();

        tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cvAllProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashbordActivity.this, FindPropertyActivity.class));
            }
        });

        cvTotalViewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cvSavedSearches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashbordActivity.this, SavedSearchesActivity.class));
            }
        });

        cvBookmarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashbordActivity.this, BookmarkedPropertiesActivity.class));
            }
        });
    }

    private void settingData() {
        SharedPref.init(DashbordActivity.this);
        fName = SharedPref.read("first_name", "");
        lName = SharedPref.read("last_name", "");
        imageUrl = SharedPref.read("image_url", "");
        userRole = SharedPref.read("user_role", "");
        token = SharedPref.read("token", "");

        tvTotalViewed.setText(SharedPref.read("total_viewed", ""));
        tvBookmarked.setText(SharedPref.read("total_bookmarked", ""));
        tvSavedSearches.setText(SharedPref.read("total_searches", ""));

        tvName.setText(fName + " " + lName);
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).into(profileImage);
        }

        if (userRole.equals("cash_buyer")) {
            cvAllProperties.setVisibility(View.GONE);
        } else {
            cvAllProperties.setVisibility(View.VISIBLE);
        }

    }

    private void getHomeApiData() {
        Call<HomeModel> call = apiService.homeApi("Bearer" + token);
        call.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, @NotNull Response<HomeModel> response) {
                customProgressDialog.dismissProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    HomeModel homeModel = response.body();
                    tvTotalViewed.setText(homeModel.data.total_viewed);
                    tvBookmarked.setText(homeModel.data.total_bookmarked);
                    tvSavedSearches.setText(homeModel.data.total_searches);
                    SharedPref.write("total_viewed", homeModel.data.total_viewed);
                    SharedPref.write("total_bookmarked", homeModel.data.total_bookmarked);
                    SharedPref.write("total_searches", homeModel.data.total_searches);
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                Toast.makeText(DashbordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}