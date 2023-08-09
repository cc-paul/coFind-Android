package com.jmr.cofindjobsearch.services

import com.google.gson.Gson
import com.jmr.cofindjobsearch.helper.RetrofitHelper
import com.jmr.cofindjobsearch.interfaces.AccountLogin
import com.jmr.cofindjobsearch.interfaces.AccountRegistration
import com.jmr.cofindjobsearch.interfaces.ChangePassword
import com.jmr.cofindjobsearch.interfaces.ChangeProfile
import com.jmr.cofindjobsearch.interfaces.CheckGmail
import com.jmr.cofindjobsearch.interfaces.GetOTP
import com.jmr.cofindjobsearch.interfaces.GetProfile
import com.jmr.cofindjobsearch.interfaces.VerifyAccount
import com.jmr.data.BaseResponse
import com.jmr.data.ChangePassSender
import com.jmr.data.LoginResponse
import com.jmr.data.LoginSender
import com.jmr.data.ProfileResponse
import com.jmr.data.ProfileSender
import com.jmr.data.RegistrationSender
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
}