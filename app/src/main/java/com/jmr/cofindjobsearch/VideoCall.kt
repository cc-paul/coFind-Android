package com.jmr.cofindjobsearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment

class VideoCall : AppCompatActivity() {
    private lateinit var tvMeetingID:TextView
    private lateinit var imgShare:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        tvMeetingID = findViewById(R.id.tvMeetingID)
        imgShare = findViewById(R.id.imgShare)

        tvMeetingID.text = "Meeting ID : " + SharedHelper.getString("meetingID","")

        imgShare.setOnClickListener {
            SharedHelper.putString("meetingMessage","You are invited in a meeting. Please use this meeting ID below \n\n" + SharedHelper.getString("meetingID",""))

            val gotoOtherActivity = Intent(this, OtherActivity::class.java).apply {
                putExtra("COMMAND", "MESSAGE_MAIN")
            }
            startActivity(gotoOtherActivity)
        }

        addFragment()
    }

    override fun onResume() {
        super.onResume()

        SharedHelper.putString("meetingMessage","")
    }

    private fun addFragment() {
        var appID: Long = 354090463
        var appSign = "275119d56650772cbef5c08d143e41f6717330c75061056affb4a235c55de098"

        var zegoConfig = ZegoUIKitPrebuiltVideoConferenceConfig()
        var zegoFragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(
            appID,
            appSign,
            SharedHelper.getString("meetingUserID",""),
            SharedHelper.getString("meetingName",""),
            SharedHelper.getString("meetingID",""),
            zegoConfig
        )

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,zegoFragment).commitNow()
    }
}