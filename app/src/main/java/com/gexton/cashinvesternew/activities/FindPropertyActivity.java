package com.gexton.cashinvesternew.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.adapters.PropertyBeanAdapter;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.PropertyBean;
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

public class FindPropertyActivity extends BaseActivity {
    ApiService apiService;
    Dialog_CustomProgress customProgressDialog;
    String token;
    ArrayList<PropertyBean> propertyBeanArrayList = new ArrayList<>();
    PropertyBeanAdapter adapter;
    RecyclerView rvProperties;
    //NestedScrollView nestedScrollView;
    int page = 1;
    LinearLayout loading_layout;
    ImageView imgSrearchFilter;
    EditText edtSearch;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_find_property, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        init();

        /*nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    loading_layout.setVisibility(View.VISIBLE);
                    getData(page);
                }
            }
        });*/

        imgSrearchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                final String query = s.toString().toLowerCase().trim();
                final ArrayList<PropertyBean> filteredList = new ArrayList<>();
                for (int k = 0; k < propertyBeanArrayList.size(); k++) {
                    final String text = propertyBeanArrayList.get(k).title.toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(propertyBeanArrayList.get(k));
                    }
                }
                rvProperties.setLayoutManager(new LinearLayoutManager(FindPropertyActivity.this));
                adapter = new PropertyBeanAdapter(FindPropertyActivity.this, filteredList);
                rvProperties.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                            propertyBeanArrayList.clear();
                            getData(page);
                        }
                    }
                }, 3000);
            }
        });
    }

    private void init() {
        apiService = APIClient.getClient().create(ApiService.class);
        customProgressDialog = new Dialog_CustomProgress(this);
        SharedPref.init(this);
        rvProperties = findViewById(R.id.rvProperties);
        rvProperties.setLayoutManager(new LinearLayoutManager(this));
        rvProperties.setHasFixedSize(true);
        loading_layout = findViewById(R.id.loading_layout);
        imgSrearchFilter = findViewById(R.id.imgSrearchFilter);
        edtSearch = findViewById(R.id.edtSearch);
        token = SharedPref.read("token", "");
        swipeRefreshLayout = findViewById(R.id.swiperefresh_layout);
    }

    private void getData(int page) {
        Call<ResponseBody> call = apiService.getAllProperties(String.valueOf(page), "Bearer" + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loading_layout.setVisibility(View.GONE);
                    try {
                        String pageContent = response.body().string();
                        System.out.println("-- find prop responce: " + pageContent);

                        JSONObject jsonObject = new JSONObject(pageContent);
                        JSONArray data = jsonObject.getJSONObject("data").getJSONObject("properties").getJSONArray("data");

                        ArrayList<PropertyBean> propertyBeans = new GsonBuilder().create().fromJson(data + "",
                                new TypeToken<List<PropertyBean>>() {
                                }.getType());

                        System.out.println("-- Prop list size : " + propertyBeans.size());
                        for (int i = 0; i < propertyBeans.size(); i++) {
                            System.out.println("-- prop : " + propertyBeans.get(i).title + "  : User : " + propertyBeans.get(i).user.first_name);

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
                            String userid3 = propertyBeans.get(i).user.id;
                            String user_first_name = propertyBeans.get(i).user.first_name;
                            String user_last_name = propertyBeans.get(i).user.last_name;
                            String user_user_name = propertyBeans.get(i).user.user_name;
                            String phone_no = propertyBeans.get(i).user.phone_no;
                            String user_image = propertyBeans.get(i).user.image;
                            String cover_image = propertyBeans.get(i).user.cover_image;
                            String user_description = propertyBeans.get(i).user.description;
                            String email = propertyBeans.get(i).user.email;
                            String user_role = propertyBeans.get(i).user.user_role;
                            String user_address = propertyBeans.get(i).user.address;
                            String user_city = propertyBeans.get(i).user.city;
                            String user_state = propertyBeans.get(i).user.state;
                            String user_zipcode = propertyBeans.get(i).user.zipcode;
                            String user_is_active = propertyBeans.get(i).user.is_active;
                            String user_platform = propertyBeans.get(i).user.platform;
                            String user_device_token = propertyBeans.get(i).user.device_token;
                            String user_deleted_at = propertyBeans.get(i).user.deleted_at;
                            String user_created_at = propertyBeans.get(i).user.created_at;
                            String user_updated_at = propertyBeans.get(i).user.updated_at;
                            String user_image_url = propertyBeans.get(i).user.image_url;
                            String user_cover_image_url = propertyBeans.get(i).user.cover_image_url;

                            PropertyBean.User user = new PropertyBean.User(userid3, user_first_name, user_last_name, user_user_name,
                                    phone_no, user_image, cover_image, user_description, email, user_role, user_address, user_city,
                                    user_state, user_zipcode, user_is_active, user_platform, user_device_token, user_deleted_at,
                                    user_created_at, user_updated_at, user_image_url, user_cover_image_url);

                            PropertyBean.Thumbnail thumbnailObj = new PropertyBean.Thumbnail(id, property_id, image, thumbnail,
                                    created_at, updated_at, image_url, thumbnail_url, hashid2);

                            PropertyBean propertyBean = new PropertyBean(id, user_id, title, slug, description, type, status, price, area,
                                    rooms, bathrooms, garages, pool, address, city, state, zipcode, country, lat, lng, year_built,
                                    est_repair_cost, amenities, approved_at, approved_by, deleted_at, created_at, updated_at, property_id,
                                    deleted_by, views_count, is_bookmarked, hashid, thumbnailObj, user);

                            propertyBeanArrayList.add(propertyBean);

                        }
                        adapter = new PropertyBeanAdapter(FindPropertyActivity.this, propertyBeanArrayList);
                        rvProperties.setAdapter(adapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading_layout.setVisibility(View.GONE);
                Toast.makeText(FindPropertyActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getData(page);
    }
}