package com.gexton.cashinvesternew.models;

public class SliderModel {
    public String id;
    public String property_id;
    public String image;
    public String thumbnail;
    public String created_at;
    public String updated_at;
    public String image_url;
    public String thumbnail_url;
    public String hashid;

    public SliderModel(String id, String property_id, String image, String thumbnail, String created_at, String updated_at, String image_url, String thumbnail_url, String hashid) {
        this.id = id;
        this.property_id = property_id;
        this.image = image;
        this.thumbnail = thumbnail;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.image_url = image_url;
        this.thumbnail_url = thumbnail_url;
        this.hashid = hashid;
    }

    public SliderModel() {
    }
}
