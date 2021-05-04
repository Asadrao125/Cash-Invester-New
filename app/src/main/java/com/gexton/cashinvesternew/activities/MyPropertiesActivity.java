package com.gexton.cashinvesternew.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.adapters.PropertyBeanAdapter2;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.PropertyBean2;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPropertiesActivity extends BaseActivity {
    String token;
    ApiService apiService;
    Dialog_CustomProgress customProgressDialog;
    PropertyBeanAdapter2 adapter;
    RecyclerView rvProperties;
    NestedScrollView nestedScrollView;
    int page = 1;
    LinearLayout loading_layout;
    SwipeRefreshLayout swipeRefreshLayout;
    Button btnAddNewProperty;
    ArrayList<PropertyBean2> propertyBeanArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_my_properties, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        init();

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    loading_layout.setVisibility(View.VISIBLE);
                    getData(page);
                }
            }
        });

        btnAddNewProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyPropertiesActivity.this, AddPropertyActivity.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                            getData(page);
                        }
                    }
                }, 2000);
            }
        });
    }

    private void init() {
        apiService = APIClient.getClient().create(ApiService.class);
        SharedPref.init(MyPropertiesActivity.this);
        token = SharedPref.read("token", "");
        customProgressDialog = new Dialog_CustomProgress(MyPropertiesActivity.this);
        rvProperties = findViewById(R.id.rvProperties);
        rvProperties.setLayoutManager(new LinearLayoutManager(MyPropertiesActivity.this));
        rvProperties.setHasFixedSize(true);
        nestedScrollView = findViewById(R.id.scroll_view);
        loading_layout = findViewById(R.id.loading_layout);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_layout);
        btnAddNewProperty = findViewById(R.id.btnAddNewProperty);
    }

    private void getData(int page) {
        Call<ResponseBody> call = apiService.getMyProperties(String.valueOf(page), "Bearer" + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loading_layout.setVisibility(View.GONE);
                    try {
                        String pageContent = response.body().string();
                        System.out.println("-- my prop responce: " + pageContent);

                        JSONObject jsonObject = new JSONObject(pageContent);
                        JSONArray data = jsonObject.getJSONObject("data").getJSONObject("property").getJSONArray("data");

                        ArrayList<PropertyBean2> propertyBeans = new GsonBuilder().create().fromJson(data + "",
                                new TypeToken<List<PropertyBean2>>() {
                                }.getType());

                        System.out.println("-- My Prop list size : " + propertyBeans.size());
                        if (propertyBeans.size() > 0) {
                            for (int i = 0; i < propertyBeans.size(); i++) {
                                System.out.println("-- my prop : " + propertyBeans.get(i).title);

                                String id = propertyBeans.get(i).id;
                                String user_id = propertyBeans.get(i).user_id;
                                String title = propertyBeans.get(i).title;
                                String slug = propertyBeans.get(i).slug;
                                String description = propertyBeans.get(i).description;
                                String type = propertyBeans.get(i).type;
                                String status = propertyBeans.get(i).status;
                                String price = propertyBeans.get(i).price;
                                String area = propertyBeans.get(i).area;
                                String rooms = propertyBeans.get(i).rooms;
                                String bathrooms = propertyBeans.get(i).bathrooms;
                                String garages = propertyBeans.get(i).garages;
                                String pool = propertyBeans.get(i).pool;
                                String address = propertyBeans.get(i).address;
                                String city = propertyBeans.get(i).city;
                                String state = propertyBeans.get(i).state;
                                String zipcode = propertyBeans.get(i).zipcode;
                                String country = propertyBeans.get(i).country;
                                String lat = propertyBeans.get(i).lat;
                                String lng = propertyBeans.get(i).lng;
                                String year_built = propertyBeans.get(i).year_built;
                                String est_repair_cost = propertyBeans.get(i).est_repair_cost;
                                ArrayList<String> amenities = propertyBeans.get(i).amenities;
                                String approved_at = propertyBeans.get(i).approved_at;
                                String approved_by = propertyBeans.get(i).approved_by;
                                String deleted_at = propertyBeans.get(i).deleted_at;
                                String deleted_by = propertyBeans.get(i).deleted_by;
                                String views_count = propertyBeans.get(i).views_count;
                                String is_bookmarked = propertyBeans.get(i).is_bookmarked;
                                String hashid = propertyBeans.get(i).hashid;

                                String thumbnail_id = propertyBeans.get(i).thumbnail.id;
                                String property_id = propertyBeans.get(i).thumbnail.property_id;
                                String image = propertyBeans.get(i).thumbnail.image;
                                String thumbnail = propertyBeans.get(i).thumbnail.thumbnail;
                                String image_url = propertyBeans.get(i).thumbnail.image_url;
                                String thumbnail_url = propertyBeans.get(i).thumbnail.thumbnail_url;
                                String created_at = propertyBeans.get(i).thumbnail.created_at;
                                String updated_at = propertyBeans.get(i).thumbnail.updated_at;
                                String hashid2 = propertyBeans.get(i).thumbnail.hashid;

                                PropertyBean2.Thumbnail thumbnailObj = new PropertyBean2.Thumbnail(id, property_id, image, thumbnail,
                                        created_at, updated_at, image_url, thumbnail_url, hashid2);

                                PropertyBean2 propertyBean = new PropertyBean2(id, user_id, title, slug, description, type, status, price, area,
                                        rooms, bathrooms, garages, pool, address, city, state, zipcode, country, lat, lng, year_built,
                                        est_repair_cost, amenities, approved_at, approved_by, deleted_at, created_at, updated_at, property_id,
                                        deleted_by, views_count, is_bookmarked, hashid, thumbnailObj);

                                propertyBeanArrayList.add(propertyBean);

                            }
                            adapter = new PropertyBeanAdapter2(MyPropertiesActivity.this, propertyBeanArrayList);
                            rvProperties.setAdapter(adapter);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading_layout.setVisibility(View.GONE);
                Toast.makeText(MyPropertiesActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getData(page);
    }

}