package com.hari.restaurantfinder.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.hari.restaurantfinder.R
import com.hari.restaurantfinder.RestaurantApplication
import com.hari.restaurantfinder.model.mvi.RestaurantViewState
import com.hari.restaurantfinder.presenter.MviRestaurantPresenter
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_restaurants.*

/**
 * @author Hari Hara Sudhan.N
 */
class RestaurantActivity : MviActivity<MviRestaurantView, MviRestaurantPresenter>(), MviRestaurantView {

    lateinit var adapter: RestaurantAdapter

    override fun emitPullToRefreshEvent() = refreshContainer.refreshes()

    override fun emitFirstTimeLoadEvent() = Observable.just(true).subscribeOn(Schedulers.io())

    override fun emitLoadMoreRestaurantsEvent(): Observable<String> {
        return adapter.loadMoreRestaurantsObservable()
    }

    override fun createPresenter(): MviRestaurantPresenter {
        var latitude = 0.0
        var longitude = 0.0
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location.let {
                latitude = location.latitude
                longitude = location.longitude
            }
        }

        return RestaurantApplication.getDependencyInjection(this).newRestaurantPresenter(latitude, longitude)
    }

    override fun displayRestaurants(restaurantViewState: RestaurantViewState) {
        if (restaurantViewState.isPageLoading
            && restaurantViewState.error != null) {
            renderErrorState(restaurantViewState)
        } else if (restaurantViewState.isPullToRefresh
            && restaurantViewState.error != null) {
            renderErrorStatePTR(restaurantViewState)
        } else if (restaurantViewState.isPageLoading
            && restaurantViewState.restaurantsObject == null) {
            renderPageLoading()
        } else if (restaurantViewState.isPageLoading
            && restaurantViewState.restaurantsObject != null) {
            renderLoadingDataState(restaurantViewState)
        } else if (restaurantViewState.isPullToRefresh
            && restaurantViewState.restaurantsObject == null) {
            renderPullToRefreshState()
        } else if (restaurantViewState.isPullToRefresh
            && restaurantViewState.restaurantsObject != null) {
            renderDataStatePTR(restaurantViewState)
        } else if (restaurantViewState.isMoreRestaurantsLoading
            && restaurantViewState.restaurantsObject != null) {
            renderDataState(restaurantViewState)
        }
    }

    private fun renderPageLoading() {
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun renderPullToRefreshState() {
        recyclerView.visibility = View.GONE
        networkError.visibility = View.GONE
    }

    private fun renderDataStatePTR(state: RestaurantViewState) {
        refreshContainer.isRefreshing = false
        recyclerView.visibility = View.VISIBLE
        adapter.setAdapterData(state.restaurantsObject!!.restaurants)
        adapter.notifyDataSetChanged()
    }

    private fun renderDataState(state: RestaurantViewState) {
        recyclerView.visibility = View.VISIBLE
        adapter.setAdapterData(state.restaurantsObject!!.restaurants)
        adapter.notifyDataSetChanged()
    }

    private fun renderLoadingDataState(state: RestaurantViewState) {
        loadingIndicator.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.setAdapterData(state.restaurantsObject!!.restaurants)
        adapter.notifyDataSetChanged()
    }

    private fun renderErrorState(state: RestaurantViewState) {
        loadingIndicator.visibility = View.GONE
        networkError.visibility = View.VISIBLE
    }

    private fun renderErrorStatePTR(state: RestaurantViewState) {
        refreshContainer.isRefreshing = false
        networkError.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { finish() }
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        adapter = RestaurantAdapter(this)
        recyclerView.adapter = adapter
    }
}