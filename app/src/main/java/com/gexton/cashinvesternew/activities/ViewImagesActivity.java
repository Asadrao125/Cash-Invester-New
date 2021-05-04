package com.gexton.cashinvesternew.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.adapters.RvViewImagesAdapter;
import com.gexton.cashinvesternew.adapters.SliderAdapterExample;

import java.util.ArrayList;

public class ViewImagesActivity extends AppCompatActivity {
    RecyclerView rvImages;
    RvViewImagesAdapter adapter;
    ArrayList<String> urlList = new ArrayList<>();
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(ViewImagesActivity.this);
        setContentView(R.layout.activity_view_images);

        pos = getIntent().getIntExtra("pos", 10000);

        rvImages = findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImages.setHasFixedSize(true);
        for (int i = 0; i < SliderAdapterExample.mSliderItems.size(); i++) {
            urlList.add(SliderAdapterExample.mSliderItems.get(i).image_url);
        }

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvImages);
        rvImages.smoothScrollToPosition(pos);
        adapter = new RvViewImagesAdapter(this, urlList);
        rvImages.setAdapter(adapter);

    }
}