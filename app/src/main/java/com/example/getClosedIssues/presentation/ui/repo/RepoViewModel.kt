package com.example.getClosedIssues.presentation.ui.repo

import com.example.getClosedIssues.data.models.Repo
import com.example.getClosedIssues.presentation.ui.base.BasePaginationViewModel
import com.example.getClosedIssues.utils.pagination.factory.ReposDataSourceFactory

class RepoViewModel : BasePaginationViewModel<ReposDataSourceFactory, Repo>() {
    init {
        dataSourceFactory = ReposDataSourceFactory(getListener(), null)
    }

    override fun getPageSize(): Int = 3

    /**
     * Handles a new user search
     */
    fun newSearch(user : String) {
        dataSourceFactory.user = user
        clearData()
    }
}