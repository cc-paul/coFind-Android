package com.jmr.cofindjobsearch.recycleview

data class ApplicantData(
    var id: Int,
    var jobID: Int,
    var applicantID: Int,
    var fullName: String,
    var resumeLink: String,
    var imageLink: String,
    var dateCreated: String,
    var status: String
)