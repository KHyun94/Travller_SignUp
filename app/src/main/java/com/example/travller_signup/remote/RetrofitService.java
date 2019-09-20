package com.example.travller_signup.remote;

import com.example.travller_signup.models.UserSignup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {
    //SignIn.class 로그인 체크
    @FormUrlEncoded
    @POST("/memorial_point/PHPs/test.php")
    Call<Boolean> goSignUp(@Body UserSignup userSignup);
}
