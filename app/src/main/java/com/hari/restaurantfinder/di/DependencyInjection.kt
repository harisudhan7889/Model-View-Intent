package com.hari.restaurantfinder.di

import com.hari.restaurantfinder.model.mvi.MviState
import com.hari.restaurantfinder.presenter.MviRestaurantPresenter
import com.hari.restaurantfinder.presenter.MviToolbarPresenter
import io.reactivex.Observable

/**
 * @author Hari Hara Sudhan.N
 */
class DependencyInjection {

    private lateinit var restaurantPresenter: MviRestaurantPresenter

    fun newRestaurantPresenter(latitude: Double, longitude: Double): MviRestaurantPresenter {
        restaurantPresenter = MviRestaurantPresenter(latitude, longitude)
        return restaurantPresenter
    }

    fun newRestaurantToolbarPresenter(): MviToolbarPresenter {
        val restaurantsCountObservable: Observable<MviState> = restaurantPresenter
            .viewStateObservable

        return MviToolbarPresenter(restaurantsCountObservable)
    }
}