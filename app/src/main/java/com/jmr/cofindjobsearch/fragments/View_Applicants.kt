package com.jmr.cofindjobsearch.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.recycleview.ApplicantAdapter
import com.jmr.cofindjobsearch.recycleview.ApplicantData
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils


class View_Applicants : Fragment() {
    private lateinit var viewApplicants:View
    private lateinit var lnBack: LinearLayout
    private lateinit var etSearch: EditText
    private lateinit var rvApplicants: RecyclerView
    private var jobID: Int = 0

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    private var applicantAdapter: ApplicantAdapter? = null
    private val applicantList  = ArrayList<ApplicantData>()
    val addedApplicants = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            jobID = it.getInt(JOB_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewApplicants = inflater.inflate(R.layout.fragment_view_applicants, container, false)

        viewApplicants.apply {
            lnBack = findViewById(R.id.lnBack)
            etSearch = findViewById(R.id.etSearch)
            rvApplicants = findViewById(R.id.rvApplicants)
        }

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        loadApplicants()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                loadApplicants()
            }
        })

        return viewApplicants
    }

    private fun loadApplicants() {
        if (Utils.hasInternet(this.requireContext())) {
            try {
                applicantList.clear()
                applicantAdapter?.notifyDataSetChanged()
                addedApplicants.clear()
                var search = etSearch.text.toString()

                if (search.trim().isEmpty()) {
                    search = "-"
                }

                apiService.loadApplicant(jobID,search) { response ->
                    if (response!!.success) {
                        val data = response.data
                        
                        if (data.rows_returned != 0) {
                            val  applicants = data.applicants
                            
                            applicants.forEach {applicant ->
                                if (!addedApplicants.contains(applicant.id)) {
                                    applicantList.add(ApplicantData(
                                        applicant.id,
                                        applicant.jobID,
                                        applicant.applicantID,
                                        applicant.fullName,
                                        applicant.resumeLink,
                                        applicant.imageLink,
                                        applicant.dateCreated,
                                        applicant.status
                                    ))
                                }
                            }

                            applicantAdapter = ApplicantAdapter(applicantList)
                            rvApplicants.layoutManager = GridLayoutManager(context, 2)
                            rvApplicants.adapter = applicantAdapter
                        }
                    }
                }
            } catch (e: Exception) {}
        }
    }

    companion object {
        private const val JOB_ID = "job_id"

        @JvmStatic
        fun newInstance(jobID: Int) =
            View_Applicants().apply {
                arguments = Bundle().apply {
                    putInt(JOB_ID, jobID)
                }
            }
    }
}