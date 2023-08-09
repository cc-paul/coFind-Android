package com.jmr.cofindjobsearch

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jmr.cofindjobsearch.databinding.ActivityOtherBinding
import com.jmr.cofindjobsearch.fragments.Profile_Details
import com.jmr.cofindjobsearch.fragments.Update_Password
import com.jmr.cofindjobsearch.helper.SharedHelper

class OtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        val command = intent.getStringExtra("COMMAND")

        when(command) {
            "PASSWORD" -> {
                loadFragment(Update_Password.newInstance(SharedHelper.getInt("user_id")))
            }
            "DETAILS" -> {
                loadFragment(Profile_Details.newInstance(SharedHelper.getInt("user_id")))
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}