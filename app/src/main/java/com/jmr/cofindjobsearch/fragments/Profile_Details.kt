package com.jmr.cofindjobsearch.fragments

import android.R.attr.data
import android.R.attr.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.ProfileSender


class Profile_Details : Fragment() {
    private lateinit var viewChangeDetails: View
    private lateinit var lnBack: LinearLayout
    private lateinit var etFirstName: EditText
    private lateinit var etMiddleName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etMobileNumber: EditText
    private lateinit var etAddress: EditText
    private lateinit var lnRegister: LinearLayout
    private lateinit var lnProfileDetails: LinearLayout

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
        viewChangeDetails = inflater.inflate(R.layout.fragment_profile_details, container, false)

        viewChangeDetails.apply {
            lnBack = findViewById(R.id.lnBack)
            etFirstName = findViewById(R.id.etFirstName)
            etMiddleName = findViewById(R.id.etMiddleName)
            etLastName = findViewById(R.id.etLastName)
            etMobileNumber = findViewById(R.id.etMobileNumber)
            etAddress = findViewById(R.id.etAddress)
            lnRegister = findViewById(R.id.lnRegister)
            lnProfileDetails = findViewById(R.id.lnProfileDetails)
        }

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lnRegister.setOnClickListener {
            if (!Utils.hasInternet(this.requireContext())) {
                Utils.showSnackMessage(lnProfileDetails,"Please check your internet connection")
                return@setOnClickListener
            }

            Utils.showProgress(requireContext())

            val profileInfo = ProfileSender(
                command = "PROFILE_DETAILS",
                user_id = SharedHelper.getInt("user_id"),
                firstName = etFirstName.text.toString(),
                middleName = etMiddleName.text.toString(),
                lastName = etLastName.text.toString(),
                mobileNumber = etMobileNumber.text.toString(),
                address = etAddress.text.toString()
            )

            apiService.changeProfile(profileInfo) {
                Utils.closeProgress()

                if (it!!.success) {
                    Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())
                    lnBack.performClick()
                } else {
                    Utils.showSnackMessage(lnProfileDetails,it?.messages?.get(0).toString())
                }
            }
        }

        loadPersonalInfo()

        return viewChangeDetails
    }

    private fun loadPersonalInfo() {
        if (!Utils.hasInternet(this.requireContext())) {
            Utils.showSnackMessage(lnProfileDetails,"Please check your internet connection")
            return
        }

        Utils.showProgress(requireContext())

        apiService.getProfileDetails(userID!!.toInt()) {
            Utils.closeProgress()

            if (it!!.success) {
                it.data.apply {
                    etFirstName.setText(firstName)
                    etMiddleName.setText(middleName)
                    etLastName.setText(lastName)
                    etMobileNumber.setText(mobileNumber)
                    etAddress.setText(address)
                }
            }
        }
    }

    companion object {
        private const val USER_ID = "user_id"

        @JvmStatic
        fun newInstance(userID: Int) =
            Profile_Details().apply {
                arguments = Bundle().apply {
                    putInt(USER_ID, userID)
                }
            }
    }
}