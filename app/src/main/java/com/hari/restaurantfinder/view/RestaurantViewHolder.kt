package com.hari.restaurantfinder.view

import android.view.View
import com.bumptech.glide.Glide
import com.hari.restaurantfinder.model.StateObject
import com.hari.restaurantfinder.model.Restaurant
import kotlinx.android.synthetic.main.activity_restaurants_grid.view.*

/**
 * @author Hari Hara Sudhan.N
 */
class RestaurantViewHolder(itemView: View) : BaseViewHolder(itemView) {
    override fun setData(stateObject: StateObject) {
        val restaurant: Restaurant? = stateObject.restaurant
        itemView.restaurantName.text = restaurant?.name
        Glide.with(itemView.context)
            .load(restaurant?.thumbnailUrl)
            .into(itemView.restaurantImage)
    }
}