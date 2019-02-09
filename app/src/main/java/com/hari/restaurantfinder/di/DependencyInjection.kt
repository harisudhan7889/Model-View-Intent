package com.hari.restaurantfinder.di

import com.hari.restaurantfinder.model.mvi.RestaurantViewState
import com.hari.restaurantfinder.presenter.MviRestaurantPresenter
import com.hari.restaurantfinder.presenter.MviToolbarPresenter
import io.reactivex.Observable

/**
 * @author Hari Hara Sudhan.N
 */
class DependencyInjection {

    private lateinit var mviRestaurantPresenter: MviRestaurantPresenter

    fun newRestaurantPresenter(latitude: Double, longitude: Double): MviRestaurantPresenter {
        mviRestaurantPresenter = MviRestaurantPresenter(latitude, longitude)
        return mviRestaurantPresenter
    }

    fun newRestaurantToolbarPresenter(): MviToolbarPresenter {
        val restaurantsCountObservable: Observable<RestaurantViewState> = mviRestaurantPresenter
            .viewStateObservable

        return MviToolbarPresenter(restaurantsCountObservable)
    }
}