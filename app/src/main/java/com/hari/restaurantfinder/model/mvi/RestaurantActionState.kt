package com.hari.restaurantfinder.model.mvi

import com.hari.restaurantfinder.model.RestaurantsObject

/**
 * @author Hari Hara Sudhan.N
 */
sealed class RestaurantActionState {
    object LoadingState: RestaurantActionState()
    object LoadMoreState: RestaurantActionState()
    object PullToRefreshState: RestaurantActionState()
    data class DataState(val restaurantsObject: RestaurantsObject) : RestaurantActionState()
    data class LoadMoreDataState(val restaurantsObject: RestaurantsObject) : RestaurantActionState()
    data class ErrorState(val error: Throwable) : RestaurantActionState()
    data class LoadMoreErrorState(val error: Throwable) : RestaurantActionState()
}