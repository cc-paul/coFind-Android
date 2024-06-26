package com.jmr.cofindjobsearch.interfaces

import com.jmr.cofindjobsearch.fragments.Profile
import com.jmr.data.ApplicantResponse
import com.jmr.data.BaseResponse
import com.jmr.data.ChangePassSender
import com.jmr.data.ChatResponse
import com.jmr.data.JobHomeResponse
import com.jmr.data.JobResponse
import com.jmr.data.JobSender
import com.jmr.data.LoginResponse
import com.jmr.data.LoginSender
import com.jmr.data.MessageResponse
import com.jmr.data.MessageSender
import com.jmr.data.ProfileResponse
import com.jmr.data.ProfileSender
import com.jmr.data.RegistrationSender
import com.jmr.data.ReviewResponse
import com.jmr.data.ReviewResponseList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GetOTP {
    @GET("users/otp/{email}")
    fun getOTP(@Path("email") email: String ) : Call<BaseResponse>
}

interface GetProfile {
    @GET("users/profile/{user_id}")
    fun getProfile(@Path("user_id") user_id: Int) : Call<ProfileResponse>
}

interface CheckGmail {
    @GET("users/gsignin/{email}")
    fun checkGmail(@Path("email") email: String ) : Call<LoginResponse>
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

interface ChangeProfile {
    @Headers("Content-Type: application/json")
    @POST("profile")
    fun changeProfile(@Body requestBody: ProfileSender) : Call<BaseResponse>
}

interface SaveJob {
    @Headers("Content-Type: application/json")
    @POST("jobs")
    fun saveJob(@Body requestBody: JobSender) : Call<BaseResponse>
}

interface GetJob {
    @Headers("Content-Type: application/json")
    @POST("jobs")
    fun getJob(@Body requestBody: JobSender) : Call<JobResponse>
}

interface GetJobDetails {
    @GET("jobs/job/{id}")
    fun getJobDetails(@Path("id") id:Int) : Call<JobResponse>
}

interface GetJobAll {
    @GET("jobs/job-all/{id}")
    fun getJobAll(@Path("id") id:Int) : Call<JobHomeResponse>
}


interface DeleteJob {
    @GET("jobs/job/delete/{id}")
    fun deleteJob(@Path("id") jobID: Int) : Call<BaseResponse>
}

interface SendMessage {
    @Headers("Content-Type: application/json")
    @POST("message")
    fun sendMessage(@Body requestBody: MessageSender) : Call<BaseResponse>
}

interface LoadMessage {
    @GET("message/my-chat/{receiver_id}/{sender_id}/{is_all}")
    fun loadMessage(@Path("receiver_id") receiverId: Int,@Path("sender_id") senderId: Int,@Path("is_all") isAll: Int) : Call<MessageResponse>
}


interface LoadChat {
    @GET("message/all-chat/{id}/{search}")
    fun loadChat(@Path("id") id: Int,@Path("search") search: String) : Call<ChatResponse>
}

interface LoadApplicants {
    @GET("jobs/view-applicants/{id}/{search}")
    fun loadApplicants(@Path("id") id: Int,@Path("search") search: String) : Call<ApplicantResponse>
}

interface LoadSingleReview {
    @GET("jobs/review/{jobID}/{reviewerID}/{reviewedID}")
    fun loadSingleReview(@Path("jobID") jobID: Int,@Path("reviewerID") reviewerID: Int,@Path("reviewedID") reviewedID: Int) : Call<ReviewResponse>
}


interface LoadReviewList {
    @GET("jobs/view-review/{reviewedID}")
    fun loadReviewList(@Path("reviewedID") reviewedID: Int) : Call<ReviewResponseList>
}