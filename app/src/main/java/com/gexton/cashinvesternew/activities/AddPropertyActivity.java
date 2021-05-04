package com.gexton.cashinvesternew.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
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
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.adapters.AmenitiesAdapter;
import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.api.ApiService;
import com.gexton.cashinvesternew.gps.GPSTracker;
import com.gexton.cashinvesternew.models.ResponseMessageModel;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.RecyclerItemClickListener;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddPropertyActivity extends AppCompatActivity {
    ImageView imgBack, imgLocation;
    RecyclerView rvAmenities;
    ViewFlipper viewFlipper;
    Button btnNext, btnNext2, btnSubmit, btnPrevious, btnPrevious2;
    ArrayList<String> amenitiesArrayList = new ArrayList<>();
    LinearLayout uploadDocumentsLayout, uploadPhotosLayout;
    EditText edtTitle, edtDescription, edtPrice, edtArea, edtYearBuilt, edtEstimatedRepairOfCost, edtEnterLocation, edtState, edtZipcode;
    Spinner spinnerPropertyType, spinnerBedroom, spinnerBathrooms;
    ArrayList<String> amenitiesSelected = new ArrayList<>();
    final int CUSTOM_REQUEST_CODE = 987;
    public static ArrayList<Uri> photoPaths = new ArrayList<>();
    public static ArrayList<Uri> docPaths = new ArrayList<>();
    ApiService apiService;
    File imageFile, docFile;
    Dialog_CustomProgress customProgressDialog;
    String token;
    MultipartBody.Part document;
    MultipartBody.Part image;
    String doc_path, image_path;
    GPSTracker gpsTracker;
    List<Address> addresses;
    String address, city, state_s, postalCode;
    String a, s, z;
    int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        imgBack = findViewById(R.id.imgBack);
        imgLocation = findViewById(R.id.imgLocation);
        rvAmenities = findViewById(R.id.rvAmenities);
        int numberOfColumns = 4;
        RecyclerView.LayoutManager mLayoutManagerRVBP = new GridLayoutManager(AddPropertyActivity.this, numberOfColumns);
        rvAmenities.setLayoutManager(mLayoutManagerRVBP);
        btnNext = findViewById(R.id.btnNext);
        btnNext2 = findViewById(R.id.btnNext2);
        viewFlipper = findViewById(R.id.viewFlipper);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrevious2 = findViewById(R.id.btnPrevious2);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtPrice = findViewById(R.id.edtPrice);
        edtArea = findViewById(R.id.edtArea);
        edtYearBuilt = findViewById(R.id.edtYearBuilt);
        edtEstimatedRepairOfCost = findViewById(R.id.edtEstimatedCostOfRepair);
        spinnerBedroom = findViewById(R.id.spBed);
        spinnerBathrooms = findViewById(R.id.spBath);
        spinnerPropertyType = findViewById(R.id.spinnerPropertyType);
        edtEnterLocation = findViewById(R.id.edtEnterLocation);
        edtState = findViewById(R.id.edtState);
        edtZipcode = findViewById(R.id.edtZipcode);
        uploadPhotosLayout = findViewById(R.id.uploadPhotosLayouts);
        uploadDocumentsLayout = findViewById(R.id.uploadDocumentsLayout);
        apiService = APIClient.getClient().create(ApiService.class);
        customProgressDialog = new Dialog_CustomProgress(this);
        SharedPref.init(this);
        token = SharedPref.read("token", "");
        gpsTracker = new GPSTracker(this);

        amenitiesArrayList.add("Air Conditioning");
        amenitiesArrayList.add("Garage");
        amenitiesArrayList.add("Swimming Pool");
        amenitiesArrayList.add("Jacuzzi");
        amenitiesArrayList.add("Dryer");
        amenitiesArrayList.add("Washer");
        amenitiesArrayList.add("Gym");
        amenitiesArrayList.add("Refrigirator");
        AmenitiesAdapter adapter = new AmenitiesAdapter(AddPropertyActivity.this, amenitiesArrayList);
        rvAmenities.setAdapter(adapter);

        rvAmenities.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                rvAmenities, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                amenitiesSelected.add(amenitiesArrayList.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                System.out.println("-- item long press pos: " + position);
            }
        }));

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPropertyActivity.this, MapActivity.class));
            }
        });

        uploadPhotosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        uploadDocumentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickDoc();
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showPrevious();
            }
        });

        btnPrevious2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showPrevious();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        a = getIntent().getStringExtra("a");
        s = getIntent().getStringExtra("s");
        z = getIntent().getStringExtra("z");
        p = getIntent().getIntExtra("p", 1000);

        if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(s) && !TextUtils.isEmpty(z) && p != 0) {
            edtEnterLocation.setText(a);
            edtState.setText(s);
            edtZipcode.setText(z);
            viewFlipper.setDisplayedChild(1);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtTitle.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();
                String price = edtPrice.getText().toString().trim();
                String area = edtArea.getText().toString().trim();
                String year_built = edtYearBuilt.getText().toString().trim();
                String estimated_cost_repair = edtEstimatedRepairOfCost.getText().toString().trim();
                String property_type = spinnerPropertyType.getSelectedItem().toString();
                String bedrooms = spinnerBedroom.getSelectedItem().toString();
                String bathrooms = spinnerBathrooms.getSelectedItem().toString();
                for (int i = 0; i < amenitiesSelected.size(); i++) {
                    Log.d("amenities_selected", "onClick: " + amenitiesSelected.get(i));
                }
                String location = edtEnterLocation.getText().toString().trim();
                String state = edtState.getText().toString().trim();
                String zipcode = edtZipcode.getText().toString().trim();
                for (int j = 0; j < photoPaths.size(); j++) {
                    Log.d("selected_image_paths", "onClick: " + photoPaths.get(j));
                }
                Log.d("selected_file_path", "onClick: " + docPaths.get(0));

                if (TextUtils.isEmpty(title)) {
                    edtTitle.setError("Empty");
                    edtTitle.requestFocus();
                } else if (TextUtils.isEmpty(description)) {
                    edtDescription.setError("Empty");
                    edtDescription.requestFocus();
                } else if (TextUtils.isEmpty(price)) {
                    edtPrice.setError("Empty");
                    edtPrice.requestFocus();
                } else if (TextUtils.isEmpty(area)) {
                    edtArea.setError("Empty");
                    edtArea.requestFocus();
                } else if (TextUtils.isEmpty(year_built)) {
                    edtYearBuilt.setError("Empty");
                    edtYearBuilt.requestFocus();
                } else if (TextUtils.isEmpty(estimated_cost_repair)) {
                    edtEstimatedRepairOfCost.setError("Empty");
                    edtEstimatedRepairOfCost.requestFocus();
                } else if (spinnerPropertyType.getSelectedItem().equals("Property Type")) {
                    Toast.makeText(AddPropertyActivity.this, "Please Select Property Type", Toast.LENGTH_SHORT).show();
                }/* else if (spinnerBedroom.getSelectedItem().equals("Bedrooms")) {
                    Toast.makeText(AddPropertyActivity.this, "Please Select Bedrooms", Toast.LENGTH_SHORT).show();
                } else if (spinnerBathrooms.getSelectedItem().equals("Bathrooms")) {
                    Toast.makeText(AddPropertyActivity.this, "Please Select Bathrooms", Toast.LENGTH_SHORT).show();
                }*/ else if (amenitiesSelected.size() == 0) {
                    Toast.makeText(AddPropertyActivity.this, "Please Select Amenities to Continue", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(location)) {
                    edtEnterLocation.setError("Empty");
                    edtEnterLocation.requestFocus();
                } else if (TextUtils.isEmpty(state)) {
                    edtState.setError("Empty");
                    edtState.requestFocus();
                } else if (TextUtils.isEmpty(zipcode)) {
                    edtZipcode.setError("Empty");
                    edtZipcode.requestFocus();
                } else if (photoPaths.size() == 0) {
                    Toast.makeText(AddPropertyActivity.this, "Please Select Photos to Continue", Toast.LENGTH_SHORT).show();
                } else if (docPaths.size() == 0) {
                    Toast.makeText(AddPropertyActivity.this, "Please Select Document to Continue", Toast.LENGTH_SHORT).show();
                } else {

                    for (int i = 0; i < photoPaths.size(); i++) {
                        try {

                            image_path = ContentUriUtils.INSTANCE.getFilePath(AddPropertyActivity.this, photoPaths.get(i));
                            RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(image_path));
                            image = MultipartBody.Part.createFormData("images[]", imageFile.getName(), fbody);

                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(doc_path));
                    document = MultipartBody.Part.createFormData("documents[]", docFile.getName(), fbody);

                    Geocoder geocoder = new Geocoder(AddPropertyActivity.this, Locale.getDefault());
                    try {

                        addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                        address = addresses.get(0).getAddressLine(0);
                        city = addresses.get(0).getLocality();
                        state_s = addresses.get(0).getAdminArea();
                        postalCode = addresses.get(0).getPostalCode();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Map<String, RequestBody> map = new HashMap<>();
                    map.put("title", RequestBody.create(MediaType.parse("text/plain"), title));
                    map.put("description", RequestBody.create(MediaType.parse("text/plain"), description));
                    map.put("price", RequestBody.create(MediaType.parse("text/plain"), price));
                    map.put("type", RequestBody.create(MediaType.parse("text/plain"), property_type));
                    map.put("area", RequestBody.create(MediaType.parse("text/plain"), area));
                    map.put("rooms", RequestBody.create(MediaType.parse("text/plain"), "4"));
                    map.put("bathrooms", RequestBody.create(MediaType.parse("text/plain"), "4"));
                    map.put("address", RequestBody.create(MediaType.parse("text/plain"), address));
                    map.put("city", RequestBody.create(MediaType.parse("text/plain"), city));
                    map.put("state", RequestBody.create(MediaType.parse("text/plain"), state_s));
                    map.put("zipcode", RequestBody.create(MediaType.parse("text/plain"), postalCode));
                    map.put("lat", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(gpsTracker.getLatitude())));
                    map.put("lng", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(gpsTracker.getLongitude())));
                    map.put("year_built", RequestBody.create(MediaType.parse("text/plain"), year_built));
                    map.put("est_repair_cost", RequestBody.create(MediaType.parse("text/plain"), estimated_cost_repair));

                    customProgressDialog.showProgressDialog();
                    Call<ResponseMessageModel> call1 = apiService.addProperty("Bearer" + token, map, image, document, amenitiesSelected);
                    call1.enqueue(new Callback<ResponseMessageModel>() {
                        @Override
                        public void onResponse(Call<ResponseMessageModel> call, Response<ResponseMessageModel> response) {
                            customProgressDialog.dismissProgressDialog();
                            ResponseMessageModel responseMessageModel = response.body();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddPropertyActivity.this, "" + responseMessageModel.msg, Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    private void pickPhoto() {
        FilePickerBuilder.getInstance()
                .setMaxCount(10)
                .setSelectedFiles(photoPaths)
                .setActivityTheme(R.style.ThemeOverlay_AppCompat_Dark)
                .setActivityTitle("Please select media")
                .setImageSizeLimit(5)
                .setVideoSizeLimit(0)
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

    private void checkPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    pickPhoto();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOM_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<Uri> dataList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            if (dataList != null) {
                photoPaths.addAll(dataList);
                for (int i = 0; i < photoPaths.size(); i++) {
                    try {
                        image_path = ContentUriUtils.INSTANCE.getFilePath(AddPropertyActivity.this, photoPaths.get(i));
                        imageFile = new File(image_path);
                        System.out.println("-- image path " + imageFile.getAbsolutePath());
                        Log.d("image_path", "onActivityResult: " + imageFile.getAbsolutePath());

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<Uri> dataList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);
                if (dataList != null) {
                    docPaths.addAll(dataList);
                    try {
                        doc_path = ContentUriUtils.INSTANCE.getFilePath(AddPropertyActivity.this, docPaths.get(0));
                        docFile = new File(doc_path);
                        System.out.println("-- document path " + docFile.getAbsolutePath());
                        Log.d("document_path", "onActivityResult: " + docFile.getAbsolutePath());

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void onPickDoc() {
        FilePickerBuilder.getInstance()
                .setMaxCount(1)
                .setSelectedFiles(docPaths)
                .setActivityTheme(R.style.ThemeOverlay_AppCompat_Dark)
                .setActivityTitle("Please select a file")
                .setImageSizeLimit(5)
                .setVideoSizeLimit(20)
                .enableDocSupport(true)
                .enableSelectAll(true)
                .sortDocumentsBy(SortingTypes.NAME)
                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .pickFile(this);
    }
}