package com.gexton.cashinvesternew.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.adapters.SavedSearchesAdapter;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.api2.ApiCallback;
import com.gexton.cashinvesternew.api2.ApiManager;
import com.gexton.cashinvesternew.models.SavedSearchesModel;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SavedSearchesActivity extends BaseActivity implements ApiCallback {
    String token;
    ApiService apiService;
    RecyclerView rvSavedSearches;
    NestedScrollView nestedScrollView;
    int page = 1;
    LinearLayout loading_layout;
    SwipeRefreshLayout swipeRefreshLayout;
    SavedSearchesAdapter adapter;
    ApiCallback apiCallback;
    ArrayList<SavedSearchesModel> savedSearchesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_saved_searches, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        SharedPref.init(this);
        token = SharedPref.read("token", "");
        apiService = APIClient.getClient().create(ApiService.class);
        rvSavedSearches = findViewById(R.id.rvSavedSearches);
        rvSavedSearches.setLayoutManager(new LinearLayoutManager(this));
        rvSavedSearches.setHasFixedSize(true);
        nestedScrollView = findViewById(R.id.scroll_view);
        loading_layout = findViewById(R.id.loading_layout);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_layout);
        apiCallback = SavedSearchesActivity.this;
        savedSearchesArrayList = new ArrayList<>();

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    loading_layout.setVisibility(View.VISIBLE);
                    loadSavedSearches(String.valueOf(page));
                }
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
                            loadSavedSearches(String.valueOf(page));
                        }
                    }
                }, 3000);
            }
        });
    }

    private void loadSavedSearches(String page) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        ApiManager apiManager = new ApiManager(SavedSearchesActivity.this, "get", ApiManager.API_SAVED_SEARCHES, requestParams, apiCallback);
        apiManager.loadURL();
    }

    @Override
    public void onApiResponce(int httpStatusCode, int successOrFail, String apiName, String apiResponce) {
        loading_layout.setVisibility(View.GONE);
        savedSearchesArrayList.clear();
        try {

            JSONObject jsonObject = new JSONObject(apiResponce);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("searches").getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String title = obj.getString("title");
                String created_at = obj.getString("created_at");
                String hashid = obj.getString("hashid");
                savedSearchesArrayList.add(new SavedSearchesModel(title, created_at, hashid));
            }

            adapter = new SavedSearchesAdapter(SavedSearchesActivity.this, savedSearchesArrayList);
            rvSavedSearches.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loadSavedSearches(String.valueOf(page));
    }

}