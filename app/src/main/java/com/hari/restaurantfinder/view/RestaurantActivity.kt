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
import com.hari.restaurantfinder.model.mvi.MviState
import com.hari.restaurantfinder.presenter.MviRestaurantPresenter
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_restaurants.*

/**
 * @author Hari Hara Sudhan.N
 */
class RestaurantActivity : MviActivity<MviView, MviRestaurantPresenter>(), MviView {

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

    override fun displayRestaurants(mviState: MviState) {
        if (mviState.isPageLoading
            && mviState.error != null) {
            renderErrorState(mviState)
        } else if (mviState.isPullToRefresh
            && mviState.error != null) {
            renderErrorStatePTR(mviState)
        } else if (mviState.isPageLoading
            && mviState.restaurantsObject == null) {
            renderPageLoading()
        } else if (mviState.isPageLoading
            && mviState.restaurantsObject != null) {
            renderLoadingDataState(mviState)
        } else if (mviState.isPullToRefresh
            && mviState.restaurantsObject == null) {
            renderPullToRefreshState()
        } else if (mviState.isPullToRefresh
            && mviState.restaurantsObject != null) {
            renderDataStatePTR(mviState)
        } else if (mviState.isMoreRestaurantsLoading
            && mviState.restaurantsObject != null) {
            renderDataState(mviState)
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

    private fun renderDataStatePTR(state: MviState) {
        refreshContainer.isRefreshing = false
        recyclerView.visibility = View.VISIBLE
        adapter.setAdapterData(state.restaurantsObject!!.restaurants)
        adapter.notifyDataSetChanged()
    }

    private fun renderDataState(state: MviState) {
        recyclerView.visibility = View.VISIBLE
        adapter.setAdapterData(state.restaurantsObject!!.restaurants)
        adapter.notifyDataSetChanged()
    }

    private fun renderLoadingDataState(state: MviState) {
        loadingIndicator.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.setAdapterData(state.restaurantsObject!!.restaurants)
        adapter.notifyDataSetChanged()
    }

    private fun renderErrorState(state: MviState) {
        loadingIndicator.visibility = View.GONE
        networkError.visibility = View.VISIBLE
    }

    private fun renderErrorStatePTR(state: MviState) {
        refreshContainer.isRefreshing = false
        networkError.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)
        setSupportActionBar(toolBar)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        adapter = RestaurantAdapter(this)
        recyclerView.adapter = adapter
    }
}