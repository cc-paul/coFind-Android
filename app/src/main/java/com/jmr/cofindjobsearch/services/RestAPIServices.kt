package com.jmr.cofindjobsearch.services

import android.util.Log
import com.google.gson.Gson
import com.jmr.cofindjobsearch.helper.RetrofitHelper
import com.jmr.cofindjobsearch.interfaces.AccountLogin
import com.jmr.cofindjobsearch.interfaces.AccountRegistration
import com.jmr.cofindjobsearch.interfaces.ChangePassword
import com.jmr.cofindjobsearch.interfaces.ChangeProfile
import com.jmr.cofindjobsearch.interfaces.CheckGmail
import com.jmr.cofindjobsearch.interfaces.DeleteJob
import com.jmr.cofindjobsearch.interfaces.GetJob
import com.jmr.cofindjobsearch.interfaces.GetJobAll
import com.jmr.cofindjobsearch.interfaces.GetJobDetails
import com.jmr.cofindjobsearch.interfaces.GetOTP
import com.jmr.cofindjobsearch.interfaces.GetProfile
import com.jmr.cofindjobsearch.interfaces.LoadApplicants
import com.jmr.cofindjobsearch.interfaces.LoadChat
import com.jmr.cofindjobsearch.interfaces.LoadMessage
import com.jmr.cofindjobsearch.interfaces.SaveJob
import com.jmr.cofindjobsearch.interfaces.SendMessage
import com.jmr.cofindjobsearch.interfaces.VerifyAccount
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class RestAPIServices {
    fun getOTP(emailAddress:String, onResult: (BaseResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(GetOTP::class.java)

        retrofit.getOTP(emailAddress).enqueue(
            object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    var otp = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        otp = gson.fromJson(response.errorBody()!!.string(), BaseResponse::class.java)
                    }

                    onResult(otp)
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun getProfileDetails(user_id:Int, onResult: (ProfileResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(GetProfile::class.java)

        Log.e("User ID Response: ", user_id.toString())

        retrofit.getProfile(user_id).enqueue(
            object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    var profile = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        profile = gson.fromJson(response.errorBody()!!.string(), ProfileResponse::class.java)
                    }

                    onResult(profile)
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun checkGmail(emailAddress:String, onResult: (LoginResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(CheckGmail::class.java)

        retrofit.checkGmail(emailAddress).enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    var gmail = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        gmail = gson.fromJson(response.errorBody()!!.string(), LoginResponse::class.java)
                    }

                    onResult(gmail)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun verifyAccount(emailAddress:String, onResult: (BaseResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(VerifyAccount::class.java)

        retrofit.verifyAccount(emailAddress).enqueue(
            object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    var verifyAccount = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        verifyAccount = gson.fromJson(response.errorBody()!!.string(), BaseResponse::class.java)
                    }

                    onResult(verifyAccount)
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun accountRegistration(registrationSender: RegistrationSender, onResult: (BaseResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(AccountRegistration::class.java)

        retrofit.saveAccount(registrationSender).enqueue(
            object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    var account = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        account = gson.fromJson(response.errorBody()!!.string(), BaseResponse::class.java)
                    }

                    onResult(account)
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun loginAccount(loginSender: LoginSender , onResult: (LoginResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(AccountLogin::class.java)

        retrofit.loginAccount(loginSender).enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    var login = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        login = gson.fromJson(response.errorBody()!!.string(), LoginResponse::class.java)
                    }

                    onResult(login)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun changePassword(chagePassSender: ChangePassSender , onResult: (BaseResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(ChangePassword::class.java)

        retrofit.changePassword(chagePassSender).enqueue(
            object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    var changepass = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        changepass = gson.fromJson(response.errorBody()!!.string(), BaseResponse::class.java)
                    }

                    onResult(changepass)
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun changeProfile(profileSender: ProfileSender , onResult: (BaseResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(ChangeProfile::class.java)

        retrofit.changeProfile(profileSender).enqueue(
            object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    var profile = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        profile = gson.fromJson(response.errorBody()!!.string(), BaseResponse::class.java)
                    }

                    onResult(profile)
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun saveJob(jobSender: JobSender , onResult: (BaseResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(SaveJob::class.java)

        retrofit.saveJob(jobSender).enqueue(
            object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    var job = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        job = gson.fromJson(response.errorBody()!!.string(), BaseResponse::class.java)
                    }

                    onResult(job)
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun getJob(jobSender: JobSender , onResult: (JobResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(GetJob::class.java)

        retrofit.getJob(jobSender).enqueue(
            object : Callback<JobResponse> {
                override fun onResponse(call: Call<JobResponse>, response: Response<JobResponse>) {
                    var job = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        job = gson.fromJson(response.errorBody()!!.string(), JobResponse::class.java)
                    }

                    onResult(job)
                }

                override fun onFailure(call: Call<JobResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun getJobDetails(id: Int , onResult: (JobResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(GetJobDetails::class.java)

        retrofit.getJobDetails(id).enqueue(
            object : Callback<JobResponse> {
                override fun onResponse(call: Call<JobResponse>, response: Response<JobResponse>) {
                    var job = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        job = gson.fromJson(response.errorBody()!!.string(), JobResponse::class.java)
                    }

                    onResult(job)
                }

                override fun onFailure(call: Call<JobResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun getJobAll(applicantID: Int,onResult: (JobHomeResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(GetJobAll::class.java)

        retrofit.getJobAll(applicantID).enqueue(
            object : Callback<JobHomeResponse> {
                override fun onResponse(call: Call<JobHomeResponse>, response: Response<JobHomeResponse>) {
                    var job = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        job = gson.fromJson(response.errorBody()!!.string(), JobHomeResponse::class.java)
                    }

                    onResult(job)
                }

                override fun onFailure(call: Call<JobHomeResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun deleteJob(jobID: Int,onResult: (BaseResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(DeleteJob::class.java)

        retrofit.deleteJob(jobID).enqueue(
            object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    var job = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        job = gson.fromJson(response.errorBody()!!.string(), BaseResponse::class.java)
                    }

                    onResult(job)
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun sendMessage(messageSender: MessageSender, onResult: (BaseResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(SendMessage::class.java)

        retrofit.sendMessage(messageSender).enqueue(
            object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    var message = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        message = gson.fromJson(response.errorBody()!!.string(), BaseResponse::class.java)
                    }

                    onResult(message)
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun loadMessage(receiverId:Int,senderId:Int,isAll:Int, onResult: (MessageResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(LoadMessage::class.java)

        retrofit.loadMessage(receiverId,senderId,isAll).enqueue(
            object : Callback<MessageResponse> {
                override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                    var message = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        message = gson.fromJson(response.errorBody()!!.string(), MessageResponse::class.java)
                    }

                    onResult(message)
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun loadChat(id:Int,search:String, onResult: (ChatResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(LoadChat::class.java)

        retrofit.loadChat(id,search).enqueue(
            object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    var chat = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        chat = gson.fromJson(response.errorBody()!!.string(), ChatResponse::class.java)
                    }

                    onResult(chat)
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun loadApplicant(id:Int,search:String, onResult: (ApplicantResponse?) -> Unit) {
        val retrofit = RetrofitHelper.buildService(LoadApplicants::class.java)

        retrofit.loadApplicants(id,search).enqueue(
            object : Callback<ApplicantResponse> {
                override fun onResponse(call: Call<ApplicantResponse>, response: Response<ApplicantResponse>) {
                    var applicants = response.body()

                    Log.e("Applicant Test",applicants.toString())

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        applicants = gson.fromJson(response.errorBody()!!.string(), ApplicantResponse::class.java)
                    }

                    onResult(applicants)
                }

                override fun onFailure(call: Call<ApplicantResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }
}