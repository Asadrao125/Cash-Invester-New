
package com.gexton.cashinvesternew.activities;

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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.models.NewLoginResponse;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends BaseActivity {
    EditText edtFirstName, edtLastName, edtPhoneNo, edtAddress, edtState, edtCity, edtZipcode;
    Button btnSubmit;
    ApiService apiService;
    CircleImageView profileImage;
    File file;
    final int CUSTOM_REQUEST_CODE = 987;
    String image_path;
    private ArrayList<Uri> photoPaths = new ArrayList<>();
    String token;
    ImageView imgBack;
    Dialog_CustomProgress customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_update_profile, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        init();

        settingData();


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhoto();
            }
        });

        token = SharedPref.read("token", "");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = edtFirstName.getText().toString().trim();
                String lname = edtLastName.getText().toString().trim();
                String phone = edtPhoneNo.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String state = edtState.getText().toString().trim();
                String city = edtCity.getText().toString().trim();
                String zipcode = edtZipcode.getText().toString().trim();

                if (TextUtils.isEmpty(image_path)) {
                    Toast.makeText(UpdateProfileActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(fname)) {
                    edtFirstName.setError("Empty");
                    edtFirstName.requestFocus();
                } else if (TextUtils.isEmpty(lname)) {
                    edtLastName.setError("Empty");
                    edtLastName.requestFocus();
                } else if (TextUtils.isEmpty(phone)) {
                    edtPhoneNo.setError("Empty");
                    edtPhoneNo.requestFocus();
                } else if (TextUtils.isEmpty(address)) {
                    edtAddress.setError("Empty");
                    edtAddress.requestFocus();
                } else if (TextUtils.isEmpty(city)) {
                    edtCity.setError("Empty");
                    edtCity.requestFocus();
                } else if (TextUtils.isEmpty(state)) {
                    edtState.setError("Empty");
                    edtState.requestFocus();
                } else if (TextUtils.isEmpty(zipcode)) {
                    edtZipcode.setError("Empty");
                    edtZipcode.requestFocus();
                } else {

                    customProgressDialog.showProgressDialog();

                    Log.d("image_ka_path", "onClick: " + image_path);

                    File file1 = new File(image_path);
                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                    Map<String, RequestBody> map = new HashMap<>();
                    MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", file.getName(), fbody);
                    map.put("first_name", RequestBody.create(MediaType.parse("text/plain"), fname));
                    map.put("last_name", RequestBody.create(MediaType.parse("text/plain"), lname));
                    map.put("phone_no", RequestBody.create(MediaType.parse("text/plain"), phone));
                    map.put("address", RequestBody.create(MediaType.parse("text/plain"), address));
                    map.put("city", RequestBody.create(MediaType.parse("text/plain"), city));
                    map.put("state", RequestBody.create(MediaType.parse("text/plain"), state));
                    map.put("zipcode", RequestBody.create(MediaType.parse("text/plain"), zipcode));

                    Call<NewLoginResponse> call = apiService.update("Bearer" + token, map, body);
                    call.enqueue(new Callback<NewLoginResponse>() {
                        @Override
                        public void onResponse(Call<NewLoginResponse> call, Response<NewLoginResponse> response) {

                            customProgressDialog.dismissProgressDialog();

                            if (response.isSuccessful() && response.body() != null) {
                                NewLoginResponse updateResponse = response.body();
                                Toast.makeText(UpdateProfileActivity.this, "" + updateResponse.msg, Toast.LENGTH_SHORT).show();

                                SharedPref.write("first_name", updateResponse.data.user.first_name);
                                SharedPref.write("last_name", updateResponse.data.user.last_name);
                                SharedPref.write("phone", updateResponse.data.user.phone_no);
                                SharedPref.write("image_url", updateResponse.data.user.image_url);
                            }
                        }

                        @Override
                        public void onFailure(Call<NewLoginResponse> call, Throwable t) {

                            customProgressDialog.dismissProgressDialog();

                            Toast.makeText(UpdateProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private void settingData() {
        edtFirstName.setText(SharedPref.read("first_name", ""));
        edtLastName.setText(SharedPref.read("last_name", ""));
        edtPhoneNo.setText(SharedPref.read("phone", ""));
        edtAddress.setText(SharedPref.read("address", ""));
        edtCity.setText(SharedPref.read("city", ""));
        edtZipcode.setText(SharedPref.read("zipcode", ""));
        String imageUrl = SharedPref.read("image_url", "");
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).placeholder(R.mipmap.ic_launcher).into(profileImage);
        }
    }

    private void init() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtPhoneNo = findViewById(R.id.edtPhoneNo);
        edtAddress = findViewById(R.id.edtAddress);
        edtState = findViewById(R.id.edtState);
        edtCity = findViewById(R.id.edtCity);
        edtZipcode = findViewById(R.id.edtZipcode);
        btnSubmit = findViewById(R.id.btnSubmit);
        profileImage = findViewById(R.id.profileImage);
        apiService = APIClient.getClient().create(ApiService.class);
        customProgressDialog = new Dialog_CustomProgress(UpdateProfileActivity.this);
        imgBack = findViewById(R.id.imgBack);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOM_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<Uri> dataList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            if (dataList != null) {
                photoPaths = new ArrayList<Uri>();
                photoPaths.addAll(dataList);
                try {
                    image_path = ContentUriUtils.INSTANCE.getFilePath(UpdateProfileActivity.this, photoPaths.get(0));
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
}