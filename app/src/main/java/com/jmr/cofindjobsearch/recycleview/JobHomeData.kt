package com.jmr.cofindjobsearch.recycleview

data class JobHomeData(
    var imageLink: String,
    var fullName: String,
    var datePosted: String,
    var salary: String,
    var jobTitle: String,
    var jobDescription: String,
    var jobType: String,
    var requirements: String,
    var createdBy: Int
)