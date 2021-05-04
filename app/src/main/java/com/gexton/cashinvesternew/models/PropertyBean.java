package com.gexton.cashinvesternew.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PropertyBean implements Serializable {

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
    public User user;

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

    public static class User {
        public String id;
        public String first_name;
        public String last_name;
        public String user_name;
        public String phone_no;
        public String image;
        public String cover_image;
        public String description;
        public String email;
        public String user_role;
        public String address;
        public String city;
        public String state;
        public String zipcode;
        public String is_active;
        public String platform;
        public String device_token;
        public String deleted_at;
        public String created_at;
        public String updated_at;
        public String image_url;
        public String cover_image_url;

        public User(String id, String first_name, String last_name, String user_name, String phone_no, String image, String cover_image, String description, String email, String user_role, String address, String city, String state, String zipcode, String is_active, String platform, String device_token, String deleted_at, String created_at, String updated_at, String image_url, String cover_image_url) {
            this.id = id;
            this.first_name = first_name;
            this.last_name = last_name;
            this.user_name = user_name;
            this.phone_no = phone_no;
            this.image = image;
            this.cover_image = cover_image;
            this.description = description;
            this.email = email;
            this.user_role = user_role;
            this.address = address;
            this.city = city;
            this.state = state;
            this.zipcode = zipcode;
            this.is_active = is_active;
            this.platform = platform;
            this.device_token = device_token;
            this.deleted_at = deleted_at;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.image_url = image_url;
            this.cover_image_url = cover_image_url;
        }
    }

    public PropertyBean(String id, String user_id, String title, String slug, String description, String type, String status, String price, String area, String rooms, String bathrooms, String garages, String pool, String address, String city, String state, String zipcode, String country, String lat, String lng, String year_built, String est_repair_cost, ArrayList<String> amenities, String approved_at, String approved_by, String deleted_at, String created_at, String updated_at, String property_id, String deleted_by, String views_count, String is_bookmarked, String hashid, Thumbnail thumbnail, User user) {
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
        this.user = user;
    }
}
