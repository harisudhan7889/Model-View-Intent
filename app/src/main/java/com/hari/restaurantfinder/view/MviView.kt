package com.hari.restaurantfinder.view

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hari.restaurantfinder.model.mvi.MviState
import io.reactivex.Observable

/**
 * @author Hari Hara Sudhan.N
 */
interface MviView : MvpView {
    fun emitFirstTimeLoadEvent(): Observable<Boolean>
    fun emitPullToRefreshEvent(): Observable<Unit>
    fun emitLoadMoreRestaurantsEvent(): Observable<String>
    fun displayRestaurants(mviState: MviState)
}