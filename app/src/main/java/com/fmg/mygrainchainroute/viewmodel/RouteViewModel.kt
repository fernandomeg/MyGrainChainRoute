package com.fmg.mygrainchainroute.viewmodel

import androidx.lifecycle.ViewModel
import com.fmg.mygrainchainroute.repository.RouteRepository

class RouteViewModel(val repository: RouteRepository) :ViewModel() {

    companion object {
        /**
         * Factory for creating [RouteViewModel]
         *
         * @param arg the repository to pass to [RouteViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::RouteViewModel)
    }

}