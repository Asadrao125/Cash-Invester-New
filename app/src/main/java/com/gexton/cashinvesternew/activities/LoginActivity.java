package com.gexton.cashinvesternew.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.NewLoginResponse;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.google.android.gms.maps.model.Dash;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvForgetPassword;
    LinearLayout layoutSignup;
    ApiService apiService;
    Dialog_CustomProgress customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        layoutSignup = findViewById(R.id.layoutSignup);
        btnLogin = findViewById(R.id.btnLogin);
        customProgressDialog = new Dialog_CustomProgress(LoginActivity.this);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        apiService = APIClient.getClient().create(ApiService.class);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Empty");
                    edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Empty");
                    edtPassword.requestFocus();
                } else {
                    login(email, password);
                }
            }
        });

        layoutSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagePasswordActivity.class);
                intent.putExtra("val", "1");
                startActivity(intent);
            }
        });
    }

    private void login(String email, String password) {
        customProgressDialog.showProgressDialog();
        Call<NewLoginResponse> call1 = apiService.loginUser(email, password);
        call1.enqueue(new Callback<NewLoginResponse>() {
            @Override
            public void onResponse(Call<NewLoginResponse> call, Response<NewLoginResponse> response) {

                customProgressDialog.dismissProgressDialog();

                if (response.isSuccessful() && response.body() != null) {
                    SharedPref.init(getApplicationContext());
                    NewLoginResponse loginResponse = response.body();
                    Log.d("Login_Api_Response", "onResponse: " + loginResponse.data.user.image_url);
                    SharedPref.write("first_name", loginResponse.data.user.first_name);
                    SharedPref.write("token", loginResponse.data.token);
                    SharedPref.write("last_name", loginResponse.data.user.last_name);
                    SharedPref.write("phone", loginResponse.data.user.phone_no);
                    SharedPref.write("email", loginResponse.data.user.email);
                    SharedPref.write("address", loginResponse.data.user.address);
                    SharedPref.write("user_role", loginResponse.data.user.user_role);
                    SharedPref.write("image_url", loginResponse.data.user.image_url);
                    SharedPref.write("cover_image_url", loginResponse.data.user.cover_image_url);
                    SharedPref.write("isLogin", "true");
                    startActivity(new Intent(getApplicationContext(), DashbordActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<NewLoginResponse> call, Throwable t) {

                customProgressDialog.dismissProgressDialog();

                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPref.init(getApplicationContext());
        if (!TextUtils.isEmpty(SharedPref.read("isLogin", ""))) {
            if (SharedPref.read("isLogin", "").equals("true")) {
                startActivity(new Intent(getApplicationContext(), DashbordActivity.class));
                finish();
            }
        }

    }
}