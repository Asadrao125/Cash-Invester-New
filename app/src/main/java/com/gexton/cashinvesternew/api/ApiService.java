package com.gexton.cashinvesternew.api;

import com.gexton.cashinvesternew.models.HomeModel;
import com.gexton.cashinvesternew.models.NewLoginResponse;
import com.gexton.cashinvesternew.models.ResponseMessageModel;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {

    @Multipart
    @POST("auth/register")
    Call<ResponseMessageModel> registerUser(
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("auth/login")
    Call<NewLoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @Multipart
    @POST("auth/update_profile")
    Call<NewLoginResponse> update(
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("auth/change_password")
    Call<ResponseMessageModel> changePassword(
            @Header("Authorization") String authorization,
            @Field("old_password") String old_password,
            @Field("password") String new_password,
            @Field("password_confirmation") String confirm_password
    );

    @POST("auth/logout")
    Call<ResponseMessageModel> logout(
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("auth/forget_password")
    Call<ResponseMessageModel> forgetPassword(
            @Field("email") String email
    );

    @GET("properties/find")
    Call<ResponseBody> getAllProperties(
            @Query("page") String page,
            @Header("Authorization") String authorization
    );

    @GET("properties/view?")
    Call<ResponseBody> getSingleProperty(
            @Query("property_id") String property_id,
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("properties/add_lead")
    Call<ResponseMessageModel> addPropertyLead(
            @Field("description") String description,
            @Field("property_id") String property_id,
            @Header("Authorization") String authorization
    );

    @GET("properties/my_properties")
    Call<ResponseBody> getMyProperties(
            @Query("page") String page,
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("properties/bookmark")
    Call<ResponseMessageModel> bookmarkProperty(
            @Field("property_id") String property_id,
            @Header("Authorization") String authorization
    );

    @GET("properties/bookmarked")
    Call<ResponseBody> getBookmarkedProperties(@Header("Authorization") String authorization);

    @GET("home")
    Call<HomeModel> homeApi(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("saved-searches/delete")
    Call<ResponseMessageModel> deleteSavedSearch(
            @Field("search_id") String searchId,
            @Header("Authorization") String authorization
    );

    @Multipart
    @POST("properties/add")
    Call<ResponseMessageModel> addProperty(
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part document,
            @Part("amenities") ArrayList<String> amenitiesList
    );

}