package com.gexton.cashinvesternew.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.ResponseMessageModel;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends BaseActivity {
    CircleImageView imgProfile;
    TextView tvName, tvUserRole, tvEditProfile, tvPhone, tvEmail, tvAddress, tvChangePassword, tvPrivacyPolicy, tvTermsAndConditions;
    TextView tvAboutApp, tvAppVersion, tvLogout;
    String fName, lName, userRole, imageUrl, phone, email, address, token;
    ApiService apiService;
    Dialog_CustomProgress customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_my_profile, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        init();

        fName = SharedPref.read("first_name", "");
        lName = SharedPref.read("last_name", "");
        userRole = SharedPref.read("user_role", "");
        imageUrl = SharedPref.read("image_url", "");
        phone = SharedPref.read("phone", "");
        email = SharedPref.read("email", "");
        address = SharedPref.read("address", "");

        tvName.setText(fName + " " + lName);
        tvUserRole.setText(userRole);
        tvPhone.setText(phone);
        tvEmail.setText(email);
        tvAddress.setText(address);
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).placeholder(R.mipmap.ic_launcher).into(imgProfile);
        }

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                token = SharedPref.read("token", "");

                customProgressDialog.show();

                Call<ResponseMessageModel> call = apiService.logout("Bearer" + token);
                call.enqueue(new Callback<ResponseMessageModel>() {
                    @Override
                    public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {

                        customProgressDialog.dismissProgressDialog();

                        if (response.body() != null && response.isSuccessful()) {
                            ResponseMessageModel responseMessageModel = response.body();
                            Toast.makeText(MyProfileActivity.this, "" + responseMessageModel.msg, Toast.LENGTH_SHORT).show();

                            SharedPref.remove("first_name");
                            SharedPref.remove("last_name");
                            SharedPref.remove("phone");
                            SharedPref.remove("email");
                            SharedPref.remove("address");
                            SharedPref.remove("user_role");
                            SharedPref.remove("image_url");
                            SharedPref.remove("cover_image_url");
                            SharedPref.remove("isLogin");
                            SharedPref.remove("fcm_token");
                            startActivity(new Intent(MyProfileActivity.this, LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessageModel> call, Throwable t) {
                        customProgressDialog.dismissProgressDialog();
                        Toast.makeText(MyProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProfileActivity.this, UpdateProfileActivity.class));
            }
        });

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, ManagePasswordActivity.class);
                intent.putExtra("val", "0");
                startActivity(intent);
            }
        });

        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChromeWithLink();
            }
        });

        tvTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChromeWithLink();
            }
        });
    }

    private void openChromeWithLink() {
        String urlString = "https://cashinvestornetwork.com/privacy-policy";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    private void init() {
        imgProfile = findViewById(R.id.imgProfile);
        tvName = findViewById(R.id.tvName);
        tvUserRole = findViewById(R.id.tvUserRole);
        tvEditProfile = findViewById(R.id.tvEditProfile);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        tvAboutApp = findViewById(R.id.tvAboutApp);
        tvAppVersion = findViewById(R.id.tvAppVersion);
        tvLogout = findViewById(R.id.tvLogout);
        SharedPref.init(this);
        apiService = APIClient.getClient().create(ApiService.class);
        customProgressDialog = new Dialog_CustomProgress(this);
    }
}