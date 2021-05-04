package com.gexton.cashinvesternew.models;

public class SavedSearchesModel {
    public String title;
    public String created_at;
    public String hashid;

    public SavedSearchesModel(String title, String created_at, String hashid) {
        this.title = title;
        this.created_at = created_at;
        this.hashid = hashid;
    }
}