package com.hari.restaurantfinder.model

import com.google.gson.annotations.SerializedName

/**
 * @author Hari Hara Sudhan.N
 */
data class Restaurant(val id: String,
                      val name: String,
                      @SerializedName("featured_image") val thumbnailUrl: String,
                      @SerializedName("user_rating") val userRating: UserRating)