package com.jmr.cofindjobsearch.interfaces

import com.jmr.data.BaseResponse
import com.jmr.data.ChangePassSender
import com.jmr.data.LoginResponse
import com.jmr.data.LoginSender
import com.jmr.data.RegistrationSender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GetOTP {
    @GET("users/otp/{email}")
    fun getOTP(@Path("email") email: String ) : Call<BaseResponse>
}

interface CheckGmail {
    @GET("users/gsignin/{email}")
    fun checkGmail(@Path("email") email: String ) : Call<BaseResponse>
}

interface VerifyAccount {
    @GET("users/verify/{email}")
    fun verifyAccount(@Path("email") email: String ) : Call<BaseResponse>
}

interface AccountRegistration {
    @Headers("Content-Type: application/json")
    @POST("users")
    fun saveAccount(@Body requestBody: RegistrationSender) : Call<BaseResponse>
}

interface AccountLogin {
    @Headers("Content-Type: application/json")
    @POST("sessions")
    fun loginAccount(@Body requestBody: LoginSender) : Call<LoginResponse>
}

interface ChangePassword {
    @Headers("Content-Type: application/json")
    @PATCH("users")
    fun changePassword(@Body requestBody: ChangePassSender) : Call<BaseResponse>
}