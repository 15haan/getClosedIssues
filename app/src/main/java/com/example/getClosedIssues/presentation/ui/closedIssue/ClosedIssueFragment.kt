package com.example.getClosedIssues.presentation.ui.closedIssue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.androchef.githubsampleapp.extensions.toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.getClosedIssues.R
import com.example.getClosedIssues.data.models.ClosedIssue
import com.example.getClosedIssues.presentation.ui.HomeActivity
import com.example.getClosedIssues.presentation.utils.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.empty_view.empty_view_imageView
import kotlinx.android.synthetic.main.fragment_closed_issue.pr_emptyView
import kotlinx.android.synthetic.main.fragment_closed_issue.pr_recyclerView
import kotlinx.android.synthetic.main.fragment_closed_issue.pr_emptyView
import kotlinx.android.synthetic.main.fragment_closed_issue.pr_recyclerView

class ClosedIssueFragment : Fragment() {
    private lateinit var viewModel: ClosedIssueViewModel
    private lateinit var adapter: ClosedIssueAdapter
    private lateinit var userName: String
    private lateinit var repo: String
    private val fetchDataDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(ClosedIssueViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mView = inflater.inflate(R.layout.fragment_closed_issue, container, false)
        return mView.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViews()
        registerObservables()
        arguments?.getString(ARGS_REPO)?.let {
            repo = it
        }
        arguments?.getString(ARGS_USERNAME)?.let {
            userName = it
            viewModel.fetchClosedPullRequest(it, repo)
        }
    }

    private fun setupViews() {
        (requireActivity() as HomeActivity).supportActionBar?.title = "CLOSED ISSUES"

        Glide.with(this).asGif().load(R.raw.gif_search_folder)
            .apply(RequestOptions().override(300, 300))
            .into(empty_view_imageView)

        adapter = ClosedIssueAdapter(requireContext(),
            object : ClosedIssueAdapter.ItemClickListener {
                override fun onItemClicked(pr: ClosedIssue) {

                }
            }
        )
        pr_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        pr_recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fetchDataDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        fetchDataDisposable.dispose()
    }

    private fun registerObservables() {
        // We start by submitting the list to the adapter initially
        submitItems()

        // Toast for API Errors
        viewModel.errorEvent.observe(
            viewLifecycleOwner
        ) {
            onError()
        }

        // Clearing the data of the adapter when doing a new search
        viewModel.clearDataEvents.observe(
            viewLifecycleOwner
        ) {
            viewModel.clearDataSource()
            submitItems()
            adapter.notifyDataSetChanged()
        }

        // Showing an empty view on the screen
        viewModel.emptyVisibilityEvents.observe(
            viewLifecycleOwner
        ) {
            onNoDataFound(it)
        }

        // Display the recyclerview loading item
        viewModel.recyclerViewLoadingEvents.observe(
            viewLifecycleOwner
        ) { event ->
            event.peek().let { adapter.loading = it }
        }
    }

    private fun onError() {
        Glide.with(this).load(R.drawable.ic_empty)
            .apply(RequestOptions().override(800, 500))
            .into(empty_view_imageView)
        toast(getString(R.string.err_search))
    }

    private fun onNoDataFound(show: Event<Boolean?>) {
        this.pr_emptyView.visibility = if (show.peek() == true) View.VISIBLE else View.GONE

        Glide.with(this).load(R.drawable.ic_empty)
            .into(empty_view_imageView)
    }

    // Submits the list (with the pagination) to the adapter
    private fun submitItems() {
        viewModel.getItems()!!
            .subscribeOn(Schedulers.io())
            .subscribe(
                { items -> adapter.submitList(items) },
                {
                    pr_emptyView.visibility = View.VISIBLE
                    onError()
                }
            ).also {
                fetchDataDisposable.add(it)
            }
    }

    companion object {
        const val TAG = "UserPullRequestFragment"
        private const val ARGS_USERNAME = "args_username"
        private const val ARGS_REPO = "args_repo"
        fun newInstance(userName: String, repo: String): ClosedIssueFragment {
            val bundle = Bundle()
            bundle.putString(ARGS_USERNAME, userName)
            bundle.putString(ARGS_REPO, repo)
            val fragment = ClosedIssueFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
