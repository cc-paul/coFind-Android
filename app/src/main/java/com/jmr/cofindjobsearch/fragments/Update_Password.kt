package com.jmr.cofindjobsearch.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.ChangePassSender
import com.jmr.data.ProfileSender
import kotlin.math.ln

class Update_Password : Fragment() {
    private lateinit var viewUpdatePassword: View
    private lateinit var lnBack: LinearLayout
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var lnChangePassword: LinearLayout
    private lateinit var lnForgotPass: LinearLayout

    private var userID: Int? = null
    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getInt(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewUpdatePassword = inflater.inflate(R.layout.fragment_update_password, container, false)

        viewUpdatePassword.apply {
            lnBack = findViewById(R.id.lnBack)
            etNewPassword = findViewById(R.id.etNewPassword)
            etConfirmPassword = findViewById(R.id.etConfirmPassword)
            lnChangePassword = findViewById(R.id.lnChangePassword)
            lnForgotPass = findViewById(R.id.lnForgotPass)
        }

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lnChangePassword.setOnClickListener {
            if (!Utils.hasInternet(this.requireContext())) {
                Utils.showSnackMessage(lnForgotPass,"Please check your internet connection")
                return@setOnClickListener
            }

            if (etConfirmPassword.text.toString().trim().isEmpty() || etNewPassword.text.toString().trim().isEmpty()) {
                Utils.showSnackMessage(lnForgotPass,"Please fill in required fields")
                return@setOnClickListener
            }

            if (etNewPassword.text.toString() != etConfirmPassword.text.toString()) {
                Utils.showSnackMessage(lnForgotPass,"Passwords are not the same")
                return@setOnClickListener
            }


            Utils.showProgress(viewUpdatePassword.context)

            val profileInfo = ProfileSender(
                command = "CHANGE_PASSWORD",
                user_id = SharedHelper.getInt("user_id"),
                password = etNewPassword.text.toString()
            )

            apiService.changeProfile(profileInfo) {
                Utils.closeProgress()

                if (it!!.success) {
                    Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())
                    lnBack.performClick()
                } else {
                    var errorMessage = if (it.data != null) {
                        it.data.joinToString("\n")
                    } else {
                        it?.messages?.get(0).toString()
                    }

                    Utils.showSnackMessage(lnForgotPass,errorMessage)
                }
            }
        }

        return viewUpdatePassword
    }

    companion object {
        private const val USER_ID = "user_id"

        @JvmStatic
        fun newInstance(userID: Int) =
            Update_Password().apply {
                arguments = Bundle().apply {
                    putInt(USER_ID, userID)
                }
            }
    }
}