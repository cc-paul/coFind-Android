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


class ReviewAdapter(var items: ArrayList<ReviewDataList>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_review,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            if (item.imageLink != "-") {
                Glide.with(itemView.context)
                    .load(Uri.parse(item.imageLink))
                    .into(imgProfile)
            }

            tvFullName.text = item.fullName
            tvReview.text = item.review

            when(item.countStars) {
                0 -> {
                    imgStar1.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar2.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar3.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar4.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar5.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                }
                1 -> {
                    imgStar1.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar2.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar3.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar4.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar5.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                }
                2 -> {
                    imgStar1.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar2.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar3.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar4.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar5.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                }
                3 -> {
                    imgStar1.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar2.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar3.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar4.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                    imgStar5.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                }
                4 -> {
                    imgStar1.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar2.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar3.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar4.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar5.setImageResource(itemView.context.resources.getIdentifier("icn_star_unselected", "drawable", itemView.context.packageName))
                }
                5 -> {
                    imgStar1.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar2.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar3.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar4.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                    imgStar5.setImageResource(itemView.context.resources.getIdentifier("icn_star_selected", "drawable", itemView.context.packageName))
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile : ImageView = itemView.findViewById(R.id.imgProfile)
        val tvFullName : TextView = itemView.findViewById(R.id.tvFullName)
        val tvReview : TextView = itemView.findViewById(R.id.tvReview)
        val imgStar1: ImageView = itemView.findViewById(R.id.imgStar1)
        val imgStar2: ImageView = itemView.findViewById(R.id.imgStar2)
        val imgStar3: ImageView = itemView.findViewById(R.id.imgStar3)
        val imgStar4: ImageView = itemView.findViewById(R.id.imgStar4)
        val imgStar5: ImageView = itemView.findViewById(R.id.imgStar5)
    }
}