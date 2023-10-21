package com.example.getClosedIssues.presentation.ui.closedIssue

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.getClosedIssues.R
import com.example.getClosedIssues.data.models.ClosedIssue
import com.example.getClosedIssues.presentation.ui.base.BaseDiffAdapter
import com.example.getClosedIssues.presentation.ui.base.VIEW_TYPE_NORMAL
import kotlinx.android.synthetic.main.item_ci.view.ci_number
import kotlinx.android.synthetic.main.item_ci.view.ci_title
import kotlinx.android.synthetic.main.item_ci.view.ci_user_image
import kotlinx.android.synthetic.main.item_ci.view.ci_username
import kotlinx.android.synthetic.main.item_ci.view.closed_on_textview
import kotlinx.android.synthetic.main.item_ci.view.created_on_textview
import kotlinx.android.synthetic.main.item_ci.view.closed_on_textview
import kotlinx.android.synthetic.main.item_ci.view.created_on_textview
import kotlinx.android.synthetic.main.item_ci.view.ci_number
import kotlinx.android.synthetic.main.item_ci.view.ci_title
import kotlinx.android.synthetic.main.item_ci.view.ci_user_image
import kotlinx.android.synthetic.main.item_ci.view.ci_username

class ClosedIssueAdapter(private val context: Context, var listener: ItemClickListener) :
    BaseDiffAdapter<ClosedIssue, RecyclerView.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClicked(pr: ClosedIssue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NORMAL)
            PrViewModel(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ci, parent, false)
            )
        else LoadingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            val closedPr = getItem(position)
            val viewHolder = holder as PrViewModel
            viewHolder.userName.text = closedPr?.user?.prOwner
            viewHolder.prNumber.text = "#${closedPr?.closedIssueId}"
            viewHolder.createdOn.text = closedPr?.createdDate?.split("T")?.get(0) ?: ""
            viewHolder.closedOn.text = closedPr?.closedDate?.split("T")?.get(0) ?: ""
            viewHolder.prTitle.text = closedPr?.title
            closedPr?.let { repo ->
                viewHolder.itemView.setOnClickListener { listener.onItemClicked(repo) }
            }
            closedPr?.user?.profileUrl?.let {
                Glide.with(context).load(it)
                    .placeholder(R.drawable.ic_user)
                    .apply(RequestOptions.circleCropTransform())
                    .into(viewHolder.userImage)
            }
        }
    }

    class PrViewModel(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.ci_username
        val prNumber: TextView = view.ci_number
        val createdOn: TextView = view.created_on_textview
        val closedOn: TextView = view.closed_on_textview
        val prTitle: TextView = view.ci_title
        val userImage: ImageView = view.ci_user_image
    }

}