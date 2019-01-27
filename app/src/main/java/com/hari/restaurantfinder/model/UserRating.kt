package com.hari.restaurantfinder.model

import com.google.gson.annotations.SerializedName

/**
 * @author Hari Hara Sudhan.N
 */
data class UserRating(@SerializedName("aggregate_rating")val rating: String,
                      @SerializedName("rating_text")val ratingText: String,
                      @SerializedName("rating_color")val ratingColor: String)