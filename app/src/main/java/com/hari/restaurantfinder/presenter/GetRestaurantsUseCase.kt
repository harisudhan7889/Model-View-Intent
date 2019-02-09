package com.hari.restaurantfinder.presenter

import com.hari.restaurantfinder.api.RestaurantApi
import com.hari.restaurantfinder.api.RestaurantApiEndpoint
import com.hari.restaurantfinder.model.mvi.RestaurantActionState
import io.reactivex.Observable

/**
 * @author Hari Hara Sudhan.N
 */
object GetRestaurantsUseCase {

    /*Note: All are same api service calls,
      only the Loading, data, error states differs. */

    // Default Loading
    fun getRestaurants(latitude: Double, longitude: Double): Observable<RestaurantActionState> {
        val endpoint = RestaurantApi.getClient().create(RestaurantApiEndpoint::class.java)
        return endpoint.getRestaurantsAtLocation(latitude, longitude, 0, 3)
            .map<RestaurantActionState> { RestaurantActionState.DataState(it) }
            .startWith(RestaurantActionState.LoadingState)
            .onErrorReturn { (RestaurantActionState.ErrorState(it)) }
    }

    // Load more restaurants
    fun getMoreRestaurants(latitude: Double, longitude: Double): Observable<RestaurantActionState> {
        val endpoint = RestaurantApi.getClient().create(RestaurantApiEndpoint::class.java)
        return endpoint.getRestaurantsAtLocation(latitude, longitude, 0, 3)
            .map<RestaurantActionState> { RestaurantActionState.LoadMoreDataState(it) }
            .startWith(RestaurantActionState.LoadMoreState)
            .onErrorReturn { (RestaurantActionState.LoadMoreErrorState(it)) }
    }

    // PullToRefresh
    fun getRestaurantsByPTR(latitude: Double, longitude: Double) : Observable<RestaurantActionState> {
        val endpoint = RestaurantApi.getClient().create(RestaurantApiEndpoint::class.java)
        return endpoint.getRestaurantsAtLocation(latitude, longitude, 0, 3)
            .map<RestaurantActionState> { RestaurantActionState.DataState(it) }
            .startWith(RestaurantActionState.PullToRefreshState)
            .onErrorReturn{(RestaurantActionState.ErrorState(it))}
    }
}