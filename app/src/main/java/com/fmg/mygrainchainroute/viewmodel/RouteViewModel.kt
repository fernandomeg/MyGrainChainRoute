package com.fmg.mygrainchainroute.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmg.mygrainchainroute.repository.RouteRepository
import com.fmg.mygrainchainroute.source.room.entities.Route
import kotlinx.coroutines.launch

class RouteViewModel(val repository: RouteRepository) :ViewModel() {

    companion object {
        /**
         * Factory for creating [RouteViewModel]
         *
         * @param arg the repository to pass to [RouteViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::RouteViewModel)
    }

    fun insertRoute(route:Route) = viewModelScope.launch {
        repository.insertRoute(route)
    }

}