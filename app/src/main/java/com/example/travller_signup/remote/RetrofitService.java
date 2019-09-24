package com.example.travller_signup.remote;

import com.example.travller_signup.models.UserSignup;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitService {



    //1. 데이터 클래스(받는 이름:UserSignUp) + 이미지(받는 이름:us_profile_img)
/*
    @Multipart
    @POST("/upload")
    Call<Void> goSignUp(@Part("UserSignUp") UserSignup userSignup, @Part MultipartBody.Part imgBody);
*/
    //2. 각 변수 + 이미지(받는 이름:us_profile_img)
    @Multipart
    @POST("/upload")
    Call<Void> goSignUp(@Part("us_email") String us_email,
                           @Part("us_pwd") String us_pwd,
                           @Part("us_nick_name") String us_nick_name,
                           @Part("us_device") String us_device,
                           @Part MultipartBody.Part imgBody);


}
