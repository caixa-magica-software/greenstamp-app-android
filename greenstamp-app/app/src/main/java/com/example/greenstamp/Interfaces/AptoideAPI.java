package com.example.greenstamp.Interfaces;

import com.example.greenstamp.Models.ApiResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AptoideAPI {

    @GET("getApp")
    Observable<ApiResponse> getAppDetails(@Query("package_name") String packageName);
}