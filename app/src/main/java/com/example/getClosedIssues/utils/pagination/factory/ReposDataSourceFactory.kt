package com.example.getClosedIssues.utils.pagination.factory

import androidx.paging.DataSource
import com.example.getClosedIssues.data.models.Repo
import com.example.getClosedIssues.utils.pagination.datasource.ReposDataSource
import com.example.getClosedIssues.utils.pagination.datasource.base.OnDataSourceLoading

/**
 * Factory class that handles the creation of DataSources
 */
class ReposDataSourceFactory(
    private var loading: OnDataSourceLoading,
    var user: String?
) : DataSource.Factory<Int, Repo>() {
    private lateinit var source: ReposDataSource

    override fun create(): DataSource<Int, Repo>? {
        // invalidate the previous data source, if available
        if (::source.isInitialized) source.invalidate()

        // if we have a user, then create a data source
        user?.let {
            source = ReposDataSource(it)
            source.onDataSourceLoading = loading
            return source

        }
        return null
    }
}