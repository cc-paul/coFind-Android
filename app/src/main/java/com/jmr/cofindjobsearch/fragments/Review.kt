package com.jmr.cofindjobsearch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.recycleview.ApplicantAdapter
import com.jmr.cofindjobsearch.recycleview.ApplicantData
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender


class Review : Fragment() {
    private lateinit var reviewView: View
    private lateinit var tvPersonName: TextView
    private lateinit var imgStar1: ImageView
    private lateinit var imgStar2: ImageView
    private lateinit var imgStar3: ImageView
    private lateinit var imgStar4: ImageView
    private lateinit var imgStar5: ImageView
    private lateinit var etReview: EditText
    private lateinit var lnProceed: LinearLayout
    private lateinit var lnReview: LinearLayout
    private lateinit var lnBack: LinearLayout

    private var jobID: Int? = 0
    private var reviewerName: String? = null
    private var reviewerID: Int? = 0
    private var reviewedID: Int? = 0
    private var starCount: Int? = 0
    private var reviewID: Int? = 0

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            jobID = it.getInt(JOBID)
            reviewerName = it.getString(REVIEWERSNAME)
            reviewerID = it.getInt(REVIEWERID)
            reviewedID = it.getInt(REVIEWEDID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reviewView = inflater.inflate(R.layout.fragment_review, container, false)

        reviewView.apply {
            tvPersonName = findViewById(R.id.tvPersonName)
            imgStar1 = findViewById(R.id.imgStar1)
            imgStar2 = findViewById(R.id.imgStar2)
            imgStar3 = findViewById(R.id.imgStar3)
            imgStar4 = findViewById(R.id.imgStar4)
            imgStar5 = findViewById(R.id.imgStar5)
            etReview = findViewById(R.id.etReview)
            lnProceed = findViewById(R.id.lnProceed)
            lnReview = findViewById(R.id.lnReview)
            lnBack = findViewById(R.id.lnBack)
        }

        tvPersonName.text = reviewerName

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        imgStar1.setOnClickListener {
            changeStarValue(1)
        }

        imgStar2.setOnClickListener {
            changeStarValue(2)
        }

        imgStar3.setOnClickListener {
            changeStarValue(3)
        }

        imgStar4.setOnClickListener {
            changeStarValue(4)
        }

        imgStar5.setOnClickListener {
            changeStarValue(5)
        }

        lnReview.setOnClickListener {
            if (!Utils.hasInternet(this.requireContext())) {
                Utils.showSnackMessage(lnReview,"Please check your internet connection")
                return@setOnClickListener
            }

            if (starCount == 0 || etReview.text.toString().isNullOrEmpty()) {
                Utils.showSnackMessage(lnReview,"Please complete all review details")
                return@setOnClickListener
            }

            Utils.showProgress(requireContext())
            var command = if (reviewID == 0) {
                "ADD_REVIEW"
            } else {
                "EDIT_REVIEW"
            }

            val jobInfo = JobSender(
                command = command,
                id = reviewID!!,
                jobID = jobID!!,
                reviewerID = reviewerID!!,
                reviewedID = reviewedID!!,
                review = etReview.text.toString(),
                starsCount = starCount!!
            )

            apiService.saveJob(jobInfo) {
                Utils.closeProgress()
                Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())
                loadReview()
            }
        }

        //Toast.makeText(requireContext(), "$jobID,$reviewerName,$reviewerID,$reviewedID", Toast.LENGTH_SHORT).show()
        loadReview()

        return reviewView
    }

    private fun loadReview() {
        Utils.showProgress(requireContext())

        apiService.loadSingleReview(jobID!!,reviewerID!!,reviewedID!!) { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val singleReview = data.review

                    singleReview.forEach {review ->
                        reviewID = review.id
                        etReview.setText(review.review)
                        changeStarValue(review.starsCount)
                    }
                }

                Utils.closeProgress()
            }
        }
    }

    private fun changeStarValue(count: Int) {
        starCount = count

        when(count) {
            1 -> {
                imgStar1.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar2.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
                imgStar3.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
                imgStar4.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
                imgStar5.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
            }
            2 -> {
                imgStar1.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar2.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar3.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
                imgStar4.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
                imgStar5.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
            }
            3 -> {
                imgStar1.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar2.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar3.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar4.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
                imgStar5.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
            }
            4 -> {
                imgStar1.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar2.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar3.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar4.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar5.setImageResource(resources.getIdentifier("icn_star_unselected", "drawable", requireContext().packageName))
            }
            5 -> {
                imgStar1.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar2.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar3.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar4.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
                imgStar5.setImageResource(resources.getIdentifier("icn_star_selected", "drawable", requireContext().packageName))
            }
        }
    }

    companion object {
        private const val JOBID = "jobid"
        private const val REVIEWERSNAME = "reviewersname"
        private const val REVIEWERID = "reviewerid"
        private const val REVIEWEDID = "reviewedid"

        @JvmStatic
        fun newInstance(
            jobID: Int,
            reviewersName: String,
            reviewerID: Int,
            reviewedID: Int
        ) =
            Review().apply {
                arguments = Bundle().apply {
                    putInt(JOBID, jobID)
                    putString(REVIEWERSNAME, reviewersName)
                    putInt(REVIEWERID, reviewerID)
                    putInt(REVIEWEDID, reviewedID)
                }
            }
    }
}