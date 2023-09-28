package com.jmr.cofindjobsearch.recycleview

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.jmr.cofindjobsearch.OtherActivity
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender
import java.io.File


class ApplicantAdapter(var items: ArrayList<ApplicantData>) : RecyclerView.Adapter<ApplicantAdapter.ViewHolder>() {
    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_applicants,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            Glide.with(itemView.context)
                .load(Uri.parse(item.imageLink))
                .into(imgProfile)

            tvFullName.text = item.fullName
            tvDateApplied.text = item.dateCreated
            tvPending.visibility = View.GONE
            tvAccepted.visibility = View.GONE
            tvDeclined.visibility = View.GONE

            when (item.status) {
                "Pending" -> {
                    tvPending.visibility = View.VISIBLE
                }
                "Accepted" -> {
                    tvAccepted.visibility = View.VISIBLE
                }
                "Declined" -> {
                    tvDeclined.visibility = View.VISIBLE
                }
            }

            imgMore.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_applicants, popupMenu.menu)

                popupMenu.apply {
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.menu_accept -> {
                                if (!Utils.hasInternet(itemView.context)) {
                                    Utils.showToastMessage(itemView.context,"Please check your internet connection")
                                } else {
                                    val jobInfo = JobSender(
                                        command = "CHANGE_STATUS",
                                        id = item.id,
                                        status = "Accepted"
                                    )

                                    apiService.saveJob(jobInfo) {
                                        if (it!!.success) {
                                            tvPending.visibility = View.GONE
                                            tvAccepted.visibility = View.VISIBLE
                                            tvDeclined.visibility = View.GONE
                                        }
                                    }
                                }
                                true
                            }
                            R.id.menu_decline -> {
                                if (!Utils.hasInternet(itemView.context)) {
                                    Utils.showToastMessage(itemView.context,"Please check your internet connection")
                                } else {
                                    val jobInfo = JobSender(
                                        command = "CHANGE_STATUS",
                                        id = item.id,
                                        status = "Declined"
                                    )

                                    apiService.saveJob(jobInfo) {
                                        if (it!!.success) {
                                            tvPending.visibility = View.GONE
                                            tvAccepted.visibility = View.GONE
                                            tvDeclined.visibility = View.VISIBLE
                                        }
                                    }
                                }
                                true
                            }
                            R.id.menu_view_document -> {
//                                Log.e("Resume Link",item.resumeLink)
//                                SharedHelper.apply {
//                                    putString("resume_link",item.resumeLink)
//                                }
//
//                                val gotoOtherActivity = Intent(itemView.context, OtherActivity::class.java).apply {
//                                    putExtra("COMMAND", "VIEW_RESUME")
//                                }
//                                itemView.context.startActivity(gotoOtherActivity)

                                val openURL = Intent(Intent.ACTION_VIEW)
                                openURL.data = Uri.parse(item.resumeLink)
                                itemView.context.startActivity(openURL)
                                true
                            }
                            else -> false
                        }
                    }

                    // Show the PopupMenu
                    show()
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgMore : ImageView = itemView.findViewById(R.id.imgMore)
        val imgProfile : ImageView = itemView.findViewById(R.id.imgProfile)
        val tvFullName : TextView = itemView.findViewById(R.id.tvFullName)
        val tvDateApplied : TextView = itemView.findViewById(R.id.tvDateApplied)
        val tvPending : TextView = itemView.findViewById(R.id.tvPending)
        val tvAccepted : TextView = itemView.findViewById(R.id.tvAccepted)
        val tvDeclined : TextView = itemView.findViewById(R.id.tvDeclined)
    }
}