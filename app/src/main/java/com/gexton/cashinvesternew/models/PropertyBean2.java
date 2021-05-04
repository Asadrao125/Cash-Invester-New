package com.gexton.cashinvesternew.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PropertyBean2 implements Serializable {

    public String id;
    public String user_id;
    public String title;
    public String slug;
    public String description;
    public String type;
    public String status;
    public String price;
    public String area;
    public String rooms;
    public String bathrooms;
    public String garages;
    public String pool;
    public String address;
    public String city;
    public String state;
    public String zipcode;
    public String country;
    public String lat;
    public String lng;
    public String year_built;
    public String est_repair_cost;
    public ArrayList<String> amenities;
    public String approved_at;
    public String approved_by;
    public String deleted_at;
    public String created_at;
    public String updated_at;
    public String property_id;
    public String deleted_by;
    public String views_count;
    public String is_bookmarked;
    public String hashid;
    public Thumbnail thumbnail;

    public static class Thumbnail {
        public String id;
        public String property_id;
        public String image;
        public String thumbnail;
        public String created_at;
        public String updated_at;
        public String image_url;
        public String thumbnail_url;
        public String hashid;

        public Thumbnail(String id, String property_id, String image, String thumbnail, String created_at, String updated_at, String image_url, String thumbnail_url, String hashid) {
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
    }

    public PropertyBean2(String id, String user_id, String title, String slug, String description, String type, String status, String price, String area, String rooms, String bathrooms, String garages, String pool, String address, String city, String state, String zipcode, String country, String lat, String lng, String year_built, String est_repair_cost, ArrayList<String> amenities, String approved_at, String approved_by, String deleted_at, String created_at, String updated_at, String property_id, String deleted_by, String views_count, String is_bookmarked, String hashid, Thumbnail thumbnail) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.type = type;
        this.status = status;
        this.price = price;
        this.area = area;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.garages = garages;
        this.pool = pool;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
        this.lat = lat;
        this.lng = lng;
        this.year_built = year_built;
        this.est_repair_cost = est_repair_cost;
        this.amenities = amenities;
        this.approved_at = approved_at;
        this.approved_by = approved_by;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.property_id = property_id;
        this.deleted_by = deleted_by;
        this.views_count = views_count;
        this.is_bookmarked = is_bookmarked;
        this.hashid = hashid;
        this.thumbnail = thumbnail;
    }
}
