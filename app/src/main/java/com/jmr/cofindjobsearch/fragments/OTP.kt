package com.jmr.cofindjobsearch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OTP : Fragment() {
    private lateinit var viewOTP: View
    private lateinit var lnOTP: ConstraintLayout
    private lateinit var lnBack: LinearLayout
    private lateinit var etOTP: EditText
    private lateinit var lnRegister: LinearLayout

    private val Utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOTP = inflater.inflate(R.layout.fragment_otp, container, false)

        val apiService = RestAPIServices()

        viewOTP.apply {
            lnOTP = findViewById(R.id.lnOTP)
            lnBack = findViewById(R.id.lnBack)
            etOTP = findViewById(R.id.etOTP)
            lnRegister = findViewById(R.id.lnRegister)
        }

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lnRegister.setOnClickListener {
            if (SharedHelper.getString("otp") != etOTP.text.toString()) {
                Utils.showSnackMessage(lnOTP,"Incorrect OTP")
            } else {
                Utils.showProgress(requireContext())

                apiService.verifyAccount(
                    emailAddress = SharedHelper.getString("emailAddress")
                ) { it ->
                    Utils.closeProgress()
                    Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())

                    if (it!!.success) {
                        lnBack.performClick()
                    }
                }
            }
        }

        return viewOTP
    }
}