package com.fmg.mygrainchainroute.repository

import com.fmg.mygrainchainroute.source.room.dao.RouteDao
import com.fmg.mygrainchainroute.source.room.entities.Route

class RouteRepository constructor(private val routeDao:RouteDao) {

    suspend fun insertRoute(route:Route) = routeDao.insertRoute(route)

    suspend fun updateRoute(route: Route) = routeDao.updateRoute(route)

    suspend fun deleteRoute(route:Route) = routeDao.deleteRoute(route)

    fun getRoutesList() = routeDao.getRouteList()

    fun getRoutesOrderByDate() = routeDao.getRouteListByDate()

}