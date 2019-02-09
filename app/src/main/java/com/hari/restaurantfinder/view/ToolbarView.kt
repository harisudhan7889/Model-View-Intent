package com.hari.restaurantfinder.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import com.hannesdorfmann.mosby3.ViewGroupMviDelegate
import com.hannesdorfmann.mosby3.ViewGroupMviDelegateCallback
import com.hannesdorfmann.mosby3.ViewGroupMviDelegateImpl
import com.hari.restaurantfinder.R
import com.hari.restaurantfinder.RestaurantApplication
import com.hari.restaurantfinder.model.mvi.MviState
import com.hari.restaurantfinder.presenter.MviToolbarPresenter

/**
 * @author Hari Hara Sudhan.N
 */
class ToolbarView constructor(context: Context, attributeSet: AttributeSet) : Toolbar(context, attributeSet),
    ToolbarMviView, ViewGroupMviDelegateCallback<ToolbarMviView, MviToolbarPresenter> {

    private val delegate: ViewGroupMviDelegate<ToolbarMviView, MviToolbarPresenter> =
        ViewGroupMviDelegateImpl(this, this, true)

    override fun superOnSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()
    }

    override fun setRestoringViewState(restoringViewState: Boolean) {
    }

    override fun superOnRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }

    override fun createPresenter(): MviToolbarPresenter {
        return RestaurantApplication.getDependencyInjection(context).newRestaurantToolbarPresenter()
    }

    override fun getMvpView(): ToolbarMviView {
        return this
    }

    override fun displayRestaurantsCount(state: MviState) {
        if((state.isPageLoading && state.error != null)
            || (state.isPullToRefresh && state.error != null)) {
            title = resources.getString(R.string.load_error)
        } else if (state.isPageLoading
            && state.restaurantsObject == null) {
            title = resources.getString(R.string.loading_page_message)
        } else if (state.restaurantsObject != null
            && (state.isPageLoading || state.isPullToRefresh || state.isMoreRestaurantsLoading)) {
            val count = state.restaurantsObject.restaurants.count()
            title = String.format(resources.getString(R.string.restaurants_count), if (count == 0) count else count - 1)
        } else if(state.isPullToRefresh
            && state.restaurantsObject == null) {
            title = resources.getString(R.string.pull_to_refresh_message)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        delegate.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        delegate.onDetachedFromWindow()
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(): Parcelable? {
        return delegate.onSaveInstanceState()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRestoreInstanceState(state: Parcelable?) {
        delegate.onRestoreInstanceState(state)
    }
}