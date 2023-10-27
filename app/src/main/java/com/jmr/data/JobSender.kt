package com.jmr.data

import com.google.gson.annotations.SerializedName

data class JobSender(
    @SerializedName("command") val command : String = "",
    @SerializedName("id") val id : Int  = 0,
    @SerializedName("jobTitle") val jobTitle : String = "",
    @SerializedName("description") val description : String = "",
    @SerializedName("requirementsList") val requirementsList : String = "",
    @SerializedName("jobType") val jobType : String = "",
    @SerializedName("additionalInfo") val additionalInfo : String = "",
    @SerializedName("salary") val salary : Double = 0.0,
    @SerializedName("forDiscussion") val forDiscussion : Int = 0,
    @SerializedName("imageList") val imageList : String = "",
    @SerializedName("status") val status : String = "",
    @SerializedName("createdBy") val createdBy : Int = 0,
    @SerializedName("applicantId") val applicantId : Int = 0,
    @SerializedName("fileLink") val fileLink : String = "",

    @SerializedName("jobID") val jobID : Int  = 0,
    @SerializedName("reviewerID") val reviewerID : Int  = 0,
    @SerializedName("reviewedID") val reviewedID : Int  = 0,
    @SerializedName("review") val review : String  = "",
    @SerializedName("starsCount") val starsCount : Int  = 0
)
