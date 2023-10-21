package com.example.getClosedIssues.presentation.ui.closedIssue

import com.example.getClosedIssues.data.models.ClosedIssue
import com.example.getClosedIssues.presentation.ui.base.BasePaginationViewModel
import com.example.getClosedIssues.utils.pagination.factory.PullRequestDataSourceFactory

class ClosedIssueViewModel : BasePaginationViewModel<PullRequestDataSourceFactory, ClosedIssue>() {
    init {
        dataSourceFactory = PullRequestDataSourceFactory(getListener(), null, null)
    }

    override fun getPageSize(): Int = 10

    /**
     * Handles a new user search
     */
    fun fetchClosedPullRequest(user : String, repo: String) {
        dataSourceFactory.user = user
        dataSourceFactory.repo = repo
        clearData()
    }
}