package com.example.getClosedIssues.data.services

import com.example.getClosedIssues.data.endpoints.RepoApi
import com.example.getClosedIssues.data.models.Repo
import com.example.getClosedIssues.utils.networking.NetworkTools
import io.reactivex.Single
import retrofit2.Response

class ReposService {
    private var api: RepoApi = NetworkTools.retrofit.create(RepoApi::class.java)

    fun getReposForUser(user: String, page: Int, perPage: Int): Single<Response<List<Repo>>> {
        return api.getRepos(user, page, perPage)
    }
}