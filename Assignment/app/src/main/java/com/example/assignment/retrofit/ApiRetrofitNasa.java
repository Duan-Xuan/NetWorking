package com.example.assignment.retrofit;

import com.example.assignment.models.ImageNasa;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRetrofitNasa {

    //Sử dụng Retrofit trỏ api Nasa
    ApiRetrofitNasa apiRetrofit = new Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiRetrofitNasa.class);

    //Sử dung phương thức Get để lấy dữ liệu
    @GET("planetary/apod")
    Call<ImageNasa> getNasaUrl(@Query("api_key") String api_key,
                               @Query("date") String date);

}
