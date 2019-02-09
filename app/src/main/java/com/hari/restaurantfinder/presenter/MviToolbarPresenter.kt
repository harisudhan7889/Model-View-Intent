package com.hari.restaurantfinder.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hari.restaurantfinder.model.mvi.RestaurantViewState
import com.hari.restaurantfinder.view.MviToolbarView
import io.reactivex.Observable

/**
 * @author Hari Hara Sudhan.N
 */
class MviToolbarPresenter(private val countObservable: Observable<RestaurantViewState>)
    : MviBasePresenter<MviToolbarView, RestaurantViewState>() {

    override fun bindIntents() {
        subscribeViewState(countObservable, MviToolbarView::displayRestaurantsCount)
    }
}