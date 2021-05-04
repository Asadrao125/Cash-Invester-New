package com.gexton.cashinvesternew.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.ResponseMessageModel;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText edtFirstName, edtLastName, edtEmail, edtPhoneNo, edtPassword, edtConfirmPassword;
    CheckBox cbTermsAndConditions;
    Button btnSignup;
    ImageView imgBack;
    ApiService apiService;
    CircleImageView profileImage;
    Spinner spinner;
    final int CUSTOM_REQUEST_CODE = 987;
    String image_path;
    String token;
    private ArrayList<Uri> photoPaths = new ArrayList<>();
    File file;
    Dialog_CustomProgress customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = edtFirstName.getText().toString().trim();
                String lName = edtLastName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String phone = edtPhoneNo.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();
                String user_role = spinner.getSelectedItem().toString();
                if (TextUtils.isEmpty(fName)) {
                    edtFirstName.setError("Empty");
                    edtFirstName.requestFocus();
                } else if (TextUtils.isEmpty(lName)) {
                    edtLastName.setError("Empty");
                    edtLastName.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Empty");
                    edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(phone)) {
                    edtPhoneNo.setError("Empty");
                    edtPhoneNo.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Empty");
                    edtPassword.requestFocus();
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    edtConfirmPassword.setError("Empty");
                    edtConfirmPassword.requestFocus();
                } else {
                    if (cbTermsAndConditions.isChecked()) {
                        if (user_role.equals("Cash Buyer")) {
                            user_role = "cash_buyer";
                        } else {
                            user_role = "wholesaler";
                        }
                        if (password.equals(confirmPassword)) {
                            signupUser(fName, lName, email, phone, password, confirmPassword, user_role);
                            edtFirstName.setText("");
                            edtLastName.setText("");
                            edtEmail.setText("");
                            edtPhoneNo.setText("");
                            edtPassword.setText("");
                            edtConfirmPassword.setText("");
                        } else {
                            edtPassword.setError("Not Matched");
                            edtConfirmPassword.setError("Not Matched");
                            edtPassword.requestFocus();
                            edtConfirmPassword.requestFocus();
                        }
                    } else {
                        cbTermsAndConditions.setError("Please Accept");
                        cbTermsAndConditions.requestFocus();
                    }
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void signupUser(String fName, String lName, String email, String phone, String password,
                            String confirmPassword, String user_role) {

        customProgressDialog.showProgressDialog();

        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(image_path));
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", file.getName(), fbody);

        Map<String, RequestBody> map = new HashMap<>();
        map.put("phone_no", RequestBody.create(MediaType.parse("text/plain"), phone));
        map.put("user_role", RequestBody.create(MediaType.parse("text/plain"), user_role));
        map.put("password", RequestBody.create(MediaType.parse("text/plain"), password));
        map.put("password_confirmation", RequestBody.create(MediaType.parse("text/plain"), confirmPassword));
        map.put("last_name", RequestBody.create(MediaType.parse("text/plain"), lName));
        map.put("first_name", RequestBody.create(MediaType.parse("text/plain"), fName));
        map.put("email", RequestBody.create(MediaType.parse("text/plain"), email));
        map.put("device_token", RequestBody.create(MediaType.parse("text/plain"), token));
        map.put("platform", RequestBody.create(MediaType.parse("text/plain"), "android"));

        Call<ResponseMessageModel> call1 = apiService.registerUser(map, body);
        call1.enqueue(new Callback<ResponseMessageModel>() {
            @Override
            public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {

                customProgressDialog.dismissProgressDialog();

                if (response.body() != null && response.isSuccessful()) {
                    ResponseMessageModel newLoginResponse = response.body();
                    Log.d("Signup_Api_Response", "onResponse: " + newLoginResponse.msg);
                    Toast.makeText(SignupActivity.this, "" + newLoginResponse.msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessageModel> call, Throwable t) {

                customProgressDialog.dismissProgressDialog();

                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void init() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNo = findViewById(R.id.edtPhoneNo);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        cbTermsAndConditions = findViewById(R.id.cbTermsAndConditions);
        btnSignup = findViewById(R.id.btnSignup);
        imgBack = findViewById(R.id.imgBack);
        profileImage = findViewById(R.id.profileImage);
        apiService = APIClient.getClient().create(ApiService.class);
        spinner = findViewById(R.id.spinner);
        customProgressDialog = new Dialog_CustomProgress(SignupActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOM_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<Uri> dataList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            if (dataList != null) {
                photoPaths = new ArrayList<Uri>();
                photoPaths.addAll(dataList);
                try {
                    image_path = ContentUriUtils.INSTANCE.getFilePath(SignupActivity.this, photoPaths.get(0));
                    if (image_path != null) {
                        file = new File(image_path);
                        Picasso.get().load(file).into(profileImage);
                        System.out.println("-- file path " + file.getAbsolutePath());
                        Log.d("image_path", "onActivityResult: " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            pickPhoto();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void pickPhoto() {
        FilePickerBuilder.getInstance()
                .setMaxCount(1)
                .setSelectedFiles(photoPaths)
                .setActivityTheme(R.style.ThemeOverlay_AppCompat_Dark)
                .setActivityTitle("Please select media")
                .setImageSizeLimit(5)
                .setVideoSizeLimit(10)
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN, 4)
                .enableVideoPicker(false)
                .enableCameraSupport(true)
                .showGifs(false)
                .showFolderView(true)
                .enableSelectAll(false)
                .enableImagePicker(true)
                .setCameraPlaceholder(R.drawable.ic_camera)
                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .pickPhoto(this, CUSTOM_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("task_status", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        token = task.getResult();
                        SharedPref.init(getApplicationContext());
                        SharedPref.write("fcm_token", token);
                        System.out.println("-- FCM Token : " + token);
                    }
                });
    }

}