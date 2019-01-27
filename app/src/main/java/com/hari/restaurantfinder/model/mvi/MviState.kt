package com.hari.restaurantfinder.model.mvi

import com.hari.restaurantfinder.model.RestaurantsObject

/**
 * @author Hari Hara Sudhan.N
 */
data class MviState(
    var isPageLoading: Boolean = false,
    var isPullToRefresh: Boolean = false,
    var isMoreRestaurantsLoading: Boolean = false,
    var restaurantsObject: RestaurantsObject? = null,
    var error: Throwable? = null) {

    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(mviState: MviState) {
        private var isPageLoading = mviState.isPageLoading
        private var isPullToRefresh = mviState.isPullToRefresh
        private var isMoreRestaurantsLoading = mviState.isMoreRestaurantsLoading
        private var restaurantsObject: RestaurantsObject? = mviState.restaurantsObject
        private var error: Throwable? = mviState.error

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

        fun build(): MviState {
            return MviState(isPageLoading, isPullToRefresh, isMoreRestaurantsLoading, restaurantsObject, error)
        }
    }
}