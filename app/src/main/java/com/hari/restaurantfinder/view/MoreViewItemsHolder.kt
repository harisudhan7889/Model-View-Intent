package com.hari.restaurantfinder.view

import android.view.View
import com.hari.restaurantfinder.model.StateObject
import kotlinx.android.synthetic.main.item_more_available.view.*

/**
 * @author Hari Hara Sudhan.N
 */
class MoreViewItemsHolder(itemView: View,
                          private val moreItemClickListener: MoreItemClickListener?) :
    BaseViewHolder(itemView), View.OnClickListener {

    interface MoreItemClickListener {
        fun loadMoreItems()
    }

    private val loadMoreButton = itemView.loadMoreButtton
    private val errorRetryButton = itemView.errorRetryButton

    init {
        moreItemClickListener?.let {
            itemView.setOnClickListener(this)
            loadMoreButton.setOnClickListener(this)
            errorRetryButton.setOnClickListener(this)
        }
    }

    override fun setData(stateObject: StateObject) {
        when {
            stateObject.isLoading == true -> {
                itemView.loadingView.visibility = View.VISIBLE
                itemView.moreItemsCount.visibility = View.GONE
                loadMoreButton.visibility = View.GONE
                errorRetryButton.visibility = View.GONE
                itemView.isClickable = false
            }
            stateObject.error != null -> {
                itemView.loadingView.visibility = View.GONE
                itemView.moreItemsCount.visibility = View.GONE
                loadMoreButton.visibility = View.GONE
                errorRetryButton.visibility = View.VISIBLE
                itemView.isClickable = true
            }
            else -> {
                itemView.moreItemsCount.visibility = View.VISIBLE
                itemView.moreItemsCount.text = "+" + stateObject.itemCount
                loadMoreButton.visibility = View.VISIBLE
                errorRetryButton.visibility = View.GONE
                itemView.loadingView.visibility = View.GONE
                itemView.isClickable = true
            }
        }
    }

    override fun onClick(v: View?) {
        moreItemClickListener?.loadMoreItems()
    }
}
