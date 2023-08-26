package com.jmr.cofindjobsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.fragments.Home
import com.jmr.cofindjobsearch.fragments.Inbox
import com.jmr.cofindjobsearch.fragments.Job
import com.jmr.cofindjobsearch.fragments.Profile
import com.jmr.cofindjobsearch.services.Utils
import me.ibrahimsn.lib.NiceBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var bottomBar: NiceBottomBar

    private val Utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar = findViewById(R.id.bottomBar)

        bottomBar.setActiveItem(0)
        bottomBar.onItemSelected = {
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

        loadFragment(Home.newInstance())
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}