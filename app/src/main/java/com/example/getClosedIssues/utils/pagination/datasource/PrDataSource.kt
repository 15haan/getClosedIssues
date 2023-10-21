package com.example.getClosedIssues.utils.pagination.datasource

import android.annotation.SuppressLint
import com.example.getClosedIssues.data.models.ClosedIssue
import com.example.getClosedIssues.domain.CIManager
import com.example.getClosedIssues.utils.pagination.datasource.base.BaseDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Class that handles how to retrieve data for the recyclerview
 * @see BaseDataSource
 */
class PrDataSource(var user: String, var repo: String) : BaseDataSource<ClosedIssue>() {
    private val manager: CIManager = CIManager()

    @SuppressLint("CheckResult")
    override fun loadInitialData(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ClosedIssue>
    ) {
        // in the initial load, we will start at page 0, and retrieve the number of pages in the params.requestLoadSize
        manager.getClosedPRList(user, repo, 0, params.requestedLoadSize, "closed")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::addDisposable)
            .subscribe(
                { items -> submitInitialData(items, params, callback) },
                { error -> submitInitialError(error) }
            )
    }

    @SuppressLint("CheckResult")
    override fun loadAdditionalData(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ClosedIssue>
    ) {
        manager.getClosedPRList(user, repo, params.key, params.requestedLoadSize, "closed")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::addDisposable)
            .subscribe(
                { items -> submitData(items, params, callback) },
                { error -> submitError(error) }
            )
    }
}