package com.jmr.cofindjobsearch.fragments

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.jmr.cofindjobsearch.OtherActivity
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.VideoCall
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import java.util.UUID
import kotlin.random.Random


class VideoStart : Fragment() {
    private lateinit var viewVideoCall: View
    private lateinit var etUserID: EditText
    private lateinit var etMeetingID: EditText
    private lateinit var lnCreateMeeting : LinearLayout
    private lateinit var lnStartCalling: LinearLayout

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewVideoCall = inflater.inflate(R.layout.fragment_video_start, container, false)

        viewVideoCall.apply {
            etUserID = findViewById(R.id.etUserID)
            lnStartCalling = findViewById(R.id.lnStartCalling)
            lnCreateMeeting = findViewById(R.id.lnCreateMeeting)
            etMeetingID = findViewById(R.id.etMeetingID)
        }

        loadPersonalInfo()

        lnStartCalling.setOnClickListener {
            var meetingID = etMeetingID.text.toString()
            var userID = etUserID.text.toString()

            if (userID.isEmpty() || meetingID.length != 10 || meetingID.isEmpty()) {
                Utils.showToastMessage(requireContext(),"Please provide Your Name and Meeting ID")
                return@setOnClickListener
            }

            startMeeting(meetingID,userID)
        }

        lnCreateMeeting.setOnClickListener {
            var userID = etUserID.text.toString()

            if (userID.isEmpty()) {
                Utils.showToastMessage(requireContext(),"Please provide your Name")
                return@setOnClickListener
            }


            startMeeting(generateMeetingID(),userID)
        }

        return viewVideoCall
    }

    private fun loadPersonalInfo() {
        try {
            if (!Utils.hasInternet(this.requireContext())) {
                return
            }

            apiService.getProfileDetails(SharedHelper.getInt("user_id")) {
                if (it!!.success) {
                    it.data.apply {
                        etUserID.setText("$lastName, $firstName $middleName")
                    }
                }

                Utils.closeProgress()
            }
        } catch (e:Exception) {

        }
    }

    private fun startMeeting(meetingID:String,name: String) {
        var userID = UUID.randomUUID().toString()

        SharedHelper.apply {
            putString("meetingID",meetingID)
            putString("meetingName",name)
            putString("meetingUserID",userID)
        }

        val gotoVideoCall = Intent(requireContext(), VideoCall::class.java)
        startActivity(gotoVideoCall)
    }

    private fun generateMeetingID() : String {
        val stringBuilder = StringBuilder()

        repeat(10) {
            val randomDigit = Random.nextInt(10) // Generate a random digit between 0 and 9
            stringBuilder.append(randomDigit)
        }

        return stringBuilder.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance() = VideoStart()
    }
}