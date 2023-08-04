package com.example.assignment.retrofit;

import com.example.assignment.models.ImageNasa;
import com.example.assignment.models.Status;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiRetrofitImage {

    //Sử dụng Retrofit trỏ api Server
    ApiRetrofitImage apiRetrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.102:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiRetrofitImage.class);

    //Sử dung phương thức GET để lấy dữ liệu
    @GET("get-image")
    Call<Status> getNasaUrl();

    //Sử dung phương thức Post để đẩy dữ liệu
    @POST("upload")
    Call<Status> postNasaUrl(@Body ImageNasa imageNasa);

    //Sử dung phương thức PUT để sửa dữ liệu
    @PUT("update-image")
    Call<Status> updateNasaUrl(@Body ImageNasa imageNasa);

    //Sử dung phương thức DELETE để xóa dữ liệu
    @DELETE("delete-image/{id}")
    Call<Status> deleteNasaUrl(@Path("id") int id);

}
