package com.gexton.cashinvesternew.activities;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.gps.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    GPSTracker gpsTracker;
    GoogleMap mMap;
    ImageView imgBack;
    Button btnChooseThisLocation;
    Marker marker;
    String address, state, zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gpsTracker = new GPSTracker(this);
        imgBack = findViewById(R.id.imgBack);
        btnChooseThisLocation = findViewById(R.id.btnChooseThisLocation);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnChooseThisLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        if (gpsTracker.canGetLocation()) {
            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
                    .title("Title")
                    .snippet("Snippet"));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 15.0f));
            setInfoWindow(gpsTracker.getLatitude(), gpsTracker.getLongitude());

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    marker.remove();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Title")
                            .snippet("Snippet"));
                    setInfoWindow(latLng.latitude, latLng.longitude);
                    marker.showInfoWindow();
                }
            });
            marker.showInfoWindow();
        }
    }

    private void setInfoWindow(double latitude, double longitude) {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.marker_layout, null);
                TextView tvAddress = v.findViewById(R.id.tvAddress);
                TextView tvCity = v.findViewById(R.id.tvCity);
                TextView tvState = v.findViewById(R.id.tvState);
                TextView tvZipcode = v.findViewById(R.id.tvZipcode);

                List<Address> addresses;
                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    zipcode = addresses.get(0).getPostalCode();
                    tvAddress.setText(address);
                    tvCity.setText(city);
                    tvState.setText(state);
                    tvZipcode.setText(zipcode);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return v;
            }
        });

        btnChooseThisLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), AddPropertyActivity.class);
        intent.putExtra("a", address);
        intent.putExtra("s", state);
        intent.putExtra("z", zipcode);
        intent.putExtra("p", 1);
        startActivity(intent);
    }
}