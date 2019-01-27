package com.hari.restaurantfinder.api

import com.hari.restaurantfinder.model.RestaurantsObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Hari Hara Sudhan.N
 */
interface RestaurantApiEndpoint {

    @GET("api/v2.1/search")
    fun getRestaurantsAtLocation(@Query("lat") lat: Double, @Query("lon") lon: Double,
                                 @Query("start") start: Int, @Query("count") count: Int): Observable<RestaurantsObject>
}