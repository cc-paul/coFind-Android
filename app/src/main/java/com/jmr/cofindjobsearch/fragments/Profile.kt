package com.jmr.cofindjobsearch.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.jmr.cofindjobsearch.LoginActivity
import com.jmr.cofindjobsearch.MainActivity
import com.jmr.cofindjobsearch.OtherActivity
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.LoginSender
import com.jmr.data.ProfileSender
import java.util.Timer
import kotlin.concurrent.schedule

class Profile : Fragment() {
    private lateinit var viewProfile: View
    private lateinit var tvFullName: TextView
    private lateinit var tvMobileNumber: TextView
    private lateinit var tvEmailAddress: TextView
    private lateinit var tvAddress: TextView
    private lateinit var lnAddress: LinearLayout
    private lateinit var lnProfileHolder: LinearLayout
    private lateinit var crdProfile: CardView
    private lateinit var imgProfile: ImageView
    private lateinit var crdEditProfile: CardView
    private lateinit var crdChangePassword: CardView
    private lateinit var crdChangeProfile: CardView
    private lateinit var lnMenuHolder: LinearLayout
    private lateinit var lnMenuHolderDummy: LinearLayout
    private lateinit var lnLogOut: LinearLayout
    private lateinit var skeleton: Skeleton

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    public var isGalleryOpened: Boolean = false

    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    val firebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference : StorageReference = firebaseStorage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewProfile = inflater.inflate(R.layout.fragment_profile, container, false)

        viewProfile.apply {
            tvFullName = findViewById(R.id.tvFullName)
            tvMobileNumber = findViewById(R.id.tvMobileNumber)
            tvEmailAddress = findViewById(R.id.tvEmailAddress)
            tvAddress = findViewById(R.id.tvAddress)
            lnAddress = findViewById(R.id.lnAddress)
            lnProfileHolder = findViewById(R.id.lnProfileHolder)
            crdProfile = findViewById(R.id.crdProfile)
            imgProfile = findViewById(R.id.imgProfile)
            crdEditProfile = findViewById(R.id.crdEditProfile)
            crdChangePassword = findViewById(R.id.crdChangePassword)
            crdChangeProfile = findViewById(R.id.crdChangeProfile)
            lnMenuHolder = findViewById(R.id.lnMenuHolder)
            lnMenuHolderDummy = findViewById(R.id.lnMenuHolderDummy)
            lnLogOut = findViewById(R.id.lnLogOut)
            skeleton = findViewById(R.id.skeletonLayout)
        }

        regActivityForResult()

        crdChangeProfile.setOnClickListener {
            val permissionlistener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    val imageSelectIntent = Intent()

                    imageSelectIntent.apply {
                        type = "image/*"
                        action = Intent.ACTION_GET_CONTENT
                    }
                    activityResultLauncher.launch(imageSelectIntent)
                    isGalleryOpened = true
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Utils.showSnackMessage(lnProfileHolder,"Some permissions were denied. Unable to use this function")
                }
            }

            TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Storage is required.\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_MEDIA_IMAGES)
                .check()
        }

        crdChangePassword.setOnClickListener {
            val gotoOtherActivity = Intent(requireContext(), OtherActivity::class.java).apply {
                putExtra("COMMAND", "PASSWORD")
            }
            startActivity(gotoOtherActivity)
        }

        crdEditProfile.setOnClickListener {
            isGalleryOpened = false
            val gotoOtherActivity = Intent(requireContext(), OtherActivity::class.java).apply {
                putExtra("COMMAND", "DETAILS")
            }
            startActivity(gotoOtherActivity)
        }

        lnLogOut.setOnClickListener {
            val gotoMainActivity = Intent(requireContext(), LoginActivity::class.java).apply {
                putExtra("COMMAND", "PASSWORD")
            }
            startActivity(gotoMainActivity)
            requireActivity().finish()
        }

        return viewProfile
    }

    override fun onResume() {
        super.onResume()

        if (!isGalleryOpened) {
            applyLoadingSkeleton()
        }
    }



    fun applyLoadingSkeleton() {
        tvMobileNumber.text = ""
        tvEmailAddress.text = ""
        lnMenuHolder.visibility = View.GONE
        lnMenuHolderDummy.visibility = View.VISIBLE

        skeleton = lnProfileHolder.createSkeleton()
        skeleton.maskCornerRadius = 20f
        skeleton.showSkeleton()

        Handler().postDelayed({
            loadPersonalInfo()
        }, 2000)
    }

    private fun loadPersonalInfo() {
        if (!Utils.hasInternet(this.requireContext())) {
            Utils.showSnackMessage(lnProfileHolder,"Please check your internet connection")
            return
        }

        apiService.getProfileDetails(SharedHelper.getInt("user_id")) {
            if (it!!.success) {
                skeleton.showOriginal()
                lnMenuHolder.visibility = View.VISIBLE
                lnMenuHolderDummy.visibility = View.GONE

                it.data.apply {
                    tvFullName.text = "$lastName, $firstName $middleName"
                    tvEmailAddress.text = emailAddress
                    tvMobileNumber.text = mobileNumber
                    tvAddress.text = address

                    if (imageLink != "-") {
                        Glide.with(requireActivity())
                            .load(Uri.parse(imageLink))
                            .into(imgProfile)
                    }
                }
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
                val imageReference = storageReference.child("profiles").child(SharedHelper.getInt("user_id").toString())
                Utils.showProgress(requireContext())

                imageReference.putFile(imageData.data!!).addOnSuccessListener {
                    val linkRef = storageReference.child("profiles").child(SharedHelper.getInt("user_id").toString())

                    linkRef.downloadUrl.addOnSuccessListener { url ->
                        val profileInfo = ProfileSender(
                            command = "PROFILE_PIC",
                            user_id = SharedHelper.getInt("user_id"),
                            imageLink = url.toString()
                        )

                        apiService.changeProfile(profileInfo) {
                            Utils.closeProgress()
                            Utils.showSnackMessage(lnProfileHolder,it?.messages?.get(0).toString())

                            if (it!!.success) {
                                Glide.with(requireActivity())
                                    .load(Uri.parse(url.toString()))
                                    .into(imgProfile)
                            }
                        }
                    }
                }.addOnFailureListener {
                    Utils.closeProgress()
                    Utils.showSnackMessage(lnProfileHolder,"Error updating profile. Please try again later")
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = Profile()
    }
}