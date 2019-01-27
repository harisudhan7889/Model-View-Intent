package com.hari.restaurantfinder.model

/**
 * @author Hari Hara Sudhan.N
 */
data class StateObject(
    val type: String,
    val restaurant: Restaurant? = null,
    val itemCount: Int? = null,
    val isLoading: Boolean? = null,
    val error: Throwable? = null) {
    companion object {
        const val TYPE_MORE_RESTAURANT = "MORE_RESTAURANT"
    }
}
