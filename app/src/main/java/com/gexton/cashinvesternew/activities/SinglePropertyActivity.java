package com.gexton.cashinvesternew.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.adapters.AmenitiesAdapter;
import com.gexton.cashinvesternew.adapters.SliderAdapterExample;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.ResponseMessageModel;
import com.gexton.cashinvesternew.models.SliderModel;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SinglePropertyActivity extends AppCompatActivity {
    ApiService apiService;
    String p_id, token;
    SliderView sliderView;
    SliderModel sliderModel;
    RecyclerView rvAmenities;
    ArrayList<SliderModel> sliderModelArrayList = new ArrayList<>();
    SliderAdapterExample adapter;
    ImageView imgBack, imgBookmark;
    TextView tvPrice, tvDescription, tvTotalViews, tvType, tvTitle, tvUploaded, tvBed, tvBath, tvArea, tvYearBuilt, tvPropertyId, tvType1;
    TextView tvEstRepairCost, tvCity, tvZipcode;
    Button btnBuyNow, btnAskAbout;
    Dialog_CustomProgress customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_property);

        setTitle("Property Detail");

        init();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkProperty(p_id, token);
                getSingleProperty();
            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        btnAskAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void init() {

        sliderView = findViewById(R.id.imageSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.FILL); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        apiService = APIClient.getClient().create(ApiService.class);
        p_id = getIntent().getStringExtra("p_id");
        SharedPref.init(this);
        token = SharedPref.read("token", "");
        imgBack = findViewById(R.id.imgBack);
        imgBookmark = findViewById(R.id.imgBookmark);
        tvPrice = findViewById(R.id.tvPrice);
        rvAmenities = findViewById(R.id.rvAmenities);
        int numberOfColumns = 4;
        RecyclerView.LayoutManager mLayoutManagerRVBP = new GridLayoutManager(SinglePropertyActivity.this, numberOfColumns);
        rvAmenities.setLayoutManager(mLayoutManagerRVBP);
        tvDescription = findViewById(R.id.tvDescription);
        tvTotalViews = findViewById(R.id.tvTotalViews);
        tvType = findViewById(R.id.tvType);
        tvTitle = findViewById(R.id.tvTitle);
        tvUploaded = findViewById(R.id.tvUploaded);
        tvBed = findViewById(R.id.tvBed);
        tvBath = findViewById(R.id.tvBath);
        tvArea = findViewById(R.id.tvArea);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnAskAbout = findViewById(R.id.btnAskAbout);
        customProgressDialog = new Dialog_CustomProgress(SinglePropertyActivity.this);
        tvPropertyId = findViewById(R.id.tvPropertyId);
        tvType1 = findViewById(R.id.tvType1);
        tvYearBuilt = findViewById(R.id.tvYearBuilt);
        tvEstRepairCost = findViewById(R.id.tvEstRepairCost);
        tvCity = findViewById(R.id.tvCity);
        tvZipcode = findViewById(R.id.tvZipcode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSingleProperty();
    }

    private void getSingleProperty() {
        //customProgressDialog.show();
        sliderModelArrayList.clear();
        Call<ResponseBody> call1 = apiService.getSingleProperty(p_id, "Bearer" + token);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //customProgressDialog.dismissProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String pageContent = response.body().string();
                        System.out.println("-- view single prop responce: " + pageContent);
                        JSONObject jsonObject = new JSONObject(pageContent);
                        JSONObject jsonObj = jsonObject.getJSONObject("data").getJSONObject("property");

                        parseResult(jsonObj);

                        JSONArray imagesArray = jsonObj.getJSONArray("images");
                        for (int i = 0; i < imagesArray.length(); i++) {
                            Log.d("images_array", "onResponse: " + imagesArray.get(i));
                            JSONObject imgObj = imagesArray.getJSONObject(i);
                            String id = imgObj.getString("id");
                            String property_id = imgObj.getString("property_id");
                            String image = imgObj.getString("image");
                            String thumbnail = imgObj.getString("thumbnail");
                            String created_at = imgObj.getString("created_at");
                            String updated_at = imgObj.getString("updated_at");
                            String image_url = imgObj.getString("image_url");
                            String thumbnail_url = imgObj.getString("thumbnail_url");
                            String hashid = imgObj.getString("hashid");

                            sliderModel = new SliderModel(id, property_id, image, thumbnail, created_at, updated_at, image_url, thumbnail_url, hashid);
                            sliderModelArrayList.add(sliderModel);
                        }

                        adapter = new SliderAdapterExample(SinglePropertyActivity.this);
                        sliderView.setSliderAdapter(adapter);
                        renewItems(sliderModelArrayList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //customProgressDialog.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void parseResult(JSONObject jsonObject) {
        try {
            String state = jsonObject.getString("state");
            String country = jsonObject.getString("country");
            String lat = jsonObject.getString("lat");
            String lng = jsonObject.getString("lng");
            String garages = jsonObject.getString("garages");
            String pool = jsonObject.getString("pool");
            String address = jsonObject.getString("address");
            String status = jsonObject.getString("status");
            String user_id = jsonObject.getString("user_id");
            String amenities = jsonObject.getString("amenities");
            String approved_by = jsonObject.getString("approved_by");
            String deleted_at = jsonObject.getString("deleted_at");
            String deleted_by = jsonObject.getString("deleted_by");
            String is_bookmarked = jsonObject.getString("is_bookmarked");
            String hashid = jsonObject.getString("hashid");
            String title = jsonObject.getString("title");
            String description = jsonObject.getString("description");
            String type = jsonObject.getString("type");
            String price = jsonObject.getString("price");
            String area = jsonObject.getString("area");
            String rooms = jsonObject.getString("rooms");
            String bathrooms = jsonObject.getString("bathrooms");
            String city = jsonObject.getString("city");
            String zipcode = jsonObject.getString("zipcode");
            String year_built = jsonObject.getString("year_built");
            String est_repair_cost = jsonObject.getString("est_repair_cost");
            String approved_at = jsonObject.getString("approved_at");
            String views_count = jsonObject.getString("views_count");

            Log.d("bookmark_status", "parseResult: " + is_bookmarked);
            if (is_bookmarked.equals("true")) {
                imgBookmark.setImageResource(R.drawable.bookmark_enable);
            } else {
                imgBookmark.setImageResource(R.drawable.bookmark_disable);
            }

            JSONArray amenitiesArray = jsonObject.getJSONArray("amenities");
            ArrayList<String> amenitiesList = new ArrayList<>();
            for (int i = 0; i < amenitiesArray.length(); i++) {
                Log.d("amenities", "parseResult: " + amenitiesArray.get(i));
                amenitiesList.add(amenitiesArray.get(i).toString());
            }
            AmenitiesAdapter adapter = new AmenitiesAdapter(SinglePropertyActivity.this, amenitiesList);
            rvAmenities.setAdapter(adapter);

            tvTotalViews.setText(views_count);
            tvTitle.setText(title);
            tvUploaded.setText(approved_at);
            tvDescription.setText(description);
            tvPrice.setText("$ " + price);
            tvBed.setText(rooms + " Bedrooms");
            tvBath.setText(bathrooms + " Bathrooms");
            tvArea.setText(area + " Sq.Ft");
            tvPropertyId.setText(jsonObject.getJSONObject("images").getString("property_id"));
            tvType1.setText(type);
            tvYearBuilt.setText(year_built);
            tvEstRepairCost.setText(est_repair_cost);
            tvCity.setText(city);
            tvZipcode.setText(zipcode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void renewItems(ArrayList<SliderModel> url) {
        List<SliderModel> sliderItemList = new ArrayList<>();
        for (int i = 0; i < url.size(); i++) {
            SliderModel sliderModel = new SliderModel();
            sliderModel.image_url = url.get(i).image_url;
            sliderItemList.add(sliderModel);
        }
        adapter.renewItems(sliderItemList);
    }

    public void addLead(String token, String description, String property_id) {
        customProgressDialog.show();
        Call<ResponseMessageModel> call1 = apiService.addPropertyLead(description, property_id, "Bearer" + token);
        call1.enqueue(new Callback<ResponseMessageModel>() {
            @Override
            public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {
                customProgressDialog.dismissProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    SharedPref.init(getApplicationContext());
                    ResponseMessageModel responseMessageMode = response.body();
                    Toast.makeText(SinglePropertyActivity.this, "" + responseMessageMode.msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessageModel> call, Throwable t) {
                customProgressDialog.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_buy_now, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        EditText edtDescription = (EditText) dialogView.findViewById(R.id.edtDescription);
        Button btnBuyNow2 = dialogView.findViewById(R.id.btnBuyNow2);
        ImageView imgCross = dialogView.findViewById(R.id.imgCross);

        btnBuyNow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = edtDescription.getText().toString().trim();
                if (TextUtils.isEmpty(desc)) {
                    edtDescription.setError("Empty");
                    edtDescription.requestFocus();
                } else {
                    addLead(token, desc, p_id);
                    alertDialog.dismiss();
                }
            }
        });

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void bookmarkProperty(String property_id, String token) {
        Call<ResponseMessageModel> call1 = apiService.bookmarkProperty(property_id, "Bearer" + token);
        call1.enqueue(new Callback<ResponseMessageModel>() {
            @Override
            public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseMessageModel responseMessageModel = response.body();
                    Toast.makeText(SinglePropertyActivity.this, "" + responseMessageModel.msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessageModel> call, Throwable t) {
                Toast.makeText(SinglePropertyActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

}