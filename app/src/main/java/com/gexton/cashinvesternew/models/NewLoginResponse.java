package com.gexton.cashinvesternew.models;

import com.google.gson.annotations.SerializedName;

public class NewLoginResponse {
    public boolean status;
    @SerializedName("user")
    public User user;
    @SerializedName("data")
    public Data data;
    public String msg;

    public class Data {
        public String token;
        @SerializedName("user")
        public User user;
    }

    public class User {
        public float id;
        public String first_name;
        public String last_name;
        public String phone_no;
        public String email;
        public String user_role;
        public String address = null;
        public String image_url = null;
        public String cover_image_url = null;
        public String user_name = null;
        public String image = null;
        public String cover_image = null;
        public String description = null;
        public String city = null;
        public String state = null;
        public String zipcode = null;
        public float is_active;
        public String platform;
        public String device_token;
        public String deleted_at = null;
        public String created_at;
        public String updated_at;
    }
}