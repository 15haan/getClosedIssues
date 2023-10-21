package com.example.getClosedIssues.domain

import com.example.getClosedIssues.data.models.ClosedIssue
import com.example.getClosedIssues.data.services.CIService
import io.reactivex.Single

/**
 * Class that connects the Data layer to Presentation, where the API objects are manipulated and observed by
 * the Views (Activity, Fragment or View)
 */
class CIManager {
    var reposService: CIService = CIService()

    fun getClosedPRList(
        owner: String,
        repo: String,
        page: Int,
        perPage: Int,
        state: String
    ): Single<List<ClosedIssue>> {
        return reposService.getClosedPRs(owner, repo, page, perPage, state)
            // By calling `onErrorResumeNext` we could apply our own error handling function
            .onErrorResumeNext { throwable -> Single.error(throwable) }
            // Since we are using Retrofit's Response, we will need to parse it and check
            // if the response was successful or not
            .flatMap { response ->
                if (!response.isSuccessful) {
                    Single.error(Throwable(response.code().toString()))
                } else Single.just(response)
            }
            // If the response is successful, we retrieve its body
            .map { response -> response.body() }
    }
}