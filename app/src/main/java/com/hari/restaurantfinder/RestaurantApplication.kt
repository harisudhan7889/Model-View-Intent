package com.hari.restaurantfinder

import android.app.Application
import android.content.Context
import com.hari.restaurantfinder.di.DependencyInjection

/**
 * @author Hari Hara Sudhan.N
 */
class RestaurantApplication : Application() {

    private val dependencyInjection: DependencyInjection = DependencyInjection()

    companion object {
        fun getDependencyInjection(context: Context): DependencyInjection {
            return (context.applicationContext as RestaurantApplication).dependencyInjection
        }
    }

}