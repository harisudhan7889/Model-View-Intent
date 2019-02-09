package com.hari.restaurantfinder.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hari.restaurantfinder.model.mvi.MviState
import com.hari.restaurantfinder.view.ToolbarMviView
import io.reactivex.Observable

/**
 * @author Hari Hara Sudhan.N
 */
class MviToolbarPresenter(private val countObservable: Observable<MviState>)
    : MviBasePresenter<ToolbarMviView, MviState>() {

    override fun bindIntents() {
        subscribeViewState(countObservable, ToolbarMviView::displayRestaurantsCount)
    }
}