package com.jmr.cofindjobsearch.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmr.cofindjobsearch.OtherActivity
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.recycleview.JobAdapter
import com.jmr.cofindjobsearch.recycleview.JobData
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender

class Job : Fragment() {
    private lateinit var viewJob: View
    private lateinit var etSearch: EditText
    private lateinit var imgAdd: ImageView
    private lateinit var lnPosted: LinearLayout
    private lateinit var tvPosted: TextView
    private lateinit var lnApplied: LinearLayout
    private lateinit var tvApplied: TextView
    private lateinit var lnActive: LinearLayout
    private lateinit var tvActive: TextView
    private lateinit var lnCompleted: LinearLayout
    private lateinit var tvCompleted: TextView
    private lateinit var rvJobs: RecyclerView
    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    private var jobAdapter: JobAdapter? = null
    private val jobList  = ArrayList<JobData>()
    val postedAddedJobs = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewJob = inflater.inflate(R.layout.fragment_job, container, false)

        viewJob.apply {
            etSearch = findViewById(R.id.etSearch)
            imgAdd = findViewById(R.id.imgAdd)
            lnPosted = findViewById(R.id.lnPosted)
            tvPosted = findViewById(R.id.tvPosted)
            lnApplied = findViewById(R.id.lnApplied)
            tvApplied = findViewById(R.id.tvApplied)
            lnActive = findViewById(R.id.lnActive)
            tvActive = findViewById(R.id.tvActive)
            lnCompleted = findViewById(R.id.lnCompleted)
            tvCompleted = findViewById(R.id.tvCompleted)
            rvJobs = findViewById(R.id.rvJobs)
        }

        imgAdd.setOnClickListener {
            SharedHelper.apply {
                putInt("job_id",0)
                putString("job_command","SAVE_JOB")
            }

            val gotoOtherActivity = Intent(requireContext(), OtherActivity::class.java).apply {
                putExtra("COMMAND", "NEW_JOB")
            }
            startActivity(gotoOtherActivity)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                //
            }
        })

        setupButtonListener()
        return viewJob
    }

    override fun onResume() {
        super.onResume()

        SharedHelper.putString("job_command","SEARCH_POST")
        loadJobs()
    }

    private fun loadJobs() {
        try {
            jobList.clear()
            jobAdapter?.notifyDataSetChanged()
            postedAddedJobs.clear()

            val jobInfo = JobSender(
                command = SharedHelper.getString("job_command"),
                jobTitle = etSearch.text.toString(),
                createdBy = SharedHelper.getInt("user_id")
            )

            apiService.getJob(jobInfo) {response ->
                if (response!!.success) {
                    val data = response.data

                    if (data.rows_returned != 0) {
                        val jobs = data.jobs

                        jobs.forEach { job ->
                            if (!postedAddedJobs.contains(job.id)) {
                                jobList.add(JobData(
                                    job.id,
                                    job.jobTitle,
                                    job.requirementsList,
                                    job.address,
                                    job.f_dateCreated
                                ))
                                postedAddedJobs.add(job.id)
                            }
                        }

                        jobAdapter = JobAdapter(jobList)
                        rvJobs.layoutManager = LinearLayoutManager(requireContext())
                        rvJobs.adapter = jobAdapter
                    }
                }
            }
        } catch (e:Exception) {
            Log.e("Post Error",e.message.toString())
        }
    }

    private fun setupButtonListener() {
        lnPosted.setOnClickListener {
            lnPosted.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.orange)
            tvPosted.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.white))

            lnApplied.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvApplied.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnActive.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvActive.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnCompleted.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvCompleted.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            SharedHelper.putString("job_command","SEARCH_POST")
            loadJobs()
        }

        lnApplied.setOnClickListener {
            lnPosted.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvPosted.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnApplied.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.orange)
            tvApplied.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.white))

            lnActive.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvActive.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnCompleted.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvCompleted.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))
        }

        lnActive.setOnClickListener {
            lnPosted.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvPosted.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnApplied.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvApplied.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnActive.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.orange)
            tvActive.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.white))

            lnCompleted.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvCompleted.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))
        }

        lnCompleted.setOnClickListener {
            lnPosted.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvPosted.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnApplied.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvApplied.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnActive.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)
            tvActive.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.black))

            lnCompleted.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.orange)
            tvCompleted.setTextColor(ContextCompat.getColorStateList(requireContext(),R.color.white))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Job()
    }
}