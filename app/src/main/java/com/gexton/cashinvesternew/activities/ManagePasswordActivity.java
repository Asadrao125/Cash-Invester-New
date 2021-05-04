package com.gexton.cashinvesternew.activities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.ResponseMessageModel;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;

public class ManagePasswordActivity extends BaseActivity {
    EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword, edtEmail;
    Button btnSubmit, btnForgetPassword;
    ApiService apiService;
    String token, val;
    LinearLayout changePasswordLayout, forgetPasswordLayout;
    Dialog_CustomProgress customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_manage_password, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        SharedPref.init(this);
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        apiService = APIClient.getClient().create(ApiService.class);
        edtEmail = findViewById(R.id.edtEmail);
        btnForgetPassword = findViewById(R.id.btnForgetPassword);
        token = SharedPref.read("token", "");
        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        forgetPasswordLayout = findViewById(R.id.forgetPasswordLayout);
        customProgressDialog = new Dialog_CustomProgress(ManagePasswordActivity.this);

        val = getIntent().getStringExtra("val");
        if (!TextUtils.isEmpty(val)) {
            if (val.equals("0")) {
                /* 2 For Change Password */
                forgetPasswordLayout.setVisibility(View.GONE);
                changePasswordLayout.setVisibility(View.VISIBLE);
            } else if (val.equals("1")) {
                /* 1 For Forget Password */
                forgetPasswordLayout.setVisibility(View.VISIBLE);
                changePasswordLayout.setVisibility(View.GONE);
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c_p = edtCurrentPassword.getText().toString().trim();
                String n_p = edtNewPassword.getText().toString().trim();
                String co_p = edtConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(c_p)) {
                    edtCurrentPassword.setError("Empty");
                    edtCurrentPassword.requestFocus();
                } else if (TextUtils.isEmpty(n_p)) {
                    edtNewPassword.setError("Empty");
                    edtNewPassword.requestFocus();
                } else if (TextUtils.isEmpty(co_p)) {
                    edtConfirmPassword.setError("Empty");
                    edtConfirmPassword.requestFocus();
                } else {
                    if (n_p.equals(co_p)) {
                        changePassword(c_p, n_p, co_p);
                    } else {
                        edtNewPassword.setError("Not Matched");
                        edtNewPassword.requestFocus();
                        edtConfirmPassword.setError("Not Matched");
                        edtConfirmPassword.requestFocus();
                    }
                }
            }
        });

        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Empty");
                    edtEmail.requestFocus();
                } else {
                    forgetPassword(email);
                }

            }
        });
    }

    private void forgetPassword(String email) {

        customProgressDialog.show();

        Call<ResponseMessageModel> call = apiService.forgetPassword(email);
        call.enqueue(new Callback<ResponseMessageModel>() {
            @Override
            public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {

                customProgressDialog.dismissProgressDialog();

                if (response.body() != null && response.isSuccessful()) {
                    ResponseMessageModel updateResponse = response.body();
                    Toast.makeText(ManagePasswordActivity.this, "" + updateResponse.msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessageModel> call, Throwable t) {

                customProgressDialog.dismissProgressDialog();

                Toast.makeText(ManagePasswordActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePassword(String c_p, String n_p, String co_p) {

        customProgressDialog.showProgressDialog();

        Call<ResponseMessageModel> call = apiService.changePassword("Bearer" + token, c_p, n_p, co_p);
        call.enqueue(new Callback<ResponseMessageModel>() {
            @Override
            public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {

                customProgressDialog.dismissProgressDialog();

                if (response.body() != null && response.isSuccessful()) {
                    ResponseMessageModel updateResponse = response.body();
                    Toast.makeText(ManagePasswordActivity.this, "" + updateResponse.msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessageModel> call, Throwable t) {

                customProgressDialog.dismissProgressDialog();

                Toast.makeText(ManagePasswordActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}