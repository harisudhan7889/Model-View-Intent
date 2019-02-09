package com.hari.restaurantfinder.presenter

import android.util.Log
import com.google.gson.Gson
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hari.restaurantfinder.model.StateObject
import com.hari.restaurantfinder.model.mvi.RestaurantViewState
import com.hari.restaurantfinder.model.mvi.RestaurantActionState
import com.hari.restaurantfinder.view.MviRestaurantView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author Hari Hara Sudhan.N
 */
class MviRestaurantPresenter(
    private val latitude: Double,
    private val longitude: Double
) : MviBasePresenter<MviRestaurantView, RestaurantViewState>() {

    override fun bindIntents() {

        // Default loading of page
        val restaurantsState: Observable<RestaurantActionState> = intent(MviRestaurantView::emitFirstTimeLoadEvent)
            .subscribeOn(Schedulers.io())
            .debounce(200, TimeUnit.MILLISECONDS)
            .doOnNext { Log.d("Action", "First time page load event.") }
            .flatMap { GetRestaurantsUseCase.getRestaurants(latitude, longitude) }
            .observeOn(AndroidSchedulers.mainThread())

        // Load more restaurants
        val loadMoreRestaurantsState: Observable<RestaurantActionState> = intent(MviRestaurantView::emitLoadMoreRestaurantsEvent)
            .subscribeOn(Schedulers.io())
            .debounce(200, TimeUnit.MILLISECONDS)
            .doOnNext { Log.d("Action", "Load more restaurants event.") }
            .switchMap { GetRestaurantsUseCase.getMoreRestaurants(latitude, longitude) }
            .observeOn(AndroidSchedulers.mainThread())

        // Loading page using pull to refresh
        val pullToRefreshState: Observable<RestaurantActionState> = intent(MviRestaurantView::emitPullToRefreshEvent)
            .subscribeOn(Schedulers.io())
            .debounce(200, TimeUnit.MILLISECONDS)
            .doOnNext { Log.d("Action", "Pull to refresh event.") }
            .switchMap { GetRestaurantsUseCase.getRestaurantsByPTR(latitude, longitude) }
            .observeOn(AndroidSchedulers.mainThread())

        val allViewState: Observable<RestaurantActionState> = Observable.merge(
            restaurantsState,
            loadMoreRestaurantsState,
            pullToRefreshState)

        val initializeState = RestaurantViewState(isPageLoading = true)
        val stateObservable = allViewState
            .scan(initializeState, this::viewStateReducer)
            .doOnNext { Log.d("State", Gson().toJson(it)) }

        subscribeViewState(
            stateObservable,
            MviRestaurantView::displayRestaurants
        )
    }

    /**
     * Create a new immutable state by comparing current and previous states.
     */
    private fun viewStateReducer(previousState: RestaurantViewState,
                                 currentState: RestaurantActionState): RestaurantViewState {
        return when (currentState) {
            RestaurantActionState.LoadingState -> {
                previousState
                    .copy()
                    .isPageLoading(true)
                    .isPullToRefresh(false)
                    .isMoreRestaurantsLoading(false)
                    .data(null)
                    .error(null)
                    .build()
            }
            RestaurantActionState.PullToRefreshState -> {
                previousState
                    .copy()
                    .isPullToRefresh(true)
                    .isPageLoading(false)
                    .isMoreRestaurantsLoading(false)
                    .data(null)
                    .error(null)
                    .build()
            }
            RestaurantActionState.LoadMoreState -> {
                val bridgeObject = StateObject(type = StateObject.TYPE_MORE_RESTAURANT, isLoading = true)
                val newRestaurantsArray = ArrayList<StateObject>()
                newRestaurantsArray.addAll(previousState.restaurantsObject!!.restaurants)
                newRestaurantsArray.removeAt(newRestaurantsArray.lastIndex)
                newRestaurantsArray.add(bridgeObject)
                previousState.restaurantsObject!!.restaurants = newRestaurantsArray
                previousState
                    .copy()
                    .isMoreRestaurantsLoading(true)
                    .isPullToRefresh(false)
                    .isPageLoading(false)
                    .data(previousState.restaurantsObject)
                    .build()
            }
            is RestaurantActionState.DataState -> {
                // Adds the item to load the remaining restaurantsObject while loading
                val bridgeObject = StateObject(type = StateObject.TYPE_MORE_RESTAURANT, itemCount = 3)
                val newRestaurantsArray = ArrayList<StateObject>()
                newRestaurantsArray.addAll(currentState.restaurantsObject.restaurants)
                newRestaurantsArray.add(bridgeObject)
                currentState.restaurantsObject.restaurants = newRestaurantsArray
                previousState
                    .copy()
                    .data(currentState.restaurantsObject)
                    .build()
            }
            is RestaurantActionState.ErrorState -> {
                previousState
                    .copy()
                    .data(null)
                    .error(currentState.error)
                    .build()
            }
            is RestaurantActionState.LoadMoreDataState -> {
                val bridgeObject = StateObject(type = StateObject.TYPE_MORE_RESTAURANT, itemCount = 3)
                val newRestaurantsArray = ArrayList<StateObject>()
                newRestaurantsArray.addAll(previousState.restaurantsObject!!.restaurants)
                newRestaurantsArray.removeAt(newRestaurantsArray.lastIndex)
                newRestaurantsArray.addAll(currentState.restaurantsObject.restaurants)
                newRestaurantsArray.add(bridgeObject)
                currentState.restaurantsObject.restaurants = newRestaurantsArray
                previousState
                    .copy()
                    .isPageLoading(false)
                    .isPullToRefresh(false)
                    .data(currentState.restaurantsObject)
                    .build()
            }
            is RestaurantActionState.LoadMoreErrorState -> {
                val bridgeObject = StateObject(type = StateObject.TYPE_MORE_RESTAURANT, error = currentState.error)
                val newRestaurantsArray = ArrayList<StateObject>()
                newRestaurantsArray.addAll(previousState.restaurantsObject!!.restaurants)
                newRestaurantsArray.removeAt(newRestaurantsArray.lastIndex)
                newRestaurantsArray.add(bridgeObject)
                previousState.restaurantsObject!!.restaurants = newRestaurantsArray
                previousState
                    .copy()
                    .isPageLoading(false)
                    .isPullToRefresh(false)
                    .data(previousState.restaurantsObject)
                    .build()
            }
        }
    }

    public override fun getViewStateObservable(): Observable<RestaurantViewState> {
        return super.getViewStateObservable()
    }
}