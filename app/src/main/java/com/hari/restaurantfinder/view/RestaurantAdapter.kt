package com.hari.restaurantfinder.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hari.restaurantfinder.R
import com.hari.restaurantfinder.model.StateObject
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author Hari Hara Sudhan.N
 */
class RestaurantAdapter(private val context: Context)
    : RecyclerView.Adapter<BaseViewHolder>(),
    MoreViewItemsHolder.MoreItemClickListener {

    private val loadMoreRestaurants = PublishSubject.create<String>()
    private var restaurants: List<StateObject>?=null

    companion object {
        const val RESTAURANT = 0
        const val MORE_ITEMS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType) {
            MORE_ITEMS -> MoreViewItemsHolder(LayoutInflater.from(context).inflate(R.layout.item_more_available,
                parent,
                false), this)
            else -> {
                RestaurantViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_restaurants_grid,
                    parent,
                    false))
            }
        }
    }

    override fun getItemCount(): Int {
        return if (restaurants == null) {
            0
        } else {
            restaurants!!.size
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.setData(restaurants!![position])
    }

    override fun getItemViewType(position: Int): Int {
        return if(restaurants!![position].type == StateObject.TYPE_MORE_RESTAURANT) {
            MORE_ITEMS
        } else {
            RESTAURANT
        }
    }

    override fun loadMoreItems() {
        loadMoreRestaurants.onNext("")
    }

    fun setAdapterData(restaurants: List<StateObject>) {
        this.restaurants = restaurants
    }

    fun loadMoreRestaurantsObservable(): Observable<String> {
        return loadMoreRestaurants
    }
}