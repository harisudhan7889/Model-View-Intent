package com.hari.restaurantfinder.view

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hari.restaurantfinder.model.mvi.MviState

/**
 * @author Hari Hara Sudhan.N
 */
interface ToolbarMviView : MvpView {
    fun displayRestaurantsCount(state: MviState)
}