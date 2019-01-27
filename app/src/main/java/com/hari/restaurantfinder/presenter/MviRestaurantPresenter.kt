package com.hari.restaurantfinder.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hari.restaurantfinder.model.StateObject
import com.hari.restaurantfinder.model.mvi.MviState
import com.hari.restaurantfinder.model.mvi.PartialMviState
import com.hari.restaurantfinder.view.MviView
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
) : MviBasePresenter<MviView, MviState>() {

    override fun bindIntents() {

        // Default loading of page
        val restaurantsState: Observable<PartialMviState> = intent(MviView::emitFirstTimeLoadEvent)
            .subscribeOn(Schedulers.io())
            .debounce(200, TimeUnit.MILLISECONDS)
            .flatMap { GetRestaurantsUseCase.getRestaurants(latitude, longitude) }
            .observeOn(AndroidSchedulers.mainThread())

        // Load more restaurants
        val loadMoreRestaurantsState: Observable<PartialMviState> = intent(MviView::emitLoadMoreRestaurantsEvent)
            .subscribeOn(Schedulers.io())
            .debounce(200, TimeUnit.MILLISECONDS)
            .switchMap { GetRestaurantsUseCase.getMoreRestaurants(latitude, longitude) }
            .observeOn(AndroidSchedulers.mainThread())

        // Loading page using pull to refresh
        val pullToRefreshState: Observable<PartialMviState> = intent(MviView::emitPullToRefreshEvent)
            .subscribeOn(Schedulers.io())
            .debounce(200, TimeUnit.MILLISECONDS)
            .switchMap { GetRestaurantsUseCase.getRestaurantsByPTR(latitude, longitude) }
            .observeOn(AndroidSchedulers.mainThread())

        val allViewState: Observable<PartialMviState> = Observable.merge(
            restaurantsState,
            loadMoreRestaurantsState,
            pullToRefreshState)

        val initializeState = MviState(isPageLoading = true)
        subscribeViewState(
            allViewState.scan(initializeState, this::viewStateReducer),
            MviView::displayRestaurants
        )
    }

    private fun viewStateReducer(previousState: MviState, currentState: PartialMviState): MviState {
        return when (currentState) {
            PartialMviState.LoadingState -> {
                previousState
                    .copy()
                    .isPageLoading(true)
                    .isPullToRefresh(false)
                    .isMoreRestaurantsLoading(false)
                    .data(null)
                    .error(null)
                    .build()
            }
            PartialMviState.PullToRefreshState -> {
                previousState
                    .copy()
                    .isPullToRefresh(true)
                    .isPageLoading(false)
                    .isMoreRestaurantsLoading(false)
                    .data(null)
                    .error(null)
                    .build()
            }
            PartialMviState.LoadMoreState -> {
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
            is PartialMviState.DataState -> {
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
            is PartialMviState.ErrorState -> {
                previousState
                    .copy()
                    .data(null)
                    .error(currentState.error)
                    .build()
            }
            is PartialMviState.LoadMoreDataState -> {
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
            is PartialMviState.LoadMoreErrorState -> {
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
}