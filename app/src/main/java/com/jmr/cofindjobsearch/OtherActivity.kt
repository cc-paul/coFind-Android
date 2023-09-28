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
import com.jmr.cofindjobsearch.fragments.Apply_Job
import com.jmr.cofindjobsearch.fragments.Create_Job
import com.jmr.cofindjobsearch.fragments.Messaging
import com.jmr.cofindjobsearch.fragments.Profile_Details
import com.jmr.cofindjobsearch.fragments.Update_Password
import com.jmr.cofindjobsearch.fragments.Veiw_Document
import com.jmr.cofindjobsearch.fragments.View_Applicants
import com.jmr.cofindjobsearch.helper.SharedHelper

class OtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        when(intent.getStringExtra("COMMAND")) {
            "PASSWORD" -> {
                loadFragment(Update_Password.newInstance(SharedHelper.getInt("user_id")))
            }
            "DETAILS" -> {
                loadFragment(Profile_Details.newInstance(SharedHelper.getInt("user_id")))
            }
            "NEW_JOB" -> {
                loadFragment(Create_Job.newInstance())
            }
            "JOB_DETAILS" -> {
                loadFragment(Apply_Job.newInstance(SharedHelper.getInt("job_id")))
            }
            "MESSAGE" -> {
                loadFragment(Messaging.newInstance(
                    SharedHelper.getInt("receiver_id"),
                    SharedHelper.getString("message_name"),
                    SharedHelper.getString("message_image_link")
                ))
            }
            "VIEW_APPLICANT" -> {
                loadFragment(View_Applicants.newInstance(SharedHelper.getInt("job_id")))
            }
            "VIEW_RESUME" -> {
                loadFragment(Veiw_Document.newInstance(SharedHelper.getString("resume_link")))
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}