package com.gexton.cashinvesternew.api2;

public interface ApiCallback {

    public void onApiResponce(int httpStatusCode, int successOrFail, String apiName, String apiResponce);

}