package com.gexton.cashinvesternew.api2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.gexton.cashinvesternew.api.APIClient;
import com.gexton.cashinvesternew.utils.Dialog_CustomProgress;
import com.gexton.cashinvesternew.utils.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import static android.content.Context.MODE_PRIVATE;

public class ApiManager {
    final int DEFAULT_TIMEOUT = 1000000000;
    Activity activity;
    String getOrPost;
    String apiName;
    RequestParams params;
    ApiCallback apiCallback;
    public static final String API_SAVED_SEARCHES = "saved-searches";
    String token;
    Dialog_CustomProgress customProgressDialog;

    public ApiManager(Activity activity, String getOrPost, String apiName, RequestParams params, ApiCallback apiCallback) {
        this.activity = activity;
        this.getOrPost = getOrPost;
        this.apiName = apiName;
        this.params = params;
        this.apiCallback = apiCallback;
        SharedPref.init(activity);
        token = SharedPref.read("token", "");
        customProgressDialog = new Dialog_CustomProgress(activity);

        System.out.println("-- Req URL : " + APIClient.BASE_URL + apiName);
        System.out.println("-- Params : " + params.toString());
    }

    public void loadURL() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.addHeader("Authorization", "Bearer" + token);
        System.out.println("-- request headers : FCM token : " + token);
        client.get(APIClient.BASE_URL + apiName, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        try {
                            String content = new String(responseBody);
                            apiCallback.onApiResponce(statusCode, 1, apiName, content);
                            Log.d("onSuccess", "onSuccess: " + content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        try {
                            String content = new String(responseBody);
                            Log.d("onFailure", "onFailure: " + content);
                            apiCallback.onApiResponce(statusCode, 0, apiName, content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
