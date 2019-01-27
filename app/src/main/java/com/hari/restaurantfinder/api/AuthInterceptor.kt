package com.hari.restaurantfinder.api

import com.hari.restaurantfinder.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds api key to the header.
 *
 * @author Hari Hara Sudhan.N
 */
class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request?.newBuilder()
            ?.addHeader("user-key", BuildConfig.ZOMATO_API_KEY)
            ?.build()
        return chain.proceed(request)
    }
}