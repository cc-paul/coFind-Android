package com.jmr.cofindjobsearch.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.recycleview.ImagesData
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender
import com.jmr.data.ProfileSender

class Apply_Job : Fragment() {
    private lateinit var jobDetailsView: View
    private lateinit var lnBack: LinearLayout
    private lateinit var tvJobTitle: TextView
    private lateinit var tvJobDescription: TextView
    private lateinit var lnRequirements: LinearLayout
    private lateinit var tvAdditionalInfo: TextView
    private lateinit var tvSalary: TextView
    private lateinit var tvJobType: TextView
    private lateinit var skeleton: Skeleton
    private lateinit var lnJobHolder: LinearLayout
    private lateinit var lnImageHolder: LinearLayout
    private lateinit var imgProfile: ImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvDatePosted: TextView
    private lateinit var lnApply: LinearLayout
    private lateinit var lnApplyJobDetails: LinearLayout
    private lateinit var lnMessage: LinearLayout
    private lateinit var crdUpload: CardView
    private lateinit var fragManager: FragmentManager
    private lateinit var fragTransaction: FragmentTransaction
    private var jobId: Int = 0
    private var isResumeUploaded: Boolean = false
    private var fileLink: String = ""
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    val firebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference : StorageReference = firebaseStorage.reference

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            jobId = it.getInt(JOB_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jobDetailsView = inflater.inflate(R.layout.fragment_apply_job, container, false)

        jobDetailsView.apply {
            lnBack = findViewById(R.id.lnBack)
            tvJobTitle = findViewById(R.id.tvJobTitle)
            tvJobDescription = findViewById(R.id.tvJobDescription)
            lnRequirements = findViewById(R.id.lnRequirements)
            tvAdditionalInfo = findViewById(R.id.tvAdditionalInfo)
            lnBack = findViewById(R.id.lnBack)
            tvSalary = findViewById(R.id.tvSalary)
            tvJobType = findViewById(R.id.tvJobType)
            skeleton = findViewById(R.id.skeletonLayout)
            lnJobHolder = findViewById(R.id.lnJobHolder)
            lnImageHolder = findViewById(R.id.lnImageHolder)
            imgProfile = findViewById(R.id.imgProfile)
            tvFullName = findViewById(R.id.tvFullName)
            tvDatePosted = findViewById(R.id.tvDatePosted)
            lnApplyJobDetails = findViewById(R.id.lnApplyJobDetails)
            lnApply = findViewById(R.id.lnApply)
            lnMessage = findViewById(R.id.lnMessage)
            crdUpload = findViewById(R.id.crdUpload)
        }

        regActivityForResult()

        fragManager = requireActivity().supportFragmentManager
        fragTransaction = fragManager.beginTransaction()

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lnApply.setOnClickListener {
            if (!Utils.hasInternet(this.requireContext())) {
                Utils.showSnackMessage(lnApplyJobDetails,"Please check your internet connection")
                return@setOnClickListener
            }

            if (!isResumeUploaded || fileLink.trim().isEmpty()) {
                Utils.showSnackMessage(lnApplyJobDetails,"Please upload resume before your application")
                return@setOnClickListener
            }

            Utils.showProgress(requireContext())

            val jobInfo = JobSender(
                command = "APPLY_JOB",
                id = SharedHelper.getInt("job_id"),
                applicantId = SharedHelper.getInt("user_id"),
                fileLink = fileLink
            )

            apiService.saveJob(jobInfo) {
                Utils.closeProgress()
                Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())

                if (it!!.success) {
                    lnBack.performClick()
                }
            }
        }

        SharedHelper.apply {
            if (getString("person_link") != "-") {
                Glide.with(requireActivity())
                    .load(Uri.parse(getString("person_link")))
                    .into(imgProfile)

                tvFullName.text = getString("person_name")
                tvDatePosted.text = getString("person_ago")
            }
        }

        lnMessage.setOnClickListener {
            fragTransaction.apply {
                replace(R.id.container, Messaging.newInstance(
                    receiverID = SharedHelper.getInt("receiver_id"),
                    fullName = tvFullName.text.toString(),
                    imageLink = SharedHelper.getString("person_link")
                ))
                addToBackStack("messaging")
                commit()
            }
        }

        crdUpload.setOnClickListener {
//            val permissionlistener: PermissionListener = object : PermissionListener {
//                override fun onPermissionGranted() {
//                    val fileSelectIntent = Intent()
//
//                    fileSelectIntent.apply {
//                        type = "application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
//                        action = Intent.ACTION_GET_CONTENT
//                    }
//                    activityResultLauncher.launch(fileSelectIntent)
//                }
//
//                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                    Utils.showSnackMessage(lnApplyJobDetails,"Some permissions were denied. Unable to use this function")
//                }
//            }
//
//            TedPermission.with(context)
//                .setPermissionListener(permissionlistener)
//                .setDeniedMessage("Storage is required.\n\nPlease turn on permissions at [Setting] > [Permission]")
//                .setPermissions(Manifest.permission.READ_MEDIA_IMAGES)
//                .check()

            val fileSelectIntent = Intent()
            fileSelectIntent.apply {
                type = "application/*"
                action = Intent.ACTION_GET_CONTENT
            }
            activityResultLauncher.launch(fileSelectIntent)
        }

        populateJobFields()

        return jobDetailsView
    }

    private fun regActivityForResult() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val resultCode = result.resultCode
            val imageData = result.data

            if (resultCode == Activity.RESULT_OK && imageData != null) {
                val fileUri = imageData.data
                val fileName = getFileName(fileUri)
                val fileReference = storageReference.child("files").child("${SharedHelper.getInt("job_id")}_${SharedHelper.getInt("user_id")}_$fileName")
                Utils.showProgress(requireContext())

                fileReference.putFile(imageData.data!!).addOnSuccessListener {
                    val linkRef = storageReference.child("files").child("${SharedHelper.getInt("job_id")}_${SharedHelper.getInt("user_id")}_$fileName")

                    linkRef.downloadUrl.addOnSuccessListener { url ->
                        fileLink = url.toString()
                        isResumeUploaded = true
                        Utils.closeProgress()
                        Utils.showSnackMessage(lnApplyJobDetails,"File has been uploaded successfully")
                    }.addOnFailureListener {
                        Utils.showSnackMessage(lnApplyJobDetails,it.message.toString())
                    }
                }.addOnFailureListener {
                    Utils.closeProgress()
                    Utils.showSnackMessage(lnApplyJobDetails,"Error uploading file. Please try again later")
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri?): String {
        if (uri == null) {
            return ""
        }

        var result = ""
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            if (!displayName.isNullOrBlank()) {
                result = displayName
            }
        }
        return result
    }

    private fun populateJobFields() {
        skeleton = lnJobHolder.createSkeleton()
        skeleton.maskCornerRadius = 20f
        skeleton.showSkeleton()

        apiService.getJobDetails(jobId) { response ->
            Utils.showToastMessage(requireContext(),response?.messages?.get(0).toString())

            if (response!!.success) {
                val data = response.data

                skeleton.showOriginal()
                if (data.rows_returned != 0) {
                    val jobs = data.jobs

                    jobs.forEach { job ->
                        job.apply {
                            tvJobTitle.text = jobTitle
                            tvJobDescription.text = description
                            tvAdditionalInfo.text = additionalInfo
                            tvSalary.text = if (forDiscussion == 1) {
                                "Salary for Discussion"
                            } else {
                                SharedHelper.formatNumber(salary.toDouble())
                            }
                            tvJobType.text = jobType

                            if (tvAdditionalInfo.text.toString().trim().isEmpty()) {
                                tvAdditionalInfo.visibility = View.GONE
                            }

                            lnRequirements.removeAllViews()

                            val requirementsList = requirementsList.split("~")
                            for (requirement in requirementsList) {
                                val inflater = LayoutInflater.from(requireContext())
                                val newRowView = inflater.inflate(R.layout.row_req_tv, lnRequirements, false)
                                val tvReq = newRowView.findViewById<TextView>(R.id.tvRequirements)
                                tvReq.text = requirement
                                lnRequirements.addView(newRowView)
                            }

                            lnImageHolder.removeAllViews()

                            val imageList = imageList.split("~")
                            for (image in imageList) {
                                val inflater = LayoutInflater.from(requireContext())
                                val newRowView = inflater.inflate(R.layout.row_req_image, lnImageHolder, false)
                                val photo = newRowView.findViewById<ImageView>(R.id.imgPhoto)

                                Glide.with(requireActivity())
                                    .load(Uri.parse(image))
                                    .into(photo)

                                lnImageHolder.addView(newRowView)
                            }

                            if (imageList.toString().trim().isEmpty()) {
                                lnImageHolder.visibility = View.GONE
                            }
                        }
                    }
                }
            } else {
                lnBack.performClick()
            }
        }
    }

    companion object {
        private const val JOB_ID = "job_id"

        @JvmStatic
        fun newInstance(jobId: Int) =
            Apply_Job().apply {
                arguments = Bundle().apply {
                    putInt(JOB_ID, jobId)
                }
            }
    }
}