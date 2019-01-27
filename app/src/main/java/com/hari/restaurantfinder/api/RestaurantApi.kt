package com.hari.restaurantfinder.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides the retrofit instance.
 *
 * @author Hari Hara Sudhan.N
 */
class RestaurantApi {
    companion object {
        fun getClient(): Retrofit {
            val client = OkHttpClient.Builder()
            client.addInterceptor(AuthInterceptor())

            // To display the request and response details in log
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            client.addInterceptor(interceptor)

            return Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/")
                .client(client.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}