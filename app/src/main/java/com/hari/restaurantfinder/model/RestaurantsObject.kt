package com.hari.restaurantfinder.model

import com.google.gson.annotations.SerializedName

/**
 * @author Hari Hara Sudhan.N
 */
data class RestaurantsObject(@SerializedName("restaurants") var restaurants: List<StateObject>)