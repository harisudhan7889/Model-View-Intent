package com.hari.restaurantfinder.presenter

import com.hari.restaurantfinder.api.RestaurantApi
import com.hari.restaurantfinder.api.RestaurantApiEndpoint
import com.hari.restaurantfinder.model.mvi.PartialMviState
import io.reactivex.Observable

/**
 * @author Hari Hara Sudhan.N
 */
object GetRestaurantsUseCase {

    /*Note: All are same api service calls,
      only the Loading, data, error states differs. */

    // Default Loading
    fun getRestaurants(latitude: Double, longitude: Double): Observable<PartialMviState> {
        val endpoint = RestaurantApi.getClient().create(RestaurantApiEndpoint::class.java)
        return endpoint.getRestaurantsAtLocation(latitude, longitude, 0, 3)
            .map<PartialMviState> { PartialMviState.DataState(it) }
            .startWith(PartialMviState.LoadingState)
            .onErrorReturn { (PartialMviState.ErrorState(it)) }
    }

    // Load more restaurants
    fun getMoreRestaurants(latitude: Double, longitude: Double): Observable<PartialMviState> {
        val endpoint = RestaurantApi.getClient().create(RestaurantApiEndpoint::class.java)
        return endpoint.getRestaurantsAtLocation(latitude, longitude, 0, 3)
            .map<PartialMviState> { PartialMviState.LoadMoreDataState(it) }
            .startWith(PartialMviState.LoadMoreState)
            .onErrorReturn { (PartialMviState.LoadMoreErrorState(it)) }
    }

    // PullToRefresh
    fun getRestaurantsByPTR(latitude: Double, longitude: Double) : Observable<PartialMviState> {
        val endpoint = RestaurantApi.getClient().create(RestaurantApiEndpoint::class.java)
        return endpoint.getRestaurantsAtLocation(latitude, longitude, 0, 3)
            .map<PartialMviState> { PartialMviState.DataState(it) }
            .startWith(PartialMviState.PullToRefreshState)
            .onErrorReturn{(PartialMviState.ErrorState(it))}
    }
}