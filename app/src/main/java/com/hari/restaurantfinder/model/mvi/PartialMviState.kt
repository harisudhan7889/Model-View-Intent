package com.hari.restaurantfinder.model.mvi

import com.hari.restaurantfinder.model.RestaurantsObject

/**
 * @author Hari Hara Sudhan.N
 */
sealed class PartialMviState {
    object LoadingState: PartialMviState()
    object LoadMoreState: PartialMviState()
    object PullToRefreshState: PartialMviState()
    data class DataState(val restaurantsObject: RestaurantsObject) : PartialMviState()
    data class LoadMoreDataState(val restaurantsObject: RestaurantsObject) : PartialMviState()
    data class ErrorState(val error: Throwable) : PartialMviState()
    data class LoadMoreErrorState(val error: Throwable) : PartialMviState()
}