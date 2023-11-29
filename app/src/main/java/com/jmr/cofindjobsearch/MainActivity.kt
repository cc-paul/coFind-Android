package com.jmr.cofindjobsearch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.jmr.cofindjobsearch.fragments.Home
import com.jmr.cofindjobsearch.fragments.Inbox
import com.jmr.cofindjobsearch.fragments.Job
import com.jmr.cofindjobsearch.fragments.Profile
import com.jmr.cofindjobsearch.services.Utils
import me.ibrahimsn.lib.NiceBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var bottomBar: NiceBottomBar
    private lateinit var rlVideoCall: RelativeLayout
    val utils = Utils()
    var isAlreadyClick = false
    var lastFragment:Fragment = Home.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar = findViewById(R.id.bottomBar)
        rlVideoCall = findViewById(R.id.rlVideoCall)

        bottomBar.setActiveItem(0)

        bottomBar.onItemSelected = {
            if (!isAlreadyClick) {
                isAlreadyClick
                utils.showProgress(this)
                bottomBar.isVisible = false

                Handler(Looper.getMainLooper()).postDelayed({
                    bottomBar.isVisible = true
                }, 200)

                when(it) {
                    0 -> {
                        loadFragment(Home.newInstance())
                    }
                    1 -> {
                        loadFragment(Inbox.newInstance("",""))
                    }
                    2 -> {
                        loadFragment(Job.newInstance())
                    }
                    3 -> {
                        loadFragment(Profile.newInstance())
                    }
                }
            }
        }

        rlVideoCall.setOnClickListener {
            val gotoOtherActivity = Intent(this, OtherActivity::class.java).apply {
                putExtra("COMMAND", "VIDEO_CALL")
            }
            startActivity(gotoOtherActivity)
        }

        bottomBar.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        loadFragment(lastFragment)
    }


    private  fun loadFragment(fragment: Fragment){
        lastFragment = fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()

        Handler(Looper.getMainLooper()).postDelayed({
            bottomBar.isVisible = true
        }, 200)

        Handler(Looper.getMainLooper()).postDelayed({
            utils.closeProgress()
            isAlreadyClick = false
        }, 2000)
    }
}