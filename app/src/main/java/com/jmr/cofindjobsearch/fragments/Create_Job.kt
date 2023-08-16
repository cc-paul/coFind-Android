package com.jmr.cofindjobsearch.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.recycleview.ImagesAdapter
import com.jmr.cofindjobsearch.recycleview.ImagesData
import com.jmr.cofindjobsearch.recycleview.JobAdapter
import com.jmr.cofindjobsearch.recycleview.JobData
import com.jmr.cofindjobsearch.recycleview.RequirementsAdapter
import com.jmr.cofindjobsearch.recycleview.RequirementsData
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender
import com.jmr.data.ProfileSender
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule

data class RequirementsRow(val id: Int, var value: String)

class Create_Job : Fragment() {
    private lateinit var viewCreateJob: View
    private lateinit var tvTitle: TextView
    private lateinit var lnBack: LinearLayout
    private lateinit var etJobTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var imgAddRequirements: ImageView
    private lateinit var rdPartTime: RadioButton
    private lateinit var rdFullTime: RadioButton
    private lateinit var etAdditionalInfo: EditText
    private lateinit var etSalary: EditText
    private lateinit var chkForDiscussion: CheckBox
    private lateinit var lnUpload: LinearLayout
    private lateinit var lnSaveChanges: LinearLayout
    private lateinit var lnReqHolder: LinearLayout
    private lateinit var rvReq: RecyclerView
    private lateinit var rvImages: RecyclerView
    private lateinit var lnJobHolder: FrameLayout
    private lateinit var tvNoImage: TextView
    private lateinit var timer: Timer
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var handler: Handler

    private val requirementsList = ArrayList<RequirementsRow>()
    private val imageList = ArrayList<ImagesData>()
    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }
    private val firebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference : StorageReference = firebaseStorage.reference

    private var imagesAdapter: ImagesAdapter? = null
    private var editTextCount = 0
    private var jobType = ""
    private var isForDiscussion = 0
    private var jobID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requirementsList.clear()
        imageList.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewCreateJob = inflater.inflate(R.layout.fragment_create__job, container, false)

        viewCreateJob.apply {
            tvTitle = findViewById(R.id.tvTitle)
            lnBack = findViewById(R.id.lnBack)
            etJobTitle = findViewById(R.id.etJobTitle)
            etDescription = findViewById(R.id.etDescription)
            imgAddRequirements = findViewById(R.id.imgAddRequirements)
            rdPartTime = findViewById(R.id.rdPartTime)
            rdFullTime = findViewById(R.id.rdFullTime)
            etAdditionalInfo = findViewById(R.id.etAdditionalInfo)
            etSalary = findViewById(R.id.etSalary)
            chkForDiscussion = findViewById(R.id.chkForDiscussion)
            lnUpload = findViewById(R.id.lnUpload)
            lnSaveChanges = findViewById(R.id.lnSaveChanges)
            rvReq = findViewById(R.id.rvReq)
            lnReqHolder = findViewById(R.id.lnReqHolder)
            lnJobHolder = findViewById(R.id.lnJobHolder)
            rvImages = findViewById(R.id.rvImages)
            tvNoImage = findViewById(R.id.tvNoImage)
            timer = Timer()
            this@Create_Job.handler = Handler(Looper.getMainLooper())
        }

        regActivityForResult()

        jobID = SharedHelper.getInt("job_id",0)
        tvTitle.text = if (jobID == 0) { "Create" } else { "Update" }

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        rdPartTime.setOnClickListener{
            rdFullTime.isChecked = false
            jobType = rdPartTime.text.toString()
        }

        rdFullTime.setOnClickListener{
            rdPartTime.isChecked = false
            jobType = rdFullTime.text.toString()
        }

        chkForDiscussion.setOnClickListener {
            etSalary.apply {
                isEnabled = if (chkForDiscussion.isChecked) {
                    isForDiscussion = 1
                    setText("0")
                    false
                } else {
                    isForDiscussion = 0
                    setText("")
                    true
                }
            }
        }

        imgAddRequirements.setOnClickListener {
            addRequirements("")
        }

        if (jobID == 0) {
            imgAddRequirements.performClick()
        } else {
            populateJobFields()
        }

        lnSaveChanges.setOnClickListener {
            val imageStringList = ArrayList<String>()
            val reqStringList = ArrayList<String>()

            if (etJobTitle.text.toString().trim().isEmpty()) {
                Utils.showSnackMessage(lnJobHolder,"Please provide Job Title")
                return@setOnClickListener
            }

            if (etDescription.text.toString().trim().isEmpty()) {
                Utils.showSnackMessage(lnJobHolder,"Please provide Job Description")
                return@setOnClickListener
            }

            if (checkForEmptyFields()) {
                Utils.showSnackMessage(lnJobHolder, "You have an empty field in your requirements")
                return@setOnClickListener
            }

            if (jobType.trim().isEmpty()) {
                Utils.showSnackMessage(lnJobHolder, "Please select Job Type")
                return@setOnClickListener
            }

            if (etSalary.text.toString().trim().isEmpty()) {
                Utils.showSnackMessage(lnJobHolder,"Please provide salary")
                return@setOnClickListener
            }

            for (item in imageList) {
                imageStringList.add(item.imageLink)
            }

            for (item in requirementsList) {
                reqStringList.add(item.value)
            }

            Utils.showProgress(requireContext())

            val jobInfo = JobSender(
                command = SharedHelper.getString("job_command"),
                id = jobID,
                jobTitle = etJobTitle.text.toString(),
                description = etDescription.text.toString(),
                requirementsList = reqStringList.joinToString("~"),
                jobType = jobType,
                additionalInfo = etAdditionalInfo.text.toString(),
                salary = etSalary.text.toString().toDouble(),
                forDiscussion = isForDiscussion,
                imageList = imageStringList.joinToString("~"),
                status = "",
                createdBy = SharedHelper.getInt("user_id")
            )

            apiService.saveJob(jobInfo) {
                Utils.closeProgress()
                Utils.showToastMessage(requireContext(),it?.messages?.get(0).toString())

                if (it!!.success) {
                    lnBack.performClick()
                }
            }
        }

        lnUpload.setOnClickListener {
            val permissionlistener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    val imageSelectIntent = Intent()

                    imageSelectIntent.apply {
                        type = "image/*"
                        action = Intent.ACTION_GET_CONTENT
                    }
                    activityResultLauncher.launch(imageSelectIntent)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Utils.showSnackMessage(lnJobHolder,"Some permissions were denied. Unable to use this function")
                }
            }

            TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Storage is required.\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_MEDIA_IMAGES)
                .check()
        }

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    countImages()
                }
            }
        }, 0, 1000)


        loadImages()
        return viewCreateJob
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
    }

    private fun populateJobFields() {
        Utils.showProgress(requireContext())

        apiService.getJobDetails(jobID) { response ->
            Utils.closeProgress()
            Utils.showToastMessage(requireContext(),response?.messages?.get(0).toString())

            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val jobs = data.jobs

                    jobs.forEach { job ->
                        etJobTitle.setText(job.jobTitle)
                        etDescription.setText(job.description)

                        for (requirement in job.requirementsList.split("~")) {
                            addRequirements(requirement)
                        }

                        when(job.jobType) {
                            "Full Time" -> {
                                rdFullTime.performClick()
                            }
                            "Part Time" -> {
                                rdPartTime.performClick()
                            }
                        }

                        etAdditionalInfo.setText(job.additionalInfo)

                        if (job.forDiscussion == 1) {
                            chkForDiscussion.performClick()
                        } else {
                            etSalary.setText(job.salary.replace(".00",""))
                        }

                        if (job.imageList.trim().isNotEmpty()) {
                            for (imageLink in job.imageList.split("~")) {
                                imageList.add(ImagesData(imageLink))
                            }
                        }

                        loadImages()
                    }
                }
            } else {
                lnBack.performClick()
            }
        }
    }

    private fun addRequirements(text: String) {
        val inflater = LayoutInflater.from(requireContext())
        val newRowView = inflater.inflate(R.layout.row_req, lnReqHolder, false)
        val deleteButton = newRowView.findViewById<ImageView>(R.id.imgDelete)
        val editText = newRowView.findViewById<EditText>(R.id.etRequirements)

        deleteButton.setOnClickListener {
            if (requirementsList.size > 1) {
                deleteItemById(deleteButton.tag as Int)
                lnReqHolder.removeView(newRowView)
            } else {
                Utils.showSnackMessage(lnJobHolder,"Please provide at least 1 requirement.")
            }
        }

        editText.setText(text)
        editText.tag = editTextCount
        deleteButton.tag = editTextCount
        requirementsList.add(RequirementsRow(editTextCount,text))
        lnReqHolder.addView(newRowView)
        editTextCount++

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateItemById(editText.tag as Int,s.toString())
            }
        })
    }

    private fun checkForEmptyFields(): Boolean {
        var isThereEmpty = false

        for (item in requirementsList) {
            if (item.value.trim().isEmpty()) {
                isThereEmpty = true
                break
            }
        }

        return  isThereEmpty
    }

    private fun deleteItemById(idToDelete: Int) {
        val iterator = requirementsList.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.id == idToDelete) {
                iterator.remove()
            }
        }
    }

    fun deleteImageItemByLink(link: String) {
        val iterator = imageList.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.imageLink == link) {
                iterator.remove()
            }
        }
    }

    fun loadImages() {
        imagesAdapter = ImagesAdapter(imageList, Create_Job())
        rvImages.layoutManager = GridLayoutManager(requireContext(), 3)
        rvImages.adapter = imagesAdapter
    }

    fun countImages() {
        tvNoImage.visibility = if (imageList.size == 0) {
            View.VISIBLE
        } else {
            View.GONE
        }

        Log.e("Image Count",imageList.size.toString())
    }

    private fun updateItemById(idToUpdate: Int, newValue: String) {
        for (item in requirementsList) {
            if (item.id == idToUpdate) {
                item.value = newValue
                break
            }
        }
    }

    private fun regActivityForResult() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val resultCode = result.resultCode
            val imageData = result.data

            if (resultCode == Activity.RESULT_OK && imageData != null) {
                val imageReference = storageReference.child("profiles")
                Utils.showProgress(requireContext())

                imageReference.putFile(imageData.data!!).addOnSuccessListener {
                    val linkRef = storageReference.child("profiles")

                    linkRef.downloadUrl.addOnSuccessListener { url ->
                        imageList.add(ImagesData(url.toString()))
                        loadImages()
                        Utils.closeProgress()
                    }
                }.addOnFailureListener {
                    Utils.closeProgress()
                    Utils.showSnackMessage(lnJobHolder,"Error adding image. Please try again later")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Create_Job()
    }
}