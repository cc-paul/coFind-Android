package com.jmr.cofindjobsearch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.dynamic.IFragmentWrapper
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.ChangePassSender


class Change_Password : Fragment() {
    private lateinit var changePassView: View
    private lateinit var lnBack: LinearLayout
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etOTP: EditText
    private lateinit var lnChangePassword: LinearLayout
    private lateinit var lnForgotPass: LinearLayout

    private var otp: String? = null
    private var emailAddress: String? = null

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            otp = it.getString(OTP)
            emailAddress = it.getString(EMAIL_ADDRESS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changePassView = inflater.inflate(R.layout.fragment_change__password, container, false)

        changePassView.apply {
            lnBack = findViewById(R.id.lnBack)
            etNewPassword = findViewById(R.id.etNewPassword)
            etConfirmPassword = findViewById(R.id.etConfirmPassword)
            etOTP = findViewById(R.id.etOTP)
            lnChangePassword = findViewById(R.id.lnChangePassword)
            lnForgotPass = findViewById(R.id.lnForgotPass)
        }

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lnChangePassword.setOnClickListener {
            if (etConfirmPassword.text.toString().trim().isEmpty() || etNewPassword.text.toString().trim().isEmpty() ||
                    etOTP.text.toString().trim().isEmpty()) {

                Utils.showSnackMessage(lnForgotPass,"Please fill in required fields")
                return@setOnClickListener
            }

            if (etNewPassword.text.toString() != etConfirmPassword.text.toString()) {
                Utils.showSnackMessage(lnForgotPass,"Passwords are not the same")
                return@setOnClickListener
            }

            if (etOTP.text.toString() != otp.toString()) {
                Utils.showSnackMessage(lnForgotPass,"OTP is Incorrect")
                return@setOnClickListener
            }

            Utils.showProgress(changePassView.context)

            val changePassInfo = ChangePassSender(
                emailAddress = emailAddress.toString(),
                password = etNewPassword.text.toString()
            )

            apiService.changePassword(changePassInfo) {
                Utils.closeProgress()
                Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())

                if (it!!.success) {
                    lnBack.performClick()
                }
            }
        }

        return changePassView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {
        private const val OTP = "otp"
        private const val EMAIL_ADDRESS = "email_address"

        @JvmStatic
        fun newInstance(otp: String,emailAddress: String) =
            Change_Password().apply {
                arguments = Bundle().apply {
                    putString(OTP, otp)
                    putString(EMAIL_ADDRESS, emailAddress)
                }
            }
    }
}