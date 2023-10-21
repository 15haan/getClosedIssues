package com.example.getClosedIssues.data.models

import com.google.gson.annotations.SerializedName

data class ClosedIssue(
    @SerializedName("title") var title: String?,
    @SerializedName("created_at") var createdDate: String?,
    @SerializedName("closed_at") var closedDate: String?,
    @SerializedName("user") var user: User?,
    @SerializedName("number") var closedIssueId: Int?,
    var userName: String? = null
)