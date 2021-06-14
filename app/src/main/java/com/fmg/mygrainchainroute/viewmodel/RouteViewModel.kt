package com.fmg.mygrainchainroute.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmg.mygrainchainroute.Results
import com.fmg.mygrainchainroute.repository.RouteRepository
import com.fmg.mygrainchainroute.source.room.entities.Route
import kotlinx.coroutines.launch
import java.lang.Exception

class RouteViewModel(val repository: RouteRepository) :ViewModel() {

    companion object {
        /**
         * Factory for creating [RouteViewModel]
         *
         * @param arg the repository to pass to [RouteViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::RouteViewModel)
    }

    //Variable Intermediary Viewmodel and IU
    var mutableListRoutes: MutableLiveData<Results<List<Route>>> = MutableLiveData()
    var mutableRoute: MutableLiveData<Results<Route>> = MutableLiveData()

    //Connected to IU
    private val liveDataListRoutesResults: LiveData<Results<List<Route>>> get() = mutableListRoutes
    private val liveDataRouteResults: LiveData<Results<Route>> get() = mutableRoute

    fun getRoutesInOrderByDate(context: Context){
        viewModelScope.launch {
            try {
                mutableListRoutes.postValue(repository.getRoutesOrderByDate(context))
            } catch (e: Exception) {
                mutableListRoutes.postValue(Results.Error(e))
            }
        }
    }

    fun getRoutesById(context: Context,routeId:Int){
        viewModelScope.launch {
            try {
                mutableRoute.postValue(repository.getRouteById(context,routeId))
            } catch (e: Exception) {
                mutableRoute.postValue(Results.Error(e))
            }
        }
    }

    fun insertRoute(route:Route) = viewModelScope.launch {
        repository.insertRoute(route)
    }

    fun deleteRoute(route:Route) = viewModelScope.launch {
        repository.deleteRoute(route)
    }

}