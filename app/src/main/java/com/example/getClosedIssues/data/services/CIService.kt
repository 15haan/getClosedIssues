package com.example.getClosedIssues.data.services

import com.example.getClosedIssues.data.endpoints.ClosedIssuesApi
import com.example.getClosedIssues.data.models.ClosedIssue
import com.example.getClosedIssues.utils.networking.NetworkTools
import io.reactivex.Single
import retrofit2.Response

class CIService {
    var api: ClosedIssuesApi = NetworkTools.retrofit.create(ClosedIssuesApi::class.java)

    fun getClosedPRs(
        owner: String,
        repo: String,
        page: Int,
        perPage: Int,
        state: String
    ): Single<Response<List<ClosedIssue>>> {
        return api.getClosedIssues(owner, repo, page, perPage, state)
    }
}