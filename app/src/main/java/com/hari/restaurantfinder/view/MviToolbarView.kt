package com.hari.restaurantfinder.view

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hari.restaurantfinder.model.mvi.RestaurantViewState

/**
 * @author Hari Hara Sudhan.N
 */
interface MviToolbarView : MvpView {
    fun displayRestaurantsCount(state: RestaurantViewState)
}