package com.hari.restaurantfinder.view

import android.support.v7.widget.RecyclerView
import android.view.View
import com.hari.restaurantfinder.model.StateObject

/**
 * @author Hari Hara Sudhan.N
 */
abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
   abstract fun setData(stateObject: StateObject)
}