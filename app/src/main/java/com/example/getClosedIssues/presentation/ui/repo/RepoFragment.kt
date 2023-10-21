package com.example.getClosedIssues.presentation.ui.repo

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
import com.example.getClosedIssues.data.models.Repo
import com.example.getClosedIssues.utils.extensions.replaceFragmentBackStack
import com.example.getClosedIssues.presentation.ui.HomeActivity
import com.example.getClosedIssues.presentation.ui.closedIssue.ClosedIssueFragment
import com.example.getClosedIssues.presentation.utils.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.empty_view.empty_view_imageView
import kotlinx.android.synthetic.main.fragment_repo.main_emptyView
import kotlinx.android.synthetic.main.fragment_repo.main_recyclerView

class RepoFragment : Fragment() {
    private lateinit var viewModel: RepoViewModel
    private lateinit var adapter: RepoAdapter
    private lateinit var userName: String
    private val fetchDataDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(RepoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mView = inflater.inflate(R.layout.fragment_repo, container, false)
        return mView.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
        registerObservables()

        arguments?.getString(ARGS_USERNAME)?.let {
            userName = it
            viewModel.newSearch(it)
        }
    }

    private fun setupViews() {
        (requireActivity() as HomeActivity).supportActionBar?.title = "Your Repositories"

        Glide.with(this).asGif().load(R.raw.gif_search_folder)
            .apply(RequestOptions().override(300, 300))
            .into(empty_view_imageView)

        adapter = RepoAdapter(
            object : RepoAdapter.ItemClickListener {
                override fun onItemClicked(repos: Repo) {
                    repos.name?.let {
                        goToClosedPrFragment(userName, it)
                    }
                }
            }
        )
        main_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        main_recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fetchDataDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        fetchDataDisposable.dispose()
    }

    private fun onError() {
        Glide.with(this).load(R.drawable.ic_empty)
            .apply(RequestOptions().override(800, 500))
            .into(empty_view_imageView)
        toast(getString(R.string.err_search))
    }

    private fun onNoDataFound(show: Event<Boolean>?) {
        this.main_emptyView.visibility = if (show?.peek() == true) View.VISIBLE else View.GONE

        Glide.with(this).load(R.drawable.ic_empty)
            .into(empty_view_imageView)
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
        ) {
            it?.let { adapter.loading = it.peek() }
        }
    }

    // Submits the list (with the pagination) to the adapter
    private fun submitItems() {
        viewModel.getItems()!!
            .subscribeOn(Schedulers.io())
            .subscribe(
                { items -> adapter.submitList(items) },
                {
                    onError()
                }
            ).also {
                fetchDataDisposable.add(it)
            }
    }

    private fun goToClosedPrFragment(userName: String, repo: String) {
        replaceFragmentBackStack(
            R.id.mainFragmentContainer,
            ClosedIssueFragment.newInstance(userName, repo),
            null
        )
    }

    companion object {
        const val TAG = "UserRepositoriesFragment"
        private const val ARGS_USERNAME = "args_username"
        fun newInstance(userName: String): RepoFragment {
            val bundle = Bundle()
            bundle.putString(ARGS_USERNAME, userName)
            val fragment = RepoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
