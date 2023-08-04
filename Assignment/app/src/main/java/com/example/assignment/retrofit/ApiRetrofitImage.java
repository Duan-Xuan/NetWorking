package com.example.assignment.retrofit;

import com.example.assignment.models.ImageNasa;
import com.example.assignment.models.Status;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiRetrofitImage {

    //Sử dụng Retrofit trỏ api Server
    ApiRetrofitImage apiRetrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.101:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiRetrofitImage.class);

    //Sử dung phương thức GET để lấy dữ liệu
    @GET("get-image")
    Call<Status> getNasaUrl();

    //Sử dung phương thức Post để đẩy dữ liệu
    @POST("upload")
    Call<Status> postNasaUrl(@Body ImageNasa imageNasa);

}
