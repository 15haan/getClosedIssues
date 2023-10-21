package com.example.getClosedIssues.data.endpoints

import com.example.getClosedIssues.data.models.ClosedIssue
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClosedIssuesApi {

    @GET("repos/{owner}/{repo}/issues")
    fun getClosedIssues(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("state") state: String
    ): Single<Response<List<ClosedIssue>>>
}