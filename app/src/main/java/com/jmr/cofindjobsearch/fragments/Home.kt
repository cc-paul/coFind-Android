package com.jmr.cofindjobsearch.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.recycleview.JobAdapter
import com.jmr.cofindjobsearch.recycleview.JobData
import com.jmr.cofindjobsearch.recycleview.JobHomeAdapter
import com.jmr.cofindjobsearch.recycleview.JobHomeData
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender

class Home : Fragment() {
    private lateinit var viewHome: View
    private lateinit var etSearch: EditText
    private lateinit var rvJobs: RecyclerView

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    private var jobAdapter: JobHomeAdapter? = null
    private val jobList  = ArrayList<JobHomeData>()
    private val postedAddedJobs = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewHome = inflater.inflate(R.layout.fragment_home, container, false)


        viewHome.apply {
            etSearch = findViewById(R.id.etSearch)
            rvJobs = findViewById(R.id.rvJobs)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString().trim()
                loadJobs(searchQuery)
            }
        })

        return viewHome
    }

    override fun onResume() {
        super.onResume()

        loadJobs()
    }

    private fun loadJobs(jobName:String = "") {
        try {
            jobList.clear()
            jobAdapter?.notifyDataSetChanged()
            postedAddedJobs.clear()

            apiService.getJobAll(SharedHelper.getInt("user_id")) {response ->
                if (response!!.success) {
                    val data = response.data

                    if (data.rows_returned != 0) {
                        val jobs = data.jobs

                        jobs.forEach { job ->
                            if (jobName.trim().isEmpty()) {
                                if (!postedAddedJobs.contains(job.id)) {
                                    jobList.add(JobHomeData(
                                        job.id,
                                        job.imageLink,
                                        job.fullName,
                                        job.f_dateCreated,
                                        job.salary,
                                        job.jobTitle,
                                        job.description,
                                        job.jobType,
                                        job.requirementsList,
                                        job.createdBy,
                                        job.enableApplyButton
                                    ))
                                    postedAddedJobs.add(job.id)
                                }
                            } else {
                                if (!postedAddedJobs.contains(job.id) &&
                                    job.jobTitle.contains(jobName, ignoreCase = true)
                                ) {
                                    jobList.add(JobHomeData(
                                        job.id,
                                        job.imageLink,
                                        job.fullName,
                                        job.f_dateCreated,
                                        job.salary,
                                        job.jobTitle,
                                        job.description,
                                        job.jobType,
                                        job.requirementsList,
                                        job.createdBy,
                                        job.enableApplyButton
                                    ))
                                    postedAddedJobs.add(job.id)
                                }
                            }
                        }

                        jobAdapter?.notifyDataSetChanged()
                        jobAdapter = JobHomeAdapter(jobList)
                        rvJobs.layoutManager = LinearLayoutManager(requireContext())
                        rvJobs.adapter = jobAdapter
                    }
                }
            }
        } catch (e:Exception) {
            Log.e("Post Error",e.message.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Home()
    }
}