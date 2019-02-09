package com.hari.restaurantfinder.model.mvi

import com.hari.restaurantfinder.model.RestaurantsObject

/**
 * @author Hari Hara Sudhan.N
 */
data class RestaurantViewState(
    val isPageLoading: Boolean = false,
    val isPullToRefresh: Boolean = false,
    val isMoreRestaurantsLoading: Boolean = false,
    val restaurantsObject: RestaurantsObject? = null,
    val error: Throwable? = null) {

    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(restaurantViewState: RestaurantViewState) {
        private var isPageLoading = restaurantViewState.isPageLoading
        private var isPullToRefresh = restaurantViewState.isPullToRefresh
        private var isMoreRestaurantsLoading = restaurantViewState.isMoreRestaurantsLoading
        private var restaurantsObject: RestaurantsObject? = restaurantViewState.restaurantsObject
        private var error: Throwable? = restaurantViewState.error

        fun isPageLoading(isPageLoading: Boolean): Builder {
            this.isPageLoading = isPageLoading
            return this
        }

        fun isPullToRefresh(isPullToRefresh: Boolean): Builder {
            this.isPullToRefresh = isPullToRefresh
            return this
        }

        fun isMoreRestaurantsLoading(isMoreRestaurantsLoading: Boolean): Builder {
            this.isMoreRestaurantsLoading = isMoreRestaurantsLoading
            return this
        }

        fun data(newRestaurantsObject: RestaurantsObject?): Builder {
            restaurantsObject = newRestaurantsObject
            return this
        }

        fun error(error: Throwable?): Builder {
            this.error = error
            return this
        }

        fun build(): RestaurantViewState {
            return RestaurantViewState(isPageLoading, isPullToRefresh, isMoreRestaurantsLoading, restaurantsObject, error)
        }
    }
}