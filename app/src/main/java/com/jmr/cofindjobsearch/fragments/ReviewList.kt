package com.jmr.cofindjobsearch.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.recycleview.JobHomeAdapter
import com.jmr.cofindjobsearch.recycleview.JobHomeData
import com.jmr.cofindjobsearch.recycleview.ReviewAdapter
import com.jmr.cofindjobsearch.recycleview.ReviewDataList
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils


class ReviewList : Fragment() {
    private lateinit var reviewList: View
    private lateinit var lnBack: LinearLayout
    private lateinit var imgProfile: ImageView
    private lateinit var imgStar1: ImageView
    private lateinit var imgStar2: ImageView
    private lateinit var imgStar3: ImageView
    private lateinit var imgStar4: ImageView
    private lateinit var imgStar5: ImageView
    private lateinit var tvFullName: TextView
    private lateinit var rvReviewList: RecyclerView

    private var userID: Int? = 0
    private var fullName: String? = null
    private var countStars: Int? = 0
    private var imageLink: String? = null

    private var reviewAdapter: ReviewAdapter? = null
    private val reviewListData  = ArrayList<ReviewDataList>()

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getInt(USERID)
            fullName = it.getString(FULLNAME)
            countStars = it.getInt(COUNTSTARS)
            imageLink = it.getString(IMAGELINK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reviewList = inflater.inflate(R.layout.fragment_review_list, container, false)

        reviewList.apply {
            lnBack = findViewById(R.id.lnBack)
            imgProfile = findViewById(R.id.imgProfile)
            imgStar1 = findViewById(R.id.imgStar1)
            imgStar2 = findViewById(R.id.imgStar2)
            imgStar3 = findViewById(R.id.imgStar3)
            imgStar4 = findViewById(R.id.imgStar4)
            imgStar5 = findViewById(R.id.imgStar5)
            tvFullName = findViewById(R.id.tvFullName)
            rvReviewList = findViewById(R.id.rvReviewList)
        }

        tvFullName.text = fullName
        changeStarValue(countStars!!)

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        if (imageLink != "-") {
            Glide.with(requireActivity())
                .load(Uri.parse(imageLink))
                .into(imgProfile)
        }

        loadReviews()

        return reviewList
    }

    private fun loadReviews() {
        Utils.showProgress(requireContext())

        apiService.loadReviewList(userID!!) { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val reviews = data.review

                    reviews.forEach { review ->
                        reviewListData.add(
                            ReviewDataList(
                            review.fullName,
                            review.review,
                            review.starsCount,
                            review.imageLink
                        ))
                    }

                    reviewAdapter?.notifyDataSetChanged()
                    reviewAdapter = ReviewAdapter(reviewListData)
                    rvReviewList.layoutManager = LinearLayoutManager(requireContext())
                    rvReviewList.adapter = reviewAdapter
                }

                Utils.closeProgress()
            }
        }
    }

    private fun changeStarValue(count: Int) {
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
        private const val USERID = "userid"
        private const val FULLNAME = "fullname"
        private const val COUNTSTARS = "countstars"
        private const val IMAGELINK = "imagelink"

        @JvmStatic
        fun newInstance(
            userID: Int,
            fullName: String,
            countStars: Int,
            imageLink: String
        ) =
            ReviewList().apply {
                arguments = Bundle().apply {
                    putInt(USERID, userID)
                    putString(FULLNAME, fullName)
                    putInt(COUNTSTARS, countStars)
                    putString(IMAGELINK, imageLink)
                }
            }
    }
}