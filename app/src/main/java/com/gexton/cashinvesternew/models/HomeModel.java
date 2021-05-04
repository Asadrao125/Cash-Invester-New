package com.gexton.cashinvesternew.models;

import java.util.ArrayList;

public class HomeModel {

    public String status;
    public Data data;
    public String msg;

    public class Data {
        public String total_searches;
        public String total_viewed;
        public String total_bookmarked;
        ArrayList<String> notifications = new ArrayList<>();
    }

}
